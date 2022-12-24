package com.huaqing.samplerecord.listener;

import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

/**
 * 下载文件的回调
 */
public interface onUploadFileListener {
    /**
     * 开始上传前，UI线程
     */
    void onStart();

    /**
     * 上传过程中进度回调，UI线程
     * @param progress
     */
    void onUploadProgress(Progress progress);

    /**
     * 上传成功回调，UI线程
     * @param response
     */
    void onSuccess(Response<String> response);

    /**
     * 上传失败回调，UI线程
     * @param response
     */
    void onError(Response<String> response);

    /**
     * 上传完成回调无论下载成功或失败，UI线程
     */
    void onFinish();
}
