package com.systemcontrol.corpsele.systemcontrol;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hjq.toast.Toaster;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private static int currentLight = 0;
    public static Context mainContext = null;
    private AlarmManager alarmService = null;


    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        mainContext = context;
        CharSequence widgetText = "打开activity";
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views1 = views;
        getAudioDetail(context);
        getSystemLight(context);
        getSysMemorySize();
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent fullIntent = new Intent(context, MainActivity.class);
//        fullIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        fullIntent.putExtra("pending_", "过了");
        PendingIntent Pfullintent = PendingIntent.getActivity(context, 0, fullIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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

        Intent lightAddAction = new Intent("com.action.lightAddAction",null,context,NewAppWidget.class);
        PendingIntent lightAddPendingIntent = PendingIntent.getBroadcast(context, 0, lightAddAction, 0);
        views.setOnClickPendingIntent(R.id.btnVoipAdd3, lightAddPendingIntent);

        Intent lightDecAction = new Intent("com.action.lightDecAction",null,context,NewAppWidget.class);
        PendingIntent lightDecPendingIntent = PendingIntent.getBroadcast(context, 0, lightDecAction, 0);
        views.setOnClickPendingIntent(R.id.btnVoipDec3, lightDecPendingIntent);

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

//    public static void showMessage(View view, String str, int length) {
//        Snackbar snackbar = Snackbar.make(view, str, length);
//
//        View snackbarView = snackbar.getView();
//        //设置布局居中
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(snackbarView.getLayoutParams().width, snackbarView.getLayoutParams().height);
//        params.gravity = Gravity.CENTER;
//        snackbarView.setLayoutParams(params);
//        //文字居中
//        TextView message = (TextView) snackbarView.findViewById(R.id.snackbar_text);
//        //View.setTextAlignment需要SDK>=17
//        message.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
//        message.setGravity(Gravity.CENTER);
//        message.setMaxLines(1);
//        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
//            @Override
//            public void onDismissed(Snackbar transientBottomBar, int event) {
//                super.onDismissed(transientBottomBar, event);
//                //Snackbar关闭
//            }
//
//            @Override
//            public void onShown(Snackbar transientBottomBar) {
//                super.onShown(transientBottomBar);
//                //Snackbar显示
//            }
//        });
//        snackbar.setAction("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //显示一个默认的Snackbar。
//                Snackbar.make(view, "我先走", BaseTransientBottomBar.LENGTH_LONG).show();
//            }
//        });
//        snackbar.show();
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews remoteViews = null;

        if (Objects.equals(intent.getAction(), "com.action.haha")) {
            Toast toast = Toast.makeText(context, "收到了", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, -50, 100);
            toast.show();
            Toaster.show("收到了");
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
            getSystemLight(context);
            getSysMemorySize();
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
//            context.startService(intent1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //适配8.0机制
                context.startForegroundService(intent1);
            } else {
                context.startService(intent1);
            }
            alarmService = alarm;
        }
        else if (Objects.equals(intent.getAction(), "com.action.cancelservice")){
            try{
                Intent intent1=new Intent(context ,MyService.class );
                PendingIntent refreshIntent=PendingIntent.getService(context, 0 , intent1,  0 );
                AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarm.cancel(refreshIntent);
            }catch(Exception e){
                System.out.println(e);
                Log.e("error", e.getLocalizedMessage());
            }
        }
        else if (Objects.equals(intent.getAction(), "com.action.musicAddAction")){
            Toast.makeText(context, "音乐音量加", Toast.LENGTH_SHORT).show();
            Toaster.show("音乐音量加");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (musicCurrent < musicMax){
                musicCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicCurrent, AudioManager.FLAG_SHOW_UI);
            }
getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.musicDecAction")){
            Toast.makeText(context, "音乐音量减", Toast.LENGTH_SHORT).show();
            Toaster.show("音乐音量减");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (musicCurrent > 0){
                musicCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.systemAddAction")){
            Toast.makeText(context, "系统音量加", Toast.LENGTH_SHORT).show();
            Toaster.show("系统音量加");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (systemCurrent < systemMax){
                systemCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.systemDecAction")){
            Toast.makeText(context, "系统音量减", Toast.LENGTH_SHORT).show();
            Toaster.show("系统音量减");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (systemCurrent > 0){
                systemCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.voipAddAction")){
            Toast.makeText(context, "通话音量加", Toast.LENGTH_SHORT).show();
            Toaster.show("通话音量加");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (voipCurrent < voipMax){
                voipCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voipCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.voipDecAction")){
            Toast.makeText(context, "通话音量减", Toast.LENGTH_SHORT).show();
            Toaster.show("通话音量减");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (voipCurrent > 0){
                voipCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voipCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.smsAddAction")){
            Toast.makeText(context, "提示音量加", Toast.LENGTH_SHORT).show();
            Toaster.show("提示音量加");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (smsCurrent < smsMax){
                smsCurrent++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, smsCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.smsDecAction")){
            Toast.makeText(context, "提示音量减", Toast.LENGTH_SHORT).show();
            Toaster.show("提示音量减");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (smsCurrent > 0){
                smsCurrent--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, smsCurrent, AudioManager.FLAG_SHOW_UI);
            }
            getAudioDetail(context);
            getSysMemorySize();
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.systemcontrol.restart")){
            Intent intent2=new Intent(context,NewAppWidget.class);
            context.startService(intent2);
        }else if (Objects.equals(intent.getAction(), "com.action.lightAddAction")){
            Toast.makeText(context, "系统亮度加", Toast.LENGTH_SHORT).show();
            Toaster.show("系统亮度加");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (currentLight < 200 ){
                currentLight+=10;
            }else{
                currentLight = 200;
            }
            ContentResolver contentResolver = context.getContentResolver();
            Settings.System.putInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, currentLight);
            getSystemLight(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }else if (Objects.equals(intent.getAction(), "com.action.lightDecAction")){
            Toast.makeText(context, "系统亮度减", Toast.LENGTH_SHORT).show();
            Toaster.show("系统亮度减");
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views1 = remoteViews;
            if (currentLight > 0 ){
                currentLight-=10;
            }else{
                currentLight = 0;
            }
            ContentResolver contentResolver = context.getContentResolver();
            Settings.System.putInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, currentLight);
            getSystemLight(context);
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新widget
            manger.updateAppWidget(thisName, remoteViews);
        }


    }

    public void getAllActivity(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.
                    getPackageName(), PackageManager.GET_ACTIVITIES);
            //所有的Activity
            ActivityInfo[] activities = packageInfo.activities;
            for (ActivityInfo activity :activities ) {
                Class<?> aClass = Class.forName(activity.name);
            }

        } catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static List<Activity> getActivitiesByApplication(Application application) {
        List<Activity> list = new ArrayList<>();
        try {
            Class<Application> applicationClass = Application.class;
            Field mLoadedApkField = applicationClass.getDeclaredField("mLoadedApk");
            mLoadedApkField.setAccessible(true);
            Object mLoadedApk = mLoadedApkField.get(application);
            Class<?> mLoadedApkClass = mLoadedApk.getClass();
            Field mActivityThreadField = mLoadedApkClass.getDeclaredField("mActivityThread");
            mActivityThreadField.setAccessible(true);
            Object mActivityThread = mActivityThreadField.get(mLoadedApk);
            Class<?> mActivityThreadClass = mActivityThread.getClass();
            Field mActivitiesField = mActivityThreadClass.getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            Object mActivities = mActivitiesField.get(mActivityThread);
            // 注意这里一定写成Map，低版本这里用的是HashMap，高版本用的是ArrayMap
            if (mActivities instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> arrayMap = (Map<Object, Object>) mActivities;
                for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                    Object value = entry.getValue();
                    Class<?> activityClientRecordClass = value.getClass();
                    Field activityField = activityClientRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Object o = activityField.get(value);
                    list.add((Activity) o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    public static String getCurrentActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }

    /**
     *调节当前屏幕亮度
     */
    public static void SetSystemLight(int lightnumber, Activity activity){
        Window window = activity.getWindow();//对当前窗口进行设置
        WindowManager.LayoutParams layoutparams = window.getAttributes();//获取窗口属性为后面亮度做铺垫作用
        layoutparams.screenBrightness =lightnumber / 255.0f;//用窗口管理（自定义的）layoutparams获取亮度值，android亮度值处于在0-255之间的整形数值
        window.setAttributes(layoutparams);//设置当前窗口屏幕亮度
    }

    private static void getSystemLight(Context context){
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
        views1.setProgressBar(R.id.progressBar, brightnessSettingMaximum, systemLight, false);
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

    private static void getSysMemorySize(){
        String total = formatFileSize(getTotalInternalMemorySize(), false);
        views1.setTextViewText(R.id.textView7, total);
        String available = formatFileSize(getAvailableInternalMemorySize(), false);
        views1.setTextViewText(R.id.textView9, available);
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    /**
     * 单位换算
     *
     * @param size 单位为B
     * @param isInteger 是否返回取整的单位
     * @return 转换后的单位
     */
    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        String fileSizeString = "0M";
        if (size < 1024 && size > 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }

}

