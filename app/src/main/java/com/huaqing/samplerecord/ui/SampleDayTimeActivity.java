package com.huaqing.samplerecord.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.ResponseBean;
import com.huaqing.samplerecord.imageselect.FullyGridLayoutManager;
import com.huaqing.samplerecord.imageselect.GridImageAdapter;
import com.huaqing.samplerecord.listener.onUploadFileListener;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
import com.huaqing.samplerecord.utlis.KyDialogBuilder;
import com.huaqing.samplerecord.utlis.OkGoUtils;
import com.huaqing.samplerecord.utlis.compresshelper.CompressHelper;
import com.huaqing.samplerecord.widget.TopBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 采样信息  天次 频次  暂时舍弃
 */
public class SampleDayTimeActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtDayTime;
    private EditText mEtPinCiTime;
    private TextView tvSave;
    private TopBar topBar;
    private String mTaskId;
    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private RecyclerView dispose_recycler;
    private PopupWindow pop;
    private LinearLayout home_f;
    List<Integer> uploadError = new ArrayList<>();
    List<PostBean.ImageFileListBean> uploadAddress = new ArrayList<>();
    LinearLayout llUploadAddress;

    public static void goActivity(Context context, String taskId) {
        Intent intent = new Intent(context, SampleDayTimeActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_sample_day_time;
    }

    @Override
    protected void initData() {


    }


    @Override
    protected void initViews() {
        mEtDayTime = findViewById(R.id.et_day_times);
        mEtPinCiTime = findViewById(R.id.et_pinci_time);
        topBar = findViewById(R.id.topbar);
        llUploadAddress = findViewById(R.id.ll_upload_address);
        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.divider_1)
                .apply();
        mTaskId = getIntent().getStringExtra("taskId");
        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.getTvCenter().setText("采样信息");
        tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);
        dispose_recycler = (RecyclerView) findViewById(R.id.dispose_recycler);
        home_f = (LinearLayout) findViewById(R.id.home_f);
        if (ConstantsUtils.isUploadImage) {
            llUploadAddress.setVisibility(View.VISIBLE);
            initPicSelect();
        } else {
            llUploadAddress.setVisibility(View.GONE);
        }


    }

    private void initPicSelect() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        dispose_recycler.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);

        dispose_recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(SampleDayTimeActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(SampleDayTimeActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(SampleDayTimeActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @SuppressLint("CheckResult")
        @Override
        public void onAddPicClick() {
            //获取写的权限
            RxPermissions rxPermission = new RxPermissions(SampleDayTimeActivity.this);
            rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) {
                            if (permission.granted) {// 用户已经同意该权限
                                //第一种方式，弹出选择和拍照的dialog
                                showPop();
                                //第二种方式，直接进入相册，但是 是有拍照得按钮的
//                                showAlbum();
                            } else {
                                Toast.makeText(SampleDayTimeActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    private void showPop() {
        View bottomView = View.inflate(this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(home_f, Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(SampleDayTimeActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(3)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(SampleDayTimeActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                String strDayTime = mEtDayTime.getText().toString();
                String strPinCiTime = mEtPinCiTime.getText().toString();
                if (TextUtils.isEmpty(strDayTime)) {
                    showToast("请输入天次");
                    return;
                }
                if (TextUtils.isEmpty(strPinCiTime)) {
                    showToast("请输入频次");
                    return;
                }
                if (ConstantsUtils.isUploadImage) {
                    /*上传照片得  最后再改吧*/
                    List<File> filePath = new ArrayList<>();
                    uploadAddress.clear();
                    if (selectList.size() > 0) {
                        for (int m = 0; m < selectList.size(); m++) {
                            String path = selectList.get(m).getPath();
                            File file = CompressHelper.getDefault(SampleDayTimeActivity.this).compressToFile(new File(path));
                            filePath.add(file);
                        }
                        uploadError.clear();
//                        for (int n = 0; n < filePath.size(); n++) {
//                            XLog.e("上传第n=" + n + "张图片");
//                            uploadFile(filePath.get(n), n, strDayTime, strPinCiTime);
//                        }
                    } else {
                        getDatas(strDayTime, strPinCiTime);
                    }
                } else {
                    PostBean postBean = new PostBean();
                    postBean.setTaskId("" + mTaskId);
                    postBean.setDayNum("" + strDayTime);
                    postBean.setFrequency("" + strPinCiTime);
//                    OkGoUtils.normalRequest(this, ConstantsUtils.APPADDSAMPLERECORD,
//                            "" + new Gson().toJson(postBean), "" + AccountHelper.getToken(),
//                            new OkGoUtils.httpCallBack() {
//                                @Override
//                                public void doCallBack(boolean success, String response, String message, int code) {
//                                    if (success) {
//                                        showToast("添加成功");
//                                        finish();
//                                    } else {
//                                        showToast("" + message);
//                                    }
//                                }
//                            });
                }
                break;
        }
    }

    private void getDatas(String strDayTime, String strPinCiTime) {

        if (uploadError.size() > 0) {
            showToast("图片上传失败");
            return;
        }
        PostBean postBean = new PostBean();
        if (uploadAddress.size() > 0) {
            postBean.setImageFileList(uploadAddress);
        }
        postBean.setTaskId("" + mTaskId);
        postBean.setDayNum("" + strDayTime);
        postBean.setFrequency("" + strPinCiTime);
//        OkGoUtils.normalRequest(this, ConstantsUtils.APPADDSAMPLERECORD,
//                "" + new Gson().toJson(postBean), "" + AccountHelper.getToken(),
//                new OkGoUtils.httpCallBack() {
//                    @Override
//                    public void doCallBack(boolean success, String response, String message, int code) {
//                        if (success) {
//                            showToast("添加成功");
//                            finish();
//                        } else {
//                            showToast("" + message);
//                        }
//                    }
//                });
    }

    private void uploadFile(List<File> file, int position, String content, String title) {
//BASE_URL + "/lims/sampling/app/uploadSamplingFile";  上传文件
        OkGoUtils.upLoadFiles(BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/uploadSamplingFile",
                SampleDayTimeActivity.this, null, "fileList", file, new onUploadFileListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onUploadProgress(Progress progress) {

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        ResponseBean responseBean = new Gson().fromJson(response.body(), ResponseBean.class);
                        String msg = responseBean.getMsg();
                        PostBean.ImageFileListBean imageFileListBean = new PostBean.ImageFileListBean();
                        imageFileListBean.setName("" + position);
                        imageFileListBean.setUrl("" + msg);
                        uploadAddress.add(imageFileListBean);
                        XLog.e("" + new Gson().toJson(response.body()));
                        XLog.e("上传第n=" + position + "图片成功 地址" + msg);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        uploadError.add(position);
                        XLog.e("上传第n=图片失败 地址position===" + position);
                        if (position == selectList.size() - 1) {
                            dismissLoading();
                            showToast("上传失败");
                        }
                    }

                    @Override
                    public void onFinish() {
                        XLog.e("okgo  uploadAddress===" + uploadAddress.size() + "-" + "===" + selectList.size());
                        if (uploadAddress.size() >= selectList.size()) {
                            XLog.e("finish position===" + position + "address size===" + uploadAddress.size());
                            XLog.e("插入图片请求数据");
                            getDatas(content, title);
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调

                images = PictureSelector.obtainMultipleResult(data);
                selectList.addAll(images);
                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
                XLog.e("" + new Gson().toJson(selectList));

            }
        }
    }
}