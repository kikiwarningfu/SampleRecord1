package com.huaqing.samplerecord.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.adapter.SampleCourInfoAdapter;
import com.huaqing.samplerecord.adapter.SampleCourInfoListAdapter;
import com.huaqing.samplerecord.adapter.SampleInformationAdapter;
import com.huaqing.samplerecord.adapter.SampleInformationTitleAdapter;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.SampleCourseInfoListBean;
import com.huaqing.samplerecord.bean.SampleInformationBean;
import com.huaqing.samplerecord.bean.SampleInformationListBean;
import com.huaqing.samplerecord.bean.TestBean;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
import com.huaqing.samplerecord.utlis.KyDialogBuilder;
import com.huaqing.samplerecord.utlis.OkGoUtils;
import com.huaqing.samplerecord.utlis.Utils;
import com.huaqing.samplerecord.widget.TopBar;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 采样信息界面
 */
public class SampleInformationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvSave, tvCancel;
    private ImageView ivAdd;
    private LinearLayout llFirstSave, llSampleCourse;
    private SampleInformationTitleAdapter sampleInformationTitleAdapter;//水域信息  环境信息 标题
    private RecyclerView rvSampleInformationTitle;
    private SampleInformationAdapter sampleInformationAdapter;//水域信息 列表 的适配器
    private ShimmerRecyclerView rvSampleInformationList;

    private SampleCourInfoListAdapter sampleCourInfoListAdapter;//样品  加入列表
    private RecyclerView rvCourseList;
    private SampleCourInfoAdapter sampleCourInfoAdapter;//样品 数目 填写
    private RecyclerView rvCourse;

    List<SampleInformationBean.SampleInfoTitleBean.FillContentBean> fillContentBeanList = new ArrayList<>();
    List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> bottomContenBeanList = new ArrayList<>();

    private String mTaskId = "";
    private String mRecordId = "";
    private float scrollX;
    private float scrollY;
    private TopBar topBar;
    private boolean isFirst = true;
    private NestedScrollView nsSampleList;
    private LinearLayout llEmptyView;

    public static void goActivity(Context context, String taskId, String recordId) {
        Intent intent = new Intent(context, SampleInformationActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("recordId", recordId);
        context.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_sample_information;
    }

    @Override
    protected void initViews() {

        ivAdd = (ImageView) findViewById(R.id.iv_add);
        tvSave = findViewById(R.id.tv_save);
        tvCancel = findViewById(R.id.tv_cancel);
        mTaskId = getIntent().getStringExtra("taskId");
        mRecordId = getIntent().getStringExtra("recordId");
        llFirstSave = findViewById(R.id.ll_first_save);
        llSampleCourse = findViewById(R.id.ll_sample_course);
        nsSampleList = findViewById(R.id.nescrllView);
        llEmptyView = findViewById(R.id.ll_empty_view);
        topBar = findViewById(R.id.topbar);
        tvSave.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        ivAdd.setOnClickListener(this);


        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.divider_1)
                .apply();

        rvSampleInformationTitle = findViewById(R.id.rv_sample_information_title);
        sampleInformationTitleAdapter = new SampleInformationTitleAdapter();
        rvSampleInformationTitle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvSampleInformationTitle.setAdapter(sampleInformationTitleAdapter);
        rvSampleInformationList = findViewById(R.id.rv_sample_information_list);
        sampleInformationAdapter = new SampleInformationAdapter(SampleInformationActivity.this);
        LinearLayoutManager layout1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvSampleInformationList.setLayoutManager(layout1);
        rvSampleInformationList.setAdapter(sampleInformationAdapter);
        rvSampleInformationList.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                scrollX = event.getX();
                scrollY = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v.getId() != 0 && Math.abs(scrollX - event.getX()) <= 5 && Math.abs(scrollY - event.getY()) <= 5) {
                    //recyclerView空白处点击事件
                    Utils.hideSoftKeyboard(SampleInformationActivity.this);
                }
            }
            return false;
        });

        sampleInformationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_dismiss_keyboard:
                        Utils.hideSoftKeyboard(SampleInformationActivity.this);
                        break;
                }
            }
        });
        rvCourseList = findViewById(R.id.rv_course_list);
        sampleCourInfoListAdapter = new SampleCourInfoListAdapter();
        rvCourseList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvCourseList.setAdapter(sampleCourInfoListAdapter);
        rvCourse = findViewById(R.id.rv_course);
        sampleCourInfoAdapter = new SampleCourInfoAdapter(SampleInformationActivity.this);
        LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvCourse.setLayoutManager(layout);
        rvCourse.setAdapter(sampleCourInfoAdapter);

        rvSampleInformationList.setVisibility(View.VISIBLE);
        nsSampleList.setVisibility(View.VISIBLE);
        llFirstSave.setVisibility(View.VISIBLE);
        ivAdd.setVisibility(View.GONE);
        llSampleCourse.setVisibility(View.GONE);
        llEmptyView.setVisibility(View.GONE);
        sampleCourInfoListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String codeX ="";
