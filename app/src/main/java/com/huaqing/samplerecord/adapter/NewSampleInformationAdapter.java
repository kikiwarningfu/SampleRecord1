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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.SampleInformationBean;
import com.huaqing.samplerecord.bean.SelectBean;
import com.huaqing.samplerecord.bean.TaskHeadInfoListBean;
import com.huaqing.samplerecord.utlis.Utils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 水域环境列表 输入的适配器 字段类型 0文本框 1文本域 2单选框 3复选框 4下拉框  时间控件 5
 */
public class NewSampleInformationAdapter extends BaseQuickAdapter<TaskHeadInfoListBean.DataBean.HeadInfoListDTO, BaseViewHolder> {
    public LinkedHashMap<String, String> contents = new LinkedHashMap<>();
    private int currentPosition = 0;
    private Context context;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    OnClickItemListener onClickItemListener;

    public NewSampleInformationAdapter(Context context) {
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
    protected void convert(@NonNull @NotNull BaseViewHolder helper, TaskHeadInfoListBean.DataBean.HeadInfoListDTO item) {
        EditText etMessage = helper.getView(R.id.et_message_wenben);
        EditText etMessageWenBenYu = helper.getView(R.id.et_message_wenbenyu);
        TextView tvTitle = helper.getView(R.id.tv_title);
        LinearLayout llDisskeyBorad = helper.getView(R.id.ll_dismiss_keyboard);
        TextView tvSelect = helper.getView(R.id.tv_select);
        LinearLayout llContentWenBen = helper.getView(R.id.ll_content_wenben);
        LinearLayout llContentWenBenYu = helper.getView(R.id.ll_content_wenbenyu);
        LinearLayout llContentDanXuan = helper.getView(R.id.ll_content_danxuan);
        LinearLayout llContentDuoxuan = helper.getView(R.id.ll_content_duoxuan);
        LinearLayout llContentXiaLa = helper.getView(R.id.ll_content_xiala);
        if (helper.getAdapterPosition() == getData().size() - 1) {
            etMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            etMessage.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
        TagFlowLayout flowLayoutSingle = helper.getView(R.id.flow_single);
        TagFlowLayout flowLayoutMul = helper.getView(R.id.flow_mul);

        List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data = getData();
//    输入的适配器 字段类型 0文本框 1文本域 2单选框 3复选框 4下拉框  时间控件 5
        String fieldName = item.getFieldName();
        tvTitle.setText("" + fieldName);
        int adapterPosition = helper.getAdapterPosition();
        MyTextNewChangedListener myTextNewChangedListener = new MyTextNewChangedListener();
        myTextNewChangedListener.setDatas(helper, contents, item, adapterPosition, data);


        switch (item.getFieldType()) {
            case "0"://文本框
            case "1"://1文本域
                if (!TextUtils.isEmpty(item.getPromptInfo())) {
                    etMessage.setHint("" + item.getPromptInfo());
                    etMessage.setText("");
                } else {
                    etMessage.setHint("请输入" + fieldName);
                    etMessage.setText("");
                }
                String fieldValue = item.getFieldValue();
                if (!TextUtils.isEmpty(fieldValue)) {
                    etMessage.setText("" + fieldValue);
                }
                llContentWenBen.setVisibility(View.VISIBLE);
                llContentWenBenYu.setVisibility(View.GONE);
                llContentDanXuan.setVisibility(View.GONE);
                llContentXiaLa.setVisibility(View.GONE);
                llContentDuoxuan.setVisibility(View.GONE);

                if (item.getFieldType().equals("0")) {
                    etMessage.setLines(1);
                } else {
                    etMessage.setLines(4);
                }

                etMessage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
//                        Iterator<Map.Entry<String, String>> it = hashMap.entrySet().iterator();
//                        while (it.hasNext()) {
//                            Map.Entry<String, String> entry = it.next();
//                            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//                        }
                        if (fieldName.contains("样品编号")) {
                            if (s.length() > 0) {
                                for (int i = 0; i < s.length(); i++) {
                                    char c = s.charAt(i);
                                    if (c >= 0x4e00 && c <= 0X9fff) {
                                        // 根据字节码判断
                                        // 如果是中文，则清除输入的字符，否则保留
                                        s.delete(i, i + 1);
                                    }
                                }
                            }
                        }
//

                        if (!TextUtils.isEmpty(s.toString())) {
                            hashMap.put(item.getFieldName(), s.toString());
                            if (onClickItemListener != null) {
                                onClickItemListener.onItemClick(data, helper.getAdapterPosition(), tvTitle.getText().toString(), s.toString(), hashMap);
                            }
                        }

                    }
                });
//                XLog.e("" + new Gson().toJson(hashMap));
                break;


            case "2"://2单选框
                llContentWenBen.setVisibility(View.GONE);
                llContentWenBenYu.setVisibility(View.GONE);
                llContentDanXuan.setVisibility(View.VISIBLE);
                llContentXiaLa.setVisibility(View.GONE);
                llContentDuoxuan.setVisibility(View.GONE);
//                String dataSource = "东南,南南南,西南,北,东南南南南南南,南,西南南南南南南,北,东南南南南南南,南,西,北南南南,东,南,南南西,北";
                String dataSource = item.getDataSource();
                String[] split = dataSource.split(",");
                List<SelectBean> selectBeans = new ArrayList<>();
                if (item.getSelectBeans() != null && item.getSelectBeans().size() > 0) {
                    for (int m = 0; m < split.length; m++) {
                        if (item.getSelectBeans().get(m).isSelect()) {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(true);
                            selectBean.setFileValue("" + split[m]);
                            selectBeans.add(selectBean);
                        } else {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(false);
                            selectBean.setFileValue("" + split[m]);
                            selectBeans.add(selectBean);
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(item.getFieldValue())) {
                        String[] splitFieldValue = item.getFieldValue().split(",");
                        for (int m = 0; m < split.length; m++) {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(false);
                            selectBean.setFileValue("" + split[m]);
                            for (int n = 0; n < splitFieldValue.length; n++) {
                                if (split[m].equals(splitFieldValue[n])) {
                                    XLog.e("值====" + split[m] + "  另一方的值" + splitFieldValue[n]);
                                    selectBean.setSelect(true);
                                    break;
                                }
                            }
                            selectBeans.add(selectBean);

                        }
                    } else {
                        for (int m = 0; m < split.length; m++) {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(false);
                            selectBean.setFileValue("" + split[m]);
                            selectBeans.add(selectBean);

                        }
                    }
                }
                MyTagSingleAdapter tagSingleAdapter = new MyTagSingleAdapter(context, selectBeans);
                flowLayoutSingle.setAdapter(tagSingleAdapter);
                flowLayoutSingle.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        List<SelectBean> list = tagSingleAdapter.getList();
                        for (int m = 0; m < list.size(); m++) {
                            list.get(m).setSelect(false);
                        }
                        list.get(position).setSelect(true);
                        tagSingleAdapter.setList(list);
                        item.setSelectBeans(list);
                        return false;
                    }
                });
                break;
            case "3":// 3复选框
                llContentWenBen.setVisibility(View.GONE);
                llContentWenBenYu.setVisibility(View.GONE);
                llContentDanXuan.setVisibility(View.GONE);
                llContentXiaLa.setVisibility(View.GONE);
                llContentDuoxuan.setVisibility(View.VISIBLE);
                String dataSource1 = item.getDataSource();
                String[] splits = dataSource1.split(",");
                List<SelectBean> selectBeanss = new ArrayList<>();
                if (item.getSelectBeans() != null && item.getSelectBeans().size() > 0) {
                    for (int m = 0; m < splits.length; m++) {
                        if (item.getSelectBeans().get(m).isSelect()) {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(true);
                            selectBean.setFileValue("" + splits[m]);
                            selectBeanss.add(selectBean);
                        } else {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(false);
                            selectBean.setFileValue("" + splits[m]);
                            selectBeanss.add(selectBean);
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(item.getFieldValue())) {
                        String[] splitFieldValue = item.getFieldValue().split(",");
                        for (int m = 0; m < splits.length; m++) {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(false);
                            selectBean.setFileValue("" + splits[m]);
                            for (int n = 0; n < splitFieldValue.length; n++) {
                                if (splits[m].equals(splitFieldValue[n])) {
                                    XLog.e("值====" + splits[m] + "  另一方的值" + splitFieldValue[n]);
                                    selectBean.setSelect(true);
//                                    selectBean.setFileValue("" + splitFieldValue[n]);
                                    break;
                                }
                            }
                            selectBeanss.add(selectBean);

                        }


                    } else {
                        for (int m = 0; m < splits.length; m++) {
                            SelectBean selectBean = new SelectBean();
                            selectBean.setSelect(false);
                            selectBean.setFileValue("" + splits[m]);
                            selectBeanss.add(selectBean);

                        }
                    }
                }


                MyTagMulAdapter myTagMulAdapter = new MyTagMulAdapter(context, selectBeanss);
                flowLayoutMul.setAdapter(myTagMulAdapter);

                flowLayoutMul.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {

                        List<SelectBean> list = myTagMulAdapter.getList();
                        SelectBean selectBean = list.get(position);
                        selectBean.setSelect(!selectBean.isSelect());
                        selectBean.setFileValue("" + list.get(position).getFileValue());
                        list.set(position, selectBean);
                        myTagMulAdapter.setList(list);
                        item.setSelectBeans(list);
                        return false;
                    }
                });

                break;
            case "4":// 4下拉框
                llContentWenBen.setVisibility(View.GONE);
                llContentWenBenYu.setVisibility(View.GONE);
                llContentDanXuan.setVisibility(View.GONE);
                llContentXiaLa.setVisibility(View.VISIBLE);
                llContentDuoxuan.setVisibility(View.GONE);
                if (TextUtils.isEmpty(item.getFieldValue())) {
                    tvSelect.setText("请选择" + fieldName);
                } else {
                    tvSelect.setText("" + item.getFieldValue());
                }
                helper.addOnClickListener(R.id.ll_content_xiala);
                break;
            case "5":// 5 时间控件
                llContentWenBen.setVisibility(View.GONE);
                llContentWenBenYu.setVisibility(View.GONE);
                llContentDanXuan.setVisibility(View.GONE);
                llContentXiaLa.setVisibility(View.VISIBLE);
                llContentDuoxuan.setVisibility(View.GONE);
                helper.addOnClickListener(R.id.ll_content_xiala);

                if (TextUtils.isEmpty(item.getFieldValue())) {
                    tvSelect.setText("请选择" + fieldName);
                } else {
                    tvSelect.setText("" + item.getFieldValue());
                }

                break;
        }
        llDisskeyBorad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard((Activity) context);
                etMessage.setCursorVisible(false);
            }
        });
        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    etMessage.setCursorVisible(true);
                }
                return false;
            }
        });

