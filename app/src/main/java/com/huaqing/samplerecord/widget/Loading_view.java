package com.huaqing.samplerecord.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.huaqing.samplerecord.R;


public class Loading_view extends ProgressDialog {
    TextView tvLoadDialog;

    public Loading_view(Context context) {
        super(context);
    }

    public Loading_view(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading);//loading的xml文件
        tvLoadDialog=findViewById(R.id.tv_load_dialog);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {//开启
        super.show();
    }

    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }

    public void setText(String s) {
        tvLoadDialog.setText(""+s);
    }
}
