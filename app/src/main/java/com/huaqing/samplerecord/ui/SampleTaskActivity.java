package com.huaqing.samplerecord.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.adapter.SampleTaskAdapter;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.TaskListBean;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.AppManager;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
import com.huaqing.samplerecord.utlis.KyDialogBuilder;
import com.huaqing.samplerecord.utlis.OkGoUtils;
import com.huaqing.samplerecord.utlis.Utils;
import com.huaqing.samplerecord.widget.AlertDialog;
import com.huaqing.samplerecord.widget.TopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

/**
 * 采样任务
 */
public class SampleTaskActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, OnRefreshListener {

    private SampleTaskAdapter sampleTaskAdapter;
    private RecyclerView rvTaskList;
    private EditText etSearchContent;
    private String status = "0";
    private String noticeCode = "";
    private SmartRefreshLayout swip;
    private ImageView ivFinish;
    private LinearLayout llSwitchUser, llQrCode;

    //ali打开扫描界面请求码
    private int ALI_REQUEST_CODE = 99;
    public static final int CALL_LOGIN = 1002;//Call permission code
    private static final int SET_BASE_URL_REQUEST_CODE = 0X01;


    @Override
    public int[] hideSoftByEditViewIds() {
        return new int[]{R.id.et_search_content};
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_sample_task;
    }

    private AlertDialog myDialog;//弹窗


    @Override
    protected void onResume() {
        super.onResume();
        getData("", status);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initViews() {
        ImageView ivSearch = findViewById(R.id.iv_search);
        ivFinish = findViewById(R.id.iv_finish);
        swip = findViewById(R.id.swip_refresh);
        llSwitchUser = findViewById(R.id.ll_switch_user);
        llQrCode = findViewById(R.id.ll_qr_code);
        myDialog = new AlertDialog(this).builder();

        llQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(SampleTaskActivity.this, Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CALL_LOGIN);
//                } else {
//                    Intent aliIntent = new Intent();
//                    aliIntent.setClass(SampleTaskActivity.this, CaptureActivity.class);
//                    startActivityForResult(aliIntent, ALI_REQUEST_CODE);
//                }
                BaseUrlManager.getInstance().startBaseUrlManager(SampleTaskActivity.this,SET_BASE_URL_REQUEST_CODE);


            }
        });
        swip.setOnRefreshListener(this);
        swip.setEnableLoadmore(false);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAfterTwice();
            }
        });
        llSwitchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccountHelper.setToken("");
                startActivity(LoginActivity.class);
                finish();
            }
        });
        etSearchContent = findViewById(R.id.et_search_content);
        ivSearch.setOnClickListener(this);

        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.white)
                .apply();

        etSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    Utils.hideSoftKeyboard(SampleTaskActivity.this);
                    noticeCode = v.getText().toString();
                    getData("" + noticeCode, status);
                    return true;
                }
                return false;
            }
        });
//        getWindowManager()
//        windowSoftInputMode

