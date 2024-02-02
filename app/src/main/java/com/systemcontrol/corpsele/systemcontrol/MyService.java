package com.systemcontrol.corpsele.systemcontrol;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyService extends Service {

    private static final int ID = 0;

    private String notificationId = "serviceid";
    private String notificationName = "servicename";

    private String notificationId1 = "serviceid1";
    private String notificationName1 = "servicename1";

    private NotificationManager notificationManager;
    private NotiBroadcastReceiver notiBroadcastReceiver;
    private Notification notificationControl;

    private static AudioManager mAudioManager;

    private static final String NOTIFICATION_CHANNEL_ID = "Notification_Normal_Channel_ID";
    private static final String NOTIFICATION_CHANNEL_NAME = "Notification_Normal_Channel_Name";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "通知控制中心";

    private static final int NOTIFICATION_CODE = 20078;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void showNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1,getNotification());
    }

    private void showNotification(String title, String content){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationId1, notificationName1, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(2,getNotification(title, content));
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)//通知的图片
                .setContentTitle("SystemControl运行中")
                .setContentText("开启通知刷新音量存储");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        Notification notification = builder.build();
        return notification;
    }

    private Notification getNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)//通知的图片
                .setContentTitle(title)
                .setContentText(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId1);
        }
        Notification notification = builder.build();
        return notification;
    }

    @SuppressLint("NotificationId0")
    @Override
    public void onCreate() {

//        showNotification();

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // 注意notification也要适配Android 8 哦
//            startForeground(ID, new Notification());// 通知栏标识符 前台进程对象唯一ID
//        }
//
//        Notification notification = OpenNotificationsUtil.createNotification(this, "服务常驻通知", "APP正在运行中...", 0);
//        startForeground(OpenNotificationsUtil.OPEN_SERVICE_NOTIFICATION_ID, notification);//显示常驻通知

        initNotiManager(DataManager.getInstance().getNotiBigContent());
        initReceiver();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // 注意notification也要适配Android 8 哦
            startForeground(NOTIFICATION_CODE, notificationControl);
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            String identify = "";
            if(intent == null && intent.getStringExtra("identify") == null){
                identify = "showNotification";
            }else{
                identify = intent.getStringExtra("identify");
            }
            if (identify != null && identify.length() > 0){
                if(identify.contains("showNotification")){
                    Toast.makeText(this, "推送成功", Toast.LENGTH_LONG).show();
                    String title = intent.getStringExtra("notificationTitle");
                    String content = intent.getStringExtra("notificationContent");
                    showNotification(title,content);
                }else if(identify.contains("alwaysNotification")){
                    boolean isNotiBigContent = intent.getBooleanExtra("isNotiBigCotent", false);
//                Notification notification = OpenNotificationsUtil.createNotification(this, "服务常驻通知", "APP正在运行中...", 0);
//                startForeground(OpenNotificationsUtil.OPEN_SERVICE_NOTIFICATION_ID, notification);//显示常驻通知

                    initNotiManager(DataManager.getInstance().getNotiBigContent());
                    initReceiver();
                }
//                return super.onStartCommand(intent, flags, startId);
            }
        }catch (Exception e){
            Log.e("crash error", e.getLocalizedMessage());
            Toast.makeText(this, "服务异常 " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            initNotiManager(DataManager.getInstance().getNotiBigContent());
//            initReceiver();
//            return super.onStartCommand(intent, flags, startId);

        }finally {

        }


        Toast.makeText(this, "服务启动了 ", Toast.LENGTH_SHORT).show();
        RemoteViews rv = new RemoteViews( this.getPackageName(), R.layout.new_app_widget);

        getAudioDetail(rv);

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

    private void initNotiManager(boolean isBig){
        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViewsNormal = new RemoteViews(this.getPackageName(), R.layout.notification_normal);
        RemoteViews remoteViewsBig = new RemoteViews(this.getPackageName(), R.layout.notification_big);

        //get Audio
        Intent iBtnGetAudio = new Intent(NotiBroadcastReceiver.actionGetSystemAudio);
        PendingIntent piBtnGetAudio = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnGetAudio, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnGetAudio, piBtnGetAudio);

        //music
        Intent iBtnMusicAdd = new Intent(NotiBroadcastReceiver.actionMusicAdd);
        PendingIntent piBtnMusicAdd = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnMusicAdd, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnMusicAdd, piBtnMusicAdd);

        Intent iBtnMusicDec = new Intent(NotiBroadcastReceiver.actionMusicDec);
        PendingIntent piBtnMusicDec = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnMusicDec, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnMusicDec, piBtnMusicDec);

        //system
        Intent iBtnSystemAdd = new Intent(NotiBroadcastReceiver.actionSystemAdd);
        PendingIntent piBtnSystemAdd = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnSystemAdd, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnSystemAdd, piBtnSystemAdd);

        Intent iBtnSystemDec = new Intent(NotiBroadcastReceiver.actionSystemDec);
        PendingIntent piBtnSystemDec = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnSystemDec, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnSystemDec, piBtnSystemDec);

        //ring
        Intent iBtnRingAdd = new Intent(NotiBroadcastReceiver.actionRingAdd);
        PendingIntent piBtnRingAdd = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnRingAdd, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnRingAdd, piBtnRingAdd);

        Intent iBtnRingDec = new Intent(NotiBroadcastReceiver.actionRingDec);
        PendingIntent piBtnRingDec = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnRingDec, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnRingDec, piBtnRingDec);

        //voice
        Intent iBtnVoiceAdd = new Intent(NotiBroadcastReceiver.actionVoiceAdd);
        PendingIntent piBtnVoiceAdd = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnVoiceAdd, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnVoiceAdd, piBtnVoiceAdd);

        Intent iBtnVoiceDec = new Intent(NotiBroadcastReceiver.actionVoiceDec);
        PendingIntent piBtnVoiceDec = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnVoiceDec, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnVoiceDec, piBtnVoiceDec);

        //alarm
        Intent iBtnAlarmAdd = new Intent(NotiBroadcastReceiver.actionAlarmAdd);
        PendingIntent piBtnAlarmAdd = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnAlarmAdd, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnAlarmAdd, piBtnAlarmAdd);

        Intent iBtnAlarmDec = new Intent(NotiBroadcastReceiver.actionAlarmDec);
        PendingIntent piBtnAlarmDec = PendingIntent.getBroadcast(getBaseContext(), 0, iBtnAlarmDec, 0);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViewsBig.setOnClickPendingIntent(R.id.noti_big_btnAlarmDec, piBtnAlarmDec);


        getAudioDetail(remoteViewsBig);

        NotificationCompat.Builder builder = null;
        boolean isNotiBigContent = DataManager.getInstance().getNotiBigContent();
        if (isNotiBigContent){
            builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setOnlyAlertOnce(true)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setCustomBigContentView(remoteViewsBig)
                    .setStyle(new NotificationCompat.BigTextStyle())
//                .setCustomHeadsUpContentView(remoteViewsBig)
                    .setCustomContentView(remoteViewsBig);
        }else{
            builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setOnlyAlertOnce(true)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setCustomBigContentView(remoteViewsBig)
                    .setStyle(new NotificationCompat.BigTextStyle())
//                .setCustomHeadsUpContentView(remoteViewsBig)
                    .setCustomContentView(remoteViewsNormal);
        }
        //设置优先级
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        } else {
//            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        //设置点击通知栏要跳转的Activity
        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(Constants.EXTRA.NOTIFICATION_FROM, Constants.NotificationType.FROM_NOTIFICATION);
//        intent.putExtra(Constants.EXTRA.NOTIFICATION_TYPE, NOTIFICATION_CODE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        }
        //Android8以上需要设置通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mNotificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mNotificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            mNotificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(mNotificationChannel);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        Notification notification = builder.build();
        notificationControl = notification;
        //推送通知
        notificationManager.notify(NOTIFICATION_CODE, builder.build());

    }

    public void updateNotiControl(){
        initNotiManager(DataManager.getInstance().getNotiBigContent());
    }

    private void initReceiver(){
        if(notiBroadcastReceiver == null){
            notiBroadcastReceiver = new NotiBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(NotiBroadcastReceiver.actionOpenMain);
            intentFilter.addAction(NotiBroadcastReceiver.actionGetSystemAudio);
            intentFilter.addAction(NotiBroadcastReceiver.actionMusicAdd);
            intentFilter.addAction(NotiBroadcastReceiver.actionMusicDec);
            intentFilter.addAction(NotiBroadcastReceiver.actionSystemAdd);
            intentFilter.addAction(NotiBroadcastReceiver.actionSystemDec);
            intentFilter.addAction(NotiBroadcastReceiver.actionRingAdd);
            intentFilter.addAction(NotiBroadcastReceiver.actionRingDec);
            intentFilter.addAction(NotiBroadcastReceiver.actionAlarmAdd);
            intentFilter.addAction(NotiBroadcastReceiver.actionAlarmDec);
            intentFilter.addAction(NotiBroadcastReceiver.actionVoiceAdd);
            intentFilter.addAction(NotiBroadcastReceiver.actionVoiceDec);
            registerReceiver(notiBroadcastReceiver, intentFilter);
        }

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
//        Intent intent1=new Intent(this ,MyService.class );
//        PendingIntent refreshIntent=PendingIntent.getService(this, 0 , intent1,  0 );
//        this.startService(intent1);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.startForegroundService(intent1);
//        } else {
//            this.startService(intent1);
//        }

//        unregisterReceiver(notiBroadcastReceiver);
    }

    private void getAudioDetail(RemoteViews remoteViews){
        if (mAudioManager == null){
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }

        //通话音量

        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        String currentVoice = String.valueOf(current);
        String maxVoice = String.valueOf(max);
        remoteViews.setTextViewText(R.id.noti_big_tvVoiceMax, maxVoice);
        remoteViews.setTextViewText(R.id.noti_big_tvVoiceCurrent, currentVoice);
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);



        //系统音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        String currentSystem = String.valueOf(current);
        String maxSystem = String.valueOf(max);
        remoteViews.setTextViewText(R.id.noti_big_tvSystemCurrent, currentSystem);
        remoteViews.setTextViewText(R.id.noti_big_tvSystemMax, maxSystem);

        Log.d("System", "max : " + max + " current : " + current);


//铃声音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
        String currentRing = String.valueOf(current);
        String maxRing = String.valueOf(max);
        remoteViews.setTextViewText(R.id.noti_big_tvRingCurrent, currentRing);
        remoteViews.setTextViewText(R.id.noti_big_tvRingMax, maxRing);

//音乐音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        String currentMusic = String.valueOf(current);
        String maxMusic = String.valueOf(max);
        remoteViews.setTextViewText(R.id.noti_big_tvMusicCurrent, currentMusic);
        remoteViews.setTextViewText(R.id.noti_big_tvMusicMax, maxMusic);
        Log.d("Music", "max : " + max + " current : " + current);



//提示声音音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
        String currentAlarm = String.valueOf(current);
        String maxAlarm = String.valueOf(max);
        remoteViews.setTextViewText(R.id.noti_big_tvAlarmCurrent, currentAlarm);
        remoteViews.setTextViewText(R.id.noti_big_tvAlarmMax, maxAlarm);


    }
}
