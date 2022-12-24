package com.huaqing.samplerecord.listener;

import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * 下载文件的回调
 */
public interface onDownloadFileListener {
    /**
     * 开始下载前，UI线程
     */
    void onStart();

    /**
     * 下载过程中进度回调，UI线程
     * @param progress
     */
    void onDownloadProgress(Progress progress);

    /**
     * 下载成功回调，UI线程
     * @param response
     */
    void onSuccess(Response<File> response);

    /**
     * 下载失败回调，UI线程
     * @param response
     */
    void onError(Response<File> response);

    /**
     * 下载完成回调无论下载成功或失败，UI线程
     */
    void onFinish();
}
