package com.example.mymurmurapp.domain.repository

import com.example.mymurmurapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Result<User?>
    suspend fun getUserById(userId: String): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun followUser(userId: String): Result<Unit>
    suspend fun unfollowUser(userId: String): Result<Unit>
    suspend fun isFollowing(userId: String): Result<Boolean>
    suspend fun getFollowers(userId: String): Result<List<User>>
    suspend fun getFollowing(userId: String): Result<List<User>>
    fun observeCurrentUser(): Flow<User?>
}

