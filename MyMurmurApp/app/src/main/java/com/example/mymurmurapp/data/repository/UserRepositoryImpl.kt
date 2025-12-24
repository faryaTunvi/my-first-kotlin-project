package com.example.mymurmurapp.data.repository

import com.example.mymurmurapp.data.local.dao.UserDao
import com.example.mymurmurapp.data.mapper.toDomain
import com.example.mymurmurapp.data.mapper.toDto
import com.example.mymurmurapp.data.mapper.toEntity
import com.example.mymurmurapp.data.remote.FirebaseService
import com.example.mymurmurapp.domain.model.User
import com.example.mymurmurapp.domain.repository.AuthRepository
import com.example.mymurmurapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val firebaseService: FirebaseService,
    private val userDao: UserDao,
    private val authRepository: AuthRepository
) : UserRepository {

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                getUserById(userId)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return try {
            val userDto = firebaseService.getUserById(userId)
            if (userDto != null) {
                val user = userDto.toDomain()
                userDao.insertUser(user.toEntity())
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            try {
                val localUser = userDao.getUserById(userId)
                if (localUser != null) {
                    Result.success(localUser.toDomain())
                } else {
                    Result.failure(Exception("User not found"))
                }
            } catch (_: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            firebaseService.updateUser(user.toDto())
            userDao.updateUser(user.toEntity())
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun followUser(userId: String): Result<Unit> {
        return try {
            val currentUserId = authRepository.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            firebaseService.followUser(currentUserId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unfollowUser(userId: String): Result<Unit> {
        return try {
            val currentUserId = authRepository.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            firebaseService.unfollowUser(currentUserId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isFollowing(userId: String): Result<Boolean> {
        return try {
            val currentUserId = authRepository.getCurrentUserId()
                ?: return Result.success(false)
            val isFollowing = firebaseService.isFollowing(currentUserId, userId)
            Result.success(isFollowing)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollowers(userId: String): Result<List<User>> {
        return try {
            // This would require additional Firestore queries
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFollowing(userId: String): Result<List<User>> {
        return try {
            // This would require additional Firestore queries
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeCurrentUser(): Flow<User?> {
        return kotlinx.coroutines.flow.flow {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                userDao.observeUserById(userId).map { it?.toDomain() }.collect { emit(it) }
            } else {
                emit(null)
            }
        }
    }
}

