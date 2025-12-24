package com.example.mymurmurapp.data.local.dao

import androidx.room.*
import com.example.mymurmurapp.data.local.entity.LikeEntity

@Dao
interface LikeDao {
    @Query("SELECT * FROM likes WHERE userId = :userId AND murmurId = :murmurId")
    suspend fun getLike(userId: String, murmurId: String): LikeEntity?

    @Query("SELECT * FROM likes WHERE murmurId = :murmurId")
    suspend fun getLikesForMurmur(murmurId: String): List<LikeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: LikeEntity)

    @Delete
    suspend fun deleteLike(like: LikeEntity)

    @Query("DELETE FROM likes WHERE userId = :userId AND murmurId = :murmurId")
    suspend fun deleteLikeByIds(userId: String, murmurId: String)
}

