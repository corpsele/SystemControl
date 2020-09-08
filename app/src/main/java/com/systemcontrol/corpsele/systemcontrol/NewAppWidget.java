package com.systemcontrol.corpsele.systemcontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static boolean isTurning = false;
    private static AudioManager mAudioManager;
    private static RemoteViews views1 = null;
    private static int musicCurrent = 0;
    private static int musicMax = 0;
    private static int systemCurrent = 0;
    private static int systemMax = 0;
    private static int voipCurrent = 0;
    private static int voipMax = 0;
    private static int smsCurrent = 0;
    private static int smsMax = 0;
    public static Context mainContext = null;


    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        mainContext = context;
        CharSequence widgetText = "打开activity";
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views1 = views;
        getAudioDetail(context);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent fullIntent = new Intent(context, MainActivity.class);
//        fullIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        fullIntent.putExtra("pending_", "过了");
        PendingIntent Pfullintent = PendingIntent.getActivity(context, 0, fullIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, Pfullintent);
        /*
       打开activity
         */
        //打开activity按钮不带参数
        //        PendingIntent Pfullintent = PendingIntent.getActivity(context, 0, fullIntent, 0);
        //打开activity按钮带参数
        //        PendingIntent Pfullintent = PendingIntent.getActivity(context, 0, fullIntent, PendingIntent.FLAG_CANCEL_CURRENT);

       /*
       打开service
         */
        Intent serviceIntent = new Intent(context, MyService.class);
        PendingIntent servicePendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_service_btn, servicePendingIntent);

        Intent sIntent = new Intent("com.action.service", null,context,NewAppWidget.class);
        PendingIntent sPendingIntent = PendingIntent.getBroadcast(context,0,sIntent,0);
        views.setOnClickPendingIntent(R.id.count_down, sPendingIntent);

        /*
       发送action
         */
//        Intent anIntent = new Intent("com.action.haha");
        Intent iIntent = new Intent("com.action.haha",null,context,NewAppWidget.class);
        PendingIntent anPendingIntent = PendingIntent.getBroadcast(context, 0, iIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_brocast_btn, anPendingIntent);

        Intent musicAddIntent = new Intent("com.action.musicAddAction",null,context,NewAppWidget.class);
        PendingIntent musicAddPendingIntent = PendingIntent.getBroadcast(context, 0, musicAddIntent, 0);
        views.setOnClickPendingIntent(R.id.btnMusicAdd, musicAddPendingIntent);

        Intent musicDecIntent = new Intent("com.action.musicDecAction",null,context,NewAppWidget.class);
        PendingIntent musicDecPendingIntent = PendingIntent.getBroadcast(context, 0, musicDecIntent, 0);
        views.setOnClickPendingIntent(R.id.btnMusicDec, musicDecPendingIntent);

        Intent systemAddIntent = new Intent("com.action.systemAddAction",null,context,NewAppWidget.class);
        PendingIntent systemAddPendingIntent = PendingIntent.getBroadcast(context, 0, systemAddIntent, 0);
        views.setOnClickPendingIntent(R.id.btnSystemAdd, systemAddPendingIntent);

        Intent systemDecIntent = new Intent("com.action.systemDecAction",null,context,NewAppWidget.class);
        PendingIntent systemDecPendingIntent = PendingIntent.getBroadcast(context, 0, systemDecIntent, 0);
        views.setOnClickPendingIntent(R.id.btnSystemDec, systemDecPendingIntent);

        Intent voipAddIntent = new Intent("com.action.voipAddAction",null,context,NewAppWidget.class);
        PendingIntent voipAddPendingIntent = PendingIntent.getBroadcast(context, 0, voipAddIntent, 0);
        views.setOnClickPendingIntent(R.id.btnVoipAdd, voipAddPendingIntent);

        Intent voipDecIntent = new Intent("com.action.voipDecAction",null,context,NewAppWidget.class);
        PendingIntent voipDecPendingIntent = PendingIntent.getBroadcast(context, 0, voipDecIntent, 0);
        views.setOnClickPendingIntent(R.id.btnVoipDec, voipDecPendingIntent);

        Intent smsAddIntent = new Intent("com.action.smsAddAction",null,context,NewAppWidget.class);
        PendingIntent smsAddPendingIntent = PendingIntent.getBroadcast(context, 0, smsAddIntent, 0);
        views.setOnClickPendingIntent(R.id.btnVoipAdd2, smsAddPendingIntent);

        Intent smsDecIntent = new Intent("com.action.smsDecAction",null,context,NewAppWidget.class);
        PendingIntent smsDecPendingIntent = PendingIntent.getBroadcast(context, 0, smsDecIntent, 0);
        views.setOnClickPendingIntent(R.id.btnVoipDec2, smsDecPendingIntent);