//        List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data = getData();

//        etMessage.addTextChangedListener(new MyTextNewChangedListener(helper, contents, data));
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setOnClickItemListener(OnClickItemListener listener) {
        this.onClickItemListener = listener;
    }


    /**
     * Created by zejian
     * Time   16/1/6 下午3:15
     * Email shinezejian@163.com
     * Description:RecyclerView点击事件接口
     */
    public interface OnClickItemListener {
        void onItemClick(List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data, int adapterPosition, String toString, String toString1, HashMap<String, String> hashMap);
    }

}

class MyTextNewChangedListener implements TextWatcher {

    public BaseViewHolder holder;
    List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data;
    TaskHeadInfoListBean.DataBean.HeadInfoListDTO item;
    private int currentposition = 0;
    public LinkedHashMap<String, String> contents;


    public MyTextNewChangedListener() {

    }

    public void setDatas(BaseViewHolder helper,
                         LinkedHashMap<String, String> contents, TaskHeadInfoListBean.DataBean.HeadInfoListDTO item, int adapterPosition,
                         List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> datas) {
        this.holder = helper;
        this.item = item;
        this.currentposition = adapterPosition;
        this.contents = contents;
        this.data = datas;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
//        if (holder != null && item != null) {
//            int adapterPosition = holder.getAdapterPosition();
////            item.setFieldName("" + data.get(adapterPosition).getFieldName());
//            item.setFieldValue("" + editable.toString());
//
//            XLog.e("item==="+item.getFieldName()+"------------------"+"修改值====="+editable.toString());
//            data.set(currentposition, item);
//        }
        if (holder != null && data != null) {
//            int adapterPosition = holder.getAdapterPosition();
//            item.setFieldName("" + data.get(adapterPosition).getFieldName());
//            item.setFieldValue("" + editable.toString());
//            data.set(adapterPosition, item);
//
//            contents.put("" + data.get(adapterPosition).getFieldName(), editable.toString());
            data.get(currentposition).setFieldValue("" + editable.toString());


        }
        XLog.e("currentPosition===" + currentposition + "当前的key值 是什么" + data.get(currentposition).getFieldName() + "----------------------------" + new Gson().toJson(data));
    }


}