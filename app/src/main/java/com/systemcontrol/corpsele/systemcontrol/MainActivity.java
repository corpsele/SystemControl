package com.systemcontrol.corpsele.systemcontrol;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.getAudioDetail();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                requestAPI();
            }
        });
        thread.run();

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
            builder.sslSocketFactory(sslSocketFactory);

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
            String url = "https://api.apiopen.top/searchMusic";
//            RequestBody body = new FormBody.Builder().add("scope", "103").add("format", "json").add("appid", "379020").addEncoded("bk_key", "Android").add("bk_length", "600").build();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Map map = new HashMap();
//            map.put("scope", "103");
//            map.put("format", "json");
//            map.put("appid", "379020");
//            map.put("bk_key", "Android");
//            map.put("bk_length", "600");
            map.put("name", "Bye Bye Bye");
            Gson gson = new Gson();
            String param = gson.toJson(map);
            RequestBody body = RequestBody.create(JSON, param);

            Request request = new Request.Builder().url(url).post(body).build();
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
                    Result result = gson1.fromJson(responseResult, Result.class);
                    System.out.println(result.getMessage());
                    System.out.println(response.message());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    textView12.setVisibility(View.VISIBLE);
                }else{
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

    }

    private void pushOtherActivity(){
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("type", 1);
//        startActivity(intent);
        startActivityForResult(intent, 200);
    }

}
