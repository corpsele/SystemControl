package com.systemcontrol.corpsele.systemcontrol;

import android.app.Service;

import android.accessibilityservice.AccessibilityService;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjq.toast.Toaster;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.Unit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import io.reactivex.rxjava3.core.*;

public class MainActivity extends AppCompatActivity implements NotiBroadcastReceiver.NotiBigInterface {
    private AudioManager mAudioManager;

    private SeekBar mSeekBar1;
    private TextView tvSeekCur1;
    private TextView tvSeekMax1;

    private SeekBar mSeekBar2;
    private TextView tvSeekCur2;
    private TextView tvSeekMax2;

    private SeekBar mSeekBar3;
    private TextView tvSeekCur3;
    private TextView tvSeekMax3;

    private SeekBar mSeekBar4;
    private TextView tvSeekCur4;
    private TextView tvSeekMax4;

    private SeekBar mSeekBar5;
    private TextView tvSeekCur5;
    private TextView tvSeekMax5;
    private TextView textView12;
    private CheckBox checkBox1;
    private Button btnPush1;
    private Button btnPushS;
    private Button btnNeverSleep;
    private Button btnThirtySleep;
    private TextView tvTotalCount;
    private TextView tvAvaliCount;
    private boolean hasChecked = false;

    private Button btnOpenService;
    private boolean isNotiBigContent = false;
    private CheckBox checkBoxIsNotiBig;

    private Button btnAdminPermission;

    private NotificationManager notificationManager;
    private NotiBroadcastReceiver notiBroadcastReceiver;

    private LockScreenUtil lockScreenUtil;

    public static final int OPEN_DRAW_OVERLAYS = 188;
    public static final int OP_BACKGROUND_START_ACTIVITY = 10021;
    private static final String NOTIFICATION_CHANNEL_ID = "Notification_Normal_Channel_ID";
    private static final String NOTIFICATION_CHANNEL_NAME = "Notification_Normal_Channel_Name";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "通知控制中心";

    private static final int NOTIFICATION_CODE = 20078;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toaster.init(this.getApplication());
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.getAudioDetail();

        if (!OpenNotificationsUtil.isNotificationEnabledForApp(this)) {//未开启通知，去开启
            OpenNotificationsUtil.openNotificationSettingsForApp(this);
        }

        if (!checkFloatPermission(this)){
            requestSettingCanDrawOverlays();
        }

        initUI();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                requestAPI();
                requestAllAppPackage();
            }
        });
        thread.run();

//        initNotiManager();

