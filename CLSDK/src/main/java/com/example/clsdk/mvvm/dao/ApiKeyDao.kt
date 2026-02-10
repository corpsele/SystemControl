package com.example.clsdk.mvvm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clsdk.mvvm.model.ApiKeyModel

import kotlinx.coroutines.flow.Flow
@Dao
interface ApiKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApiKey(model: ApiKeyModel)
    @Delete
    suspend fun deleteApiKey(model: ApiKeyModel)
    @Query("DELETE FROM ApiKey WHERE id = :id")
    suspend fun deleteApiKeyById(id: Int)
    @Query("SELECT * FROM ApiKey ORDER BY priority ASC, id DESC")
    // 使用 Flow 可以自动刷新，也可以用 LiveData
    fun getAllApiKey(): Flow<List<ApiKeyModel>>
}