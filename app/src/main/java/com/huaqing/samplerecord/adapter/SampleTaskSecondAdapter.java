package com.huaqing.samplerecord.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleTaskSecondBean;

import org.jetbrains.annotations.NotNull;


public class SampleTaskSecondAdapter extends BaseQuickAdapter<SampleTaskSecondBean.DataDTO, BaseViewHolder> {
    public SampleTaskSecondAdapter() {
        super(R.layout.item_sample_task_second);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, SampleTaskSecondBean.DataDTO item) {
        helper.setText(R.id.tv_group_name, item.getItemGroup())
                .setText(R.id.tv_point_name, item.getPointName())
                .setText(R.id.tv_day, item.getDayNum() + "天")
                .setText(R.id.tv_pinci, item.getFrequency() + "次")
                .setText(R.id.tv_time_last, item.getFinalTime())
                .setText(R.id.tv_desc, item.getDescription());
        TextView tvClick = helper.getView(R.id.tv_click);
        TextView tvClickJixu = helper.getView(R.id.tv_click_jixu);
        helper.addOnClickListener(R.id.tv_click);
        helper.addOnClickListener(R.id.tv_click_jixu);
        switch (item.getStatus()) {
            case "1":
                tvClick.setVisibility(View.VISIBLE);
                //未开始
                tvClickJixu.setVisibility(View.GONE);
                helper.setText(R.id.tv_click, "开始采样");
                break;
            case "2":
                tvClick.setVisibility(View.VISIBLE);
                tvClickJixu.setVisibility(View.VISIBLE);
                //进行中
                helper.setText(R.id.tv_click, "采样完成");
                break;
            case "3"://不显示
                tvClick.setVisibility(View.GONE);
                tvClickJixu.setVisibility(View.GONE);
                //已完成
                helper.setText(R.id.tv_click, "采样完成");
                break;
            default:

                break;
        }
    }
}
