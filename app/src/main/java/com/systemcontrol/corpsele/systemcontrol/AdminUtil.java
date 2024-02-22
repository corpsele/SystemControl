package com.systemcontrol.corpsele.systemcontrol;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class AdminUtil extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        //设备管理可用
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        //设备管理不可用
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
    }

}