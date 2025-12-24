package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.model.Murmur
import com.example.mymurmurapp.domain.repository.MurmurRepository

class GetUserMurmursUseCase(private val repository: MurmurRepository) {
    suspend operator fun invoke(userId: String, page: Int, pageSize: Int = 10): Result<List<Murmur>> {
        return repository.getUserMurmurs(userId, page, pageSize)
    }
}

