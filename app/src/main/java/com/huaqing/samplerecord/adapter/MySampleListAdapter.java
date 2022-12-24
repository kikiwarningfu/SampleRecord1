package com.huaqing.samplerecord.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleInformationDetailBean;

import org.jetbrains.annotations.NotNull;


public class MySampleListAdapter extends BaseQuickAdapter<SampleInformationDetailBean.DataDTO.FillContentBean, BaseViewHolder> {
    public MySampleListAdapter() {
        super(R.layout.item_my_sample_list);
    }

    /**
     * compose 还得多看
     * @param helper
     * @param item
     */
    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, SampleInformationDetailBean.DataDTO.FillContentBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_weituo_danwei, item.getContent());
    }
}