//        initReceiver();

        initMoreBackgroundServer();
    }

    private void initUI(){
        btnOpenService = findViewById(R.id.main_btnOpenService);
        checkBoxIsNotiBig = findViewById(R.id.main_checkboxIsBigNoti);
        RxView.clicks(btnOpenService).subscribe(new Observer<Unit>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Unit unit) {
                System.out.println("btnOpenService click ");
                Intent intent1=new Intent(getBaseContext() ,MyService.class );
                intent1.putExtra("identify","alwaysNotification");
                intent1.putExtra("isNotiBigCotent",isNotiBigContent);
                startService(intent1);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        RxView.clicks(checkBoxIsNotiBig).subscribe(new Observer<Unit>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Unit unit) {
                isNotiBigContent = !isNotiBigContent;
                DataManager.getInstance().setNotiBigContent(isNotiBigContent);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Button btnMainMenu = findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        String currentDateTime = GlobalUtil.getCurrentDateTimeInChina();
        Log.d("CurrentDateTime", currentDateTime); // 输出示例：2025年08月26日 星期二 10:25:10
        Toast.makeText(this, currentDateTime, Toast.LENGTH_SHORT).show();

        long timestamp = System.currentTimeMillis();
        Log.d("Timestamp", String.valueOf(timestamp)); // 输出当前时间戳

        String currentDateTime1 = DateTimeUtils.getCurrentDateTime();
        Log.d("DateTime", "当前时间: " + currentDateTime1);

        String futureDate = DateTimeUtils.addDaysToDate(7);
        Log.d("DateTime", "7天后: " + futureDate);

        String dayOfWeek = DateTimeUtils.getCurrentDayOfWeek();
        Log.d("DateTime", "今天是: " + dayOfWeek);

        LocalDate date1 = LocalDate.of(2025, 8, 1);
        LocalDate date2 = LocalDate.of(2025, 8, 26);
        long daysBetween = DateTimeUtils.daysBetweenDates(date1, date2);
        Log.d("DateTime", "相差天数: " + daysBetween);

        String currentDateTime2 = DateTimeUtilsLegacy.getCurrentDateTime();
        Log.d("DateTime", "当前时间: " + currentDateTime2);

        String futureDate1 = DateTimeUtilsLegacy.addDaysToDate(7);
        Log.d("DateTime", "7天后: " + futureDate1);

        String dayOfWeek1 = DateTimeUtilsLegacy.getCurrentDayOfWeek();
        Log.d("DateTime", "今天是: " + dayOfWeek1);


    }

    private void showPopupMenu(View anchorView) {
        // 1. 创建 PopupMenu 实例
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        popupMenu.setGravity(Gravity.END); // 设置菜单显示位置

        // 2. 加载菜单资源
        popupMenu.getMenuInflater().inflate(R.menu.menu_item_main, popupMenu.getMenu());

        // 3. 强制显示图标（通过反射）
        try {
            Object menuHelper = popupMenu.getClass().getDeclaredField("mPopup").get(popupMenu);
            Class<?> classPopupHelper = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. 设置菜单项点击事件
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.main_menu_item1) {
                Toast.makeText(this, "disenable timer schedule", Toast.LENGTH_SHORT).show();
                GlobalUtil.isEnableRepeatService = false;
                return true;
            } else if (id == R.id.main_menu_item2) {
                Toast.makeText(this, "enable timer schedule", Toast.LENGTH_SHORT).show();
                GlobalUtil.isEnableRepeatService = true;
                return true;
            } else if (id == R.id.main_menu_item3) {
                Toast.makeText(this, "set weekend", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.main_menu_item4) {
                Toast.makeText(this, "set worktime", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.main_menu_item5) {
                Toast.makeText(this, "item", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // 5. 显示菜单
        popupMenu.show();
    }

    private void initMoreBackgroundServer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        // 设置定时任务
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NewAppWidget.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 设置循环任务，间隔 15 分钟（Android 14 最小间隔）
        if (alarmManager != null) {
//            alarmManager.setRepeating(
//                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime(),
//                    15 * 60 * 1000, // 15 分钟
//                    pendingIntent
//            );
            alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    60, // 15 分钟
                    pendingIntent
            );
        }
    }

    private void initNotiManager(){
        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViewsNormal = new RemoteViews(this.getPackageName(), R.layout.notification_normal);
        RemoteViews remoteViewsBig = new RemoteViews(this.getPackageName(), R.layout.notification_big);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOngoing(true)
                .setCustomBigContentView(remoteViewsBig)
                .setCustomContentView(remoteViewsNormal);
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
        //推送通知
        notificationManager.notify(NOTIFICATION_CODE, builder.build());
        Notification notification = OpenNotificationsUtil.createNotification(this, "服务常驻通知", "APP正在运行中...", 0);

    }

    private void initReceiver(){
        notiBroadcastReceiver = new NotiBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(NotiBroadcastReceiver.actionOpenMain);
//        intentFilter.addAction(MyBroadcastReceiver.ACTION_2);
        registerReceiver(notiBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAudioDetail();


        if (hasChecked) {
            textView12.setVisibility(View.VISIBLE);
        } else {
            textView12.setVisibility(View.INVISIBLE);
        }

//        RemoteViews views = new RemoteViews(getPackageName(), R.layout.new_app_widget);
//        //获得appwidget管理实例，用于管理appwidget以便进行更新操作
//        Context tmp = NewAppWidget.mainContext;
//        if (NewAppWidget.mainContext == null){
//            tmp = this;
//        }
//        Intent intent = new Intent(tmp, NewAppWidget.class);
//        tmp.startService(intent);

//        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());//获得appwidget管理实例，用于管理appwidget以便进行更新操作
//        ComponentName componentName = new ComponentName(getApplicationContext(),NewAppWidget.class);//获得所有本程序创建的appwidget
//        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.new_app_widget);//获取远程视图
//        manager.updateAppWidget(componentName,remoteViews);

//        Intent intent = new Intent(this, Settings.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.new_app_widget);//获取远程视图
//        remoteViews.setOnClickPendingIntent(R.id.appwidget_brocast_btn, pendingIntent);

//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        appWidgetManager.updateAppWidget(new ComponentName(this.getPackageName(), NewAppWidget.class.getName()), remoteViews);

        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

//        AppWidgetManager manger = AppWidgetManager.getInstance(tmp);
//        // 相当于获得所有本程序创建的appwidget
//        ComponentName thisName = new ComponentName(this, NewAppWidget.class);
//        //更新widget
//        manger.updateAppWidget(thisName, views);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OpenNotificationsUtil.OPEN_APP_NOTIFICATION) {
            //1.创建普通消息通知
            //OpenNotificationsUtil.createNotification(this, "普通消息通知", "欢迎来到APP！", 0);

            //2.启动前台服务，创建服务常驻通知
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                startForegroundService(new Intent(this, MyService.class));
            } else {
                startService(new Intent(this, MyService.class));
            }
        }
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory);
            builder.sslSocketFactory(sslSocketFactory, new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            });

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void requestAPI() {
        try {


            OkHttpClient httpClient = getUnsafeOkHttpClient();
            String url = "https://api.apiopen.top/getAllUrl";
//            RequestBody body = new FormBody.Builder().add("scope", "103").add("format", "json").add("appid", "379020").addEncoded("bk_key", "Android").add("bk_length", "600").build();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Map map = new HashMap();
//            map.put("scope", "103");
//            map.put("format", "json");
//            map.put("appid", "379020");
//            map.put("bk_key", "Android");
//            map.put("bk_length", "600");
//            map.put("name", "Bye Bye Bye");
            Gson gson = new Gson();
            String param = gson.toJson(map);
//            RequestBody body = RequestBody.crea
            RequestBody body = RequestBody.create(param.getBytes(), JSON);

            Request request = new Request.Builder().url(url).get().build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    textView12.setText(e.getLocalizedMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseResult = response.body().string();
                    ResponseBody responseBody = response.body();

                    textView12.setText(response.message() + "\n" + responseResult);
                    Gson gson1 = new Gson();
//                    Type type = new TypeToken<Result>() {}.getType();
//                    Result result = gson1.fromJson(responseResult, Result.class);
//                    System.out.println(result.getMessage());
                    System.out.println(response.message());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void requestAllAppPackage() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> pis = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo pi : pis){
            System.out.println("pi = " + pi.applicationInfo.packageName);
            if (isSystemApp(pi)){
                System.out.println(pi.packageName + " is " + "系统应用");
            }else{
                System.out.println(pi.packageName + " is " + "非系统应用");
            }
        }
    }

    private boolean isSystemApp(PackageInfo pi) {
        boolean isSysApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
        boolean isSysUpd = (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
        return isSysApp || isSysUpd;
    }

    private void getAudioDetail() {
        //通话音量

        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);

        mSeekBar1 = findViewById(R.id.seekBar1);
        mSeekBar1.setMax(max);
        mSeekBar1.setProgress(current);

        mSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekCur1.setText(String.valueOf(progress));
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSeekCur1 = findViewById(R.id.tvSeekCur1);
        tvSeekCur1.setText(String.valueOf(current));

        tvSeekMax1 = findViewById(R.id.tvSeekMax1);
        tvSeekMax1.setText(String.valueOf(max));

        //系统音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.d("SYSTEM", "max : " + max + " current : " + current);

        mSeekBar2 = findViewById(R.id.seekBar2);
        mSeekBar2.setMax(max);
        mSeekBar2.setProgress(current);
        mSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekCur2.setText(String.valueOf(progress));
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, AudioManager.FLAG_SHOW_UI);
                getAudioDetail();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSeekCur2 = findViewById(R.id.tvSeekCur2);
        tvSeekCur2.setText(String.valueOf(current));

        tvSeekMax2 = findViewById(R.id.tvSeekMax2);
        tvSeekMax2.setText(String.valueOf(max));

//铃声音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        Log.d("RING", "max : " + max + " current : " + current);

        mSeekBar3 = findViewById(R.id.seekBar3);
        mSeekBar3.setMax(max);
        mSeekBar3.setProgress(current);
        mSeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekCur3.setText(String.valueOf(progress));
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress, AudioManager.FLAG_SHOW_UI);
                getAudioDetail();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSeekCur3 = findViewById(R.id.tvSeekCur3);
        tvSeekCur3.setText(String.valueOf(current));

        tvSeekMax3 = findViewById(R.id.tvSeekMax3);
        tvSeekMax3.setText(String.valueOf(max));

//音乐音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("MUSIC", "max : " + max + " current : " + current);

        mSeekBar4 = findViewById(R.id.seekBar4);
        mSeekBar4.setMax(max);
        mSeekBar4.setProgress(current);
        mSeekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekCur4.setText(String.valueOf(progress));
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
                getAudioDetail();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSeekCur4 = findViewById(R.id.tvSeekCur4);
        tvSeekCur4.setText(String.valueOf(current));

        tvSeekMax4 = findViewById(R.id.tvSeekMax4);
        tvSeekMax4.setText(String.valueOf(max));


//提示声音音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        Log.d("ALARM", "max : " + max + " current : " + current);

        mSeekBar5 = findViewById(R.id.seekBar5);
        mSeekBar5.setMax(max);
        mSeekBar5.setProgress(current);
        mSeekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekCur5.setText(String.valueOf(progress));
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, AudioManager.FLAG_SHOW_UI);
                getAudioDetail();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSeekCur5 = findViewById(R.id.tvSeekCur5);
        tvSeekCur5.setText(String.valueOf(current));

        tvSeekCur5 = findViewById(R.id.tvSeekMax5);
        tvSeekCur5.setText(String.valueOf(max));


        textView12 = findViewById(R.id.textView12);
        textView12.setVisibility(View.INVISIBLE);
        textView12.setMovementMethod(ScrollingMovementMethod.getInstance());

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasChecked = isChecked;
                if (isChecked) {
                    textView12.setVisibility(View.VISIBLE);
                } else {
                    textView12.setVisibility(View.INVISIBLE);
                }

            }
        });

        btnPush1 = findViewById(R.id.btnPush5);
        btnPush1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushOtherActivity();
            }
        });

        btnPushS = findViewById(R.id.buttonPushS);
        btnPushS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pushSActivity();
            }
        });

        Button btnKill = findViewById(R.id.btnKillService);
        RxView.clicks(btnKill).subscribe(new Observer<Unit>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Unit unit) {
                stopService();
                System.out.println("btnKill click ");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        btnAdminPermission = findViewById(R.id.btnAdminPermission);
        RxView.clicks(btnAdminPermission).subscribe(new Observer<Unit>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Unit unit) {
                if(lockScreenUtil == null){
                    lockScreenUtil = new LockScreenUtil(getBaseContext(), this.getClass());
                }
                if(lockScreenUtil.lockscreen() == false){
                    checkAmin();
                }
                System.out.println(" click ");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 200);
            } else {
                // 如果有权限做些什么
            }

        }

        btnNeverSleep = findViewById(R.id.btnNeverSleep);
        btnNeverSleep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setScreenOffTime(Integer.MAX_VALUE);
            }
        });

        btnThirtySleep = findViewById(R.id.btnThirtySleep);
        btnThirtySleep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setScreenOffTime(30000);
            }
        });

        String total = formatFileSize(getTotalInternalMemorySize(), false);
        String available = formatFileSize(getAvailableInternalMemorySize(), false);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalCount.setText(total);
        tvAvaliCount = findViewById(R.id.tvAvaliCount);
        tvAvaliCount.setText(available);

    }

    private void checkAmin(){
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(this, AdminUtil.class);

        if (!devicePolicyManager.isAdminActive(componentName)) {
            // 请求设备管理员权限
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, 200);
        } else {
            Toast.makeText(this, "已获得设备管理权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopService() {
        try {
//            Intent stopIntent = new Intent(this, MyService.class);
//            this.stopService(stopIntent);

//            AlarmManager service = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//            PendingIntent pending = PendingIntent.getBroadcast(this, 0, stopIntent,
//                    PendingIntent.FLAG_CANCEL_CURRENT);
//            service.cancel(pending);
//            Intent intent = new Intent(this, NewAppWidget.class);
            Intent intent = new Intent("com.action.cancelservice", null, this, NewAppWidget.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
//            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(intent);

        } catch (Exception e) {
            System.out.println(e);
            Log.e("error", e.getLocalizedMessage());
        }
    }

    // 获取当前休眠时间
    private int getScreenOffTime() {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenOffTime;
    }

    // 设置新的休眠时间，单位是毫秒
    private void setScreenOffTime(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, paramInt);
//            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, Integer.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushOtherActivity() {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("type", 1);
//        startActivity(intent);
        startActivityForResult(intent, 200);

    }

    private void pushSActivity() {
        if (DataManager.getInstance().getOnAcc()) {
            DataManager.getInstance().setPoped(false);
            return;
        }
        Intent intent = new Intent(MainActivity.this, MainActivityK1.class);
        intent.putExtra("type", 1);
//        startActivity(intent);
        startActivityForResult(intent, 200);

//        if (isAccessibilitySettingsOn()){
//            ComponentName component = new ComponentName(getApplicationContext(), MyService1.class);
//            getApplicationContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP);
//
//            Intent intent1 = new Intent("com.systemcontrol.corpsele.systemcontrol.ACCESSIBILITY_ACTION");
//            intent1.putExtra("action", AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
//            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);

//        }
    }

    public boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        final String service = "com.systemcontrol.corpsele.systemcontrol";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {

            String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return accessibilityFound;
    }

    private boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

    //权限打开
    private void requestSettingCanDrawOverlays() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intent, OPEN_DRAW_OVERLAYS);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OPEN_DRAW_OVERLAYS);
        } else {//4.4-6.0以下
            //无需处理了
        }
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

    @Override
    public void setBrightCurrentText(String content) {
        if (content != null){

        }
    }

    @Override
    public void setBrightMaxText(String content) {
        if (content != null){

        }
    }
}
