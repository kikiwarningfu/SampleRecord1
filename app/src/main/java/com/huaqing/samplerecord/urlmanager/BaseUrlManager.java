package com.huaqing.samplerecord.urlmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;


import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huaqing.samplerecord.urlmanager.bean.UrlInfo;
import com.huaqing.samplerecord.urlmanager.util.BaseUrlUtil;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class BaseUrlManager implements IBaseUrlManager {

    public static final String KEY_TITLE = "key_title";

    public static final String KEY_URL_INFO = "key_url_info";

    public static final String KEY_REGEX = "key_regex";

    public static final String HTTP_URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private Context context;

    private List<UrlInfo> urlInfoList;

    private UrlInfo urlInfo;

    private String baseUrl;

    private static BaseUrlManager INSTANCE;

    public static BaseUrlManager getInstance() {
        return INSTANCE;
    }

    static void init(@NonNull Context context) {
        INSTANCE = new BaseUrlManager(context.getApplicationContext());
    }

    private BaseUrlManager(@NonNull Context context) {
        this.context = context;
        refreshData();
    }

    /**
     * 跳转至 BaseUrl 配置管理
     *
     * @param activity
     * @param requestCode
     */
    public static void startBaseUrlManager(Activity activity, int requestCode) {
        startBaseUrlManager(activity, requestCode, null);
    }

    /**
     * 跳转至 BaseUrl 配置管理
     *
     * @param activity
     * @param requestCode
     * @param bundle
     */
    public static void startBaseUrlManager(Activity activity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, BaseUrlManagerActivity.class);
        if (bundle != null && bundle.size() > 0) {
            intent.putExtra(KEY_TITLE, "");
            intent.getExtras().putAll(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转至 BaseUrl 配置管理
     *
     * @param fragment
     * @param requestCode
     */
    public static void startBaseUrlManager(Fragment fragment, int requestCode) {
        startBaseUrlManager(fragment, requestCode, null);
    }

    /**
     * 跳转至 BaseUrl 配置管理
     *
     * @param fragment
     * @param requestCode
     * @param bundle
     */
    public static void startBaseUrlManager(Fragment fragment, int requestCode, Bundle bundle) {
        Intent intent = new Intent(fragment.getContext(), BaseUrlManagerActivity.class);
        if (bundle != null && bundle.size() > 0) {
            intent.putExtra(KEY_TITLE, "");
            intent.getExtras().putAll(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 解析 onActivityResult中的结果
     *
     * @param data
     * @return
     */
    @Nullable
    public static UrlInfo parseActivityResult(Intent data) {
        if (data != null) {
            return data.getParcelableExtra(BaseUrlManager.KEY_URL_INFO);
        }
        return null;
    }


    @Override
    public String getBaseUrl() {

        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = "http://192.168.21.31:8812";
        }
        return baseUrl;
    }

    @Override
    public UrlInfo getUrlInfo() {
        return urlInfo;
    }

    @Override
    public void setUrlInfo(@NonNull UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
        this.baseUrl = urlInfo.getBaseUrl();
        if (urlInfoList != null) {
            if (urlInfoList.contains(urlInfo)) {
                urlInfoList.remove(urlInfo);
            }
            urlInfoList.add(urlInfo);
            BaseUrlUtil.put(context, urlInfo, true);
        }
    }

    @Override
    public void addUrlInfo(@NonNull UrlInfo urlInfo) {
        if (urlInfoList != null) {
            if (urlInfoList.contains(urlInfo)) {
                urlInfoList.remove(urlInfo);
            }
            urlInfoList.add(urlInfo);
            BaseUrlUtil.put(context, urlInfo);

        }
    }

    @Override
    public void addUrlInfo(@NonNull Collection<UrlInfo> list) {
        if (urlInfoList != null) {
            urlInfoList.addAll(list);
            BaseUrlUtil.put(context, list);
        }
    }

    @Override
    public void remove(@NonNull UrlInfo urlInfo) {
        if (urlInfoList != null) {
            urlInfoList.remove(urlInfo);
            BaseUrlUtil.remove(context, urlInfo.getBaseUrl());
        }
    }

    @Override
    public void clear() {
        if (urlInfoList != null) {
            urlInfoList.clear();
        }
        urlInfo = null;
        baseUrl = null;
        BaseUrlUtil.clear(context);
    }

    @Override
    public List<UrlInfo> getUrlInfoList() {
        return urlInfoList;
    }


    @Override
    public int getCount() {
        return urlInfoList != null ? urlInfoList.size() : 0;
    }

    @Override
    public void refreshData() {
        baseUrl = BaseUrlUtil.getBaseUrl(context);
        urlInfo = BaseUrlUtil.getUrlInfo(context, baseUrl);
        urlInfoList = BaseUrlUtil.getUrlInfoList(context);
    }
}
