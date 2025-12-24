package com.example.mymurmurapp.domain.model

data class Like(
    val id: String = "",
    val userId: String = "",
    val murmurId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