//                String codeX = sampleCourInfoListAdapter.getData().get(position).getCodeX();

                switch (view.getId()) {
                    case R.id.iv_del:

                        final KyDialogBuilder builder = new KyDialogBuilder(SampleInformationActivity.this);
                        builder.setTitle("是否删除该样品?");
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
                                PostBean postBean = new PostBean();
                                postBean.setId("" + mRecordId);
                                postBean.setCode("" + codeX);
                                String strMsg = new Gson().toJson(postBean);
//                                BASE_URL + "/lims/sampling/app/removeBodyRecord/"; //表体记录删除
                                OkGoUtils.normalRequest(SampleInformationActivity.this,
                                        BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/removeBodyRecord/",

                                        "" + strMsg, "" + AccountHelper.getToken(), new OkGoUtils.httpCallBack() {
                                            @Override
                                            public void doCallBack(boolean success, String response, String message, int code) {
//                                                if (success) {
//                                                    List<SampleCourseInfoListBean.DataBean> data = sampleCourInfoListAdapter.getData();
//                                                    data.remove(position);
//                                                    sampleCourInfoListAdapter.setNewData(data);
//                                                    sampleCourInfoListAdapter.notifyDataSetChanged();
//                                                    if (sampleCourInfoListAdapter.getData().size() > 0) {
//                                                        llEmptyView.setVisibility(View.GONE);
//                                                    } else {
//                                                        llEmptyView.setVisibility(View.VISIBLE);
//                                                    }
//                                                } else {
//                                                    showToast("" + message);
//                                                }
                                            }
                                        });
                            }

                        });
                        builder.show();
                        break;
                    case R.id.rl_content:

                        Intent intent = new Intent(SampleInformationActivity.this, UpdateSampleInformationActivity.class);
                        intent.putExtra("code", codeX);
                        intent.putExtra("taskId", mTaskId);
                        intent.putExtra("RecordId", mRecordId);
                        startActivityForResult(intent, 2);
                        break;
                }
            }
        });
    }

    List<SampleInformationBean.SampleInfoTitleBean> totalList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initData() {

        topBar.getTvCenter().setText("采样信息");
        topBar.getIvFinish().setVisibility(View.VISIBLE);

        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvSampleInformationList.showShimmerAdapter();
//        BASE_URL + "/lims/sampling/app/listFormData/"; //获取某条表体记录的详细信息
        OkGoUtils.doHttpGet(this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/listFormData/" + mRecordId,
                null, new OkGoUtils.httpCallBack() {
            @Override
            public void doCallBack(boolean success, String response, String message, int code) {

                if (code == 200) {
                    try {
                        TestBean sampleInformationListBean = new Gson().fromJson(response, TestBean.class);
                        if (sampleInformationListBean.getData() != null) {
                            TestBean.DataBean data = sampleInformationListBean.getData();
                            List<TestBean.DataBean.BodyDataBean> bodyData = data.getBodyData();
                            List<TestBean.DataBean.HeadDataBean> headData = data.getHeadData();
                            List<SampleInformationListBean.DataBean.HeadDataBean> head = new ArrayList<>();
                            for (int m = 0; m < headData.size(); m++) {
                                SampleInformationListBean.DataBean.HeadDataBean headDataBean = new SampleInformationListBean.DataBean.HeadDataBean();
                                List<TestBean.DataBean.HeadDataBean.ConfBean> conf = headData.get(m).getConf();
                                List<SampleInformationListBean.DataBean.HeadDataBean.ConfBean> confBeanList = new ArrayList<>();
                                for (int n = 0; n < conf.size(); n++) {
                                    SampleInformationListBean.DataBean.HeadDataBean.ConfBean confBean = new SampleInformationListBean.DataBean.HeadDataBean.ConfBean();
                                    confBean.setFieldName(conf.get(n).getFieldName());
                                    confBean.setFieldType(conf.get(n).getFieldType());
                                    confBean.setGroupName(conf.get(n).getGroupName());
                                    confBean.setSerialNum(conf.get(n).getSerialNum());
                                    confBean.setValue(conf.get(n).getValue());
                                    confBeanList.add(confBean);
                                }
                                headDataBean.setConf(confBeanList);
                                headDataBean.setGroupName(headData.get(m).getGroupName());
                                if (isFirst) {
                                    if (m == 0) {
                                        headDataBean.setSelect(true);
                                    } else {
                                        headDataBean.setSelect(false);
                                    }
                                } else {
                                    headDataBean.setSelect(false);
                                }

                                head.add(headDataBean);
                            }
                            if (head != null && head.size() > 0) {
                                totalList.clear();
                                fillContentBeanList.clear();
                                for (int m = 0; m < head.size(); m++) {
                                    if (isFirst) {
                                        if (m == 0) {
                                            head.get(m).setSelect(true);
                                        } else {
                                            head.get(m).setSelect(false);
                                        }
                                    } else {
                                        head.get(m).setSelect(false);
                                    }
                                }
                                sampleInformationTitleAdapter.setNewData(head);

                                SampleInformationListBean.DataBean.HeadDataBean headBean = head.get(0);
                                List<SampleInformationListBean.DataBean.HeadDataBean.ConfBean> conf = headBean.getConf();
                                LinkedHashMap<String, String> contents = new LinkedHashMap<>();
                                for (int m = 0; m < conf.size(); m++) {
                                    SampleInformationBean.SampleInfoTitleBean.FillContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.FillContentBean();
                                    fillContentBean.setName("" + conf.get(m).getFieldName());
                                    fillContentBean.setContent("" + conf.get(m).getValue());

                                    contents.put("" + conf.get(m).getFieldName(), "" + conf.get(m).getValue());
                                    fillContentBeanList.add(fillContentBean);
                                }
                                sampleInformationAdapter.setMyTens(0, contents);
                                sampleInformationAdapter.setNewData(fillContentBeanList);
                            }
                            sampleInformationTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    rvSampleInformationList.showShimmerAdapter();
                                    rvSampleInformationList.setVisibility(View.VISIBLE);
                                    nsSampleList.setVisibility(View.VISIBLE);
                                    llFirstSave.setVisibility(View.VISIBLE);
                                    ivAdd.setVisibility(View.GONE);
                                    llSampleCourse.setVisibility(View.GONE);
                                    llEmptyView.setVisibility(View.GONE);
                                    Utils.hideSoftKeyboard(SampleInformationActivity.this);
                                    List<SampleInformationListBean.DataBean.HeadDataBean> data = sampleInformationTitleAdapter.getData();
                                    LinkedHashMap<String, String> contents = sampleInformationAdapter.contents;
//                                    XLog.e("contents===" + new Gson().toJson(contents) + "\n");
                                    int currentPosition = sampleInformationAdapter.getCurrentPosition();

                                    List<SampleInformationListBean.DataBean.HeadDataBean.ConfBean> conf = data.get(currentPosition).getConf();
//                                    XLog.e("conf===" + new Gson().toJson(conf));
                                    for (int x = 0; x < conf.size(); x++) {
                                        String s = contents.get(conf.get(x).getFieldName());
                                        if (!TextUtils.isEmpty(s)) {
                                            conf.get(x).setValue(s);
                                        }
                                    }
                                    data.get(currentPosition).setConf(conf);
//                                    XLog.e("conf2===" + new Gson().toJson(conf));

//                                    XLog.e("data==="+new Gson().toJson(data)+"\n");

                                    for (int m = 0; m < data.size(); m++) {
                                        data.get(m).setSelect(false);
                                    }
                                    data.get(position).setSelect(true);
                                    sampleInformationTitleAdapter.setNewData(data);
                                    SampleInformationListBean.DataBean.HeadDataBean headBean = data.get(position);
                                    fillContentBeanList.clear();
                                    LinkedHashMap<String, String> myHashMap = new LinkedHashMap<>();
                                    for (int mm = 0; mm < headBean.getConf().size(); mm++) {
                                        SampleInformationBean.SampleInfoTitleBean.FillContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.FillContentBean();
                                        fillContentBean.setName("" + headBean.getConf().get(mm).getFieldName());
                                        fillContentBean.setContent("" + headBean.getConf().get(mm).getValue());
//                                        XLog.e("json=="+new Gson().toJson( headBean.getConf().get(mm))+"-----------position==="+mm+"\n");
                                        contents.put("" + headBean.getConf().get(mm), "" + headBean.getConf().get(mm).getValue());
                                        fillContentBeanList.add(fillContentBean);
                                    }
//                                    XLog.e("json==="+new Gson().toJson(fillContentBeanList));
                                    sampleInformationAdapter.setMyTens(position, myHashMap);
                                    rvSampleInformationList.setAdapter(sampleInformationAdapter);
                                    sampleInformationAdapter.setNewData(fillContentBeanList);
                                    rvSampleInformationList.hideShimmerAdapter();
                                }
                            });
                            if (bodyData != null && bodyData.size() > 0) {


                                List<SampleCourseInfoListBean.DataBean> data1 = new ArrayList<>();

                                for (int m = 0; m < bodyData.size(); m++) {
                                    String code1 = bodyData.get(m).getCode();
                                    Map<String, String> conf = bodyData.get(m).getConf();

                                    SampleCourseInfoListBean.DataBean dataBean = new SampleCourseInfoListBean.DataBean();
                                    dataBean.setProjectName(conf.get("采样项目"));
                                    dataBean.setBianhao(conf.get("样品编号"));
                                    dataBean.setCodeX(code1);
                                    data1.add(dataBean);
                                }
//                                sampleCourInfoListAdapter.setNewData(data1);
                                if (sampleCourInfoListAdapter.getData().size() > 0) {
                                    llEmptyView.setVisibility(View.GONE);
                                } else {
                                    llEmptyView.setVisibility(View.VISIBLE);
                                }
//                                BASE_URL + "/lims/sampling/app/getForm/"; //获取采样配置列表
                                OkGoUtils.doHttpGet(SampleInformationActivity.this,
                                        AccountHelper.getToken(), BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/getForm/" + mTaskId,
                                        null, new OkGoUtils.httpCallBack() {
                                    @Override
                                    public void doCallBack(boolean success, String response, String message, int code) {
                                        if (success) {
                                            SampleInformationListBean sampleInformationListBean = new Gson().fromJson(response, SampleInformationListBean.class);

                                            if (sampleInformationListBean.getData() != null) {
                                                SampleInformationListBean.DataBean data = sampleInformationListBean.getData();
                                                List<SampleInformationListBean.DataBean.BodyDataBean> body = data.getBodyData();

                                                if (body != null && body.size() > 0) {
                                                    LinkedHashMap<String, String> contentBottom = new LinkedHashMap<>();
                                                    contentBottom.clear();
                                                    bottomContenBeanList.clear();
                                                    for (int m = 0; m < body.size(); m++) {
                                                        SampleInformationBean.SampleInfoTitleBean.BottomContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.BottomContentBean();
                                                        fillContentBean.setName("" + body.get(m).getFieldName());
                                                        fillContentBean.setContent("");
                                                        fillContentBean.setCode("");
                                                        contentBottom.put("" + body.get(m).getFieldName(), "");
                                                        bottomContenBeanList.add(fillContentBean);
                                                    }
                                                    sampleCourInfoAdapter.setMyTens(0);
                                                    sampleCourInfoAdapter.setNewData(bottomContenBeanList);
                                                }

                                            }

                                        } else {
                                            showToast("" + message);
                                        }
                                    }
                                });

                            } else {
                                dealBodyData();
                            }


                        }
                    } catch (Exception e) {
                        showToast("数据异常");
                    }

                } else {
                    //获取新的数据
                    getData();
                }

                rvSampleInformationList.hideShimmerAdapter();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isFirst = false;

    }
