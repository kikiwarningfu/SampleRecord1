//package com.huaqing.samplerecord.ui;

//
///**
// * 二维码界面UI aaaaa aaaa
// */
//public class QrCodeActivity extends BaseActivity {
//    private ImageView ivLogo;
//
//    private String mCode;
//    private TopBar topBar;
//
//    //添加注释
//    @Override
//    protected int setLayout() {
//        return R.layout.activity_qr_code;
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected void initViews() {
//        ivLogo = findViewById(R.id.iv_logo);
//        topBar = findViewById(R.id.topbar);
//        topBar.getTvCenter().setText("二维码");
//        UltimateBarX.statusBarOnly(this)
//                .fitWindow(true)
//                .colorRes(R.color.white)
//                .light(true)
//                .lvlColorRes(R.color.divider_1)
//                .apply();
//        topBar.getIvFinish().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        mCode = getIntent().getStringExtra("qrCode");
//        XLog.e("code===" + mCode);
//        Bitmap qrCode = createQRCode("" + mCode, 300, 300, null);
//
////        Bitmap qrCode = QRCodeEncoder.createQRCode(""+mCode, 300, 300, null);
////        Bitmap success = QrCodeUtil.createQRImage("12312312312313", 100, 100,
////        Bitmap success = QrCodeUtil.createQRImage("" + array.toString(), 100, 100,
////                null);
////        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
////        binding.ivQrCodeImage.setImageBitmap(success);
////        Glide.with(BsQrCodeActivity.this)
////                .load(success)
////                .crossFade()
////                .into(binding.ivQrCodeImage);
//        Glide.with(this)
//                .load(qrCode)
//                .apply(new RequestOptions().centerCrop().
//                        diskCacheStrategy(DiskCacheStrategy.ALL))
//
//                .into(ivLogo);
////        var TextWatcher: NewSampleInformationAdapter.MyTextNewChangedListener()
//
//    }
//
//
//    /**
//     * 创建二维码
//     */
//    public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm) {
//        try {
//            if (content == null || "".equals(content)) {
//                return null;
//            }
//            // 配置参数
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            // 容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
//                    heightPix, hints);
//            int[] pixels = new int[widthPix * heightPix];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * widthPix + x] = 0xff000000;
//                    } else {
//                        pixels[y * widthPix + x] = 0xffffffff;
//                    }
//                }
//            }
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }
//            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
//            return bitmap;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 在二维码中间添加Logo图案
//     */
//    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
//        if (src == null) {
//            return null;
//        }
//        if (logo == null) {
//            return src;
//        }
//        //获取图片的宽高
//        int srcWidth = src.getWidth();
//        int srcHeight = src.getHeight();
//        int logoWidth = logo.getWidth();
//        int logoHeight = logo.getHeight();
//        if (srcWidth == 0 || srcHeight == 0) {
//            return null;
//        }
//        if (logoWidth == 0 || logoHeight == 0) {
//            return src;
//        }
//        //logo大小为二维码整体大小的1/5
//        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//        try {
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(src, 0, 0, null);
//            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
//            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
//            canvas.save();
//            canvas.restore();
//        } catch (Exception e) {
//            bitmap = null;
//            e.getStackTrace();
//        }
//        return bitmap;
//    }
//
//    /**
//     * 保存图片到图库
//     *
//     * @param bmp
//     */
//    public static void saveImageToGallery(Bitmap bmp, String bitName) {
//        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(),
//                "hejiumao");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//
//        String fileName = bitName + ".jpg";
//        File file = new File(appDir, fileName);
//
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        setPhotoFile(file);
//    }
//
//    /**
//     * @param bmp     获取的bitmap数据
//     * @param picName 自定义的图片名
//     */
//    public static void saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
//        if (bmp != null) {
//
//            saveImageToGallery(bmp, picName);
//        } else {
//            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
//
//        }
//        String fileName = null;
//        //系统相册目录
//        String galleryPath = Environment.getExternalStorageDirectory()
//                + File.separator + Environment.DIRECTORY_DCIM
//                + File.separator + "Camera" + File.separator;
//
//
//        // 声明文件对象
//        File file = null;
//        // 声明输出流
//        FileOutputStream outStream = null;
//        try {
//            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
//            file = new File(galleryPath, picName + ".jpg");
//            // 获得文件相对路径
//            fileName = file.toString();
//            // 获得输出流，如果文件中有内容，追加内容
//            outStream = new FileOutputStream(fileName);
//            if (null != outStream) {
//                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
//            }
//        } catch (Exception e) {
//            e.getStackTrace();
//        } finally {
//            try {
//                if (outStream != null) {
//                    outStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, null);
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        context.sendBroadcast(intent);
//        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
//
//    }
//}