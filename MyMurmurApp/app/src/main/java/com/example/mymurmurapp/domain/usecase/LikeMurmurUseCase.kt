package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.repository.MurmurRepository

class LikeMurmurUseCase(private val repository: MurmurRepository) {
    suspend operator fun invoke(murmurId: String, isLiked: Boolean): Result<Unit> {
        return if (isLiked) {
            repository.unlikeMurmur(murmurId)
        } else {
            repository.likeMurmur(murmurId)
        }
    }
}

