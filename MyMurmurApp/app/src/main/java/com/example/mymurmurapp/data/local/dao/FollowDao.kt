package com.example.mymurmurapp.data.local.dao

import androidx.room.*
import com.example.mymurmurapp.data.local.entity.FollowEntity

@Dao
interface FollowDao {
    @Query("SELECT * FROM follows WHERE followerId = :followerId AND followingId = :followingId")
    suspend fun getFollow(followerId: String, followingId: String): FollowEntity?

    @Query("SELECT * FROM follows WHERE followerId = :followerId")
    suspend fun getFollowing(followerId: String): List<FollowEntity>

    @Query("SELECT * FROM follows WHERE followingId = :followingId")
    suspend fun getFollowers(followingId: String): List<FollowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: FollowEntity)

    @Delete
    suspend fun deleteFollow(follow: FollowEntity)

    @Query("DELETE FROM follows WHERE followerId = :followerId AND followingId = :followingId")
    suspend fun deleteFollowByIds(followerId: String, followingId: String)
}

