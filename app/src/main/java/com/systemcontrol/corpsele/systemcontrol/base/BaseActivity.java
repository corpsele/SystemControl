package com.systemcontrol.corpsele.systemcontrol.base;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * 项目中所有 Activity 的基类，统一生命周期与常用逻辑
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 子类必须提供布局 ID
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }

        // 2. 统一初始化一些配置（如状态栏、埋点等）
        initCommon();

        // 3. 子类初始化视图
        initView(savedInstanceState);

        // 4. 子类初始化数据
        initData();
    }

    /**
     * 子类提供布局 ID，由基类调用 setContentView
     */
    protected abstract int getLayoutId();

    /**
     * 子类初始化 View
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 子类初始化数据（网络请求、赋值等）
     */
    protected void initData() {
        // 默认空实现，子类按需覆盖
    }

    /**
     * 基类统一处理一些通用配置
     */
    private void initCommon() {
        // 示例：统一设置状态栏颜色（需要配合 WindowInsets/系统 UI 处理）
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));

        // 示例：统一埋点（打开页面）
        onPageStart();
    }

    protected void onPageStart() {
        // 可以接统计 SDK，例如：Analytics.track(getClass().getSimpleName() + " open");
    }

    protected void onPageEnd() {
        // 例如：Analytics.track(getClass().getSimpleName() + " close");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onPageEnd();
    }

    // ============ 常用工具方法封装 ============

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 简单显示/隐藏一个 Loading 视图（真实项目可以用 Dialog/ViewBinding 封装）
     */
    protected void showLoading(boolean show) {
//        View loadingView = findViewById(R.id.loading_view);
//        if (loadingView != null) {
//            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
//        }
    }

    /**
     * 设置状态栏颜色（示例）
     */
    protected void setStatusBarColor(int colorRes) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, colorRes));
    }
}
