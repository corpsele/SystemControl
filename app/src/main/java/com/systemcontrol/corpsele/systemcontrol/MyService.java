package com.systemcontrol.corpsele.systemcontrol;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyService extends Service {

    private static final int ID = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // 注意notification也要适配Android 8 哦
            startForeground(ID, new Notification());// 通知栏标识符 前台进程对象唯一ID
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "服务启动了 ", Toast.LENGTH_SHORT).show();
        RemoteViews rv = new  RemoteViews( this.getPackageName(), R.layout.new_app_widget);
        ComponentName cn = new ComponentName( this , NewAppWidget.class );
        AppWidgetManager am = AppWidgetManager.getInstance(this );
        am.updateAppWidget(cn, rv);

        Intent intent1 = new Intent(this, NewAppWidget.class);
        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent1);
        return super.onStartCommand(intent, flags, startId);
//        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
//        Intent localIntent = new Intent();
//        localIntent.setClass(this, MyService.class); // 销毁时重新启动Service
//        this.startService(localIntent);
//
//        Intent localIntent1 = new Intent();
//        localIntent1.setAction("com.systemcontrol.restart");
//        sendBroadcast(localIntent);
        Intent intent1=new Intent(this ,MyService.class );
        PendingIntent refreshIntent=PendingIntent.getService(this, 0 , intent1,  0 );
//        this.startService(intent1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent1);
        } else {
            this.startService(intent1);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), MyService.class);
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(),
                1,
                restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT & PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmService.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);

    }
}
