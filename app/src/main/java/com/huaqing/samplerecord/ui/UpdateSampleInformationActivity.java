package com.huaqing.samplerecord.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.adapter.NewSampleInformationAdapter;
import com.huaqing.samplerecord.adapter.PopXialaAdapter;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.BodyInfoBeanSample;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.SampleInformationBean;
import com.huaqing.samplerecord.bean.SelectBean;
import com.huaqing.samplerecord.bean.ShowSwipListEvent;
import com.huaqing.samplerecord.bean.TaskHeadInfoListBean;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
import com.huaqing.samplerecord.utlis.OkGoUtils;
import com.huaqing.samplerecord.widget.TopBar;
import com.huaqing.samplerecord.widget.popupwindow.CommonPopupWindow;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 更新采样信息界面UI   //A 远程依赖B   B远程依赖C   如果C代码丢失  那么会崩溃报错
 */
public class UpdateSampleInformationActivity extends BaseActivity implements View.OnClickListener {

    private ShimmerRecyclerView rvCourseList;
    private NewSampleInformationAdapter newSampleInformationAdapter;//样品 数目 填写
    private TextView tvSave;
    private TopBar topBar;
    private String id = "";
    private String isAdd = "";
    private PopXialaAdapter popXialaAdapter;
    private Context instance;
    private String taskId = "";

    public static void goActivity(Context context, String id, String isAdd, String taskId) {
        Intent intent = new Intent(context, UpdateSampleInformationActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("isAdd", isAdd);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_update_sample_information;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        topBar = findViewById(R.id.topbar);
        id = getIntent().getStringExtra("id");
        isAdd = getIntent().getStringExtra("isAdd");
        taskId = getIntent().getStringExtra("taskId");
        instance = this;
        popXialaAdapter = new PopXialaAdapter();
        rvCourseList = findViewById(R.id.rv_course_list);
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);
        topBar.getTvCenter().setText("采样信息");
        topBar.getIvFinish().setVisibility(View.VISIBLE);
        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                setResult(RESULT_OK, intent);
                finish();


            }
        });
//        UltimateBarX.statusBarOnly(this)
//                .fitWindow(true)
//                .colorRes(R.color.white)
//                .light(true)
//                .lvlColorRes(R.color.divider_1)
//                .apply();

        newSampleInformationAdapter = new NewSampleInformationAdapter(UpdateSampleInformationActivity.this);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvCourseList.setAdapter(newSampleInformationAdapter);
        newSampleInformationAdapter.setOnClickItemListener(new NewSampleInformationAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data, int adapterPosition, String tvTitle, String message, HashMap<String, String> hashMap) {
                if (data != null && data.size() > 0) {
                    for (int m = 0; m < data.size(); m++) {
                        if (data.get(m).getFieldName().equals("" + tvTitle)) {
                            TaskHeadInfoListBean.DataBean.HeadInfoListDTO headInfoListDTO = data.get(m);
                            headInfoListDTO.setFieldValue("" + message);
                            data.set(m, headInfoListDTO);
                            XLog.e("修改的position===");

                        }
                    }
                }
            }
        });
        newSampleInformationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_content_xiala:
                        TaskHeadInfoListBean.DataBean.HeadInfoListDTO headInfoListDTO = newSampleInformationAdapter.getData().get(position);
                        String fieldType = headInfoListDTO.getFieldType();
                        if (fieldType.equals("4")) {
                            String dataSource = headInfoListDTO.getDataSource();
                            String[] split = dataSource.split(",");
                            List<String> newList = new ArrayList<>();
                            for (int m = 0; m < split.length; m++) {
                                newList.add(split[m]);
                            }
                            showPopAlert(newList, position, headInfoListDTO, view, instance);
                        } else if (fieldType.equals("5")) {
                            TimePickerView pvTime = new TimePickerView.Builder(instance, new TimePickerView.OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date dateMax, View v) {
                                    String time = getTimeMinute(dateMax);

                                    TaskHeadInfoListBean.DataBean.HeadInfoListDTO headInfoListDTO1 = newSampleInformationAdapter.getData().get(position);
                                    headInfoListDTO1.setFieldValue("" + time);
                                    newSampleInformationAdapter.setData(position, headInfoListDTO1);
                                }
                            })
                                    .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                                    .setCancelText("取消")//取消按钮文字
                                    .setSubmitText("确定")//确认按钮文字
                                    .setContentSize(20)//滚轮文字大小
                                    .setTitleSize(20)//标题文字大小
