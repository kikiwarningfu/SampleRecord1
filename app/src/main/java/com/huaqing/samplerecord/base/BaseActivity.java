package com.huaqing.samplerecord.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elvishew.xlog.XLog;
import com.huaqing.samplerecord.MyApplication;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.bean.ShowSuccessEvent;
import com.huaqing.samplerecord.bean.TaskHeadInfoListBean;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.AppManager;
import com.huaqing.samplerecord.utlis.KeyBoardUtils;
import com.huaqing.samplerecord.utlis.StatusBarUtil;
import com.huaqing.samplerecord.utlis.SystemUtil;
import com.huaqing.samplerecord.utlis.ToastUtils;
import com.huaqing.samplerecord.widget.AlertDialog;
import com.huaqing.samplerecord.widget.Loading_view;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;

/**
 * author    : ysy
 * introduce :  svn://47.92.72.140/软件/采样记录项目
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected MyApplication mineApplication;
    //    private QMUILoadingView loadView;
    private Loading_view loading;
    private AlertDialog myDialog;//通知弹窗
    protected Activity context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkGo.getInstance().init(getApplication());
        EventBus.getDefault().register(this);
        mineApplication = (MyApplication) getApplication();
        AppManager.getAppManager().addActivity(this);
        this.context = this;
//        PushAgent.getInstance(this).onAppStart();
        //SOFT_INPUT_ADJUST_RESIZE 屏幕整体上移   SOFT_INPUT_ADJUST_PAN  覆盖屏幕
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认不弹出虚拟键盘
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏省去mainifest设置竖屏
        setContentView(setLayout());
        int version = SystemUtil.getVersion();
        if (version >= 6) {
            StatusBarUtil.statusBarLightMode(this);
            StatusBarUtil.setTransparent(this); //设置状态栏全透明
        }
        initViews();
        initData();
        myDialog = new AlertDialog(context).builder();


    }

//    protected abstract int getTopBarLayout();

    protected abstract int setLayout();

    protected abstract void initData();

    protected abstract void initViews();

    public void showOut() {
        if (!myDialog.isShowing()) {
            myDialog.setGone().setTitle("由于您长时间为未登录，需要重新登录！").setMsg("").setNegativeButton("取消",
                    null).setPositiveButton("确定", v -> {
                myDialog.dismiss();
                myDialog = null;
                AccountHelper.setToken("");
                startLogin();

            }).show();
        }

//        final KyDialogBuilder builder = new KyDialogBuilder(this);
//        builder.setTitle("token失效 是否重新登录?");
//        builder.setMessage("");
//        builder.setNegativeButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                builder.dismiss();
//            }
//
//        });
//        builder.setPositiveButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                builder.dismiss();
//                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
//
//                AccountHelper.setToken("");
//                startLogin();
//            }
//
//        });
//        builder.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginError(ShowSuccessEvent event) {
        showOut();
    }

    private void startLogin() {
        AppManager.getAppManager().finishAllActivity();
        Intent intent = new Intent(getPackageName() + ".com.action.login");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            if (!isTaskRoot()) {
                finish();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkNetwork(this)) {
//            showToast("请检查网络");
            XLog.e("请检查网络");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterViewByIds(), ev)) return super.dispatchTouchEvent(ev);
            if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0)
                return super.dispatchTouchEvent(ev);
            View v = getCurrentFocus();
            if (isFocusEditText(v, hideSoftByEditViewIds())) {
                if (isTouchView(hideSoftByEditViewIds(), ev))
                    return super.dispatchTouchEvent(ev);
                //隐藏键盘
                KeyBoardUtils.hideInputForce(this);
                clearViewFocus(v, hideSoftByEditViewIds());

            }
        }
        return super.dispatchTouchEvent(ev);

    }

    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    public void clearViewFocus(View v, int... ids) {
        if (null != v && null != ids && ids.length > 0) {
            for (int id : ids) {
                if (v.getId() == id) {
                    v.clearFocus();
                    break;
                }
            }
        }


    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) return false;
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    public boolean isFocusEditText(View v, int... ids) {
        if (v instanceof EditText) {
            EditText tmp_et = (EditText) v;
            for (int id : ids) {
                if (tmp_et.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(int[] ids, MotionEvent ev) {
        int[] location = new int[2];
        for (int id : ids) {
            View view = findViewById(id);
            if (view == null) continue;
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    public int[] hideSoftByEditViewIds() {
        return null;
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    public View[] filterViewByIds() {
        return null;
    }

    /**
     * @return true 有网  false 无网
     */
    private boolean checkNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

//    /**
//     * 加载loading动画
//     */
//    private void setLoadView() {
//        loadView = new QMUILoadingView(this);
//        loadView.setVisibility(View.GONE);
//        loadView.setSize(120);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        layoutParams.gravity = Gravity.CENTER;
//        addContentView(loadView, layoutParams);
//    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setTopBar(QMUITopBar topbar) {
        topbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhiteA));
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);
        //设置状态栏黑色字体和图标，
        //支持4.4以上的MIUI和flyme  以及5.0以上的其他android
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    protected void setTopBar() {
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);
        //设置状态栏黑色字体和图标，
        //支持4.4以上的MIUI和flyme  以及5.0以上的其他android
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    /**
     * 设置半透明沉浸
     */
