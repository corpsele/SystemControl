package com.systemcontrol.corpsele.systemcontrol;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {

//        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class); // 销毁时重新启动Service
        this.startService(localIntent);

        Intent localIntent1 = new Intent();
        localIntent1.setAction("com.systemcontrol.restart");
        sendBroadcast(localIntent);
    }
}
