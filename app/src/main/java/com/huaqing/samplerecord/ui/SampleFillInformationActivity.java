package com.huaqing.samplerecord.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.huaqing.samplerecord.R;
import com.huaqing.samplerecord.adapter.SampleCourInfoAdapter;
import com.huaqing.samplerecord.base.BaseActivity;
import com.huaqing.samplerecord.bean.PostBean;
import com.huaqing.samplerecord.bean.ResponseBean;
import com.huaqing.samplerecord.bean.SampleCourseInfoListBean;
import com.huaqing.samplerecord.bean.SampleInformationBean;
import com.huaqing.samplerecord.imageselect.FullyGridLayoutManager;
import com.huaqing.samplerecord.imageselect.GridImageAdapter;
import com.huaqing.samplerecord.listener.onUploadFileListener;
import com.huaqing.samplerecord.urlmanager.BaseUrlManager;
import com.huaqing.samplerecord.utlis.AccountHelper;
import com.huaqing.samplerecord.utlis.ConstantsUtils;
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
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 填充内容
 */
public class SampleFillInformationActivity extends BaseActivity implements View.OnClickListener {

    List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> dataList;
    private ShimmerRecyclerView rvCourse;
    private SampleCourInfoAdapter sampleCourInfoAdapter;//样品 数目 填写
    private TextView tvSave;
    private String mRecordId;
    private TopBar topBar;

    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private RecyclerView dispose_recycler;
    private PopupWindow pop;
    private LinearLayout home_f;

    List<Integer> uploadError = new ArrayList<>();
    List<PostBean.ImageFileListBean> uploadAddress = new ArrayList<>();
    LinearLayout llUploadAddress;

    @Override
    protected int setLayout() {
        return R.layout.activity_sample_fill_information;
    }

    @Override
    protected void initData() {

        dataList = (List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean>) getIntent().getSerializableExtra("datas");
        if (dataList != null && dataList.size() > 0) {
            sampleCourInfoAdapter.setNewData(dataList);
        } else {
            sampleCourInfoAdapter.setNewData(new ArrayList<>());
        }
        rvCourse.hideShimmerAdapter();

    }

