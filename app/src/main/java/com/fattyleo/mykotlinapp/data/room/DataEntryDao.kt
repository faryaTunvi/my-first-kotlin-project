package com.fattyleo.mykotlinapp.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DataEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfo: UserInfoEntity)

    /**
     * A Flow is an async sequence of values
     * Flow produces values one at a time (instead of all at once) that can generate values from async operations
     * like network requests, database calls, or other async code.
     * It supports coroutines throughout its API, so you can transform a flow using coroutines as well!
     */
    @Query("SELECT * FROM user_info")
    fun getAllUsers(): Flow<List<UserInfoEntity>>

    @Query("delete from user_info where user_id = :u_id")
    suspend fun deleteById(u_id : Int)
}