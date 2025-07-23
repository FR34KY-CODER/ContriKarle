package com.example.contrikaro.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Event(
    val id: String = "",
    val name: String = "",
    val groupId: String = "",
    val archived: Boolean = false,
    @ServerTimestamp
    val createdDate: Date? = null,
    val eventStartDate: Date? = null,
    val eventEndDate: Date? = null
)