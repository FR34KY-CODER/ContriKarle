package com.example.contrikaro.data.repository

import com.example.contrikaro.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    suspend fun createUser(user: User) {
        usersCollection.document(user.uid).set(user).await()
    }

    suspend fun getUserById(uid: String): User? {
        val snapshot = usersCollection.document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    suspend fun updateUserName(uid: String, newName: String) {
        usersCollection.document(uid).update("name", newName).await()
    }

    suspend fun joinGroup(uid: String, groupId: String) {
        usersCollection.document(uid)
            .update("joinedGroups", com.google.firebase.firestore.FieldValue.arrayUnion(groupId))
            .await()
    }
}
