package com.systemcontrol.corpsele.systemcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dueeeke.videocontroller.StandardVideoController;
//import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory;
import com.dueeeke.videoplayer.listener.OnVideoViewStateChangeListener;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;

public class DKPlayerActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_k_player);

        initView();
    }


    protected int getLayoutResId() {
        return R.layout.activity_player;
    }


    protected void initView() {
        mVideoView = findViewById(R.id.dk_player);

//        Intent intent = getIntent();
//        if (intent != null) {
            StandardVideoController controller = new StandardVideoController(this);
            //根据屏幕方向自动进入/退出全屏
            controller.setEnableOrientation(true);
//            boolean isLive = intent.getBooleanExtra("isLive", false);
//            if (isLive) {
//                controller.setLive();
//            }
//            String title = intent.getStringExtra(IntentKeys.TITLE);
            controller.setTitle("播放");
            mVideoView.setVideoController(controller);

            String source1 = "https://sample-videos.com/video123/mkv/720/big_buck_bunny_720p_30mb.mkv";

//            mVideoView.setUrl(intent.getStringExtra("url"));

            mVideoView.setUrl(source1);
            //保存播放进度
//            mVideoView.setProgressManager(new ProgressManagerImpl());
            //播放状态监听
//            mVideoView.addOnVideoViewStateChangeListener(mOnVideoViewStateChangeListener);

            //使用IjkPlayer解码
//            mVideoView.setPlayerFactory(IjkPlayerFactory.create());
            //使用ExoPlayer解码
            mVideoView.setPlayerFactory(ExoMediaPlayerFactory.create());
            //使用MediaPlayer解码
//            mVideoView.setPlayerFactory(AndroidMediaPlayerFactory.create());

            mVideoView.start();

//        }
    }

    private OnVideoViewStateChangeListener mOnVideoViewStateChangeListener = new OnVideoViewStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://小屏
                    break;
                case VideoView.PLAYER_FULL_SCREEN://全屏
                    break;
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    break;
                case VideoView.STATE_PREPARED:
                    //需在此时获取视频宽高
                    int[] videoSize = mVideoView.getVideoSize();
                    L.d("视频宽：" + videoSize[0]);
                    L.d("视频高：" + videoSize[1]);
                    break;
                case VideoView.STATE_PLAYING:
                    break;
                case VideoView.STATE_PAUSED:
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    break;
                case VideoView.STATE_ERROR:
                    break;
            }
        }
    };

    private int i = 0;

    public void onButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.scale_default:
                mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
                break;
            case R.id.scale_169:
                mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
                break;
            case R.id.scale_43:
                mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_4_3);
                break;
            case R.id.scale_original:
                mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_ORIGINAL);
                break;
            case R.id.scale_match_parent:
                mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT);
                break;
            case R.id.scale_center_crop:
                mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
                break;

            case R.id.speed_0_5:
                mVideoView.setSpeed(0.5f);
                break;
            case R.id.speed_0_75:
                mVideoView.setSpeed(0.75f);
                break;
            case R.id.speed_1_0:
                mVideoView.setSpeed(1.0f);
                break;
            case R.id.speed_1_5:
                mVideoView.setSpeed(1.5f);
                break;
            case R.id.speed_2_0:
                mVideoView.setSpeed(2.0f);
                break;

            case R.id.screen_shot:
                ImageView imageView = findViewById(R.id.iv_screen_shot);
                Bitmap bitmap = mVideoView.doScreenShot();
                imageView.setImageBitmap(bitmap);
                break;

            case R.id.mirror_rotate:
                mVideoView.setMirrorRotation(i % 2 == 0);
                i++;
                break;
        }
    }

}


class IntentKeys {

    public static final String URL = "url";
    public static final String SEAMLESS_PLAY = "seamless_play";
    public static final String TITLE = "title";
    public static final String IS_LIVE = "isLive";
}