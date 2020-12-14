package com.systemcontrol.corpsele.systemcontrol

//import android.support.v7.app.AppCompatActivity;
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onContextClick
import org.jetbrains.anko.sdk25.coroutines.onLongClick

class MainActivityK1 : AppCompatActivity() {
    private val TAG = MainActivityK1::class.java.simpleName
    private lateinit var btnBack: Button
    private lateinit var btnAccAsk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_k1)
        var info = "请开启\"" + getString(R.string.accessibility_service_name) + "\"服务"
        Toast.makeText(this, info, Toast.LENGTH_LONG).show()
        btnBack = findViewById(R.id.btnBack);
        btnBack?.setOnClickListener {
            onBackPressed()
        }
        btnAccAsk = findViewById(R.id.btnAccAsk)
        btnAccAsk.setOnClickListener{
            checkAccessibilityServicePermission()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()

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
            Toast.makeText(mContext, "Error finding setting, default accessibility to not found: " + e.message, Toast.LENGTH_SHORT).show()
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
