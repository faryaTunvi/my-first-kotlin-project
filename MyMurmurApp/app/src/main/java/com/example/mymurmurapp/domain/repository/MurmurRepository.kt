package com.example.mymurmurapp.domain.repository

import com.example.mymurmurapp.domain.model.Murmur
import kotlinx.coroutines.flow.Flow

interface MurmurRepository {
    suspend fun getTimeline(page: Int, pageSize: Int): Result<List<Murmur>>
    suspend fun getMurmurById(murmurId: String): Result<Murmur>
    suspend fun getUserMurmurs(userId: String, page: Int, pageSize: Int): Result<List<Murmur>>
    suspend fun postMurmur(content: String): Result<Murmur>
    suspend fun deleteMurmur(murmurId: String): Result<Unit>
    suspend fun likeMurmur(murmurId: String): Result<Unit>
    suspend fun unlikeMurmur(murmurId: String): Result<Unit>
    fun observeTimeline(): Flow<List<Murmur>>
}


