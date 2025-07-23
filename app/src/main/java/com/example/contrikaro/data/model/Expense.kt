package com.example.contrikaro.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val paidBy: String = "",
    val participants: List<String> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
)