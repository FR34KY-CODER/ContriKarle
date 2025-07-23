package com.example.contrikaro.presentation.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contrikaro.data.model.Expense
import com.example.contrikaro.data.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    // Note: We will handle expenses in a separate GroupDetailsViewModel later,
    // as expenses are now tied to a specific group, not the main dashboard.
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<com.google.firebase.auth.FirebaseUser?> = _currentUser


    init {
        fetchGroups()
    }

    // LOGIC CHANGE: This now queries the top-level 'groups' collection.
    // It finds any group where the current user's ID is in the 'memberIds' list.
    fun fetchGroups() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("groups")
                    .whereArrayContains("memberIds", uid)
                    .get()
                    .await()

                val groupList = snapshot.toObjects(Group::class.java)
                _groups.value = groupList
                Log.d("MainViewModel", "Fetched ${groupList.size} groups.")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching groups", e)
            }
        }
    }

    // LOGIC CHANGE: This now creates a document in the top-level 'groups' collection.
    // It automatically adds the creator as the first member.
    fun addGroup(
        name: String,
        emoji: String,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("User not logged in.")
            return
        }
        if (name.isBlank()) {
            onError("Group name cannot be empty.")
            return
        }

        viewModelScope.launch {
            try {
                var coverImageUrl: String? = null
                if (imageUri != null) {
                    val storageRef = storage.reference
                        .child("group_covers/${UUID.randomUUID()}")
                    coverImageUrl = storageRef.putFile(imageUri).await()
                        .storage.downloadUrl.await().toString()
                }

                val newGroupRef = firestore.collection("groups").document()
                val newGroup = Group(
                    id = newGroupRef.id,
                    name = name,
                    emoji = emoji.ifBlank { "🎉" },
                    coverImageUrl = coverImageUrl,
                    memberIds = listOf(uid), // Add the creator as the first member
                    createdBy = uid
                )

                newGroupRef.set(newGroup).await()
                Log.d("MainViewModel", "Group added successfully: ${newGroup.id}")
                fetchGroups() // Refresh the list
                onSuccess()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error adding group", e)
                onError(e.message ?: "An unknown error occurred.")
            }
        }
    }

    // LOGIC CHANGE: This function is now designed to add an expense to a *specific group*.
    // We will need to update the AddExpenseDialog UI in a later step to pass the groupId.
    fun addExpenseToGroup(
        groupId: String,
        title: String,
        amount: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("User not logged in.")
            return
        }
        if (title.isBlank() || amount <= 0) {
            onError("Invalid title or amount.")
            return
        }

        viewModelScope.launch {
            try {
                val expenseRef = firestore.collection("groups")
                    .document(groupId)
                    .collection("expenses")
                    .document()

                val expense = Expense(
                    id = expenseRef.id,
                    title = title,
                    amount = amount,
                    paidBy = uid,
                    participants = emptyList() // We will implement splitting later
                )

                expenseRef.set(expense).await()
                Log.d("MainViewModel", "Expense added to group $groupId")
                onSuccess()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error adding expense", e)
                onError(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}