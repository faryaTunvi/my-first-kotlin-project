package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.model.Murmur
import com.example.mymurmurapp.domain.repository.MurmurRepository

class PostMurmurUseCase(private val repository: MurmurRepository) {
    suspend operator fun invoke(content: String): Result<Murmur> {
        if (content.isBlank()) {
            return Result.failure(IllegalArgumentException("Murmur content cannot be empty"))
        }
        if (content.length > 280) {
            return Result.failure(IllegalArgumentException("Murmur content cannot exceed 280 characters"))
        }
        return repository.postMurmur(content)
    }
}

