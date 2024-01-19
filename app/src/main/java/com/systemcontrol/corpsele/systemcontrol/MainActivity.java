package com.systemcontrol.corpsele.systemcontrol;

import android.accessibilityservice.AccessibilityService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjq.toast.Toaster;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

public class MainActivity extends AppCompatActivity {
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
    private boolean hasChecked = false;

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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                requestAPI();
                requestAllAppPackage();
            }
        });
        thread.run();

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

}