//BASE_URL + "/lims/sampling/app/getForm/"; //获取采样配置列表
    private void dealBodyData() {
        OkGoUtils.doHttpGet(this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/getForm" + mTaskId,
                null, new OkGoUtils.httpCallBack() {
            @Override
            public void doCallBack(boolean success, String response, String message, int code) {
                if (success) {
                    SampleInformationListBean sampleInformationListBean = new Gson().fromJson(response, SampleInformationListBean.class);

                    if (sampleInformationListBean.getData() != null) {
                        SampleInformationListBean.DataBean data = sampleInformationListBean.getData();
                        List<SampleInformationListBean.DataBean.BodyDataBean> body = data.getBodyData();

                        if (body != null && body.size() > 0) {
                            LinkedHashMap<String, String> contentBottom = new LinkedHashMap<>();
                            contentBottom.clear();
                            bottomContenBeanList.clear();
                            for (int m = 0; m < body.size(); m++) {
                                SampleInformationBean.SampleInfoTitleBean.BottomContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.BottomContentBean();
                                fillContentBean.setName("" + body.get(m).getFieldName());
                                fillContentBean.setContent("");
                                fillContentBean.setCode("");
                                contentBottom.put("" + body.get(m).getFieldName(), "");
                                bottomContenBeanList.add(fillContentBean);
                            }
                            sampleCourInfoAdapter.setMyTens(0);
                            sampleCourInfoAdapter.setNewData(bottomContenBeanList);
                        }

                    }

                } else {
                    showToast("" + message);
                }
            }
        });
    }

    private void getData() {
//        BASE_URL + "/lims/sampling/app/getForm/"; //获取采样配置列表
        OkGoUtils.doHttpGet(this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/getForm/" + mTaskId,
                null, new OkGoUtils.httpCallBack() {
            @Override
            public void doCallBack(boolean success, String response, String message, int code) {
                if (code == 200) {
                    if (success) {
                        try {


                            SampleInformationListBean sampleInformationListBean = new Gson().fromJson(response, SampleInformationListBean.class);

                            if (sampleInformationListBean.getData() != null) {
                                SampleInformationListBean.DataBean data = sampleInformationListBean.getData();
                                List<SampleInformationListBean.DataBean.HeadDataBean> head = data.getHeadData();
                                List<SampleInformationListBean.DataBean.BodyDataBean> body = data.getBodyData();

                                if (head != null && head.size() > 0) {
                                    totalList.clear();
                                    fillContentBeanList.clear();
                                    for (int m = 0; m < head.size(); m++) {
                                        if (m == 0) {
                                            head.get(m).setSelect(true);
                                        } else {
                                            head.get(m).setSelect(false);
                                        }
                                    }

                                    sampleInformationTitleAdapter.setNewData(head);

                                    SampleInformationListBean.DataBean.HeadDataBean headBean = head.get(0);
                                    List<SampleInformationListBean.DataBean.HeadDataBean.ConfBean> conf = headBean.getConf();
                                    LinkedHashMap<String, String> contents = new LinkedHashMap<>();
                                    for (int m = 0; m < conf.size(); m++) {
                                        SampleInformationBean.SampleInfoTitleBean.FillContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.FillContentBean();
                                        fillContentBean.setName("" + conf.get(m).getFieldName());
                                        fillContentBean.setContent("");
                                        contents.put("" + conf.get(m).getFieldName(), "");
                                        fillContentBeanList.add(fillContentBean);
                                    }
                                    sampleInformationAdapter.setMyTens(0, contents);
                                    sampleInformationAdapter.setNewData(fillContentBeanList);
                                }
                                if (body != null && body.size() > 0) {
                                    LinkedHashMap<String, String> contentBottom = new LinkedHashMap<>();
                                    contentBottom.clear();
                                    bottomContenBeanList.clear();
                                    for (int m = 0; m < body.size(); m++) {
                                        SampleInformationBean.SampleInfoTitleBean.BottomContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.BottomContentBean();
                                        fillContentBean.setName("" + body.get(m).getFieldName());
                                        fillContentBean.setContent("");
                                        fillContentBean.setCode("");
                                        contentBottom.put("" + body.get(m).getFieldName(), "");
                                        bottomContenBeanList.add(fillContentBean);
                                    }
                                    sampleCourInfoAdapter.setMyTens(0);
                                    sampleCourInfoAdapter.setNewData(bottomContenBeanList);
                                }
                                sampleInformationTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        rvSampleInformationList.showShimmerAdapter();
                                        rvSampleInformationList.setVisibility(View.VISIBLE);
                                        nsSampleList.setVisibility(view.VISIBLE);
                                        llFirstSave.setVisibility(View.VISIBLE);
                                        ivAdd.setVisibility(View.GONE);
                                        llSampleCourse.setVisibility(View.GONE);
                                        llEmptyView.setVisibility(View.GONE);
                                        List<SampleInformationListBean.DataBean.HeadDataBean> data = sampleInformationTitleAdapter.getData();
                                        LinkedHashMap<String, String> contents = sampleInformationAdapter.contents;
                                        if (contents != null && contents.size() > 0) {
                                            int currentPosition = sampleInformationAdapter.getCurrentPosition();
                                            List<SampleInformationListBean.DataBean.HeadDataBean.ConfBean> conf = data.get(currentPosition).getConf();
                                            for (int m = 0; m < conf.size(); m++) {
                                                String value = contents.get(conf.get(m).getFieldName());
                                                if(!TextUtils.isEmpty(value)) {
                                                    conf.get(m).setValue(value);
                                                }
                                            }
                                            data.get(currentPosition).setConf(conf);
                                        }
                                        for (int m = 0; m < data.size(); m++) {
                                            data.get(m).setSelect(false);
                                        }
                                        data.get(position).setSelect(true);

                                        sampleInformationTitleAdapter.setNewData(data);
                                        SampleInformationListBean.DataBean.HeadDataBean headBean = data.get(position);
                                        fillContentBeanList.clear();
                                        LinkedHashMap<String, String> myHashMap = new LinkedHashMap<>();
                                        for (int mm = 0; mm < headBean.getConf().size(); mm++) {
                                            SampleInformationBean.SampleInfoTitleBean.FillContentBean fillContentBean = new SampleInformationBean.SampleInfoTitleBean.FillContentBean();
                                            fillContentBean.setName("" + headBean.getConf().get(mm).getFieldName());
                                            fillContentBean.setContent("" + headBean.getConf().get(mm).getValue());
                                            contents.put("" + headBean.getConf().get(mm), "");
                                            fillContentBeanList.add(fillContentBean);
                                        }
                                        sampleInformationAdapter.setMyTens(position, myHashMap);
                                        rvSampleInformationList.setAdapter(sampleInformationAdapter);
                                        sampleInformationAdapter.setNewData(fillContentBeanList);
                                        rvSampleInformationList.hideShimmerAdapter();
                                    }
                                });
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        showToast("" + message);
                    }
                }
            }
        });
    }



    /**
     * 将JSON字符串转换为集合
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_save://保存
                List<SampleInformationListBean.DataBean.HeadDataBean> data = sampleInformationTitleAdapter.getData();

                if (data != null && data.size() > 0) {
                    if (data.size() > 0) {
                        for (int m = 0; m < data.size(); m++) {
                            List<SampleInformationListBean.DataBean.HeadDataBean.ConfBean> conf = data.get(m).getConf();
                            for (int n = 0; n < conf.size(); n++) {
                                String fieldName = conf.get(n).getFieldName();
                                String s = sampleInformationAdapter.contents.get(fieldName);
                                if (s != null) {
                                    conf.get(n).setValue(s);
                                }
                            }
                            data.get(m).setSelect(false);
                        }
                    }

                    sampleInformationTitleAdapter.setNewData(data);
                    PostBean postBean = new PostBean();
                    postBean.setId("" + mRecordId);
                    postBean.setHeadData("" + new Gson().toJson(sampleInformationTitleAdapter.getData()));
                    String s = new Gson().toJson(postBean);
//                    BASE_URL + "/lims/sampling/app/saveSamplingHead/"; //表头信息保存
                    OkGoUtils.doMyHttpPost(SampleInformationActivity.this, AccountHelper.getToken(),
                            "" + BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/saveSamplingHead/",
                            s, new OkGoUtils.httpCallBack() {
                                @Override
                                public void doCallBack(boolean success, String response, String message, int code) {
                                    if (success) {
//                                        showToast("保存成功");
//                                        List<SampleCourseInfoListBean.DataBean> data1 = sampleCourInfoListAdapter.getData();
//
//                                        rvSampleInformationList.setVisibility(View.GONE);
//                                        nsSampleList.setVisibility(View.GONE);
//                                        llFirstSave.setVisibility(View.GONE);
//                                        ivAdd.setVisibility(View.VISIBLE);
//                                        llSampleCourse.setVisibility(View.VISIBLE);
//                                        if (data1.size() > 0) {
//                                            llEmptyView.setVisibility(View.GONE);
//                                        } else {
//                                            llEmptyView.setVisibility(View.VISIBLE);
//
//                                        }
                                    } else {
                                        showToast("" + message);
                                    }
                                }
                            });
                }

                break;
            case R.id.tv_cancel://取消
                finish();

                break;
            case R.id.iv_add:
                List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> data1 = sampleCourInfoAdapter.getData();
                XLog.e("json===" + new Gson().toJson(data1));
                Intent intent = new Intent(this, SampleFillInformationActivity.class);
                intent.putExtra("mRecordId", "" + mRecordId);
                intent.putExtra("datas", (Serializable) data1);
                startActivityForResult(intent, 2);


                break;

        }
    }
}