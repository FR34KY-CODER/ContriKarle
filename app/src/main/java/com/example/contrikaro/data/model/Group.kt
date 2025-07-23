package com.example.contrikaro.data.model

data class Group(
    val id: String = "",
    val name: String = "",
    val emoji: String? = null,
    val coverImageUrl: String? = null,
    val memberIds: List<String> = emptyList(),
    val createdBy: String = ""
)