//                        .setTitleText("请选择时间")//标题文字
                                    .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                                    .isCyclic(true)//是否循环滚动
                                    .setTextColorCenter(Color.parseColor("#333333"))//设置选中项的颜色
                                    .setTextColorOut(Color.parseColor("#666666"))
                                    .setTitleColor(Color.BLACK)//标题文字颜色
                                    .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                                    .setCancelColor(Color.BLUE)//取消按钮文字颜色
                                    .setLoop(false)
                                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                        .isDialog(true)//是否显示为对话框样式
                                    .build();
                            pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                            pvTime.show();
                        }
                        break;
                }
            }
        });
        PostBean postBean = new PostBean();
        if (isAdd.equals("true")) {
            //新建
            postBean.setPlanId("" + id);
        } else {
            postBean.setRecordId("" + id);
        }
//        BASE_URL+"/lims/sampling/app/getBodyInfo";//获取采样表体配置信息
        OkGoUtils.doHttpPost(this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/getBodyInfo"
                , new Gson().toJson(postBean), new OkGoUtils.httpCallBack() {
                    @Override
                    public void doCallBack(boolean success, String response, String message, int code) {

                        BodyInfoBeanSample bodyInfoBeanSample = new Gson().fromJson(response, BodyInfoBeanSample.class);
                        List<BodyInfoBeanSample.DataDTO> data = bodyInfoBeanSample.getData();
                        List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> headInfoListDTOS = new ArrayList<>();
                        if (data != null && data.size() > 0) {
                            for (int m = 0; m < data.size(); m++) {
                                TaskHeadInfoListBean.DataBean.HeadInfoListDTO bean = new TaskHeadInfoListBean.DataBean.HeadInfoListDTO();
                                bean.setFieldValue(data.get(m).getFieldValue());
                                bean.setFieldName(data.get(m).getFieldName());
                                bean.setSelectBeans(data.get(m).getSelectBeans());
                                bean.setDataSource(data.get(m).getDataSource());
                                bean.setFieldType(data.get(m).getFieldType());
                                bean.setGroupName(data.get(m).getGroupName());
                                bean.setId(data.get(m).getId());
                                bean.setSamplingRecordId(data.get(m).getSamplingRecordId());
                                bean.setSerialNum(data.get(m).getSerialNum());
                                bean.setPromptInfo("" + data.get(m).getPromptInfo());
                                headInfoListDTOS.add(bean);
                            }
                            newSampleInformationAdapter.setNewData(headInfoListDTOS);
                            XLog.e("data===" + new Gson().toJson(headInfoListDTOS));

                        }


                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                showLoading();
                List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data = newSampleInformationAdapter.getData();
                PostBean postBean = new PostBean();
                if (isAdd.equals("false")) {
                    postBean.setId("" + id);
                }
                postBean.setTaskId("" + taskId);
                List<PostBean.BodyInfoBean> infoBeanList = new ArrayList<>();
                for (int m = 0; m < data.size(); m++) {
                    PostBean.BodyInfoBean bodyInfoBean = new PostBean.BodyInfoBean();
                    bodyInfoBean.setDataSource(data.get(m).getDataSource());
                    bodyInfoBean.setFieldName(data.get(m).getFieldName());
                    bodyInfoBean.setFieldType(data.get(m).getFieldType());
                    List<SelectBean> selectBeans = data.get(m).getSelectBeans();
                    if (selectBeans != null && selectBeans.size() > 0) {
                        String strDataSource = "";
                        for (int n = 0; n < selectBeans.size(); n++) {
                            if (selectBeans.get(n).isSelect()) {
                                strDataSource = strDataSource + selectBeans.get(n).getFileValue() + ",";
                            }
                        }
                        if (strDataSource.endsWith(",")) {
                            bodyInfoBean.setFieldValue(strDataSource.substring(0, strDataSource.length() - 1));
                        } else {
                            bodyInfoBean.setFieldValue(strDataSource);
                        }
                    }else
                    {
                        bodyInfoBean.setFieldValue(data.get(m).getFieldValue());
                    }
                    bodyInfoBean.setPromptInfo(data.get(m).getPromptInfo());
                    bodyInfoBean.setSerialNum(data.get(m).getSerialNum());
                    if (isAdd.equals("false")) {
                        bodyInfoBean.setId("" + data.get(m).getId());
                        bodyInfoBean.setSamplingRecordId("" + data.get(m).getSamplingRecordId());
                    }
                    infoBeanList.add(bodyInfoBean);
                }
                postBean.setBodyInfoList(infoBeanList);
//                BASE_URL+"/lims/sampling/app/addAndEditSamplingRecord";//新增或者修改 采样信息
                OkGoUtils.doHttpPost(this, AccountHelper.getToken(),
                        BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/addAndEditSamplingRecord"
                        , new Gson().toJson(postBean), new OkGoUtils.httpCallBack() {
                            @Override
                            public void doCallBack(boolean success, String response, String message, int code) {
                                dismissLoading();
                                if (success) {
                                    if (isAdd.equals("true")) {
                                        showToast("添加成功");
                                    } else {
                                        showToast("修改成功");
                                    }
                                    EventBus.getDefault().post(new ShowSwipListEvent());
                                    finish();
                                } else {
                                    showToast("" + message);
                                }

                            }
                        });

                break;
        }
    }

    private CommonPopupWindow mMovieTicketWindow;

    public void showPopAlert(List<String> alarmList, int currentposition, TaskHeadInfoListBean.DataBean.HeadInfoListDTO bean, View view, Context context) {
        if (mMovieTicketWindow != null && mMovieTicketWindow.isShowing()) {

        } else {
            mMovieTicketWindow = new CommonPopupWindow.Builder(this)
                    .setView(R.layout.pop_show_xiala)
                    .setBackGroundLevel(1f)
                    .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                        @Override
                        public void getChildView(View view, int layoutResId) {
                            RecyclerView rvList = view.findViewById(R.id.rv_list);
                            rvList.setLayoutManager(new LinearLayoutManager(UpdateSampleInformationActivity.this));
                            rvList.setAdapter(popXialaAdapter);
                            popXialaAdapter.setNewData(alarmList);
                            popXialaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data1 = newSampleInformationAdapter.getData();
                                    TaskHeadInfoListBean.DataBean.HeadInfoListDTO headInfoListDTO = data1.get(position);
                                    headInfoListDTO.setFieldValue("" + popXialaAdapter.getData().get(position));
                                    newSampleInformationAdapter.getData().set(currentposition, headInfoListDTO);
                                    newSampleInformationAdapter.notifyDataSetChanged();
                                    mMovieTicketWindow.dismiss();

                                }
                            });
                            popXialaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    List<TaskHeadInfoListBean.DataBean.HeadInfoListDTO> data1 = newSampleInformationAdapter.getData();
                                    TaskHeadInfoListBean.DataBean.HeadInfoListDTO headInfoListDTO = data1.get(position);
                                    headInfoListDTO.setFieldValue("" + popXialaAdapter.getData().get(position));
                                    newSampleInformationAdapter.getData().set(currentposition, headInfoListDTO);
                                    newSampleInformationAdapter.notifyDataSetChanged();
                                    mMovieTicketWindow.dismiss();
                                }
                            });
                        }
                    })
                    .setOutsideTouchable(true)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    .create();
            if (mMovieTicketWindow == null || mMovieTicketWindow.isShowing()) {
                mMovieTicketWindow.setFocusable(true); // 这个很重要
                mMovieTicketWindow.showAsDropDown(view);
            }

        }
    }


    public static String getTimeMinute(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}