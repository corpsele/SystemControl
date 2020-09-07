package com.systemcontrol.corpsele.systemcontrol;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "服务启动了 ", Toast.LENGTH_SHORT).show();
        RemoteViews rv = new  RemoteViews( this.getPackageName(), R.layout.new_app_widget);
        ComponentName cn = new ComponentName( this , NewAppWidget.class );
        AppWidgetManager am = AppWidgetManager.getInstance(this );
        am.updateAppWidget(cn, rv);
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
}
