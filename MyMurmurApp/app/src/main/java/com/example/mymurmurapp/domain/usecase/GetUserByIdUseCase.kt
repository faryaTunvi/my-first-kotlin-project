package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.model.User
import com.example.mymurmurapp.domain.repository.UserRepository

class GetUserByIdUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): Result<User> {
        return repository.getUserById(userId)
    }
}

