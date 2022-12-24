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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 水域环境列表 输入的适配器
 */
public class SampleInformationAdapter extends BaseQuickAdapter<SampleInformationBean.SampleInfoTitleBean.FillContentBean, BaseViewHolder> {
    public LinkedHashMap<String, String> contents = new LinkedHashMap<>();
    private int currentPosition = 0;
    private Context context;

    public SampleInformationAdapter(Context context) {
        super(R.layout.item_sample_information);
        this.context = context;

    }

    public void setMyTens(int currentPosition, LinkedHashMap<String, String> contents) {
        this.currentPosition = currentPosition;
        this.contents = contents;
        notifyDataSetChanged();
    }


    public int getCurrentPosition() {
        return currentPosition;
    }


    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, SampleInformationBean.SampleInfoTitleBean.FillContentBean item) {
//        EditText etMessage = helper.getView(R.id.et_message);
//        TextView tvTitle = helper.getView(R.id.tv_title);
//        LinearLayout llDisskeyBorad = helper.getView(R.id.ll_dismiss_keyboard);
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
//        if (TextUtils.isEmpty(item.getContent())) {
//            etMessage.setHint("请输入" + item.getName());
//        } else {
//            etMessage.setText("" + item.getContent());
//        }
//        List<SampleInformationBean.SampleInfoTitleBean.FillContentBean> data = getData();
//
//        etMessage.addTextChangedListener(new MyTextChangedListener(helper, contents, data));
    }

}

class MyTextChangedListener implements TextWatcher {

    public BaseViewHolder holder;
    public LinkedHashMap<String, String> contents;
    List<SampleInformationBean.SampleInfoTitleBean.FillContentBean> data;

    public MyTextChangedListener(BaseViewHolder holder, LinkedHashMap<String, String> contents, List<SampleInformationBean.SampleInfoTitleBean.FillContentBean> data) {
        this.holder = holder;
        this.contents = contents;
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
        if (holder != null && contents != null) {
            int adapterPosition = holder.getAdapterPosition();
            SampleInformationBean.SampleInfoTitleBean.FillContentBean fillContentBean = data.get(adapterPosition);
            fillContentBean.setName("" + data.get(adapterPosition).getName());
            fillContentBean.setContent("" + editable.toString());
            data.set(adapterPosition, fillContentBean);

            contents.put("" + data.get(adapterPosition).getName(), editable.toString());

        }
    }
}