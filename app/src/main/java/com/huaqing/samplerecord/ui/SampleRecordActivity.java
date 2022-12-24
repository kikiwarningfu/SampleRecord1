package com.huaqing.samplerecord.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.adapter.SamleRecordAdapter;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.SampleRecordBean;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
import com.huaqing.samplerecord.utlis.KyDialogBuilder;
import com.huaqing.samplerecord.utlis.OkGoUtils;
import com.huaqing.samplerecord.widget.TopBar;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采样记录  修改状态栏
 */
public class SampleRecordActivity extends BaseActivity implements View.OnClickListener {

    private ShimmerRecyclerView rvList;
    private SamleRecordAdapter samleRecordAdapter;
    private String taskId = "";
    private TopBar topBar;
    private RelativeLayout rlAdd;
    private String status = "0";

    //    按钮 是 未完成 已完成 分别对应 0 1
    private TextView tvUncompleted;
    private TextView tvAlreadyCompleted;
    private String clickType="1";
    public static void goActivity(Context context, String taskId) {
        Intent intent = new Intent(context, SampleRecordActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_sample_record;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void initData() {
        topBar.getTvCenter().setText("采样任务");
        topBar.getIvFinish().setVisibility(View.VISIBLE);

        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        taskId = getIntent().getStringExtra("taskId");

        samleRecordAdapter = new SamleRecordAdapter();
        rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvList.setAdapter(samleRecordAdapter);
        tvUncompleted = findViewById(R.id.tv_un_completed);
        tvAlreadyCompleted = findViewById(R.id.tv_already_completed);
        tvUncompleted.setOnClickListener(this);
        tvAlreadyCompleted.setOnClickListener(this);
        samleRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (status.equals("0")) {
                    NewSampleInformationActivity.Companion.goActivity(SampleRecordActivity.this,
                            "" + samleRecordAdapter.getData().get(position).getId(), false, taskId);
                }
            }
        });
        samleRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_del:

                        final KyDialogBuilder builder = new KyDialogBuilder(SampleRecordActivity.this);
                        builder.setTitle("是否删除该天次?");
                        builder.setMessage("");
                        builder.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                builder.dismiss();
                            }

                        });
                        builder.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                builder.dismiss();
                                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限

                                String id = samleRecordAdapter.getData().get(position).getId();

                                PostBean postBean = new PostBean();
                                postBean.setId("" + id);
//                                BASE_URL + "/lims/sampling/app/delSamplTask/"; //删除采样任务
     OkGoUtils.normalPostNoParams(SampleRecordActivity.this,
     BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/delSamplTask/" + id, "" + AccountHelper.getToken()
                                        , new OkGoUtils.httpCallBack() {
                                            @Override
                                            public void doCallBack(boolean success, String response, String message, int code) {
                                                if (success) {
                                                    List<SampleRecordBean.DataDTO> data = samleRecordAdapter.getData();
                                                    if (data != null && data.size() > 0) {
                                                        data.remove(position);
                                                    }
                                                    samleRecordAdapter.setNewData(data);
                                                    samleRecordAdapter.notifyDataSetChanged();
                                                    showToast("删除成功");
                                                } else {
                                                    showToast("" + message);
                                                }
                                            }
                                        });
                            }

                        });
                        builder.show();


                        break;
                }
            }
        });
    }

    private void getData() {
        rvList.showShimmerAdapter();
        PostBean postBean=new PostBean();
        postBean.setStatus(""+status);
        postBean.setSamplingPlanId(""+taskId);
//        BASE_URL + "/lims/sampling/app/taskList/"; //根据方案id获取采样任务集合
        OkGoUtils.doHttpPost(this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/taskList/",
                new Gson().toJson(postBean), new OkGoUtils.httpCallBack() {
            @Override
            public void doCallBack(boolean success, String response, String message, int code) {
                if (success) {
                    samleRecordAdapter.setclickType(clickType);
                    try {
                        SampleRecordBean sampleRecordBean = new Gson().fromJson(response, SampleRecordBean.class);
                        List<SampleRecordBean.DataDTO> data = sampleRecordBean.getData();
                        if (data != null && data.size() > 0) {
                            samleRecordAdapter.setNewData(data);
                        } else {
                            samleRecordAdapter.setNewData(new ArrayList<>());
                        }
                    } catch (Exception e) {
                        samleRecordAdapter.setNewData(new ArrayList<>());
                    }

                } else {
                    showToast("" + message);
                }
                rvList.hideShimmerAdapter();
            }
        });
    }

    @Override
    protected void initViews() {


        topBar = findViewById(R.id.topbar);
        rvList = findViewById(R.id.rv_record_list);
        rlAdd = findViewById(R.id.rl_add);
        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.divider_1)
                .apply();
        rlAdd.setOnClickListener(this);

//        // 布局是否侵入状态栏（true 不侵入，false 侵入）
//        fitWindow = true
//        // 状态栏背景颜色（色值）
//        color = Color.RED
//        // 状态栏背景颜色（资源 id）
//        colorRes = R.color.deepSkyBlue
//        // 状态栏背景 drawable
//        drawableRes = R.drawable.bg_common
//        // 以上三个设置背景的方法用一个即可，如多次设置，后面的会把前面的覆盖掉
//        // light模式：状态栏字体 true: 灰色，false: 白色 Android 6.0+
//        light = true
//        // 低版本 light 模式不生效，重新设置状态栏背景
//        // 防止状态栏背景色跟字体颜色一致导致字体看不见
//        // lvl 系列方法仅在低版本（不支持 light 模式的版本）下开启 light 模式生效
//        lvlColor = Color.BLACK
//        lvlColorRes = R.color.cyan
//        lvlDrawableRes = R.drawable.bg_lvl
//        // 以上三个 lvl 方法用一个即可，如多次设置，后面的会把前面的覆盖掉
    }

    TimePickerView pvTime;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add:
//                pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
//                //控制时间范围
////        Calendar calendar = Calendar.getInstance();
////        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
//                pvTime.setTime(new Date());
//                pvTime.setCyclic(true);
//                pvTime.setCancelable(true);
//                //时间选择后回调
//                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//
//                    @Override
//                    public void onTimeSelect(Date date) {
//                        XLog.e(""+getTime(date));
//                    }
//                });
//                pvTime.show();
//                expandLayout.toggleExpand();
//                showToast("跳转到新界面2");
//                SampleDayTimeActivity.goActivity(this, "" + taskId);
                NewSampleInformationActivity.Companion.goActivity(SampleRecordActivity.this,
                        "", true, taskId);
//                SampleDayTimeActivity.goActivity(this,taskId);
                break;
            case R.id.tv_un_completed:
                status = "0";
                tvUncompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select);
                tvUncompleted.setTextColor(Color.parseColor("#FFFFFF"));
                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect);
                tvAlreadyCompleted.setTextColor(Color.parseColor("#000000"));
                clickType="1";
                getData();
                break;
            case R.id.tv_already_completed:
                status = "1";
                tvUncompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect);
                tvUncompleted.setTextColor(Color.parseColor("#000000"));
                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select);
                tvAlreadyCompleted.setTextColor(Color.parseColor("#FFFFFF"));
                clickType="2";

                getData();
                break;

        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
