package com.systemcontrol.corpsele.systemcontrol;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_MODERATE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.hjq.toast.Toaster;

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
    public static final String actionLockScreen = "ActionLockScreen";
    public static final String actionNeverSleep = "ActionNeverSleep";
    public static final String actionThirtySleep = "actionThirtySleep";
    public static final String actionBrightAdd = "ActionBrightAdd";
    public static final String actionBrightDec = "ActionBrightDec";
    public static final String actionCleanMemory = "ActionCleanMemory";

    private static AudioManager mAudioManager;
    private static LockScreenUtil lockScreenUtil;

    private MemoryCleaner memoryCleaner = null;

    private static int currentLight = 0;

    private NotiBigInterface notiBigInterface;

    public interface NotiBigInterface {
        public void setBrightCurrentText(String content);
        public void setBrightMaxText(String content);
    }

    public void setNotiBigInterfaceListener(NotiBigInterface notiBigInterface) {
        this.notiBigInterface = notiBigInterface;
    }

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
        else if (action.equals(actionLockScreen)){
            if (lockScreenUtil == null){
                lockScreenUtil = new LockScreenUtil(context, this.getClass());
            }
            lockScreenUtil.lockscreen();

        } else if (action.equals(actionNeverSleep)) {
            setScreenOffTime(Integer.MAX_VALUE, context);
        } else if (action.equals(actionThirtySleep)) {
            setScreenOffTime(30000, context);
        } else if (action.equals(actionBrightAdd)) {
            if (currentLight < 200 ){
                currentLight+=10;
            }else{
                currentLight = 200;
            }
            ContentResolver contentResolver = context.getContentResolver();
            Settings.System.putInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, currentLight);

        } else if (action.equals(actionBrightDec)) {
            if (currentLight > 0 ){
                currentLight-=10;
            }else{
                currentLight = 0;
            }
            ContentResolver contentResolver = context.getContentResolver();
            Settings.System.putInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, currentLight);
        }
        else if(action.equals(actionCleanMemory)){
            Toaster.show("clean memory");
            if (memoryCleaner == null){
                memoryCleaner = new MemoryCleaner(context);
            }
            memoryCleaner.cleanBackgroundProcesses();
            memoryCleaner.triggerGarbageCollection();
            memoryCleaner.onTrimMemory(TRIM_MEMORY_MODERATE);
        }


        getAudioDetail(remoteViewsBig, context);
        getSystemLight(context, remoteViewsBig);

        MyService myService = (MyService) context;
        myService.updateNotiControl();
    }

    // 获取当前休眠时间
    private int getScreenOffTime(Context context) {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenOffTime;
    }

    // 设置新的休眠时间，单位是毫秒
    private void setScreenOffTime(int paramInt, Context context) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
//            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Integer.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void SetSystemLight(int lightnumber, Context context, RemoteViews remoteViews){


    }

    private static void getSystemLight(Context context, RemoteViews remoteViews){
        if (!Settings.System.canWrite(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // 申请权限后做的操作


        }

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
        int brightnessSettingMaximumId = context.getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android");
        int brightnessSettingMaximum = context.getResources().getInteger(brightnessSettingMaximumId);
        int brightnessSettingMinimumId = context.getResources().getIdentifier("config_screenBrightnessSettingMinimum", "integer", "android");
        int brightnessSettingMinimum = context.getResources().getInteger(brightnessSettingMinimumId);

        currentLight = systemLight;

        remoteViews.setTextViewText(R.id.noti_big_tvBrightCurrent, String.valueOf(systemLight));
        remoteViews.setTextViewText(R.id.noti_big_tvBrightMax, String.valueOf(maxSystemLight));
    }
}
