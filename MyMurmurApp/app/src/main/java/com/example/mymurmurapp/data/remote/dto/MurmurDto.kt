package com.example.mymurmurapp.data.remote.dto

data class MurmurDto(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val userDisplayName: String = "",
    val userProfileImageUrl: String = "",
    val content: String = "",
    val likesCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

