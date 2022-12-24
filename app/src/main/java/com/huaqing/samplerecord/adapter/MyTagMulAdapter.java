package com.huaqing.samplerecord.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SelectBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Create On 2019/9/26 11:45
 * Copyrights 2019/9/26 handongkeji All rights reserved
 * ClassName:MyTagHistoryAdapter
 * PackageName:com.farunwanjia.app.ui.search.adapter
 * Site:http://www.handongkeji.com
 * Author: wanghongfu
 * Date: 2019/9/26 11:45
 * Description:多次点击
 */
public class MyTagMulAdapter extends TagAdapter<SelectBean> {
    public static final String TAG=MyTagMulAdapter.class.getSimpleName();
    private Context context;
    List<SelectBean> datas = new ArrayList<>();

    public MyTagMulAdapter(Context context, List<SelectBean> datas) {
        super(datas);
        this.context = context;
        this.datas = datas;
    }

    public List<SelectBean> getList() {
        return datas;
    }

    public void setList(List<SelectBean> datas) {
        this.datas = datas;
        notifyDataChanged();
    }

    @Override
    public View getView(FlowLayout parent, int position, SelectBean tagBean) {
        View inflate = View.inflate(context, R.layout.item_mul_select, null);
        TextView tvName = inflate.findViewById(R.id.tv_name);
        tvName.setText("" + tagBean.getFileValue());
        if (tagBean.isSelect()) {
            tvName.setBackgroundColor(Color.parseColor("#34B48F"));
            tvName.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            tvName.setBackgroundColor(Color.parseColor("#EBEBEB"));
            tvName.setTextColor(Color.parseColor("#000000"));
        }
//        if (datas.get(position).isSelect()) {
//            tv.setTextColor(activity.getResources().getColor(R.color.white));
//            tv.setBackgroundResource(R.drawable.shape_shopping_selected_flow_solid);
//        } else {
//            tv.setTextColor(activity.getResources().getColor(R.color.color_6));
//            tv.setBackgroundResource( R.drawable.shape_shopping_def_flow_solid);
//        }
        return tvName;
    }
}
