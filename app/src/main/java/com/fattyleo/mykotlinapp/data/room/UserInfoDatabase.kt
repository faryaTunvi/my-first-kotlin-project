package com.fattyleo.mykotlinapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserInfoEntity::class], version = 1, exportSchema = false)
abstract class UserInfoDatabase : RoomDatabase() {

    abstract fun dataEntryDao(): DataEntryDao

    companion object {

        /**
         *  Singleton prevents multiple instances of database opening at the same time.
         */
        @Volatile
        private var INSTANCE: UserInfoDatabase? = null

        /**
         * Note: When you modify the database schema,
         *       you'll need to update the version number and define a migration strategy.
         */
        fun getDatabase(ctx: Context): UserInfoDatabase {
            return when (val temp = INSTANCE) {
                null -> synchronized(this) {
                    Room.databaseBuilder(
                        ctx.applicationContext, UserInfoDatabase::class.java,
                        "user_info"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                else -> temp
            }
        }
    }
}