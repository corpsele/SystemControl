package com.systemcontrol.corpsele.systemcontrol;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

public class NotiBroadcastReceiver extends BroadcastReceiver {
    public static final String actionOpenMain = "OpenMainActivity";
    public static final String actionGetSystemAudio = "GetSystemAudio";
    public static final String actionMusicAdd = "ActionMusicAdd";
    public static final String actionMusicDec = "ActionMusicDec";

    public static final String actionSystemAdd = "ActionSystemAdd";
    public static final String actionSystemDec = "ActionSystemDec";

    public static final String actionRingAdd = "ActionRingAdd";
    public static final String actionRingDec = "ActionRingDec";

    public static final String actionVoiceAdd = "ActionVoiceAdd";
    public static final String actionVoiceDec = "ActionVoiceDec";

    public static final String actionAlarmAdd = "ActionAlarmAdd";
    public static final String actionAlarmDec = "ActionAlarmDec";

    private static AudioManager mAudioManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        RemoteViews remoteViewsBig = new RemoteViews(context.getPackageName(), R.layout.notification_big);
        getAudioDetail(remoteViewsBig, context);
        if (action.equals(actionMusicAdd)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
            current++;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_SHOW_UI);
            //更新widget

        }
        else if (action.equals(actionMusicDec)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
            current--;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionSystemAdd)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
            current++;
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionSystemDec)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
            current--;
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionRingAdd)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
            current++;
            mAudioManager.setStreamVolume(AudioManager.STREAM_RING, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionRingDec)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
            current--;
            mAudioManager.setStreamVolume(AudioManager.STREAM_RING, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionVoiceAdd)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
            current++;
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionVoiceDec)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
            current--;
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionAlarmAdd)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
            current++;
            mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, current, AudioManager.FLAG_SHOW_UI);

        }
        else if (action.equals(actionAlarmDec)){
            int current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
            current--;
            mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, current, AudioManager.FLAG_SHOW_UI);

        }

        getAudioDetail(remoteViewsBig, context);

        MyService myService = (MyService) context;
        myService.updateNotiControl();
    }

    private void getAudioDetail(RemoteViews remoteViews, Context context){
        if (mAudioManager == null){
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