//        if (!FcfrtAppBhUtils.isIgnoringBatteryOptimizations(this)){
//            FcfrtAppBhUtils.requestIgnoreBatteryOptimizations(this);
//        }
    }


    @Override
    protected void initData() {
        sampleTaskAdapter = new SampleTaskAdapter();
        rvTaskList = findViewById(R.id.rv_task_list);
        rvTaskList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvTaskList.setAdapter(sampleTaskAdapter);
        sampleTaskAdapter.setOnItemChildClickListener(this);
        sampleTaskAdapter.setOnItemClickListener(this);
    }

    private void getData(String noticeCode, String status) {

        PostBean postBean = new PostBean();
//        if (!TextUtils.isEmpty(status)) {
//            postBean.setStatus(status);
//        }
        if (!TextUtils.isEmpty(noticeCode)) {
            postBean.setNoticeCode(noticeCode);
        }
        postBean.setToken("" + AccountHelper.getToken());
//        BASE_URL + "/lims/sampling/app/noticeList";//获取通知单集合
        OkGoUtils.doHttpPost(this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/noticeList"
                , new Gson().toJson(postBean), new OkGoUtils.httpCallBack() {
            @Override
            public void doCallBack(boolean success, String response, String message, int code) {
                swip.finishRefresh();
                swip.finishLoadmore();
                if (success) {
                    try {
                        TaskListBean taskListBean = new Gson().fromJson(response, TaskListBean.class);
                        if (taskListBean.getData() != null && taskListBean.getData().size() > 0) {
                            sampleTaskAdapter.setNewData(taskListBean.getData());
                        } else {
                            sampleTaskAdapter.setNewData(new ArrayList<>());
                        }
                    } catch (Exception e) {
                        sampleTaskAdapter.setNewData(new ArrayList<>());
                    }

                } else {
                    showToast("" + message);
                    sampleTaskAdapter.setNewData(new ArrayList<>());
                }


            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_un_completed://未完成
//                String s = etSearchContent.getText().toString();
//                noticeCode = s;
//                status = "0";
//                tvUnCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select);
//                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect);
//                tvUnCompleted.setTextColor(Color.parseColor("#FFFFFF"));
//                tvAlreadyCompleted.setTextColor(Color.parseColor("#000000"));
//                getData("" + s, status);
//                break;
//            case R.id.tv_already_completed://已完成
//                String s1 = etSearchContent.getText().toString();
//                noticeCode = s1;
//                status = "1";
//                tvUnCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect);
//                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select);
//                tvAlreadyCompleted.setTextColor(Color.parseColor("#FFFFFF"));
//                tvUnCompleted.setTextColor(Color.parseColor("#000000"));
//                getData("" + s1, status);
//                break;


            case R.id.iv_search:
                noticeCode = etSearchContent.getText().toString();
                getData("" + noticeCode, status);
                break;

        }
    }

    private long exitTime = 0;

    /**
     * 连续点击2次退出
     */
    public void exitAfterTwice() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit();
//            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALI_REQUEST_CODE) {

            if (data != null) {
                Bundle bundle = data.getExtras();
                final String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                XLog.e("" + scanResult);
                SampleInfoDetailAcytivity.goActivity(SampleTaskActivity.this, "" + scanResult);
            }
        }else if(requestCode==SET_BASE_URL_REQUEST_CODE)
        {
          String  mUrl = BaseUrlManager.getInstance().getBaseUrl();
          XLog.e(""+mUrl);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_LOGIN) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
//                    HomePageCommonManager.getInstance().showMissingDialog(myDialog, getActivity());
                    showMissingDialog(myDialog, SampleTaskActivity.this);
                } else {
                    Intent aliIntent = new Intent();
                    aliIntent.setClass(SampleTaskActivity.this, CaptureActivity.class);
                    startActivityForResult(aliIntent, ALI_REQUEST_CODE);
                }
            }
        }
    }

    /**
     * lack permission dialog
     */
    public static void showMissingDialog(AlertDialog myDialog, Context context) {
        myDialog.setGone().setTitle("帮助")
                .setMsg("当前应用缺少定位权限。请点击 设置-权限管理 -打开所需权限。最后点击两次后退按钮，即可返回。")

                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Activity context1 = (Activity) context;
//                                context1.finish();
                                AppManager.getAppManager().finishAllActivity();
                                AppManager.getAppManager().AppExit();
                                System.exit(0);
                            }
                        }).setPositiveButton("去设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo((Activity) context);
            }
        }).show();
    }

    /**
     * 应用信息界面
     *
     * @param activity
     */
    public static void ApplicationInfo(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(localIntent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        TaskListBean.DataDTO dataBean1 = sampleTaskAdapter.getData().get(position);
        int noticeId = dataBean1.getNoticeId();
        switch (view.getId()) {
            case R.id.ll_content:
                SampleTaskSecondActivity.Companion.goActivity(SampleTaskActivity.this, "" + noticeId);
//                /*开始采样接口*/
//                if (status.equals("0")) {
//                    final KyDialogBuilder builder = new KyDialogBuilder(SampleTaskActivity.this);
//                    builder.setTitle("是否开始采样?");
//                    builder.setMessage("");
//                    builder.setNegativeButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {
//                            builder.dismiss();
//                        }
//
//                    });
//                    builder.setPositiveButton("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View arg0) {
//                            builder.dismiss();
//                            OkGoUtils.doHttpGet(SampleTaskActivity.this, "" + AccountHelper.getToken(),
//                                    "" + ConstantsUtils.APPSTARTTASK + id1,
//                                    null, new OkGoUtils.httpCallBack() {
//                                        @Override
//                                        public void doCallBack(boolean success, String response, String message, int code) {
//                                            if (success) {
//                                                getData("" + noticeCode, status);
//                                                SampleRecordActivity.goActivity(SampleTaskActivity.this, "" + dataBean1.getId());
//                                            } else {
//                                                showToast("" + message);
//                                            }
//                                        }
//                                    });
//                        }
//
//                    });
//                    builder.show();
//
//                } else {
//                    SampleRecordActivity.goActivity(SampleTaskActivity.this, "" + dataBean1.getId());
//
        }
//
//                break;
//            case R.id.tv_sample_status:
//                String status = dataBean1.getStatus();
//                String id = dataBean1.getId();//taskId
//                //是否提交采样
//                final KyDialogBuilder builder = new KyDialogBuilder(SampleTaskActivity.this);
//                builder.setTitle("是否提交采样?");
//                builder.setMessage("");
//                builder.setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        builder.dismiss();
//                    }
//
//                });
//                builder.setPositiveButton("确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        builder.dismiss();
//                        dealData(status, id);
//                    }
//
//                });
//                builder.show();
//
//
//                break;
//        }

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    /**
     * 将String转换为map数据
     */
    public static Map<String, String> json2map(String str_json) {
        Map<String, String> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGoUtils.onDestory();
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        getData("" + noticeCode, status);
    }
}