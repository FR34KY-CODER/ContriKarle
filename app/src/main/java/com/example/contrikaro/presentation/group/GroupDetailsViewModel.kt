package com.example.contrikaro.presentation.group

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contrikaro.data.model.Expense
import com.example.contrikaro.data.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth, // Inject FirebaseAuth
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: String = savedStateHandle.get<String>("groupId")!!

    private val _group = MutableStateFlow<Group?>(null)
    val group = _group.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses = _expenses.asStateFlow()

    init {
        if (groupId.isNotBlank()) {
            fetchGroupDetails()
            fetchExpenses()
        }
    }

    private fun fetchGroupDetails() {
        viewModelScope.launch {
            try {
                val groupDoc = firestore.collection("groups").document(groupId).get().await()
                _group.value = groupDoc.toObject(Group::class.java)
            } catch (e: Exception) {
                Log.e("GroupDetailsVM", "Error fetching group details", e)
            }
        }
    }

    fun fetchExpenses() { // Make public to allow manual refresh
        viewModelScope.launch {
            try {
                val expensesSnapshot = firestore.collection("groups").document(groupId)
                    .collection("expenses")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()
                _expenses.value = expensesSnapshot.toObjects(Expense::class.java)
            } catch (e: Exception) {
                Log.e("GroupDetailsVM", "Error fetching expenses", e)
            }
        }
    }

    // THIS IS THE NEW FUNCTION THAT FIXES THE ERROR
    fun addExpenseToGroup(
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
                    participants = emptyList() // We will implement splitting logic later
                )

                expenseRef.set(expense).await()
                Log.d("GroupDetailsVM", "Expense added to group $groupId")
                fetchExpenses() // Refresh the list automatically
                onSuccess()
            } catch (e: Exception) {
                Log.e("GroupDetailsVM", "Error adding expense", e)
                onError(e.message ?: "An unknown error occurred.")
            }
        }
    }
}