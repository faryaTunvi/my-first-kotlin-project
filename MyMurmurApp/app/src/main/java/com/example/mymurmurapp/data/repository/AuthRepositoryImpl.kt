package com.example.mymurmurapp.data.repository

import com.example.mymurmurapp.data.remote.FirebaseService
import com.example.mymurmurapp.data.remote.dto.UserDto
import com.example.mymurmurapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseService: FirebaseService
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        username: String,
        displayName: String
    ): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Failed to create user")

            // Create user profile in Firestore
            val user = UserDto(
                id = userId,
                username = username,
                displayName = displayName,
                email = email,
                bio = "",
                profileImageUrl = "",
                followersCount = 0,
                followingCount = 0,
                createdAt = System.currentTimeMillis()
            )

            firebaseService.createUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun isAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }
}

