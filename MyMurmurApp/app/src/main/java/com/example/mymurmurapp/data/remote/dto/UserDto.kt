package com.example.mymurmurapp.data.remote.dto

data class UserDto(
    val id: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)


