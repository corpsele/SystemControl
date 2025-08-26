package com.systemcontrol.corpsele.systemcontrol

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TimerSchedule: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timer_schedule)

        val textView: TextView = findViewById(R.id.tvTimerSchedule)
//        textView.text = "Hello, Android!"

        val btnTimerScheduleSet: Button = findViewById(R.id.btnTimerScheduleSet)
        btnTimerScheduleSet.setOnClickListener({
            showPopupMenu(it)
        })
    }

    private fun showPopupMenu(anchor: android.view.View){
//        val btnTimerScheduleSet: Button = findViewById(R.id.btnTimerScheduleSet)
        val popupMenu: PopupMenu = PopupMenu(this, anchor)
        popupMenu.menuInflater.inflate(R.menu.menuitem1, popupMenu.menu)

        // 强制显示图标（通过反射）
        try {
            val field = popupMenu.javaClass.getDeclaredField("mPopup")
            field.isAccessible = true
            val menuPopupHelper = field.get(popupMenu)
            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
            val setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
            setForceShowIcon.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    Toast.makeText(this, "disenable timer schedule", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_item2 -> {
                    Toast.makeText(this, "enable timer schedule", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_item3 -> {
                    Toast.makeText(this, "set weekend", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // 设置菜单位置（可选）
        popupMenu.gravity = Gravity.CENTER

        // 显示菜单
        popupMenu.show()

    }
}