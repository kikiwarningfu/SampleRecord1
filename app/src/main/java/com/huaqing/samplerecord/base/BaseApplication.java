package com.huaqing.samplerecord.base;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.Collections;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * 项目名 MyBase
 * 描述 说明: 尽量不用静态
 */
public abstract class BaseApplication extends Application {
    public static BaseApplication sApplication;
    //若Activity的context 会有内存泄漏 而 Application在整个生命周期不会
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication=this;
        OkGo.getInstance().init(this);
        initOKgo();
    }

    //https 会有一个BUG   每次A表单页面提交数据请求，成功之后然后跳转到B页面之后接受到数据马上请求数据，请求的socket会被close，请求
    // 到一半，没有返回直接被close。这个bug还会在页面收到通知之后连续两次请求网络出现
    //https出现的BUG
    private void initOKgo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //初始化OKGo,网络框架
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
//        OkHttpClient build = builder.build();
        //防止请求两次  出现BUG
        OkHttpClient client =builder
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();


        OkGo.getInstance()

                .init(this)

                .setOkHttpClient(client);
    }


}
