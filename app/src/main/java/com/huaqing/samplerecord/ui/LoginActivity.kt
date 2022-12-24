package com.huaqing.samplerecord.ui

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.gson.Gson
import com.huaqing.samplerecord.R
import com.huaqing.samplerecord.base.BaseActivity
import com.huaqing.samplerecord.bean.LoginBean
import com.huaqing.samplerecord.bean.PostBean
import com.huaqing.samplerecord.bean.ResponseBean
import com.huaqing.samplerecord.urlmanager.BaseUrlManager
import com.huaqing.samplerecord.utlis.AccountHelper
import com.huaqing.samplerecord.utlis.ConstantsUtils
import com.huaqing.samplerecord.utlis.OkGoUtils
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX

/**
 * 登录  com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0   测试提交更换加载大图问题 解决
 */
class LoginActivity : BaseActivity() {
    //    账户admin4 密码admin123  用这个 测试

    private val textAccount: EditText? by lazy {  findViewById(R.id.et_account)}
    private val textPassword: EditText? by lazy { findViewById(R.id.et_password) }
    private val ivAccountClear:ImageView? by lazy { findViewById(R.id.iv_account_close) }
    private val ivPasswordClear:ImageView? by lazy { findViewById(R.id.iv_passord_close) }

    override fun hideSoftByEditViewIds(): IntArray? {
        return intArrayOf(R.id.et_account, R.id.et_password)
    }


    override fun setLayout(): Int {
        return R.layout.activity_login
    }

    override fun initData() {


    }

    override fun initViews() {
        if (TextUtils.isEmpty(AccountHelper.getToken())) {
            UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.divider_1)
                .apply()



            ivAccountClear!!.setOnClickListener {
                textAccount!!.setText("")
                ivAccountClear!!.setVisibility(View.GONE)
            }
            ivPasswordClear!!.setOnClickListener {
                textPassword!!.setText("")
                ivPasswordClear!!.setVisibility(View.GONE)
            }


            textAccount!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    AccountCheck(s)
                    if (s.length >= 1) {
                        ivAccountClear!!.setVisibility(View.VISIBLE)
                    }
                }
            })



            textPassword!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    passwordCheck(s)
                    if (s.length >= 1) {
                        ivPasswordClear!!.setVisibility(View.VISIBLE)
                    }
                }
            })
        } else {
            startActivity(SampleTaskActivity::class.java)
            finish()
        }


    }

    private fun passwordCheck(s: Editable) {

        if (s.length >= 1) {
        } else {
           ivPasswordClear!!.setVisibility(View.GONE)
        }
    }
    private fun AccountCheck(s: Editable) {

        if (s.length >= 1) {
        } else {
            ivAccountClear!!.setVisibility(View.GONE)
        }
    }

    fun onClick(view: View) {
        var textAct = textAccount!!.text.toString()
        var textPws = textPassword!!.text.toString()
        if (TextUtils.isEmpty(textAct)) {
            showToast("请输入账号")
            return
        }
        if (TextUtils.isEmpty(textPws)) {
            showToast("请输入密码")
            return
        }
        var postBean = PostBean()
        postBean.password = textPws
        postBean.username = textAct
        var gson = Gson()
//    public static String APPLOGIN = BASE_URL + "/lims/sampling/app/login"; //登录 的接口
        OkGoUtils.doHttpPost(
            this,
            "",
           BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/login",
            gson.toJson(postBean),
            OkGoUtils.httpCallBack { success, response, message,code ->
                if (success) {
                    var bean = Gson().fromJson(response, LoginBean::class.java)
                    if (bean.code == 200) {
                        AccountHelper.setToken("" + bean.token)
                        AccountHelper.setUserId(""+bean.userId)
                        startActivity(SampleTaskActivity::class.java)
                        finish()
                    } else {
                        showToast("" + bean.msg)
                    }

                } else {
                    showToast("请求失败")
                }



            })


    }
//    new AsyncTask<Object, Object, Object>() {
//        @Override
//        protected String doInBackground(Object... arg0) {
//            Context ctx = MainActivity.this.getBaseContext();
//
//            // -----------------第一种------------------
//            // 把客户端证书和私钥用openssl导出为P12格式
//            String p12File = "client.p12";
//            // 你需要知道P12的密码
//            String pwd1 = "123456";
//            // 生成KeyStore，密码为P12的密码
//            KeyStore keyStore1 = KsManager.getKeyStoreByP12(ctx, p12File, pwd1);
//
//            // 把服务器证书用keytool工具生成KeyStore并导入其中
//            String bksFile = "client.bks";
//            // 你需要知道bks的密码
//            String bksPwd = "123456";
//            // 生成TrustStore
//            KeyStore trustStore1 = KsManager.getTrustStoreByBks(ctx, bksFile, bksPwd);
//            // ---------------------------------------
//
//            // -----------------第二种-------------------
//            // 生成你的私钥文件，内容是...BEGIN RSA PRIVATE KEY...
//            String clientPem = "client.pem";
//            // 根据你的私钥生成客户端证书
//            String clientCrt = "client.crt";
//            // 为KeyStore设置一个密码
//            String pwd2 = "123456";
//            // 生成KeyStore
//            KeyStore keyStore2 = KsManager.getKeyStoreByCrtPem(ctx, clientCrt, clientPem, pwd2);
//
//            // 服务器证书
//            String serverCrt = "server.crt";
//            // 生成TrustStore
//            KeyStore trustStore2 = KsManager.getTrustStoreByCrt(ctx, serverCrt);
//            // ---------------------------------------
//
//            // -------------使用HTTPS工具类---------------
//            // 初始化方法只需要调用一次   生成你的私钥文件，内容是...BEGIN RSA PRIVATE KEY... pem cert
//            HTTPS.init(keyStore2, pwd1, trustStore2);
//            // 发起请求
//            new HTTPS().doGET(HTTPS_URL, null);
//
//            // ----------使用HttpClientTool工具类----------
//            // 初始化方法只需要调用一次   把客户端证书和私钥用openssl导出为P12格式
//            HttpClientTool.init(keyStore1, pwd2, trustStore1);
//            // 发起请求
//            HttpClientTool.doGET(HTTPS_URL, null);
//
//            return null;
//        }
//    }.execute();
}