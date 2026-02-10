package com.example.clsdk.mvvm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clsdk.mvvm.dao.ApiKeyDao
import com.example.clsdk.mvvm.model.ApiKeyModel

@Database(entities = [ApiKeyModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun apiKeyDao(): ApiKeyDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}