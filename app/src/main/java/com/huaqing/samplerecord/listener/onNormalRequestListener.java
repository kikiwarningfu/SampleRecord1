package com.huaqing.samplerecord.listener;

import com.lzy.okgo.model.Response;

/**
 * 普通请求的回调
 */
public interface onNormalRequestListener {

    /**
     *请求网络成功对数据进行操作，UI线程
     * @param response
     */
    void onSuccess(Response<String> response);

    /**
     *请求网络失败，UI线程
     * @param response
     */
    void onError(Response<String> response);


}
