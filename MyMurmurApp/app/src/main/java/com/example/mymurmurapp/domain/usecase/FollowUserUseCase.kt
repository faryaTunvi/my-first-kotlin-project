package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.repository.UserRepository

class FollowUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String, isFollowing: Boolean): Result<Unit> {
        return if (isFollowing) {
            repository.unfollowUser(userId)
        } else {
            repository.followUser(userId)
        }
    }
}

