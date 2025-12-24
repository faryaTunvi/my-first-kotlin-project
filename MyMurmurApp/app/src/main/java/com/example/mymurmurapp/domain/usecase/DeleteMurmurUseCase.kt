package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.repository.MurmurRepository

class DeleteMurmurUseCase(private val repository: MurmurRepository) {
    suspend operator fun invoke(murmurId: String): Result<Unit> {
        return repository.deleteMurmur(murmurId)
    }
}

