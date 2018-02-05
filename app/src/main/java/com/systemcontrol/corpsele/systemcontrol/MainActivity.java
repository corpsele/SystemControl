package com.systemcontrol.corpsele.systemcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.*;
import android.content.*;
import android.util.*;
import android.widget.SeekBar;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.getAudioDetail();
    }

    private void getAudioDetail(){
        //通话音量

        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        Log.d("VIOCE_CALL", "max : " + max + " current : " + current);

        mSeekBar1 = findViewById(R.id.seekBar1);
        mSeekBar1.setMax(max);
        mSeekBar1.setProgress(current);
        mSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

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

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        Log.d("SYSTEM", "max : " + max + " current : " + current);

        mSeekBar2 = findViewById(R.id.seekBar2);
        mSeekBar2.setMax(max);
        mSeekBar2.setProgress(current);
        mSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

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

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
        Log.d("RING", "max : " + max + " current : " + current);

        mSeekBar3 = findViewById(R.id.seekBar3);
        mSeekBar3.setMax(max);
        mSeekBar3.setProgress(current);
        mSeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

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

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        Log.d("MUSIC", "max : " + max + " current : " + current);

//提示声音音量

        max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        current = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
        Log.d("ALARM", "max : " + max + " current : " + current);
    }

}
