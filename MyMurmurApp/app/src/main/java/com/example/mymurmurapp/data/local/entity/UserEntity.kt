package com.example.mymurmurapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val displayName: String,
    val email: String,
    val bio: String,
    val profileImageUrl: String,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: Long
)


