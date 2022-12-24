package com.huaqing.samplerecord.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleRecordBean;
import com.huaqing.samplerecord.utlis.Utils;

import org.jetbrains.annotations.NotNull;

public class SamleRecordAdapter extends BaseQuickAdapter<SampleRecordBean.DataDTO, BaseViewHolder> {
    private String clickType;

    public SamleRecordAdapter() {
        super(R.layout.item_sample_record);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, SampleRecordBean.DataDTO item) {
        helper.setText(R.id.tv_day_num, "第" + Utils.numberToChinese(item.getWhichDay()) + "天")
                .setText(R.id.tv_sample_num, "第" + Utils.numberToChinese(item.getWhichTime()) + "次");
        ImageView ivDel = helper.getView(R.id.iv_del);

        if (clickType.equals("2")) {
            ivDel.setVisibility(View.GONE);
        } else {
            ivDel.setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.iv_del);
        }
    }


    public void setclickType(String clickType) {
        this.clickType = clickType;
        notifyDataSetChanged();
    }
}
