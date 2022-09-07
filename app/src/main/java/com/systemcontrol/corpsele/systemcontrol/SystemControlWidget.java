package com.systemcontrol.corpsele.systemcontrol;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.RemoteViews;
import android.media.AudioManager.*;
import android.util.*;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SystemControlWidgetConfigureActivity SystemControlWidgetConfigureActivity}
 */
public class SystemControlWidget extends AppWidgetProvider {
    private static RemoteViews views;
    private static AudioManager mAudioManager;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

//        CharSequence widgetText = SystemControlWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.system_control_widget);

//        views.setTextViewText(R.id.tvVoipNum, "SystemControl");

        getAudioDetail();
        getSystemLight(context);

        Intent fullIntent = new Intent(context, MainActivity.class);
        fullIntent.setAction("BTNSHOW_VIEW_ACTION");
        fullIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                fullIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //分别绑定id
//        views.setOnClickPendingIntent(R.id.progressBar1,getPendingIntent(context,R.id.progressBar1));//第一个按钮
//        views.setOnClickPendingIntent(R.id.progressBar2,getPendingIntent(context,R.id.progressBar1));//第一个按钮
//        views.setOnClickPendingIntent(R.id.progressBar3,getPendingIntent(context,R.id.progressBar1));//第一个按钮
//        views.setOnClickPendingIntent(R.id.tvTextView3,getPendingIntent(context,R.id.progressBar1));//第一个按钮
//        views.setOnClickPendingIntent(R.id.tvWidget1,getPendingIntent(context,R.id.progressBar1));//第一个按钮
//        views.setOnClickPendingIntent(R.id.tvWidget2,getPendingIntent(context,R.id.progressBar1));//第一个按钮
//        views.setOnClickPendingIntent(R.layout.system_control_widget,getPendingIntent(context,R.layout.system_control_widget));//第一个按钮
        views.setOnClickPendingIntent(R.id.btnShow, pendingIntent);
//        views.setOnClickPendingIntent(R.id.progressBar1, pendingIntent);
        views.setPendingIntentTemplate(R.id.btnShow, pendingIntent);

        if (SystemControlWidgetConfigureActivity.getCheckBox1State()){
            views.setViewVisibility(R.id.tvWidget1, 1);
            views.setViewVisibility(R.id.progressBar1, 1);
            views.setViewVisibility(R.id.tvSystemNum, 1);
        }else{
            views.setViewVisibility(R.id.tvWidget1, 0);
            views.setViewVisibility(R.id.progressBar1, 0);
            views.setViewVisibility(R.id.tvSystemNum, 0);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private static void getSystemLight(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        int defVal = 125;//没有拿到值时返回的默认值
        int systemLight = Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
        //获得亮度最大值
        int maxSystemLight = 0;
        try {
            maxSystemLight = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        views.setProgressBar(R.id.progressBar, maxSystemLight, systemLight, false);
    }

    private static void getAudioDetail(){
        //通话音量

        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        String strVoip = String.valueOf(current) + "/" + String.valueOf(max);
        views.setTextViewText(R.id.tvVoipNum, strVoip);
        views.setProgressBar(R.id.progressBar2, max, current, false);
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);



        //系统音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        String strSystem = String.valueOf(current) + "/" + String.valueOf(max);
        views.setTextViewText(R.id.tvSystemNum, strSystem);
        views.setProgressBar(R.id.progressBar1, max, current, false);
        Log.d("System", "max : " + max + " current : " + current);


//铃声音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );


//音乐音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        String strMusic = String.valueOf(current) + "/" + String.valueOf(max);
        views.setTextViewText(R.id.tvMusicNum, strMusic);
        views.setProgressBar(R.id.progressBar3, max, current, false);
        Log.d("Music", "max : " + max + " current : " + current);


//提示声音音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );


    }

    private static PendingIntent getPendingIntent(Context context, int resID){
        Intent intent = new Intent();
        //intent.setClass(context, AppWidget.class);//此时这句代码去掉
        intent.setAction("getAudioDetail");
        //设置data域的时候，把控件id一起设置进去，
        // 因为在绑定的时候，是将同一个id绑定在一起的，所以哪个控件点击，发送的intent中data中的id就是哪个控件的id
        intent.setData(Uri.parse("id:" + resID));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent,0);
        return pendingIntent;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.system_control_widget);
            Intent fullIntent = new Intent(context, MainActivity.class);
            fullIntent.setAction("APPWIDGET_CLICK");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
                    fullIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            v.setIntent(R.id.btnShow, "setOnClickListener", fullIntent);
//            views.setOnClickFillInIntent(R.id.btnShow, fullIntent);
            v.setOnClickPendingIntent(R.id.btnShow, pendingIntent);
//            v.setPendingIntentTemplate(R.id.btnShow, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, v);
        }
//        updateAppWidget(context, appWidgetManager, appWidgetIds);


        super.onUpdate(context, appWidgetManager, appWidgetIds);

        System.out.println("onUpdate");
    }

    public void setOnClick()
    {

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SystemControlWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.i("system", "onReceive");
        System.out.println("onReceive");

        if (intent.getAction().equals("getAudioDetail")) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.system_control_widget);
            Uri data = intent.getData();
            int resId = -1;
            if (data!=null){
                resId = Integer.parseInt(data.getSchemeSpecificPart());
            }
            switch (resId){
                case R.id.progressBar1:
//                    remoteViews.setImageViewResource(R.id.img, R.drawable.logo);
                    break;
            }
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context,SystemControlWidget.class);
            //更新widget
            manger.updateAppWidget(thisName,remoteViews);


        }else{

        }
    }
}

