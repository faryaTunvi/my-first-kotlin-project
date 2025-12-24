package com.example.mymurmurapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymurmurapp.data.local.dao.*
import com.example.mymurmurapp.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        MurmurEntity::class,
        FollowEntity::class,
        LikeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MurmurDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun murmurDao(): MurmurDao
    abstract fun followDao(): FollowDao
    abstract fun likeDao(): LikeDao
}