//    protected void setStatusBar(int color) {
//        StatusBarUtils.setStatusBarColor(getWindow(), Color.WHITE,true);
//        StatusBarUtils.setStatusBarTranslucent(context);
//        StatusBarUtils.compat(context, color);
//    }
    protected void showToast(String message) {
        ToastUtils.show(message);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    public void showLoading() {
//        loadView.setVisibility(View.VISIBLE);
//        loadView.start();
//    }
//
//
//    public void hideLoading() {
//        loadView.stop();
//        loadView.setVisibility(View.GONE);
//    }

    public void showLoading() {

        try {
            if (loading == null) {
                loading = new Loading_view(this, R.style.CustomDialog);
                loading.show();
            } else {
                loading.show();
            }

        } catch (Exception e) {

        }

    }

    public void showLoading(String text) {

        try {
            if (loading == null) {
                loading = new Loading_view(this, R.style.CustomDialog);
                loading.setText("" + text);
                loading.show();
            } else {
                loading.show();
            }
//            mLoadingDialog = LoadDialogUtils.showLoadingDialog(this, "加载中");
//            if (progressDialog == null) {
//                progressDialog = new Dialog(this, R.style.progress_dialog);
//                progressDialog.setContentView(R.layout.dialog_loading);
//                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                gifImageView = progressDialog.findViewById(R.id.gv_error);
//                gifImageView.setImageResource(R.drawable.load_gif);
//                progressDialog.setCanceledOnTouchOutside(false);
//            }
//
//            progressDialog.show();
        } catch (Exception e) {

        }

    }

    //
    public void dismissLoading() {
        if (loading != null) {
            loading.dismiss();//3秒后调用关闭加载的方法
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancel();
        dismissLoading();
        EventBus.getDefault().unregister(this);
        AppManager.getAppManager().finishActivity(this);
        if (myDialog != null) {
            myDialog.dismiss();
            myDialog = null;
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyboard(v.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isEMUI3_1() {
        if ("EmotionUI_3.1".equals(getEmuiVersion())) {
            return true;
        }
        return false;
    }

    @SuppressLint("PrivateApi")
    private static String getEmuiVersion() {
        Class<?> classType = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, "ro.build.version.emui");
        } catch (ClassNotFoundException e) {

        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 设置字体大小不随系统改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return result;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }

    //add 获取的一个权限
    static boolean isOk = false;

    @SuppressLint("CheckResult")
    protected static boolean requestPermission(Activity activity, String permissions) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(new String[]{permissions})
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            isOk = true;
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            isOk = false;
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                            isOk = false;
                        }
                    }
                });
        return isOk;
    }

    @SuppressLint("CheckResult")
    protected static void requestPermission(Activity activity, String[] permissions, final PermissionsResultListener listener) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            listener.onSuccessful();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            listener.onFailure();
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                            listener.onFailure();
                        }
                    }
                });

    }


    //权限接口
    public interface PermissionsResultListener {
        //成功
        void onSuccessful(int[] grantResults);

        void onSuccessful();

        //失败
        void onFailure();
    }

    private PermissionsResultListener mListener;
    private int mRequestCode;
    private List<String> mListPermissions = new ArrayList<>();

    /**
     * 动态权限申请
     *
     * @param permissions
     * @param requestCode
     * @param listener
     */
    protected void checkPermissions(String[] permissions, int requestCode, PermissionsResultListener listener) {
        //权限不能为空
        if (permissions != null || permissions.length != 0) {
            mListener = listener;
            mRequestCode = requestCode;
            for (int i = 0; i < permissions.length; i++) {
                if (!isHavePermissions(permissions[i])) {
                    mListPermissions.add(permissions[i]);
                }
            }
            //遍历完后申请
            applyPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestCode) {
            if (grantResults.length > 0) {
                mListener.onSuccessful(grantResults);
            } else {
                mListener.onFailure();
            }
        }
    }

    // 判断是否有写入数据的权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//        //有权限
//    } else {
//        // 获取（请求）权限
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//    }
    //判断权限是否申请
    private boolean isHavePermissions(String permissions) {
        if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    //申请权限
    private void applyPermissions() {
        if (!mListPermissions.isEmpty()) {
            int size = mListPermissions.size();
            ActivityCompat.requestPermissions(this, mListPermissions.toArray(new String[size]), mRequestCode);
        }
    }
/*使用
*    checkPermissions(new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,}, 300, new PermissionsResultListener() {
        @Override
        public void onSuccessful(int[] grantResults) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "同意权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure() {
            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
        }
    });*/

    //把小写映射成大写
//    public static class UpperTransform extends ReplacementTransformationMethod {
//        @Override
//        protected char[] getOriginal() {
//            char[] ori = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
//            return ori;
//        }
//
//        @Override
//        protected char[] getReplacement() {
//            char[] dis = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
//            return dis;
//        }
//    }
////然后
//inputDlg.setTransformationMethod(new UpperCaseTransform());
////注意取值时，需要将取到的EditText的值利用.trim().toUpperCase()转换成大写。
//    android:digits=“0123456789abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ”
//    添加了这个属性，只能输入android:digits中有的字符，其他字符点击键盘也不会被输入到EditText中。

//     mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                if (getActivity() != null){
//                    Glide.with(getActivity()).resumeRequests();//恢复Glide加载图片
//                }
//            }else {
//                if (getActivity() != null){
//                    Glide.with(getActivity()).pauseRequests();//禁止Glide加载图片
//                }
//            }
//        }
//    });
//    https://www.jianshu.com/p/43b9181bede0
}