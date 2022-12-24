package com.huaqing.samplerecord.utlis;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.ResponseBean;
import com.huaqing.samplerecord.bean.ShowSuccessEvent;
import com.huaqing.samplerecord.dialog.LoadingDialog;
import com.huaqing.samplerecord.listener.onDownloadFileListener;
import com.huaqing.samplerecord.listener.onNormalRequestListener;
import com.huaqing.samplerecord.listener.onUploadFileListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class OkGoUtils {

    private static LoadingDialog loadingDialog = null;
    private static boolean isClickCancel = false;

    private static Context mContext;
    private static httpCallBack mHCallBack;

    //    totalList = new Gson().fromJson(json, new TypeToken<List<SampleInformationBean.SampleInfoTitleBean>>() {
//    }.getType());
    /*
     * get请求
     * */
    public static void doHttpGet(Context context, String token, String url, HttpParams params, httpCallBack callBack) {
        mContext = context;
        mHCallBack = callBack;

        PostBean postBean = new PostBean();
        postBean.setId("" + url);
        OkGo.<String>get(url)
                .tag(url)
                .retryCount(3)
                .cacheTime(5000)
//                .params(params)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        requestSuccess(response.body(),callBack);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        requestError(response,callBack);
                    }
                });

    }

    /**
     * 不带加载动画的普通的post请求
     *
     * @param url 请求的网络地址
     */
    public static void normalRequest(final Context context, String url, String params, String token, httpCallBack callBack) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        mContext = context;
        mHCallBack = callBack;
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    new JSONObject(params).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
//                headers("Authorization",token).
        cacheTime(5000).//缓存时间
                upRequestBody(requestBody).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        requestSuccess(response.body(),callBack);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        requestError(response,callBack);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void normalPostNoParams(final Context context, String url, String token, httpCallBack callBack) {
//        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        System.out.println("url=====" + url + "json====" + new Gson().toJson(params));

        mContext = context;
        mHCallBack = callBack;
//        RequestBody requestBody = null;
//        try {
//            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//                    new JSONObject(params).toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
//                headers("Authorization",token).
        cacheTime(5000).//缓存时间
//                upRequestBody(requestBody).
        execute(new Callback<String>() {


    @Override
    public void onStart(Request<String, ? extends Request> request) {
        initDialog(context, url);
    }

    @Override
    public void onSuccess(Response<String> response) {
        dismisDialog();
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
        requestSuccess(response.body(),callBack);
    }

    @Override
    public void onCacheSuccess(Response<String> response) {

    }

    @Override
    public void onError(Response<String> response) {
        dismisDialog();
        requestError(response,callBack);
    }

    @Override
    public void onFinish() {
        dismisDialog();
    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }

    @Override
    public String convertResponse(okhttp3.Response response) throws Throwable {
        String result = response.body().string();
        return result;
    }
});

    }

    /*
     * post请求
     * */
    public static void doMyHttpPost(Context context, String token, String url, String params, httpCallBack callBack) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    new JSONObject(params).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mContext = context;
        mHCallBack = callBack;

        OkGo.<String>post(url)
                .tag(url)
                .retryCount(3)
//                .headers("Authorization", token)
                .upRequestBody(requestBody)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        requestSuccess(response.body().toString(),callBack);
                        dismisDialog();
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        requestError(response,callBack);
                        dismisDialog();
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    /*
     * post请求
     * */
    public static void doHttpPost(Context context, String token, String url, String json, httpCallBack callBack) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
//        url="http://192.168.21.132:8812/lims/sampling/app/login";

        StringBuilder sb = new StringBuilder();
        if (body instanceof FormBody) {
            FormBody body1 = (FormBody) body;
            for (int i = 0; i < body1.size(); i++) {
                sb.append(URLDecoder.decode(body1.encodedName(i)) + "=" + URLDecoder.decode(body1.encodedValue(i)) + "&");
            }
            if (sb.length() > 0)
                sb.delete(sb.length() - 1, sb.length());
            Log.e("okGo", "RequestParams:" + sb.toString());
        }
        mContext = context;
        mHCallBack = callBack;
        OkGo.<String>post(url)
                .tag(url)
                .retryCount(3)
