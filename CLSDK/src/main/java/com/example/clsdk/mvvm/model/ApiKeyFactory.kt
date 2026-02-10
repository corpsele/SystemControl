package com.example.clsdk.mvvm.model

/**
 * 工厂类：负责创建 Task 对象
 * 封装初始化逻辑，例如根据标题长度自动设置优先级
 */
object ApiKeyFactory {
    fun createTask(title: String, description: String, apiKey: String): ApiKeyModel {
        // 工厂逻辑：自动计算优先级
        val priority = when {
            title.contains("紧急", ignoreCase = true) -> 1
            title.length > 20 -> 2
            else -> 3
        }
        // 工厂逻辑：清洗数据
        val cleanTitle = title.trim().takeIf { it.isNotEmpty() } ?: "无标题任务"
        val cleanDesc = description.trim()
        return ApiKeyModel(
            id = 0,
            title = cleanTitle,
            description = cleanDesc,
            isCompleted = false,
            priority = priority,
            apiKey = apiKey,

        )
    }
}