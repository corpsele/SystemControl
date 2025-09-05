package com.systemcontrol.corpsele.systemcontrol;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

public class CrashMonitorService extends AccessibilityService {
    private static final String TAG = "CrashMonitorService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName().toString();
            Log.d(TAG, "Current App: " + packageName);

            // 检测应用是否崩溃（例如，出现“应用已停止”弹窗）
            if (event.getText().toString().contains("应用已停止") ||
                event.getText().toString().contains("has stopped")) {
                Log.e(TAG, "App Crashed: " + packageName);
                // 在这里可以执行崩溃后的处理逻辑，如重启应用或上报日志
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "AccessibilityService Interrupted");
    }
}

