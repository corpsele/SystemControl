package com.systemcontrol.corpsele.systemcontrol.mvvm.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.systemcontrol.corpsele.systemcontrol.LoadingDialog;
import com.systemcontrol.corpsele.systemcontrol.R;
import com.systemcontrol.corpsele.systemcontrol.TLSCheck;
import com.systemcontrol.corpsele.systemcontrol.mvvm.adapter.SearchAdapter;
import com.systemcontrol.corpsele.systemcontrol.databinding.ActivitySearchBinding;
import com.systemcontrol.corpsele.systemcontrol.mvvm.viewmodel.SearchViewModel;

import org.conscrypt.Conscrypt;

import java.security.Security;
import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.wang.avi.AVLoadingIndicatorView;

public class SearchActivity extends AppCompatActivity {
    private SearchViewModel searchViewModel;
    private SearchAdapter searchAdapter;
    private TextView textViewResult;
    private LoadingDialog loadingDialog;

    private LVCircularRing circularRing;

    private AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        TLSCheck.checkTLS();

        initViews();
        initViewModel();




    }

    private void initViews() {
        setContentView(R.layout.activity_search);

        // 显示
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);

        }

        if (circularRing == null) {
            circularRing = new LVCircularRing(this);
            circularRing.setViewColor(0xCC000000);
            circularRing.setViewColor(Color.BLUE);

        }

        textViewResult = findViewById(R.id.tv_strReply);
        textViewResult.setMovementMethod(ScrollingMovementMethod.getInstance());


//        if (loadingIndicatorView == null) {
//
//            loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi_custom_loading);
//            loadingIndicatorView.setIndicator("BallPulseIndicator");
////            loadingIndicatorView.show();
//        }
//        https://pic.616pic.com/ys_bnew_img/00/54/47/xbemNvYKIl.jpg
//        ImageView imageView = findViewById(R.id.iv_target);
//        ProgressBar progressBar = findViewById(R.id.pb_loading);
//        Glide.with(this)
//                .load(R.drawable.ic_loading)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                })
//                .into(imageView);
//
//        // 确保 Loading 显示
//        progressBar.setVisibility(View.VISIBLE);

    }

    private void initViewModel() {
        // 1. 初始化 DataBinding
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
//        ActivitySearchBinding binding = ActivitySearchBinding.inflate(LayoutInflater.from(this));
        // 2. 初始化 ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        // 3. 绑定 ViewModel 到 XML
        binding.setViewModel(searchViewModel);
        // 4. 设置 LifecycleOwner，确保 LiveData 能够感知生命周期
        binding.setLifecycleOwner(this);

        searchViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    loadingDialog.show();
//                    circularRing.startAnim(4000);
                }else{
                    loadingDialog.dismiss();
//                    circularRing.stopAnim();
                }
            }
        });

        loadingDialog.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchViewModel.getClients().getValue() != null){
                    searchViewModel.getClients().getValue().forEach(okHttpClient -> {
                        okHttpClient.dispatcher().cancelAll();
                    });
                }

            }
        });


    }
}

