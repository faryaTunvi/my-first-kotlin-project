package com.example.mymurmurapp.data.remote.dto

data class LikeDto(
    val id: String = "",
    val userId: String = "",
    val murmurId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

