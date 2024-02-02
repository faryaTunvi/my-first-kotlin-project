package com.fattyleo.mykotlinapp

import android.app.Application
import com.fattyleo.mykotlinapp.data.room.UserInfoDatabase
import com.fattyleo.mykotlinapp.repository.UserInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application(){

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    /** By using lazy the database and the repository are only created when they're needed
     * rather than when the application starts
     **/
    private val database by lazy { UserInfoDatabase.getDatabase(this) }
    val repository by lazy { UserInfoRepository(database.dataEntryDao()) }
}