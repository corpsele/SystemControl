package com.systemcontrol.corpsele.systemcontrol

import android.content.Context
import android.content.Intent
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

class MainActivityK1 : AppCompatActivity() {
    private val TAG = MainActivityK1::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_k1)
        var info = "请开启\""+getString(R.string.accessibility_service_name)+"\"服务"
        Toast.makeText(this, info, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        checkAccessibilityServicePermission()
    }

    private fun checkAccessibilityServicePermission() {
        if (!isAccessibilitySettingsOn(applicationContext)) {
            Log.d("SettingActivity", "未开启 无障碍服务")
            //直接打开无障碍权限界面。如果要先打开一个确认界面，就去掉下面AlertDialog的代码注释。
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            DataManager.getInstance().onAcc = false
        } else {
            Log.d(TAG, "已开启 无障碍服务")
            DataManager.getInstance().onAcc = true
        }
    }

    private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service = packageName + "/" + MyAccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.applicationContext.contentResolver,
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            Log.e("", "Error finding setting, default accessibility to not found: " + e.message)
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                    mContext.applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
