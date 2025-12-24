package com.example.mymurmurapp.data.mapper

import com.example.mymurmurapp.data.local.entity.UserEntity
import com.example.mymurmurapp.data.remote.dto.UserDto
import com.example.mymurmurapp.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        displayName = displayName,
        email = email,
        bio = bio,
        profileImageUrl = profileImageUrl,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        displayName = displayName,
        email = email,
        bio = bio,
        profileImageUrl = profileImageUrl,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )
}

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        displayName = displayName,
        email = email,
        bio = bio,
        profileImageUrl = profileImageUrl,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        displayName = displayName,
        email = email,
        bio = bio,
        profileImageUrl = profileImageUrl,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        displayName = displayName,
        email = email,
        bio = bio,
        profileImageUrl = profileImageUrl,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )
}

