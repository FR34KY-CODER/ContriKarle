package com.example.contrikarle.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val friends: List<String> = emptyList()

    //Why default values? --> So Firestore can auto-create objects even if some fields are missing.
)
