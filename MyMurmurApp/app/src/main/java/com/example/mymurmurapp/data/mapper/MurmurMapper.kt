package com.example.mymurmurapp.data.mapper

import com.example.mymurmurapp.data.local.entity.MurmurEntity
import com.example.mymurmurapp.data.remote.dto.MurmurDto
import com.example.mymurmurapp.domain.model.Murmur

fun Murmur.toDto(): MurmurDto {
    return MurmurDto(
        id = id,
        userId = userId,
        username = username,
        userDisplayName = userDisplayName,
        userProfileImageUrl = userProfileImageUrl,
        content = content,
        likesCount = likesCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MurmurDto.toDomain(isLikedByCurrentUser: Boolean = false): Murmur {
    return Murmur(
        id = id,
        userId = userId,
        username = username,
        userDisplayName = userDisplayName,
        userProfileImageUrl = userProfileImageUrl,
        content = content,
        likesCount = likesCount,
        isLikedByCurrentUser = isLikedByCurrentUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MurmurDto.toEntity(isLikedByCurrentUser: Boolean = false): MurmurEntity {
    return MurmurEntity(
        id = id,
        userId = userId,
        username = username,
        userDisplayName = userDisplayName,
        userProfileImageUrl = userProfileImageUrl,
        content = content,
        likesCount = likesCount,
        isLikedByCurrentUser = isLikedByCurrentUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Murmur.toEntity(): MurmurEntity {
    return MurmurEntity(
        id = id,
        userId = userId,
        username = username,
        userDisplayName = userDisplayName,
        userProfileImageUrl = userProfileImageUrl,
        content = content,
        likesCount = likesCount,
        isLikedByCurrentUser = isLikedByCurrentUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MurmurEntity.toDomain(): Murmur {
    return Murmur(
        id = id,
        userId = userId,
        username = username,
        userDisplayName = userDisplayName,
        userProfileImageUrl = userProfileImageUrl,
        content = content,
        likesCount = likesCount,
        isLikedByCurrentUser = isLikedByCurrentUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}