//                .headers("Authorization", token)
                .upRequestBody(body)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        requestSuccess(response.body(),callBack);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        requestError(response,callBack);
                    }
                });
    }

    /**
     * 请求成功
     *
     * @param response
     */
    private static void requestSuccess(String response,httpCallBack callBack) {
        try
        {
            ResponseBean baseBean1 = new Gson().fromJson(response, ResponseBean.class);
            int code = baseBean1.getCode();
            if (code == 500) {
                if (baseBean1.getMsg().contains("请先登录")) {
                    EventBus.getDefault().post(new ShowSuccessEvent());
                } else {
                    callBack.doCallBack(baseBean1.isSuccess(), response, baseBean1.getMessage(), code);
                }
            } else {
                callBack.doCallBack(baseBean1.isSuccess(), response, baseBean1.getMessage(), code);
            }
        }catch (Exception e)
        {
            callBack.doCallBack(false, response, "服务器异常", 500);
        }

//        EventBus.getDefault().post(new ShowSuccessEvent("" + ids, ""+titles));


//        if (!baseBean1.isSuccess()) {
//            Toast.makeText(mContext, baseBean1.getMsg(), Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 请求失败
     *
     * @param response
     */
    private static void requestError(Response<String> response,httpCallBack callBack) {
        if (response.body() == null) {
            callBack.doCallBack(false, "", "请求失败", 0);
        } else {
            callBack.doCallBack(false, response.body(), new Gson().toJson(response), 0);
        }
//        Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
    }

    public interface httpCallBack {
        void doCallBack(boolean success, String response, String message, int code);
    }


    /**
     * 不带加载动画的普通的post请求
     *
     * @param url                     请求的网络地址
     * @param onNormalRequestListener 请求的回调
     */
    public static void historyRequest(final Context context, String url, String json, String token, final onNormalRequestListener onNormalRequestListener) {
        System.out.println("dhshdio=======" + json);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(120000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(120000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(120000, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        OkHttpClient okHttpClient = builder.build();

        OkGo.<String>post(url).
                tag(url).client(okHttpClient).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    /**
     * 不带加载动画的普通的post请求
     *
     * @param url                     请求的网络地址
     * @param onNormalRequestListener 请求的回调
     */
    public static void normalRequest(String url, String json, String token, final onNormalRequestListener onNormalRequestListener) {
        System.out.println("dhshdio=======" + json);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    /**
     * 不带加载动画的普通的post请求
     *
     * @param url                     请求的网络地址
     * @param onNormalRequestListener 请求的回调
     */
    public static void normalRequest(final Context context, String url, String json, String token, final onNormalRequestListener onNormalRequestListener) {
        System.out.println("dhshdio=======" + json);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void PostRequest(final String url, final Context context, String json, final onNormalRequestListener onNormalRequestListener) {
        System.out.println("dhshdio=======" + json);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void CallRequest(String url, HttpParams params, String token, final onNormalRequestListener onNormalRequestListener) {

        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void NormalPostRequest(String url, HttpParams params, String token, final onNormalRequestListener onNormalRequestListener) {

        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void NormalGetRequest(String url, HttpParams params, final onNormalRequestListener onNormalRequestListener) {
        OkGo.<String>get(url).tag(url).
                retryCount(3).//超时重连次数
                cacheTime(5000).//缓存时间
                params(params).
                cacheMode(CacheMode.DEFAULT)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void GetRequest(final Context context, String url, String token, HttpParams params, final onNormalRequestListener onNormalRequestListener) {
        OkGo.<String>get(url).tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params).
                cacheMode(CacheMode.DEFAULT)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void GetRequest(String url, String token, HttpParams params, final onNormalRequestListener onNormalRequestListener) {
        OkGo.<String>get(url).tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params).
                cacheMode(CacheMode.DEFAULT)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void GetParamsRequest(final Context context, final String url, final String token, HttpParams params, final onNormalRequestListener onNormalRequestListener) {

        OkGo.<String>get(url).tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });
    }

    public static void GetParamsRequest(final String url, final String token, HttpParams params, final onNormalRequestListener onNormalRequestListener) {

        OkGo.<String>get(url).tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });
    }

    /**
     * 带加载动画的普通的post请求
     *
     * @param url                     请求的网络地址
     * @param context                 当前Avtivity
     * @param params                  请求的参数
     * @param onNormalRequestListener 请求的回调
     */
    public static void progressRequest(final String url, final Context context, HttpParams params, final onNormalRequestListener onNormalRequestListener) {
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                cacheTime(50000).//缓存时间
                params(params).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    /**
     * 下载文件请求
     *
     * @param url                    链接地址
     * @param tag                    tag，当前Activity
     * @param destFileDir            下载文件的存储路径
     * @param destFileName           下载文件的存储名称
     * @param onDownloadFileListener 下载任务的回调
     */
    public static void downloadFile(String url, Activity tag, String destFileDir, String destFileName, final onDownloadFileListener onDownloadFileListener) {
        OkGo.<File>get(url).tag(tag).
                execute(new FileCallback(destFileDir, destFileName) {

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        onDownloadFileListener.onStart();
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        onDownloadFileListener.onSuccess(response);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        onDownloadFileListener.onDownloadProgress(progress);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        onDownloadFileListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        onDownloadFileListener.onFinish();
                    }
                });
    }

    /**
     * 上传单个文件的请求
     *
     * @param url                  链接地址
     * @param tag                  tag，当前Activity
     * @param params               参数，可以为null
     * @param fileKey              文件对应的key
     * @param file                 要上传的文件
     * @param onUploadFileListener 上传文件的回调
     */
    public static void upLoadFile(final String url, final Activity tag, HttpParams params, String fileKey, File file, final onUploadFileListener onUploadFileListener) {
        PostRequest<String> postRequest = OkGo.<String>post(url).tag(tag);
        //判断如果params不为空，因为上传文件时可能不会有params
        if (params != null) {
            postRequest.params(params);
        }
        postRequest.params(fileKey, file).
                execute(new StringCallback() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(tag, url);
                        onUploadFileListener.onStart();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        onUploadFileListener.onUploadProgress(progress);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onUploadFileListener.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        onUploadFileListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                        onUploadFileListener.onFinish();
                    }
                });
    }

    /**
     * 上传多个文件的请求
     *
     * @param url                  链接地址
     * @param tag                  tag，当前Activity
     * @param params               参数，可以为null
     * @param filesKey             文件对应的key
     * @param fileList             要上传的文件集合
     * @param onUploadFileListener 上传文件的回调
     */
    public static void upLoadFiles(final String url, final Activity tag, HttpParams params, String filesKey, List<File> fileList, final onUploadFileListener onUploadFileListener) {
        PostRequest<String> postRequest = OkGo.<String>post(url).tag(tag);
        //判断如果params不为空，因为上传文件时可能不会有params
        if (params != null) {
            postRequest.params(params);
        }
        postRequest.addFileParams(filesKey, fileList).
                execute(new StringCallback() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
//                        initDialog(tag, url);
                        onUploadFileListener.onStart();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        onUploadFileListener.onUploadProgress(progress);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
//                        dismisDialog();
                        onUploadFileListener.onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
//                        dismisDialog();
                        onUploadFileListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
//                        dismisDialog();
                        onUploadFileListener.onFinish();
                    }
                });
    }

    /**
     * 初始化加载过程dialog
     */
    private static void initDialog(final Context context, final String url) {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            isClickCancel = true;
            loadingDialog.dismiss();
            loadingDialog.cancel();
            loadingDialog = null;
        }
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = 255;
        attributes.height = 255;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(attributes);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //去掉背景色（一些设备上由于系统主题原因会有背景边框）

        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //取消全局默认的OkHttpClient中标识为tag的请求
                if (!isClickCancel) {
                    OkGo.getInstance().cancelTag(url);
                } else {
                    isClickCancel = false;
                }
            }
        });
    }

    /**
     * 取消dialog
     */
    private static void dismisDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }


    public static void GetRequest(String url, final onNormalRequestListener onNormalRequestListener) {
//        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, json);
//        OkGo.<String>post(url).
//                tag(url).
//                retryCount(3).//超时重连次数
//                cacheTime(5000).//缓存时间
//                upRequestBody(body)
        OkGo.<String>get(url).tag(url).
                retryCount(3).//超时重连次数
//                headers("Authorization","Bearer " + token).
        cacheTime(5000).//缓存时间
                cacheMode(CacheMode.DEFAULT)
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void PostRequest(final String url, String json, final onNormalRequestListener onNormalRequestListener) {
        System.out.println("dhshdio=======" + json);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void RequestToken(String url, String json, String token, final onNormalRequestListener onNormalRequestListener) {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        OkGo.<String>post(url).
                tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }


    public static void PutRequest(final String url, String json, String token, final onNormalRequestListener onNormalRequestListener) {
        System.out.println("dhshdio=======" + json);
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkGo.<String>put(url).
                tag(url).
                headers("Authorization", token).
                retryCount(3).//超时重连次数
                cacheTime(5000).//缓存时间
                upRequestBody(body).
                execute(new Callback<String>() {


                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        /*JSONObject jsonObject = JSONObject.parseObject(response.body());
                        switch (jsonObject.getInteger("status")) {
                            case -100://请重新登录

                                break;
                            default:
                                break;
                        }*/
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }


    public static void PutRequestH(final Context context, final String url, HttpParams params, String token, final onNormalRequestListener onNormalRequestListener) {

        OkGo.<String>put(url).
                tag(url).
                retryCount(3).//超时重连次数
                headers("Authorization", token).
                cacheTime(5000).//缓存时间
                params(params).
                execute(new Callback<String>() {

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        initDialog(context, url);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onSuccess(response);
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        dismisDialog();
                        onNormalRequestListener.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        dismisDialog();
                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        String result = response.body().string();
                        return result;
                    }
                });

    }

    public static void onDestory() {
        if (mHCallBack != null) {
            mHCallBack = null;
        }
    }
}
