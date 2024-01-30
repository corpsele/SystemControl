package com.systemcontrol.corpsele.systemcontrol;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

public class NotificationBig extends Activity {
    private Button btnMusicAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_big);

        Intent button1I = new Intent("ActionMusicAdd");

        PendingIntent button1PI = PendingIntent.getBroadcast(this, 0, button1I, 0);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_big);
        /*
         * 对于自定义布局文件中的控件通过RemoteViews类的对象进行事件处理
         */
        remoteViews.setOnClickPendingIntent(R.id.btnMusicAdd, button1PI);
    }
}
