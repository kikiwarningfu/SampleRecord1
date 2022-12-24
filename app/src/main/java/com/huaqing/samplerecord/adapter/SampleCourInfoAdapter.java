package com.huaqing.samplerecord.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleInformationBean;
import com.huaqing.samplerecord.utlis.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 采样信息 样品编号表格 输入的适配器
 */
public class SampleCourInfoAdapter extends BaseQuickAdapter<SampleInformationBean.SampleInfoTitleBean.BottomContentBean, BaseViewHolder> {
    private int currentPosition = 0;
    private Context context;

    public SampleCourInfoAdapter(Context context) {
        super(R.layout.item_sample_information);
        this.context = context;

    }

    public void setMyTens(int currentPosition) {
        this.currentPosition = currentPosition;

        notifyDataSetChanged();
    }


    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, SampleInformationBean.SampleInfoTitleBean.BottomContentBean item) {

//        TextView tvTitle = helper.getView(R.id.tv_title);
//        LinearLayout llDisskeyBorad = helper.getView(R.id.ll_dismiss_keyboard);
//
//        if (helper.getAdapterPosition() == getData().size() - 1) {
//            etMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        } else {
//            etMessage.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        }
//        llDisskeyBorad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.hideSoftKeyboard((Activity) context);
//                etMessage.setCursorVisible(false);
//            }
//        });
//        etMessage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                    etMessage.setCursorVisible(true);
//                }
//                return false;
//            }
//        });
//        tvTitle.setText("" + item.getName());
//
//        List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> data = getData();



    }

    class MyTextChangedListener implements TextWatcher {

        public BaseViewHolder holder;
        List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> data;

        public MyTextChangedListener(BaseViewHolder holder, List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> data) {
            this.holder = holder;
            this.data = data;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            int adapterPosition = holder.getAdapterPosition();
            SampleInformationBean.SampleInfoTitleBean.BottomContentBean bottomContentBean = data.get(adapterPosition);
            bottomContentBean.setContent("" + editable.toString());
            bottomContentBean.setName("" + data.get(adapterPosition).getName());
            data.set(adapterPosition, bottomContentBean);
        }
    }
}

