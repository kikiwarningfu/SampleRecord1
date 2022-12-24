package com.huaqing.samplerecord.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.huaqing.samplerecord.MyApplication;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.utlis.ToastUtils;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITopBar;


/**
 * author    : ysy
 * introduce :
 */

public abstract class BaseFragment extends Fragment {
    private QMUILoadingView loadView;
    protected MyApplication mineApplication;
    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkGo.getInstance().init(getActivity().getApplication());
        mineApplication = (MyApplication) getActivity().getApplication();
        this.context=getActivity();
        setTopBar();
        setLoadView();
    }

    protected abstract void initData();

    protected abstract void initViews();

    /**
     * 加载loading动画
     */
    protected void setLoadView() {
        loadView = new QMUILoadingView(getActivity());
        loadView.setVisibility(View.GONE);
        loadView.setSize(120);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        getActivity().addContentView(loadView, layoutParams);
    }

    /**
     * 沉浸式状态栏
     *
     * @param topbar
     */
    protected void setTopBar(QMUITopBar topbar) {
        topbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhiteA));
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(getActivity());
        //设置状态栏黑色字体和图标，
        //支持4.4以上的MIUI和flyme  以及5.0以上的其他android
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());

    }
    protected void setTopBar() {
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(getActivity());
        //设置状态栏黑色字体和图标，
        //支持4.4以上的MIUI和flyme  以及5.0以上的其他android
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());

    }

//    public abstract int setLayout();

    public void showLoading() {
        loadView.setVisibility(View.VISIBLE);
        loadView.start();
    }

    public void hideLoading() {
        loadView.stop();
        loadView.setVisibility(View.GONE);
    }

    protected void showToast(String message) {
        ToastUtils.show(message);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadView.stop();
        ToastUtils.cancel();
    }
}
