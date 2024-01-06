package com.systemcontrol.corpsele.systemcontrol

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*

import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    private lateinit var webView: WebView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        initViews()
    }


    private fun initViews() {
        webView = this.findViewById(R.id.webView);

        var mWebSettings: WebSettings = webView.settings
        mWebSettings.javaScriptEnabled = true;//是否允许JavaScript脚本运行，默认为false
        mWebSettings.domStorageEnabled = true;//开启本地DOM存储
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true;//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.javaScriptEnabled = true;//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setSupportZoom(true);//是否可以缩放，默认true
        mWebSettings.builtInZoomControls = true;//是否显示缩放按钮，默认false
        mWebSettings.useWideViewPort = true;//设置此属性，可任意比例缩放。大视图模式
        mWebSettings.loadWithOverviewMode = true;//和setUseWideViewPort(true)一起解决网页自适应问题
//        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
        mWebSettings.cacheMode = WebSettings.LOAD_DEFAULT
        mWebSettings.domStorageEnabled = true;//开启本地DOM存储
        mWebSettings.loadsImagesAutomatically = true; // 加载图片
        mWebSettings.mediaPlaybackRequiresUserGesture = false;//播放音频，多媒体需要用户手动？设置为false为可自动播放

        webView.webViewClient = HWebViewClient()

        webView.loadUrl("www.baidu.com")

    }
}

private class HWebViewClient: WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
    }
}