package com.example.mymurmurapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val followerId: String,
    val followingId: String,
    val createdAt: Long
)

