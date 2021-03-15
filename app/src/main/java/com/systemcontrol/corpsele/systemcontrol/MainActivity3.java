package com.systemcontrol.corpsele.systemcontrol;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        mWebView = findViewById(R.id.mWebView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportMultipleWindows(false);
        //是否允许JavaScript脚本运行，默认为false
        webSettings.setJavaScriptEnabled(true);
        //开启本地DOM存储
        webSettings.setDomStorageEnabled(true);
        //设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setJavaScriptEnabled(true);
        //是否可以缩放，默认true
        webSettings.setSupportZoom(true);
        //是否显示缩放按钮，默认false
        webSettings.setBuiltInZoomControls(true);
        //设置此属性，可任意比例缩放。大视图模式
        webSettings.setUseWideViewPort(true);
        //和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setLoadWithOverviewMode(true);
        //是否使用缓存
        webSettings.setAppCacheEnabled(true);
        //开启本地DOM存储
        webSettings.setDomStorageEnabled(true);
        // 加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //播放音频，多媒体需要用户手动？设置为false为可自动播放
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        mWebView.setWebViewClient(new MyWebViewClient());

        // REMOTE RESOURCE
        mWebView.loadUrl("https://www.baidu.com");

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");

    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}