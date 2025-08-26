package com.systemcontrol.corpsele.systemcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class MoreAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MoreBackgroundService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}

