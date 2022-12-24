package com.huaqing.samplerecord.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.TaskListBean;

import org.jetbrains.annotations.NotNull;

/**
 * 采样任务
 */
public class SampleTaskAdapter extends BaseQuickAdapter<TaskListBean.DataDTO, BaseViewHolder> {

    public SampleTaskAdapter() {
        super(R.layout.item_sample_task);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, TaskListBean.DataDTO item) {
//        helper.addOnClickListener(R.id.ll_content);
//        helper.addOnClickListener(R.id.tv_sample_status);
        helper.addOnClickListener(R.id.ll_content);
//        TextView view = helper.getView(R.id.tv_sample_status);
//        view.setVisibility(View.GONE);
//        View viewDivider = helper.getView(R.id.view_divider);
//        if (helper.getAdapterPosition() == getData().size() - 1) {
//            viewDivider.setVisibility(View.VISIBLE);
//        } else {
//            viewDivider.setVisibility(View.GONE);
//        }
//        switch (item.getStatus()) {
//            case "0":
//                //未开始
//                helper.setText(R.id.tv_sample_status, "未开始");
//                break;
//            case "1":
//                view.setVisibility(View.VISIBLE);
//                //进行中
//                helper.setText(R.id.tv_sample_status, "采样完成");
//                break;
//            case "2":
//                view.setVisibility(View.VISIBLE);
//                //已完成
//                helper.setText(R.id.tv_sample_status, "采样完成");
//                break;
//            default:
//
//                break;
//        }
//        helper.setText(R.id.tv_site_name, item.getSiteName())
//                .setText(R.id.tv_item_group, item.getItemGroup())
//                .setText(R.id.tv_entrust_name,item.getEntrustName())
//                .setText(R.id.tv_sample_time,item.getSamplTime())
//                .setText(R.id.tv_desc,item.getDesc())
//                .setText(R.id.tv_task_bianhao, item.getNoticeCode());

        helper.setText(R.id.tv_bianhao, item.getNoticeCode())
                .setText(R.id.tv_weituo, item.getEntrustUnit())
                .setText(R.id.tv_shoujian, item.getEntrustedUnit())
                .setText(R.id.tv_address, item.getAddress())
                .setText(R.id.tv_time, item.getEntranceTimeRequire());
    }
}
