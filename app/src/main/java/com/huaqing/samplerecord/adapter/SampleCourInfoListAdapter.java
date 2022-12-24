package com.huaqing.samplerecord.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleCourseInfoListBean;
import com.huaqing.samplerecord.bean.getTaskBodyInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 样品编号  分析项目 采样信息 样品编号总List集合 输入的适配器
 */
public class SampleCourInfoListAdapter extends BaseQuickAdapter<getTaskBodyInfoBean.DataDTO, BaseViewHolder> {


    public SampleCourInfoListAdapter() {
        super(R.layout.item_sample_cour_infolist);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, getTaskBodyInfoBean.DataDTO item) {
        helper.addOnClickListener(R.id.iv_del);
        helper.addOnClickListener(R.id.rl_content);
        helper.addOnClickListener(R.id.tv_print_qr_code);
        helper.addOnClickListener(R.id.tv_print_tiao_code);
//                if (item.getAnalyzedItem().equals("样品编号")) {
        helper.setText(R.id.tv_sample_bianhao, "" + item.getSampleCode());
//                }
//                if (item.getBodyInfoList().get(m).getFieldName().equals("分析项目")) {
        helper.setText(R.id.tv_project_name, "" + item.getAnalyzedItem());
//                }


//        if (bodyInfoListDTO.getBianhao() == null || item.getBianhao().equals("null")) {
//            helper.setText(R.id.tv_sample_bianhao, "");
//        } else {
//            helper.setText(R.id.tv_sample_bianhao, item.getBianhao());
//        }
//        if (item.getProjectName() == null || item.getProjectName().equals("null")) {
//            helper.setText(R.id.tv_project_name, "");
//        } else {
//            helper.setText(R.id.tv_project_name, item.getProjectName());
//        }

        helper.addOnClickListener(R.id.rl_content);

    }
}
