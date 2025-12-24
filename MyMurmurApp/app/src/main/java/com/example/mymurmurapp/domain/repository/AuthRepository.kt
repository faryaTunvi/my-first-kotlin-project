package com.example.mymurmurapp.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, username: String, displayName: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUserId(): String?
    suspend fun isAuthenticated(): Boolean
}

