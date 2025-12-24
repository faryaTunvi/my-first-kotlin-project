package com.example.mymurmurapp.data.local.dao

import androidx.room.*
import com.example.mymurmurapp.data.local.entity.MurmurEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MurmurDao {
    @Query("SELECT * FROM murmurs ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getTimeline(limit: Int, offset: Int): List<MurmurEntity>

    @Query("SELECT * FROM murmurs ORDER BY createdAt DESC")
    fun observeTimeline(): Flow<List<MurmurEntity>>

    @Query("SELECT * FROM murmurs WHERE id = :murmurId")
    suspend fun getMurmurById(murmurId: String): MurmurEntity?

    @Query("SELECT * FROM murmurs WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getUserMurmurs(userId: String, limit: Int, offset: Int): List<MurmurEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMurmur(murmur: MurmurEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMurmurs(murmurs: List<MurmurEntity>)

    @Delete
    suspend fun deleteMurmur(murmur: MurmurEntity)

    @Query("DELETE FROM murmurs WHERE id = :murmurId")
    suspend fun deleteMurmurById(murmurId: String)

    @Query("DELETE FROM murmurs")
    suspend fun deleteAllMurmurs()

    @Update
    suspend fun updateMurmur(murmur: MurmurEntity)
}


