package com.huaqing.samplerecord.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.adapter.MySampleListAdapter;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.SampleInformationBean;
import com.huaqing.samplerecord.bean.SampleInformationDetailBean;
import com.huaqing.samplerecord.bean.SampleInformationListBean;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
import com.huaqing.samplerecord.utlis.OkGoUtils;
import com.huaqing.samplerecord.widget.TopBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 采样信息详情
 */
public class SampleInfoDetailAcytivity extends BaseActivity {

    private String scanCode;
    private TopBar topBar;
    private RecyclerView rvList;
    private TextView tvWeisuoCompany,tvShoujianCompany,tvNoticeNumber,tvSampleCode,tvAnalizeProject,tvSampleTime;
    public static void goActivity(Context context, String scanCode) {
        Intent intent = new Intent(context, SampleInfoDetailAcytivity.class);
        intent.putExtra("scanCode", scanCode);
        context.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_sample_info_detail_acytivity;
    }
    MySampleListAdapter mySampleListAdapter;
    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        scanCode = getIntent().getStringExtra("scanCode");
        topBar = findViewById(R.id.topbar);
        rvList=findViewById(R.id.rv_list);
        tvWeisuoCompany=findViewById(R.id.tv_weituo_danwei);
        tvShoujianCompany=findViewById(R.id.tv_shoujian_danwei);
        tvNoticeNumber=findViewById(R.id.tv_notice_number);
        tvSampleCode=findViewById(R.id.tv_sample_code);
        tvAnalizeProject=findViewById(R.id.tv_analyze_project);
        tvSampleTime=findViewById(R.id.tv_sample_time);



        rvList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        mySampleListAdapter =new MySampleListAdapter();
        rvList.setAdapter(mySampleListAdapter);
        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PostBean postBean = new PostBean();
        showLoading();
//        BASE_URL+"/lims/sampling/app/getSampleInfo";//获取采样信息详情
        postBean.setSampleCode("" + scanCode);
        OkGoUtils.doHttpPost(SampleInfoDetailAcytivity.this,
                AccountHelper.getToken(),
                BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/getSampleInfo",
                new Gson().toJson(postBean), new OkGoUtils.httpCallBack() {
                    @Override
                    public void doCallBack(boolean success, String response, String message, int code) {
                        dismissLoading();
                        SampleInformationDetailBean data = new Gson().fromJson(response, SampleInformationDetailBean.class);
                        if (data != null && data.getData() != null) {
                            SampleInformationDetailBean.DataDTO data1 = data.getData();
                            Map<String, String> otherInfo = data1.getOtherInfo();
                            Iterator<Map.Entry<String, String>> iterator = otherInfo.entrySet().iterator();
                            List<SampleInformationDetailBean.DataDTO.FillContentBean> fiiList = new ArrayList<>();
                            while (iterator.hasNext()) {
                                Map.Entry<String, String> next = iterator.next();
                                String key = next.getKey();
                                String value = next.getValue();
                                SampleInformationDetailBean.DataDTO.FillContentBean fillContentBean = new SampleInformationDetailBean.DataDTO.FillContentBean();
                                fillContentBean.setContent("" + value);
                                fillContentBean.setName("" + key);
                                fiiList.add(fillContentBean);
                            }
                            data1.setFillContentBeans(fiiList);
                            mySampleListAdapter.setNewData(fiiList);



//                            tvWeisuoCompany=findViewById(R.id.tv_weituo_danwei);
//                            tvShoujianCompany=findViewById(R.id.tv_shoujian_danwei);
//                            tvNoticeNumber=findViewById(R.id.tv_notice_number);
//                            tvSampleCode=findViewById(R.id.tv_sample_code);
//                            tvAnalizeProject=findViewById(R.id.tv_analyze_project);
//                            tvSampleTime=findViewById(R.id.tv_sample_time);

                            tvWeisuoCompany.setText(""+data1.getEntrustUnit());
                            tvShoujianCompany.setText(""+data1.getEntrustedUnit());
                            tvNoticeNumber.setText(""+data1.getNoticeCode());
                            tvSampleCode.setText(""+data1.getSampleCode());
                            tvAnalizeProject.setText(""+data1.getAnalyzedItem());
                            tvSampleTime.setText(""+data1.getSamplingTime());


                        }

                    }
                });
    }
}