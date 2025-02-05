package com.systemcontrol.corpsele.systemcontrol

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.DataOutputStream

class AppsActivity : AppCompatActivity() {
        private lateinit var rvMain: RecyclerView
    private lateinit var adapter: AppsAdapter
    private lateinit var appList: ArrayList<AppDatas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)

        initViews()
    }

    private fun initViews() {
        // 初始化数据
//        appList = listOf(
//            AppDatas("app 1", "Item 1"),
//            AppDatas("app 2", "Item 2"),
//            // ... 添加更多项
//        )
        appList = ArrayList()
        appList.add(AppDatas("app 1", "Item 1"))
        appList.add(AppDatas("app 2", "Item 2"))
        getAllAppPackages()
        // 设置 RecyclerView
        rvMain = findViewById(R.id.rvMain)
        rvMain.layoutManager = LinearLayoutManager(this)
        adapter = AppsAdapter(appList,
            clickListener = { item ->
                // 处理点击事件
        Toast.makeText(this, "Clicked: $item", Toast.LENGTH_SHORT).show()
                enablePackage(this, item)
            },
            longClickListener = { item ->
                // 处理长按事件
        Toast.makeText(this, "Long clicked: $item", Toast.LENGTH_SHORT).show()
                disablePackage(this, item)
                true
            }
        )
        rvMain.adapter = adapter
        rvMain.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    // 使用 lazy 初始化一个 View
//    private val rvMain by lazy {
//        findViewById<RecyclerView>(R.id.rvMain)
//    }

    // 调用 deletePackage 方法删除应用
    // FLAG_DELETE_ALL_USERS 表示删除所有用户的安装
//        packageManager.deletePackage(packageName, null, PackageManager.DELETE_ALL_USERS)
    private fun getAllAppPackages() {
        // 获取PackageManager
        val pm: PackageManager = packageManager
        // 获取所有已安装的应用程序
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (app in apps) {
            val icon = app.loadIcon(pm)
            val appData = AppDatas("", "")
            appData.appName = app.loadLabel(pm) as String
            appData.packageName = app.packageName
            appList.add(appData)
            // 过滤系统应用
            if ((app.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 非系统应用
                println("App Name: ${app.loadLabel(pm)}, Package Name:${app.packageName}")
            } else {
                // 系统应用
                println("System App Name: ${app.loadLabel(pm)}, Package Name:${app.packageName}")
            }
        }
    }

    private fun disablePackage(context: Context, packageName: String) {
        val packageManager: PackageManager = context.packageManager
        try {
            // 调用 setApplicationEnabledSetting 方法来禁用应用
            // COMPONENT_ENABLED_STATE_DISABLED 禁用应用
            // DONT_KILL_APP 不杀死应用进程
            packageManager.setApplicationEnabledSetting(
                packageName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
            // 没有足够的权限去执行这个操作
        }
    }

    private fun enablePackage(context: Context, packageName: String) {
        val packageManager: PackageManager = context.packageManager
        try {
            // 调用 setApplicationEnabledSetting 方法来禁用应用
            // COMPONENT_ENABLED_STATE_DISABLED 禁用应用
            // DONT_KILL_APP 不杀死应用进程
            packageManager.setApplicationEnabledSetting(
                packageName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
            // 没有足够的权限去执行这个操作
        }
    }

    private fun disableComponent(componentName: String) {
        try {
            val command = "pm disable $componentName"
            val process = Runtime.getRuntime().exec("su") // 获取root权限
            val dos = DataOutputStream(process.outputStream)
            dos.writeBytes(command + "\n")
            dos.flush()
            dos.writeBytes("exit\n")
            dos.flush()
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}