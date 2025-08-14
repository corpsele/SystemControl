package com.systemcontrol.corpsele.systemcontrol

import android.app.ActivityManager
import android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE
import android.content.ComponentCallbacks2.TRIM_MEMORY_MODERATE
import android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
import android.content.Context
import android.os.Build
import java.lang.ref.WeakReference

class MemoryCleaner(private val context: Context) {

    /**
     * 清理后台进程
     */
    fun cleanBackgroundProcesses() {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = activityManager.runningAppProcesses

        runningProcesses?.forEach { processInfo ->
            // 不清理当前应用和系统关键进程
            if (processInfo.processName != context.packageName &&
                processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activityManager.killBackgroundProcesses(processInfo.processName)
            }
        }
    }

    /**
     * 建议系统执行垃圾回收
     */
    fun triggerGarbageCollection() {
        System.gc()
        Runtime.getRuntime().runFinalization()
    }

    /**
     * 释放内存（适用于 Activity/Fragment）
     */
    fun onTrimMemory(level: Int) {
        when (level) {
            TRIM_MEMORY_UI_HIDDEN -> {
                // UI 隐藏时释放资源
                releaseResources()
            }
            TRIM_MEMORY_MODERATE, TRIM_MEMORY_COMPLETE -> {
                // 内存紧张时释放更多资源
                releaseResources()
                triggerGarbageCollection()
            }
        }
    }

    /**
     * 释放资源（示例：清除缓存、置空大对象）
     */
    private fun releaseResources() {
        // 示例：清除缓存
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { file ->
            if (file.name.endsWith(".cache")) {
                file.delete()
            }
        }

        // 示例：置空大对象
        largeObject = null
    }

    // 示例：大对象（如 Bitmap、大数组）
    private var largeObject: ByteArray? = null
}
