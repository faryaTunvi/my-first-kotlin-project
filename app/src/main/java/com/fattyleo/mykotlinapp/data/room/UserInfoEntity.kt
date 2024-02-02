package com.fattyleo.mykotlinapp.data.room

import androidx.room.*

@Entity(tableName = "user_info", indices = [Index(value = ["email","name"], unique = true)])
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id : Int = 0,
    val name: String,
    val email: String,
)