//        CountDownTimer countDownTimer = new CountDownTimer(40000, 1000) {
//            @Override
//            public void onTick(long l) {
//                String widgetText = "倒计时" + l;
//                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//                views.setTextViewText(R.id.count_down, widgetText);
//                appWidgetManager.updateAppWidget(appWidgetId, views);
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
//        countDownTimer.start();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        mainContext = context;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        mainContext = context;
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        mainContext = context;
        // Enter relevant functionality for when the last widget is disabled
        Intent intent=new Intent(context, NewAppWidget.class);
        context.stopService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews remoteViews = null;

        if (Objects.equals(intent.getAction(), "com.action.haha")) {
            Toast.makeText(context, "收到了", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (isTurning) {
//                remoteViews.setImageViewResource(R.id.widget_iv, R.mipmap.b2);
                isTurning = false;
            } else {
//                remoteViews.setImageViewResource(R.id.widget_iv, R.mipmap.b1);
                isTurning = true;
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if(Objects.equals(intent.getAction(), "com.action.service")){
            Intent intent1=new Intent(context ,MyService.class );
            PendingIntent refreshIntent=PendingIntent.getService(context, 0 , intent1,  0 );
            AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC, 0 ,  1000 , refreshIntent);
            context.startService(intent);
        }
        else if (Objects.equals(intent.getAction(), "com.action.musicAddAction")){
            Toast.makeText(context, "音乐音量加", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (musicCurrent < musicMax){
                musicCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicCurrent, AudioManager.FLAG_SHOW_UI);
            }
getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.musicDecAction")){
            Toast.makeText(context, "音乐音量减", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (musicCurrent > 0){
                musicCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.systemAddAction")){
            Toast.makeText(context, "系统音量加", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (systemCurrent < systemMax){
                systemCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.systemDecAction")){
            Toast.makeText(context, "系统音量减", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (systemCurrent > 0){
                systemCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.voipAddAction")){
            Toast.makeText(context, "通话音量加", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (voipCurrent < voipMax){
                voipCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voipCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.voipDecAction")){
            Toast.makeText(context, "通话音量减", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (voipCurrent > 0){
                voipCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voipCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.smsAddAction")){
            Toast.makeText(context, "提示音量加", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (smsCurrent < smsMax){
                smsCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, smsCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.smsDecAction")){
            Toast.makeText(context, "提示音量减", Toast.LENGTH_SHORT).show();
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (smsCurrent > 0){
                smsCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, smsCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.systemcontrol.restart")){
            Intent intent2=new Intent(context,NewAppWidget.class);
            context.startService(intent2);
        }


    }

    private static void getAudioDetail(Context context){
        if (mAudioManager == null){
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //通话音量

        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        String strVoip = String.valueOf(current) + "/" + String.valueOf(max);
        views1.setTextViewText(R.id.tvVoipNum, strVoip);
        views1.setProgressBar(R.id.progressBar2, max, current, false);
        voipCurrent = current;
        voipMax = max;
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);



        //系统音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        String strSystem = String.valueOf(current) + "/" + String.valueOf(max);
        views1.setTextViewText(R.id.tvSystemNum, strSystem);
        views1.setProgressBar(R.id.progressBar1, max, current, false);
        systemCurrent = current;
        systemMax = max;
        Log.d("System", "max : " + max + " current : " + current);


//铃声音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );


//音乐音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        String strMusic = String.valueOf(current) + "/" + String.valueOf(max);
        views1.setTextViewText(R.id.tvMusicNum, strMusic);
        views1.setProgressBar(R.id.progressBar3, max, current, false);
        musicCurrent = current;
        musicMax = max;
        Log.d("Music", "max : " + max + " current : " + current);



//提示声音音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
        String strSMS = String.valueOf(current) + "/" + String.valueOf(max);
        views1.setTextViewText(R.id.tvVoipNum2, strSMS);
        views1.setProgressBar(R.id.progressBar5, max, current, false);
        smsCurrent = current;
        smsMax = max;


    }
}

