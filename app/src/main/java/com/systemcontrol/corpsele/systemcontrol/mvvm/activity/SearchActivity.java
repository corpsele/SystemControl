package com.systemcontrol.corpsele.systemcontrol.mvvm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.ihongqiqu.util.LogUtils;
import com.systemcontrol.corpsele.systemcontrol.LoadingDialog;
import com.systemcontrol.corpsele.systemcontrol.Main2Activity;
import com.systemcontrol.corpsele.systemcontrol.R;
import com.systemcontrol.corpsele.systemcontrol.TLSCheck;
import com.systemcontrol.corpsele.systemcontrol.mvvm.adapter.SearchAdapter;
import com.systemcontrol.corpsele.systemcontrol.databinding.ActivitySearchBinding;
import com.systemcontrol.corpsele.systemcontrol.mvvm.adapter.SpinnerBindingAdapter;
import com.systemcontrol.corpsele.systemcontrol.mvvm.model.ApiModel;
import com.systemcontrol.corpsele.systemcontrol.mvvm.viewmodel.SearchViewModel;

import org.conscrypt.Conscrypt;
import org.jetbrains.anko.Logging;

import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;

import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.wang.avi.AVLoadingIndicatorView;

public class SearchActivity extends AppCompatActivity {
    private SearchViewModel searchViewModel;
    private SearchAdapter searchAdapter;
    private TextView textViewResult;
    private EditText etApiKey;

    private Spinner spSelectModel;
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

        etApiKey = findViewById(R.id.et_apikey);

        spSelectModel = findViewById(R.id.spSelectAI);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(com.blankj.utilcode.R.layout.support_simple_spinner_dropdown_item);
        spSelectModel.setAdapter(spinnerAdapter);
        SpinnerAdapter sa = spSelectModel.getAdapter();
        LogUtils.d(" =========== spselectmodel count = "+sa.getCount());
        spSelectModel.setSelected(true);
        spSelectModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("========== selectmodel = " + position);
                searchViewModel.selectModelIndex.postValue(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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

    public void onEditApiKeyClick(View view) {
        LogUtils.i("onEditApiKeyClick");

        Intent intent = new Intent(SearchActivity.this, ApiModelActivity.class);
        intent.putExtra("any", "any");
//        startActivity(intent);
        activityLaunch.launch(intent);
    }

    // 注意：方法签名必须和 BindingAdapter 接口中的定义一致 (接收 String)
    public void onItemSelected(String item, AdapterView<?> parent, View view, int position, long id) {
        // 处理选择后的业务逻辑
        Toast.makeText(this, "选中了: " + item, Toast.LENGTH_SHORT).show();
        searchViewModel.selectModelIndex.postValue(position);
        // 比如更新界面上面的 TextView
        // 注意：这里为了演示简单直接用 findViewById，实际推荐也可以用 Binding 或 LiveData
//        TextView tvResult = findViewById(R.id.tv_result);
//        tvResult.setText("选择结果: " + city);
    }

    // 1. 注册 ActivityResultLauncher，替代 startActivityForResult
    private ActivityResultLauncher<Intent> activityLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String backData = data.getStringExtra("key_result");
                            long time = data.getLongExtra("key_time", 0L);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                ApiModel apiModel = data.getSerializableExtra("apiModel", ApiModel.class);
                                if (apiModel != null && apiModel.getApiKey() != null) {
                                    searchViewModel.apiKey.postValue(apiModel.getApiKey());
                                }

                            }

                        }
                    } else {
                        // RESULT_CANCELED 或用户直接按返回键

                    }
                }
            }
    );


    private void initViewModel() {
        // 1. 初始化 DataBinding
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setActivity(this);
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

