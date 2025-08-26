package com.systemcontrol.corpsele.systemcontrol;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MoreBackgroundService extends Service {
       @Override
       public int onStartCommand(Intent intent, int flags, int startId) {
           // 执行后台任务
           new Thread(() -> {
               while (true) {
                   try {
                       Thread.sleep(60000); // 模拟任务执行间隔
                       Log.d("MoreBackgroundService", "Task is running...");
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }).start();

           // 启动前台服务，避免被系统杀死
           startForeground(1, createNotification());
           return START_STICKY; // 服务被杀死后自动重启
       }

       private Notification createNotification() {
           NotificationChannel channel = null;
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               channel = new NotificationChannel(
                       "background_service",
                       "Background Service",
                       NotificationManager.IMPORTANCE_LOW
               );
           }
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                       .createNotificationChannel(channel);
           }

//           return new NotificationCompat.Builder(this, "more_background_service")
//                   .setContentTitle("More Background Service")
//                   .setContentText("Running...")
//                   .build();
                   NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "more_background_service");
        Notification notification = builder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
           return notification;
       }

       @Override
       public IBinder onBind(Intent intent) {
           return null;
       }
   }

