package com.example.mymurmurapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "murmurs")
data class MurmurEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val username: String,
    val userDisplayName: String,
    val userProfileImageUrl: String,
    val content: String,
    val likesCount: Int,
    val isLikedByCurrentUser: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

