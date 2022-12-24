package com.huaqing.samplerecord.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleInformationListBean;
import com.huaqing.samplerecord.bean.TaskHeadInfoListBean;

import org.jetbrains.annotations.NotNull;

/**
 * 水域环境列表 输入的标题的适配器
 */
public class SampleInformationNewTitleAdapter extends BaseQuickAdapter<TaskHeadInfoListBean.DataBean, BaseViewHolder> {

    public SampleInformationNewTitleAdapter() {
        super(R.layout.item_sample_information_title);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, TaskHeadInfoListBean.DataBean item) {
        TextView tvUserName = helper.getView(R.id.tv_user_name);
        View viewRightDivider=helper.getView(R.id.view_right_divider);
        if(helper.getAdapterPosition()==getData().size()-1)
        {
            viewRightDivider.setVisibility(View.VISIBLE);
        }else
        {
            viewRightDivider.setVisibility(View.GONE);
        }
        if (item.isSelect()) {
            tvUserName.setTextColor(Color.parseColor("#FFFFFF"));
            tvUserName.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select);
        } else {
            tvUserName.setTextColor(Color.parseColor("#000000"));
            tvUserName.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect_write);

        }
        tvUserName.setText("" + item.getGroupName());
    }


}
