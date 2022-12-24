package com.huaqing.samplerecord.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.huaqing.samplerecord.R
import com.huaqing.samplerecord.adapter.SampleTaskSecondAdapter
import com.huaqing.samplerecord.base.BaseActivity
import com.huaqing.samplerecord.bean.PostBean
import com.huaqing.samplerecord.bean.SampleTaskSecondBean
import com.huaqing.samplerecord.urlmanager.BaseUrlManager
import com.huaqing.samplerecord.utlis.AccountHelper
import com.huaqing.samplerecord.utlis.ConstantsUtils
import com.huaqing.samplerecord.utlis.KyDialogBuilder
import com.huaqing.samplerecord.utlis.OkGoUtils
import com.lzy.okgo.model.HttpParams
import com.scwang.smartrefresh.layout.SmartRefreshLayout

class SampleTaskSecondActivity : BaseActivity(), View.OnClickListener {

    private val tvUnCompleted: TextView by lazy { findViewById(R.id.tv_un_completed) }
    private val tvAlreadyCompleted: TextView by lazy { findViewById(R.id.tv_already_completed) }
    private val tvWancheng: TextView by lazy { findViewById(R.id.tv_wancheng) }
    private val rvList: RecyclerView by lazy { findViewById(R.id.rv_list) }
    private var status = "1"
    private var noticeCode: String? = ""
    private val sampleTaskAdapter: SampleTaskSecondAdapter by lazy { SampleTaskSecondAdapter() }
    private val smartRefreshLayout: SmartRefreshLayout by lazy {
        findViewById(R.id.swip_refresh)
    }

    companion object {
        fun goActivity(context: Context, id: String) {
            val intent = Intent(context, SampleTaskSecondActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }


    override fun setLayout(): Int {
        return R.layout.activity_sample_task_second
    }

    override fun initData() {
        noticeCode = intent.getStringExtra("id")
        smartRefreshLayout.setOnRefreshListener {
            getdata();
        }
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = sampleTaskAdapter
        sampleTaskAdapter.setOnItemChildClickListener() { adapter, view, position ->
            var data = sampleTaskAdapter.data.get(position) as SampleTaskSecondBean.DataDTO

            when (view.id) {
                R.id.tv_click -> {
                    when (data.status) {
                        "1" -> {
                            val builder = KyDialogBuilder(this)
                            builder.setTitle("是否开始采样?")
                            builder.setMessage("")
                            builder.setNegativeButton(
                                "取消"
                            ) { builder.dismiss() }
                            builder.setPositiveButton("确定") {
                                builder.dismiss()
                                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                                //未开始
//                                BASE_URL + "/lims/sampling/app/startPlan/"; //开始采样
                                OkGoUtils.doHttpGet(this,
                                    AccountHelper.getToken(),
                                    BaseUrlManager.getInstance().baseUrl+ "/lims/sampling/app/startPlan/" +
                                            data.id,
                                    HttpParams(),
                                    OkGoUtils.httpCallBack { success, response, message, code ->
                                        SampleRecordActivity.goActivity(this, "" + data.id)
                                        finish()
                                    })
                            }
                            builder.show()

                        }
                        "2" -> {
                            val builder = KyDialogBuilder(this)
                            builder.setTitle("是否采样完成?")
                            builder.setMessage("")
                            builder.setNegativeButton(
                                "取消"
                            ) { builder.dismiss() }
                            builder.setPositiveButton("确定") {
                                builder.dismiss()
                                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                                //未开始
//                                BASE_URL + "/lims/sampling/app/completePlan/"; //完成采样
                                OkGoUtils.doHttpGet(
                                    this,
                                    "" + AccountHelper.getToken(),
                                    "" + BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/completePlan/" + data.id,
                                    null
                                ) { success, response, message, code ->
                                    if (success) {
                                        showToast("操作成功")
                                        getdata();
                                    } else {
                                        showToast("" + message)
                                    }
                                }
                            }
                            builder.show()


                        }
                        "3" -> {
//                            tvClick.setVisibility(View.GONE)
                            //已完成
//                            helper.setText(R.id.tv_click, "采样完成")
                        }
                        else -> {
                        }
                    }


                }
                R.id.tv_click_jixu -> {
                    SampleRecordActivity.goActivity(this, "" + data.id)
                }
            }


        }
    }

    override fun initViews() {
        tvUnCompleted.setOnClickListener(this)
        tvAlreadyCompleted.setOnClickListener(this)
        tvWancheng.setOnClickListener(this)

        getdata();

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_un_completed -> {
                status = "1"
                tvUnCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select)
                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect)
                tvWancheng.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect)
                tvUnCompleted.setTextColor(Color.parseColor("#FFFFFF"))
                tvAlreadyCompleted.setTextColor(Color.parseColor("#000000"))
                tvWancheng.setTextColor(Color.parseColor("#000000"))
                getdata();
            }
            R.id.tv_already_completed -> {
                status = "2"
                tvUnCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect)
                tvWancheng.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect)
                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select)
                tvAlreadyCompleted.setTextColor(Color.parseColor("#FFFFFF"))
                tvUnCompleted.setTextColor(Color.parseColor("#000000"))
                tvWancheng.setTextColor(Color.parseColor("#000000"))
                getdata();
            }
            R.id.tv_wancheng -> {
                status = "3"
                tvUnCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect)
                tvWancheng.setBackgroundResource(R.drawable.shape_solid_info_no_corners_select)
                tvAlreadyCompleted.setBackgroundResource(R.drawable.shape_solid_info_no_corners_noselect)
                tvAlreadyCompleted.setTextColor(Color.parseColor("#000000"))
                tvUnCompleted.setTextColor(Color.parseColor("#000000"))
                tvWancheng.setTextColor(Color.parseColor("#FFFFFF"))
                getdata();
            }
        }
    }

    private fun getdata() {

        val postBean = PostBean()
        if (!TextUtils.isEmpty(status)) {
            postBean.setStatus(status);
        }
        if (!TextUtils.isEmpty(noticeCode)) {
            postBean.noticeId = noticeCode
        }
        showLoading()
        postBean.token = "" + AccountHelper.getToken()
//            public static String APPPLANLIST = BASE_URL + "/lims/sampling/app/planList/";//获取采样方案集合
        OkGoUtils.doHttpPost(
            this, AccountHelper.getToken(),
                BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/planList/"

                , Gson().toJson(postBean)
        ) { success, response, message, code ->
            smartRefreshLayout.finishLoadmore()
            smartRefreshLayout.finishRefresh()

            dismissLoading()
            if (success) {
                try {
                    val taskListBean = Gson().fromJson(
                        response,
                        SampleTaskSecondBean::class.java
                    )
                    if (taskListBean.data != null && taskListBean.data.size > 0) {
                        sampleTaskAdapter.setNewData(taskListBean.data)
                    } else {
                        sampleTaskAdapter.setNewData(ArrayList())
                    }
                } catch (e: Exception) {
                    sampleTaskAdapter.setNewData(ArrayList())
                }
            } else {
                showToast("" + message)
                sampleTaskAdapter.setNewData(ArrayList())
            }
        }
    }
}