package com.fattyleo.mykotlinapp.repository

import com.fattyleo.mykotlinapp.data.room.DataEntryDao
import com.fattyleo.mykotlinapp.data.room.UserInfoEntity
import kotlinx.coroutines.flow.Flow

class UserInfoRepository(private val dataEntryDao: DataEntryDao) {

    /**
     *  Please Note : Room executes all queries on a separate thread.
     *  Observed Flow will notify the observer when the data has changed.
     */
    var allUsers: Flow<List<UserInfoEntity>> = dataEntryDao.getAllUsers()

    suspend fun insert(userInfo: UserInfoEntity) {
        dataEntryDao.insert(userInfo)
    }

    suspend fun deleteById(u_id : Int){
        dataEntryDao.deleteById(u_id)
    }
}