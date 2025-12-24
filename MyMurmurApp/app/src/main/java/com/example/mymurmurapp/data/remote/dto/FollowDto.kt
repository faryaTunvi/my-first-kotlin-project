package com.example.mymurmurapp.data.remote.dto

data class FollowDto(
    val followerId: String = "",
    val followingId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

