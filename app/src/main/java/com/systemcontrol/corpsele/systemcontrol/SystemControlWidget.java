package com.systemcontrol.corpsele.systemcontrol;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.media.AudioManager;
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
        getAudioDetail();

//        CharSequence widgetText = SystemControlWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.system_control_widget);
//        views.setTextViewText(R.id.textView1, "SystemControl");



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private static void getAudioDetail(){
        //通话音量

        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        String strVoip = String.valueOf(current) + "/" + String.valueOf(max);
//        views.setTextViewText(R.id.tvVoipNum, strVoip);
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);


        //系统音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );


//铃声音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );


//音乐音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );



//提示声音音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );


    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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
}

