package com.huaqing.samplerecord.utlis;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.huaqing.samplerecord.R;


/**
 * @author ysy
 * Toast 自定义样式
 */

public class ToastUtils {
    private static Context context;
    private static View view;
    private static TextView textView_toast;
    private static Toast toast = null;

    /*
        不管我们触发多少次Toast调用，都只会持续一次Toast显示的时长
    */

    //ToastUtil.showToast(context, "内容");
    public static void init(Context thiscontext) {
        context = thiscontext.getApplicationContext();
    }

    public static void showToast(CharSequence message) {
        cancelToast();
        toast = new Toast(context);
        //toast.setGravity(17, 0, 0);//位置
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 150);
        toast.setDuration(message.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        view = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        textView_toast = view.findViewById(R.id.textView_toast);
        textView_toast.setBackground(ContextCompat.getDrawable(context, R.mipmap.toast_frame));
        textView_toast.setTextColor(ContextCompat.getColor((Context) context, android.R.color.white));
        toast.setView(view);
        textView_toast.setText(message);
        toast.show();
    }


    public static void showIToast(Context context, CharSequence message) {

        cancelToast();
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(message.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        view = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        textView_toast = view.findViewById(R.id.textView_toast);//textview.setGravity(Gravity.CENTER);
        toast.setView(view);//
        textView_toast.getBackground().mutate().setAlpha(153);
        textView_toast.setText(message);
//		toast.setView(view);
        toast.show();
    }

    public static void show(CharSequence message) {
        cancelToast();
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(message.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        view = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        textView_toast = view.findViewById(R.id.textView_toast);//textview.setGravity(Gravity.CENTER);
        toast.setView(view);//
        textView_toast.getBackground().mutate().setAlpha(153);
        textView_toast.setText(message);
//		toast.setView(view);
        toast.show();
    }

    public static void showMToast(Context context, CharSequence message) {
        cancelToast();
        toast = new Toast(context);
        //toast.setGravity(17, 0, 0);//位置
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 180);
        toast.setDuration(message.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        view = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        textView_toast = view.findViewById(R.id.textView_toast);
        textView_toast.setBackground(ContextCompat.getDrawable(context, R.mipmap.toast_frame));
        textView_toast.setTextColor(ContextCompat.getColor((Context) context, android.R.color.white));
        toast.setView(view);
        textView_toast.setText(message);
//		toast.setView(view);
        toast.show();
    }

    public static void showToast(Context context, CharSequence message) {
        cancelToast();
        toast = new Toast(context);
        //toast.setGravity(17, 0, 0);//位置
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 180);
        toast.setDuration(message.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        view = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
        textView_toast = view.findViewById(R.id.textView_toast);
        textView_toast.setBackground(ContextCompat.getDrawable(context, R.mipmap.toast_frame));
        textView_toast.setTextColor(ContextCompat.getColor((Context) context, android.R.color.white));
        toast.setView(view);
        textView_toast.setText(message);
        toast.show();
    }

    /**
     * ToastUtil.getInstanc(MyApplication.getInstance()).showToast("");
     * 取消toast，在activity的destory方法中调用
     */
    public static void cancel() {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
            //  toast=null;
        }
    }

}
