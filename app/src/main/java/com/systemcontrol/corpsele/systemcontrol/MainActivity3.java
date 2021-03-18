package com.systemcontrol.corpsele.systemcontrol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity3 extends AppCompatActivity {

    private WebView mWebView;
    private EditText editText;
    private Button button;
    private Menu menu1;
    private Set<String> set;
    private RecyclerView recyclerView;
    private RecycleAdapterDome recycleAdapterDome;
    private PopupMenu popupMenu;
    private List<String> list = new ArrayList<>();

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

        editText = findViewById(R.id.editView);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                return true;
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editText.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View v) {
                return true;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    popupMenu(v).show();
                }else{
                    popupMenu(v).dismiss();
                }
            }
        });


        button = findViewById(R.id.btnSearch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set == null){
                    set = new HashSet<String>();
                }

                set.add(editText.getText().toString());
                mWebView.loadUrl(editText.getText().toString());
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("url", set);
                editor.commit();


            }
        });

        SharedPreferences sharedPreferences1 = getSharedPreferences("user", Context.MODE_PRIVATE);
        set = sharedPreferences1.getStringSet("url", new HashSet<String>());

        list.addAll(set);
        recyclerView = new RecyclerView(this);
        recycleAdapterDome = new RecycleAdapterDome(this, list);
//        registerForContextMenu(recyclerView);
//        unregisterForContextMenu(recyclerView);
    }

    private PopupMenu popupMenu(View view) {
        if (popupMenu == null) {
            popupMenu = new PopupMenu(view.getContext(), view);
            menu1 = popupMenu.getMenu();
            for (int i = 0; i < set.size(); i++) {
                menu1.add(android.view.Menu.NONE, android.view.Menu.FIRST + i, i, list.get(i));
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    editText.setText(item.getTitle().toString());
                    mWebView.loadUrl(item.getTitle().toString());
                    return true;
                }
            });

        }

        return popupMenu;
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