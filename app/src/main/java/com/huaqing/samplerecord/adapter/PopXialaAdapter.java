package com.huaqing.samplerecord.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huaqing.samplerecord.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PopXialaAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PopXialaAdapter() {
        super(R.layout.item_show_xiala);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);
        helper.addOnClickListener(R.id.tv_name);
    }
}
