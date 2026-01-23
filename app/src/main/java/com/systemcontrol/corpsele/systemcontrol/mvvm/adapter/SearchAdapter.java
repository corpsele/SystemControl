package com.systemcontrol.corpsele.systemcontrol.mvvm.adapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class SearchAdapter {

    @BindingAdapter("strReply")
    public static void setSearchResult(TextView textView, String strReply) {
        if (strReply == null) {
            textView.setText("");
            return;
        }
        textView.setText(strReply);
    }

}
