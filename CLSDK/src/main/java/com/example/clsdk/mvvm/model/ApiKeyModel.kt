package com.example.clsdk.mvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ApiKey")
data class ApiKeyModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    // 1: High, 2: Medium, 3: Low
    val priority: Int,
    val apiKey: String,
)