package com.example.clsdk.mvvm.repository


import com.example.clsdk.mvvm.database.AppDatabase
import com.example.clsdk.mvvm.model.ApiKeyFactory
import com.example.clsdk.mvvm.model.ApiKeyModel
import kotlinx.coroutines.flow.Flow
class ApiKeyRepository(private val database: AppDatabase) {
    val allApiKey: Flow<List<ApiKeyModel>> = database.apiKeyDao().getAllApiKey()
    suspend fun addApiKey(title: String, description: String, apiKey: String) {
        // 关键点：在这里调用工厂创建对象
        val apiKey = ApiKeyFactory.createTask(title, description, apiKey)
        database.apiKeyDao().insertApiKey(apiKey)
    }
    suspend fun deleteApikey(model: ApiKeyModel) {
        database.apiKeyDao().deleteApiKey(model)
    }
}