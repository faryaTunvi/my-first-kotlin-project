package com.example.mymurmurapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class LikeEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val murmurId: String,
    val createdAt: Long
)

