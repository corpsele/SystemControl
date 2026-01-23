package com.systemcontrol.corpsele.systemcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
        // 1. 设置布局
        setContentView(R.layout.dialog_loading);

        // 2. 设置点击外部不可取消
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }
}

