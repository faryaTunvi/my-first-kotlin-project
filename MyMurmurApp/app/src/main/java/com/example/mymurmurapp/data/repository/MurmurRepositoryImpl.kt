package com.example.mymurmurapp.data.repository

import com.example.mymurmurapp.data.local.dao.MurmurDao
import com.example.mymurmurapp.data.mapper.toDomain
import com.example.mymurmurapp.data.mapper.toDto
import com.example.mymurmurapp.data.mapper.toEntity
import com.example.mymurmurapp.data.remote.FirebaseService
import com.example.mymurmurapp.domain.model.Murmur
import com.example.mymurmurapp.domain.repository.AuthRepository
import com.example.mymurmurapp.domain.repository.MurmurRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MurmurRepositoryImpl(
    private val firebaseService: FirebaseService,
    private val murmurDao: MurmurDao,
    private val authRepository: AuthRepository
) : MurmurRepository {

    override suspend fun getTimeline(page: Int, pageSize: Int): Result<List<Murmur>> {
        return try {
            val currentUserId = authRepository.getCurrentUserId() ?: ""

            // Fetch from Firebase
            val remoteMurmurs = firebaseService.getTimeline(page, pageSize)

            // Check which murmurs are liked by current user
            val murmursWithLikes = remoteMurmurs.map { murmurDto ->
                val isLiked = if (currentUserId.isNotEmpty()) {
                    firebaseService.isLikedByUser(currentUserId, murmurDto.id)
                } else {
                    false
                }
                murmurDto.toDomain(isLiked)
            }

            // Cache in local database
            murmurDao.insertMurmurs(murmursWithLikes.map { it.toEntity() })

            Result.success(murmursWithLikes)
        } catch (e: Exception) {
            // Fallback to local cache
            try {
                val localMurmurs = murmurDao.getTimeline(pageSize, page * pageSize)
                Result.success(localMurmurs.map { it.toDomain() })
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMurmurById(murmurId: String): Result<Murmur> {
        return try {
            val currentUserId = authRepository.getCurrentUserId() ?: ""
            val murmurDto = firebaseService.getMurmurById(murmurId)

            if (murmurDto != null) {
                val isLiked = if (currentUserId.isNotEmpty()) {
                    firebaseService.isLikedByUser(currentUserId, murmurDto.id)
                } else {
                    false
                }
                val murmur = murmurDto.toDomain(isLiked)
                murmurDao.insertMurmur(murmur.toEntity())
                Result.success(murmur)
            } else {
                Result.failure(Exception("Murmur not found"))
            }
        } catch (e: Exception) {
            try {
                val localMurmur = murmurDao.getMurmurById(murmurId)
                if (localMurmur != null) {
                    Result.success(localMurmur.toDomain())
                } else {
                    Result.failure(e)
                }
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserMurmurs(userId: String, page: Int, pageSize: Int): Result<List<Murmur>> {
        return try {
            val currentUserId = authRepository.getCurrentUserId() ?: ""
            val remoteMurmurs = firebaseService.getUserMurmurs(userId, page, pageSize)

            val murmursWithLikes = remoteMurmurs.map { murmurDto ->
                val isLiked = if (currentUserId.isNotEmpty()) {
                    firebaseService.isLikedByUser(currentUserId, murmurDto.id)
                } else {
                    false
                }
                murmurDto.toDomain(isLiked)
            }

            murmurDao.insertMurmurs(murmursWithLikes.map { it.toEntity() })
            Result.success(murmursWithLikes)
        } catch (e: Exception) {
            try {
                val localMurmurs = murmurDao.getUserMurmurs(userId, pageSize, page * pageSize)
                Result.success(localMurmurs.map { it.toDomain() })
            } catch (localError: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun postMurmur(content: String): Result<Murmur> {
        return try {
            val currentUserId = authRepository.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))

            val currentUser = firebaseService.getUserById(currentUserId)
                ?: return Result.failure(Exception("User not found"))

            val murmurDto = com.example.mymurmurapp.data.remote.dto.MurmurDto(
                userId = currentUserId,
                username = currentUser.username,
                userDisplayName = currentUser.displayName,
                userProfileImageUrl = currentUser.profileImageUrl,
                content = content,
                likesCount = 0,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            val createdMurmur = firebaseService.postMurmur(murmurDto)
            val murmur = createdMurmur.toDomain(false)
            murmurDao.insertMurmur(murmur.toEntity())

            Result.success(murmur)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMurmur(murmurId: String): Result<Unit> {
        return try {
            firebaseService.deleteMurmur(murmurId)
            murmurDao.deleteMurmurById(murmurId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun likeMurmur(murmurId: String): Result<Unit> {
        return try {
            val currentUserId = authRepository.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))

            firebaseService.likeMurmur(currentUserId, murmurId)

            // Update local cache
            val murmur = murmurDao.getMurmurById(murmurId)
            murmur?.let {
                murmurDao.updateMurmur(
                    it.copy(
                        likesCount = it.likesCount + 1,
                        isLikedByCurrentUser = true
                    )
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unlikeMurmur(murmurId: String): Result<Unit> {
        return try {
            val currentUserId = authRepository.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))

            firebaseService.unlikeMurmur(currentUserId, murmurId)

            // Update local cache
            val murmur = murmurDao.getMurmurById(murmurId)
            murmur?.let {
                murmurDao.updateMurmur(
                    it.copy(
                        likesCount = maxOf(0, it.likesCount - 1),
                        isLikedByCurrentUser = false
                    )
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeTimeline(): Flow<List<Murmur>> {
        return murmurDao.observeTimeline().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}

