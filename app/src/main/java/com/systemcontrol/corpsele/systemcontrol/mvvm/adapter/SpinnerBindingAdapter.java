package com.systemcontrol.corpsele.systemcontrol.mvvm.adapter;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

public class SpinnerBindingAdapter {

    // 定义 XML 中使用的属性名：app:onItemSelected
    @BindingAdapter("onItemSelected")
    public static void setOnItemSelectedListener(Spinner spinner, final OnSpinnerItemClickListener listener) {
        if (listener == null) {
            spinner.setOnItemSelectedListener(null);
        } else {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // 获取选中项的内容
                    String item = (String) parent.getItemAtPosition(position);
                    // 回调给接口
                    listener.onItemSelected(item, parent, view, position, id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // 忽略
                    listener.onItemSelected("", parent, null, 0, 0);
                }
            });
        }
    }

    // 定义一个简单的接口，只有一个方法，方便在 XML 中用 Lambda 或方法引用
    public interface OnSpinnerItemClickListener {
        void onItemSelected(String item, AdapterView<?> parent, View view, int position, long id);
    }
}
