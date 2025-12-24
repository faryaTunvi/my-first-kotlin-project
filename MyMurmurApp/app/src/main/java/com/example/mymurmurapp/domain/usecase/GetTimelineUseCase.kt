package com.example.mymurmurapp.domain.usecase

import com.example.mymurmurapp.domain.model.Murmur
import com.example.mymurmurapp.domain.repository.MurmurRepository

class GetTimelineUseCase(private val repository: MurmurRepository) {
    suspend operator fun invoke(page: Int, pageSize: Int = 10): Result<List<Murmur>> {
        return repository.getTimeline(page, pageSize)
    }
}