    @Override
    protected void initViews() {
        rvCourse = findViewById(R.id.rv_course);
        tvSave = findViewById(R.id.tv_save);
        topBar = findViewById(R.id.topbar);
        UltimateBarX.statusBarOnly(this)
                .fitWindow(true)
                .colorRes(R.color.white)
                .light(true)
                .lvlColorRes(R.color.divider_1)
                .apply();
        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.getTvCenter().setText("采样信息");
        mRecordId = getIntent().getStringExtra("mRecordId");
        tvSave.setOnClickListener(this);
        rvCourse.showShimmerAdapter();
        llUploadAddress = findViewById(R.id.ll_upload_address);
        sampleCourInfoAdapter = new SampleCourInfoAdapter(SampleFillInformationActivity.this);
        LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvCourse.setLayoutManager(layout);
        rvCourse.setAdapter(sampleCourInfoAdapter);

        dispose_recycler = (RecyclerView) findViewById(R.id.dispose_recycler);
        home_f = (LinearLayout) findViewById(R.id.home_f);
        if (ConstantsUtils.isUploadImage) {
            initPicSelect();
            llUploadAddress.setVisibility(View.VISIBLE);
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
                            PictureSelector.create(SampleFillInformationActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(SampleFillInformationActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(SampleFillInformationActivity.this).externalPictureAudio(media.getPath());
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
            RxPermissions rxPermission = new RxPermissions(SampleFillInformationActivity.this);
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
                                Toast.makeText(SampleFillInformationActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
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
                        PictureSelector.create(SampleFillInformationActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(3)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(SampleFillInformationActivity.this)
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

    private void showAlbum() {
        //参数很多，根据需要添加
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.selectionMedia(selectList)// 是否传入已选图片
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled()// 裁剪是否可放大缩小图片
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> data1 = sampleCourInfoAdapter.getData();
                if (data1 != null && data1.size() > 0) {
                    LinkedHashMap<String, String> contents = new LinkedHashMap<>();
                    for (int m = 0; m < data1.size(); m++) {
                        contents.put("" + data1.get(m).getName(), "" + data1.get(m).getContent());
                    }
                    if (ConstantsUtils.isUploadImage) {
                        /*上传照片得  最后再改吧*/
                        List<File> filePath = new ArrayList<>();
                        uploadAddress.clear();
                        if (selectList.size() > 0) {
                            for (int m = 0; m < selectList.size(); m++) {
                                String path = selectList.get(m).getPath();
                                File file = CompressHelper.getDefault(SampleFillInformationActivity.this).compressToFile(new File(path));
                                filePath.add(file);
                            }
                            uploadError.clear();
                            for (int n = 0; n < filePath.size(); n++) {
                                XLog.e("上传第n=" + n + "张图片");
                                uploadFile(filePath.get(n), n);
                            }
                        } else {
                            getDatas();
                        }
                    } else {
                        PostBean postBean1 = new PostBean();
                        postBean1.setId("" + mRecordId);
                        postBean1.setBodyData("" + new Gson().toJson(contents));
                        postBean1.setCode("0");
//                        BASE_URL + "/lims/sampling/app/saveSamplingBody/"; //表体信息存储
                        OkGoUtils.doHttpPost(SampleFillInformationActivity.this, AccountHelper.getToken(),
                                "" + BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/saveSamplingBody/",
                                new Gson().toJson(postBean1), new OkGoUtils.httpCallBack() {
                                    @Override
                                    public void doCallBack(boolean success, String response, String message, int code) {
                                        if (success) {
                                            showToast("添加成功");
                                            SampleCourseInfoListBean bean = new Gson().fromJson(response, SampleCourseInfoListBean.class);
                                            List<SampleCourseInfoListBean.DataBean> data = bean.getData();
                                            if (data != null) {
                                                String codeX = data.get(0).getCodeX();
//                                                Intent intent = new Intent(SampleFillInformationActivity.this, QrCodeActivity.class);
//                                                intent.putExtra("qrCode", codeX);
//                                                setResult(RESULT_OK, intent);
//                                                startActivity(intent);
//                                                SampleFillInformationActivity.this.finish();
                                            }
                                        } else {
                                            showToast("" + message);
                                        }
                                    }
                                });
                    }



                    break;
                }
        }
    }


    private void getDatas() {

        if (uploadError.size() > 0) {
            showToast("图片上传失败");
            return;
        }
        List<SampleInformationBean.SampleInfoTitleBean.BottomContentBean> data1 = sampleCourInfoAdapter.getData();
        LinkedHashMap<String, String> contents = new LinkedHashMap<>();
        for (int m = 0; m < data1.size(); m++) {
            contents.put("" + data1.get(m).getName(), "" + data1.get(m).getContent());
        }
        PostBean postBean1 = new PostBean();
        if (uploadAddress.size() > 0) {
            postBean1.setImageFileList(uploadAddress);
        }
        postBean1.setId("" + mRecordId);
        postBean1.setBodyData("" + new Gson().toJson(contents));
        postBean1.setCode("0");
//        BASE_URL + "/lims/sampling/app/saveSamplingBody/"; //表体信息存储
        OkGoUtils.doHttpPost(SampleFillInformationActivity.this, AccountHelper.getToken(),
                "" + BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/saveSamplingBody/",
                new Gson().toJson(postBean1), new OkGoUtils.httpCallBack() {
                    @Override
                    public void doCallBack(boolean success, String response, String message, int code) {
                        if (success) {
                            showToast("添加成功");
                            SampleCourseInfoListBean bean = new Gson().fromJson(response, SampleCourseInfoListBean.class);
                            List<SampleCourseInfoListBean.DataBean> data = bean.getData();
                            if (data != null) {
//                                String codeX = data.get(0).getCodeX();
//                                Intent intent = new Intent(SampleFillInformationActivity.this, QrCodeActivity.class);
//                                intent.putExtra("qrCode", codeX);
//                                setResult(RESULT_OK, intent);
//                                startActivity(intent);
//                                SampleFillInformationActivity.this.finish();
                            }
                        } else {
                            showToast("" + message);
                        }
                    }
                });
    }

    private void uploadFile(File file, int position) {
//BASE_URL + "/lims/sampling/app/uploadSamplingFile";//上传文件
        OkGoUtils.upLoadFile(BaseUrlManager.getInstance().getBaseUrl()+"/lims/sampling/app/uploadSamplingFile"
                , SampleFillInformationActivity.this, null, "file", file, new onUploadFileListener() {
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
                    getDatas();
                }
            }
        });

    }
}