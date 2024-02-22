package com.systemcontrol.corpsele.systemcontrol;

import static android.content.Context.DEVICE_POLICY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class LockScreenUtil {
    private DevicePolicyManager dpm;
    private Context contextMain;
    private Class classMain;

    public LockScreenUtil(Context context, Class cls){
        contextMain = context;
        classMain = cls;
        dpm = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
    }

    public void openAdmin() {
        // 创建一个Intent
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 我要激活谁
        ComponentName mDeviceAdminSample = new ComponentName(contextMain, AdminUtil.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
        // 劝说用户开启管理员权限
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"哥们开启我可以一键锁屏，你的按钮就不会经常失灵");
        contextMain.startActivity(intent);
    }

    /**
     * 一键锁屏
     */
    public void lockscreen() {
        ComponentName who = new ComponentName(contextMain, AdminUtil.class);
        if (dpm.isAdminActive(who)) {
            dpm.lockNow();// 锁屏
//            dpm.resetPassword("", 0);// 设置屏蔽密码
            // 清除Sdcard上的数据
            // dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
            // 恢复出厂设置
            // dpm.wipeData(0);
        } else {
            Toast.makeText(contextMain, "还没有打开管理员权限", Toast.LENGTH_SHORT).show();
            openAdmin();
            return;
        }
    }
}
