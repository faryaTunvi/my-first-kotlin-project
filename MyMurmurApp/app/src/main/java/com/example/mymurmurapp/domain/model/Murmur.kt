package com.example.mymurmurapp.domain.model

data class Murmur(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val userDisplayName: String = "",
    val userProfileImageUrl: String = "",
    val content: String = "",
    val likesCount: Int = 0,
    val isLikedByCurrentUser: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

