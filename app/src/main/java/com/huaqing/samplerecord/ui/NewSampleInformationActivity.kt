package com.huaqing.samplerecord.ui

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.pickerview.TimePickerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.huaqing.samplerecord.R
import com.huaqing.samplerecord.adapter.NewSampleInformationAdapter
import com.huaqing.samplerecord.adapter.PopXialaAdapter
import com.huaqing.samplerecord.adapter.SampleCourInfoListAdapter
import com.huaqing.samplerecord.adapter.SampleInformationNewTitleAdapter
import com.huaqing.samplerecord.base.BaseActivity
import com.huaqing.samplerecord.bean.*
import com.huaqing.samplerecord.bean.SampleInformationBean.SampleInfoTitleBean.FillContentBean
import com.huaqing.samplerecord.imageselect.FullyGridLayoutManager
import com.huaqing.samplerecord.imageselect.GridImageAdapter
import com.huaqing.samplerecord.imageselect.GridImageAdapter.onAddPicClickListener
import com.huaqing.samplerecord.listener.onUploadFileListener
import com.huaqing.samplerecord.urlmanager.BaseUrlManager
import com.huaqing.samplerecord.utlis.*
import com.huaqing.samplerecord.utlis.compresshelper.CompressHelper
import com.huaqing.samplerecord.widget.popupwindow.CommonPopupWindow
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.qs.helper.printer.Device
import com.qs.helper.printer.PrintService
import com.qs.helper.printer.PrinterClass
import com.qs.helper.printer.bt.BtService
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
    采样信息详情
 */
class NewSampleInformationActivity : BaseActivity(), View.OnClickListener {
    val TAG = NewSampleInformationActivity::class.java.simpleName

    private val sampleInformationTitleAdapter: SampleInformationNewTitleAdapter by lazy { SampleInformationNewTitleAdapter() }
    private val rvSampleInformationTitle: RecyclerView by lazy { findViewById(R.id.rv_sample_information_title) }
    private val linearLayoutOne: LinearLayout by lazy { findViewById(R.id.ll_content_one) }
    private val linearLayoutTwo: LinearLayout by lazy { findViewById(R.id.ll_content_two) }
    private val linearLayoutBody: LinearLayout by lazy { findViewById(R.id.ll_body) }
    private val linearLayoutEmptyView: LinearLayout by lazy { findViewById(R.id.ll_empty_view) }
    private val linearLayoutWancheng: LinearLayout by lazy { findViewById(R.id.ll_wancheng) }
    private val linearlayoutFirstSave: LinearLayout by lazy { findViewById(R.id.ll_first_save) }
    private val nescrollview: NestedScrollView by lazy { findViewById(R.id.nescrllView) }
    private val ivAdd: ImageView by lazy { findViewById(R.id.iv_add) }
    private val tvCancel: TextView by lazy { findViewById(R.id.tv_cancel) }
    private val mEtDayTime: EditText by lazy { findViewById(R.id.et_tianci) }
    private val mEtPinCiTime: EditText by lazy { findViewById(R.id.et_pinci) }
    private val tvSave: TextView by lazy { findViewById(R.id.tv_save) }

    private lateinit var newSampleInformationAdapter: NewSampleInformationAdapter
    private val rvSampleInformationList: RecyclerView by lazy { findViewById(R.id.rv_sample_information_list) }
    private lateinit var sampleCourInfoListAdapter: SampleCourInfoListAdapter
    private val rvCourseList: RecyclerView by lazy { findViewById(R.id.rv_course_list) }
    var isAdd: Boolean = false
    var id: String? = ""
    var taskBean: SampleTaskBean? = null
    private val dispose_recycler: RecyclerView by lazy { findViewById(R.id.dispose_recycler) }
    private lateinit var adapter: GridImageAdapter
    internal var selectList: MutableList<LocalMedia> = ArrayList()

    private lateinit var pop: PopupWindow
    private val home_f: LinearLayout by lazy { findViewById(R.id.home_f) }
    private val maxSelectNum = 9
    private var samplingPlanId: String? = ""
    var uploadAddress: MutableList<String> = ArrayList()
    var upShouldSubmitAddress: MutableList<LocalMedia> = ArrayList()
    var uploadError: MutableList<Int> = ArrayList()

    var editId: String = ""
    var temp: MutableList<String> = ArrayList()
    var instance: Context? = null
    val MY_PERMISSION_REQUEST_CONSTANT = 101


    var localAddress = ""

    var mHandlermain: Handler? = null
    var handlermain: Handler? = null
    private var tv_update: Thread? = null
    private var tvFlag = true
    val MESSAGE_STATE_CHANGE = 1
    val MESSAGE_READ = 2
    val MESSAGE_WRITE = 3
    var printCode = ""
    var printType = ""

    companion object {
        fun goActivity(context: Context, id: String, isAdd: Boolean, samplingPlanId: String) {
            val intent = Intent(context, NewSampleInformationActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("isAdd", isAdd)
            intent.putExtra("samplingPlanId", samplingPlanId)
            context.startActivity(intent)
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_new_sample_information
    }

    override fun initData() {

    }

    override fun initViews() {
        isAdd = intent.getBooleanExtra("isAdd", false)
        id = intent.getStringExtra("id")
        samplingPlanId = intent.getStringExtra("samplingPlanId")
        sampleCourInfoListAdapter = SampleCourInfoListAdapter()
        instance = this
        tvCancel.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        rvSampleInformationTitle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSampleInformationTitle.adapter = sampleInformationTitleAdapter
        linearLayoutTwo.visibility = View.GONE
        linearLayoutOne.visibility = View.VISIBLE
        linearlayoutFirstSave.visibility = View.VISIBLE
        linearLayoutWancheng.visibility = View.GONE
        linearLayoutBody.visibility = View.GONE
        nescrollview.visibility = View.VISIBLE
        linearLayoutWancheng.setOnClickListener(this)

        rvCourseList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCourseList.adapter = sampleCourInfoListAdapter
        sampleCourInfoListAdapter.setOnItemClickListener { adapter, view, position ->
            var recordId = sampleCourInfoListAdapter.data.get(position).id //recordId
            UpdateSampleInformationActivity.goActivity(
                this,
                "" + recordId, "false", id
            )
        }



        sampleCourInfoListAdapter.setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_print_qr_code -> {

//                if (deviceList != null) {
//                    deviceList.clear();
//                }
                    printCode = "" + sampleCourInfoListAdapter.data.get(position).sampleCode
                    printType = "1"
                    if (!PrintService.pl.IsOpen()) {
                        PrintService.pl.open(instance)
                    }
                    printCode()
                }
                R.id.tv_print_tiao_code -> {
                    printCode = "" + sampleCourInfoListAdapter.data.get(position).sampleCode
                    printType = "2"
                    if (!PrintService.pl.IsOpen()) {
                        PrintService.pl.open(instance)
                    }
                    printCode()
                }
                R.id.iv_del -> {
                    /*删除 */
                    val builder = KyDialogBuilder(this)
                    builder.setTitle("是否删除该样品?")
                    builder.setMessage("")
                    builder.setNegativeButton(
                        "取消"
                    ) { builder.dismiss() }
                    builder.setPositiveButton("确定") {
                        builder.dismiss()
                        // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                        var id = sampleCourInfoListAdapter.data.get(position).id
                        delData(id)

                    }
                    builder.show()

                }
                R.id.rl_content -> {
                    var recordId = sampleCourInfoListAdapter.data.get(position).id //recordId
                    UpdateSampleInformationActivity.goActivity(
                        this,
                        "" + recordId, "false", id
                    )
                }
            }

        })
        newSampleInformationAdapter = NewSampleInformationAdapter(this)
        newSampleInformationAdapter.setOnClickItemListener { data, adapterPosition, tvTitle, message, hashMap ->
            if (data != null && data.size > 0) {
//                for(girl in data.withIndex()){
//                    if(girl.value.fieldName.equals(tvTitle)){
//                        girl.value.fieldValue=message
//                        data.set(girl.index,girl.value)
//                    }
//                }
                for (i in 0 until data.size) {
                    if (data.get(i).fieldName.equals("" + tvTitle)) {
                        var dataget = data.get(i)
                        dataget.fieldValue = message
                        data.set(i, dataget)
                    }
                }
            }

        }
        newSampleInformationAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.ll_content_xiala -> {
                    var bean =
                        newSampleInformationAdapter.data.get(position) as TaskHeadInfoListBean.DataBean.HeadInfoListDTO
                    var fieldType = bean.fieldType
                    if (fieldType.equals("4")) {
                        //下拉框
                        var dataSource = bean.dataSource
//                        var dataSource = "G瓶，P瓶,蒸馏瓶"
                        val temp: Array<String> = dataSource.split(",".toRegex())?.toTypedArray()!!
                        selectXiaLa(temp.toMutableList(), position, bean, view, this)
                    } else if (fieldType.equals("5")) {
                        //时间选择控件
                        val pvTime: TimePickerView = TimePickerView.Builder(
                            this,
                            object : TimePickerView.OnTimeSelectListener {
                                override fun onTimeSelect(date: Date?, v: View?) {
                                    val time = getTimeMinute(date)
                                    var datas =
                                        newSampleInformationAdapter.data.get(position) as TaskHeadInfoListBean.DataBean.HeadInfoListDTO
                                    datas.fieldValue = "" + time
                                    newSampleInformationAdapter.setData(position, datas)
                                }
                            })
                            .setType(TimePickerView.Type.ALL) //默认全部显示
                            .setCancelText("取消") //取消按钮文字
                            .setSubmitText("确定") //确认按钮文字
                            .setContentSize(20) //滚轮文字大小
                            .setTitleSize(20) //标题文字大小
                            //                        .setTitleText("请选择时间")//标题文字
                            .setOutSideCancelable(true) //点击屏幕，点在控件外部范围时，是否取消显示
                            .isCyclic(true) //是否循环滚动
                            .setTextColorCenter(Color.parseColor("#000000")) //设置选中项的颜色
                            .setTextColorOut(Color.parseColor("#666666"))
                            .setTitleColor(Color.BLACK) //标题文字颜色
                            .setSubmitColor(Color.BLUE) //确定按钮文字颜色
                            .setCancelColor(Color.BLUE) //取消按钮文字颜色
                            .setLoop(false)
                            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                            //                        .isDialog(true)//是否显示为对话框样式
                            .build()
                        pvTime.setDate(Calendar.getInstance()) //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。

                        pvTime.show()

                    }


                }
            }
        }
        rvSampleInformationList.adapter = newSampleInformationAdapter
        rvSampleInformationList.layoutManager = LinearLayoutManager(this)
        tvSave.setOnClickListener(this)
        initPicSelect();


        sampleInformationTitleAdapter.setOnItemClickListener { adapter, view, position ->
            if (position == 0) {
                //显示隐藏布局
                linearLayoutOne.visibility = View.VISIBLE
                linearLayoutTwo.visibility = View.GONE
                linearLayoutBody.visibility = View.GONE
                nescrollview.visibility = View.VISIBLE
                linearlayoutFirstSave.visibility = View.VISIBLE
                linearLayoutWancheng.visibility = View.GONE
            } else if (position == sampleInformationTitleAdapter.data.size - 1) {
                linearLayoutOne.visibility = View.GONE
                linearLayoutTwo.visibility = View.GONE
                linearLayoutBody.visibility = View.VISIBLE
                nescrollview.visibility = View.GONE
                linearlayoutFirstSave.visibility = View.GONE
                linearLayoutWancheng.visibility = View.VISIBLE
                getBodyData()
            } else {

                linearLayoutOne.visibility = View.GONE
                linearLayoutTwo.visibility = View.VISIBLE
                linearLayoutBody.visibility = View.GONE
                nescrollview.visibility = View.VISIBLE
                linearlayoutFirstSave.visibility = View.VISIBLE
                linearLayoutWancheng.visibility = View.GONE
                var data =
                    sampleInformationTitleAdapter.data.get(position) as TaskHeadInfoListBean.DataBean
                var headInfoList = data.headInfoList

                newSampleInformationAdapter.setNewData(headInfoList)
                newSampleInformationAdapter.notifyDataSetChanged()

            }
            var data = sampleInformationTitleAdapter.data
            for (i in 0 until data.size) {
                data[i].isSelect = false
            }
            data[position].isSelect = true
            sampleInformationTitleAdapter.setNewData(data)
        }
        rvSampleInformationTitle.visibility = View.GONE
        if (isAdd) {
            // 新建
            taskBean = SampleTaskBean()
        } else {
            rvSampleInformationTitle.visibility = View.VISIBLE
            val postBean1 = PostBean()
//            BASE_URL + "/lims/sampling/app/getSamplTask/";//查看采样任务详情
            OkGoUtils.doHttpPost(
                this, AccountHelper.getToken(),
                "" + BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/getSamplTask/" + "" + id, Gson().toJson(postBean1)
            ) { success, response, message, code ->
                if (success) {
                    taskBean = Gson().fromJson<SampleTaskBean>(response, SampleTaskBean::class.java)
                    var data = taskBean?.data
                    mEtDayTime.setText("" + data?.whichDay)
                    mEtPinCiTime.setText("" + data?.whichTime)
                    editId = "" + data?.id
                    if (!TextUtils.isEmpty(data?.attachment)) {
//                        val temp: Array<String> =
//                            data?.attachment?.split(",".toRegex())?.toTypedArray()!!
                        //    totalList = new Gson().fromJson(json, new TypeToken<List<SampleInformationBean.SampleInfoTitleBean>>() {}.getType());

                        //少于8张，显示继续添加的图标
                        temp = Gson().fromJson(
                            data?.attachment,
                            object : TypeToken<List<String?>?>() {}.type
                        )
                        uploadAddress.clear()
                        if (temp != null && temp.size > 0) {
                            uploadAddress.addAll(0, temp)
                        }
                        var localMediaList: MutableList<LocalMedia> = ArrayList()
                        for (i in 0 until temp.size) {
                            var localMedias = LocalMedia()
                            localMedias.path = temp.get(i)
                            localMedias.mimeType = 999
                            localMediaList.add(localMedias)
                        }

                        adapter.setList(localMediaList)
                    }
                    getData();
                } else {
                    showToast("" + message)
                }
            }

        }
        tv_update = object : Thread() {
            override fun run() {
                while (tvFlag) {
                    try {
                        sleep(200)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    if (PrintService.pl != null) {
                        if (PrintService.pl.state == PrinterClass.STATE_CONNECTED) {
                            tvFlag = false
                            PrintService.pl.stopScan()
                            try {
                                if (printType.equals("1")) {
                                    //二维码
                                    PrintService.pl.printImage(createQRImage(printCode, 300, 300))
                                    PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                                } else {
                                    //条形码
                                    val btMap = BarcodeCreater.creatBarcode(
                                        instance,
                                        "" + printCode, 384, 100, true, 1
                                    )
                                    PrintService.pl.printImage(btMap)
                                    PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                                }
                                PrintService.pl.write(byteArrayOf(0x1d, 0x0c))

                            } catch (e: Exception) {
//                                showToast("打印样品编号有问题 不能有汉字")
                            }
                        } else if (PrintService.pl.state == PrinterClass.STATE_CONNECTING) {
//                                tv_status.setText(
//                                    this@PrintSettingActivity
//                                        .getResources().getString(
//                                            R.string.str_connecting
//                                        )
//                                )
                        } else if (PrintService.pl.state == PrinterClass.STATE_SCAN_STOP) {
//                                tv_status.setText(
//                                    this@PrintSettingActivity
//                                        .getResources().getString(
//                                            R.string.str_scanover
//                                        )
//                                )
//                                layoutscan.setVisibility(View.GONE)
                            //                                    InitListView();
                        } else if (PrintService.pl.state == PrinterClass.STATE_SCANING) {
//                                tv_status.setText(
//                                    this@PrintSettingActivity
//                                        .getResources().getString(
//                                            R.string.str_scaning
//                                        )
//                                )
                            //                                    InitListView();
                        } else {
                            Log.e("msg", "没有连接")
                            val ss = PrintService.pl.state
//                                tv_status.setText(
//                                    this@PrintSettingActivity
//                                        .getResources().getString(
//                                            R.string.str_disconnected
//                                        ).toString() + "11111"
//                                )
                        }
                    }

                }
            }
        }
        tv_update!!.start()
        mHandlermain = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MESSAGE_READ -> {
                        val readBuf = msg.obj as ByteArray
                        val readMessage = String(readBuf, 0, msg.arg1)
                        Log.i(TAG, "readMessage=$readMessage")
                        Log.i(TAG, "readBuf:" + readBuf[0])
                        if (readBuf[0].equals(0x13)) {
                            PrintService.isFUll = true
                            ShowMsg(
                                resources.getString(R.string.str_printer_state) + ":" + resources.getString(
                                    R.string.str_printer_bufferfull
                                )
                            )
                        } else if (readBuf[0].equals(0x11)) {
                            PrintService.isFUll = false
                            ShowMsg(
                                resources.getString(R.string.str_printer_state) + ":" + resources.getString(
                                    R.string.str_printer_buffernull
                                )
                            )
                        } else if (readBuf[0].equals(0x08)) {
                            ShowMsg(
                                resources.getString(R.string.str_printer_state) + ":" + resources.getString(
                                    R.string.str_printer_nopaper
                                )
                            )
                        } else if (readBuf[0].equals(0x01)) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_printing));
                        } else if (readBuf[0].equals(0x04)) {
                            ShowMsg(
                                resources.getString(R.string.str_printer_state) + ":" + resources.getString(
                                    R.string.str_printer_hightemperature
                                )
                            )
                        } else if (readBuf[0].equals(0x02)) {
                            ShowMsg(
                                resources.getString(R.string.str_printer_state) + ":" + resources.getString(
                                    R.string.str_printer_lowpower
                                )
                            )
                        } else {
                            if (readMessage.contains("800")) // 80mm paper
                            {
                                PrintService.imageWidth = 72
                                Toast.makeText(
                                    applicationContext, "80mm",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (readMessage.contains("580")) // 58mm paper
                            {
                                PrintService.imageWidth = 48
                                Toast.makeText(
                                    applicationContext, "58mm",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                        PrinterClass.STATE_CONNECTED -> {
                        }
                        PrinterClass.STATE_CONNECTING -> Toast.makeText(
                            applicationContext,
                            "STATE_CONNECTING", Toast.LENGTH_SHORT
                        ).show()
                        PrinterClass.STATE_LISTEN, PrinterClass.STATE_NONE -> {
                        }
                        PrinterClass.SUCCESS_CONNECT -> {
                            ////PrintService.pl.write(new byte[] { 0x1b, 0x2b });
                            try {
                                Thread.sleep(10)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                            PrintService.pl.write(byteArrayOf(0x1d, 0x67, 0x33))
                            Toast.makeText(
                                applicationContext,
                                "SUCCESS_CONNECT", Toast.LENGTH_SHORT
                            ).show()
                        }
                        PrinterClass.FAILED_CONNECT -> Toast.makeText(
                            applicationContext,
                            "FAILED_CONNECT", Toast.LENGTH_SHORT
                        ).show()
                        PrinterClass.LOSE_CONNECT -> Toast.makeText(
                            applicationContext, "LOSE_CONNECT",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    MESSAGE_WRITE -> {
                    }
                }
                super.handleMessage(msg)
            }
        }
        handlermain = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    0 -> {
                    }
                    1 -> {
                        val d = msg.obj as Device
                        Log.e("msg", "" + Gson().toJson(d))
                        if (d != null) {
//                            if (com.qs.printer.activity.PrintSettingActivity.deviceList == null) {
//                                com.qs.printer.activity.PrintSettingActivity.deviceList =
//                                    java.util.ArrayList<Device>()
//                            }
//                            if (!checkData(
//                                    com.qs.printer.activity.PrintSettingActivity.deviceList,
//                                    d
//                                )
//                            ) {
//                                com.qs.printer.activity.PrintSettingActivity.deviceList.add(d)
//                            }
                        }
                    }
                    2 -> {
                    }
                }
            }
        }

        PrintService.pl = BtService(this, mHandlermain, handlermain)
        if (!PrintService.pl.IsOpen()) {
            PrintService.pl.open(instance)
        }
        BtService.mBTService.setOnReceive {
            if (it != null) {
                XLog.e("msg" + "name===" + it.name + "----" + it.address)
                if (!TextUtils.isEmpty(it.name)) {
                    if (it.name.equals("Qsprinter")) {
                        PrintService.pl.stopScan()
                        PrintService.pl.connect(it.address)
                        AccountHelper.setSpLocalAddress("" + it.address)
                        localAddress = "" + it.address
                        XLog.e("开始连接", "address===$localAddress")
                    }
                }
            }
        }

    }

    private fun printCode() {

        if (Build.VERSION.SDK_INT >= 6.0) {

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                MY_PERMISSION_REQUEST_CONSTANT
            )
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_REQUEST_CONSTANT -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //permission granted!
                    val state = BtService.mBTService.state
//                    printCode="hqkj-0102-1-1"
                    Log.e("message", "state====$state")
                    if (state == 6 || state == 3) {
                        if (PrintService.pl.getState() != PrinterClass.STATE_CONNECTED) {
                            showToast("设备未连接")
                            return;
                        }
                        try {
                            if (printType.equals("1")) {
                                //二维码
                                PrintService.pl.printImage(createQRImage(printCode, 300, 300))
                                PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                            } else {
                                //条形码
                                val btMap = BarcodeCreater.creatBarcode(
                                    this,
                                    ""+printCode, 384, 100, true, 1
                                )
                                PrintService.pl.printImage(btMap)
                                PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                            }
                            PrintService.pl.write(byteArrayOf(0x1d, 0x0c))

                        } catch (e: Exception) {
                            showToast("打印样品编号有问题 不能有汉字")
                        }


                    } else {


                        val bluetoothDevices = BtService.mBTService.GetBondedDevice()
                        if (bluetoothDevices.size > 0) {
                            Log.e("已配对总列表", "" + Gson().toJson(bluetoothDevices))
                            var ishave = false;
//                            if(!TextUtils.isEmpty(AccountHelper.getSpLocalAddress())) {
                                val it: Iterator<BluetoothDevice> = bluetoothDevices.iterator()
                                while (it.hasNext()) {
                                    val next = it.next()
                                    if (next.name.equals("Qsprinter")) {
                                        PrintService.pl.connect(next.address)
                                        ishave = true
                                    }
                                }
                                if (!ishave) {
                                    PrintService.pl.scan()
                                }
                        } else {
                            PrintService.pl.scan()
                        }
                    }

                } else {
                    showMissingPermissionDialog()
                }
                return

            }
        }

    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        try {
            val builder = AlertDialog.Builder(instance)
            builder.setTitle("提示")
            builder.setMessage("打印机需要获取定位权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限")

            // 拒绝, 退出应用
            builder.setNegativeButton(
                "取消"
            ) { dialog, which ->
                try {
                    showToast("打印机需要获取定位权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限")
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            builder.setPositiveButton(
                "设置"
            ) { dialog, which ->
                try {
                    startAppSettings()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            builder.setCancelable(false)
            builder.show()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private fun startAppSettings() {
        try {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            )
            intent.data = Uri.parse("package:" + instance!!.getPackageName())
            startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun delData(id: Int) {
//        BASE_URL+"/lims/sampling/app/delSamplingRecord/";//删除采样记录
        OkGoUtils.doHttpPost(this,
            AccountHelper.getToken(),
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/delSamplingRecord/" + "" + id,
            "",
            OkGoUtils.httpCallBack { success, response, message, code ->
                if (success) {
                    showToast("删除成功")
                    getBodyData()
                } else {
                    showToast("" + message)
                }

            })
    }

    /*选择下拉数据*/
    private fun selectXiaLa(
        temp: MutableList<String>,
        position: Int,
        bean: TaskHeadInfoListBean.DataBean.HeadInfoListDTO,
        view: View,
        context: Context
    ) {
        showPopAlert(temp, position, bean, view, context)
    }

    var mMovieTicketWindow: CommonPopupWindow? = null
    fun showPopAlert(
        alarmList: MutableList<String>,
        currentposition: Int,
        bean: TaskHeadInfoListBean.DataBean.HeadInfoListDTO,
        view: View, context: Context
    ) {
        if (mMovieTicketWindow != null && mMovieTicketWindow!!.isShowing()) {
//            popHomeAlertAdapter.setNewData(alarmList)
//            popHomeAlertAdapter.notifyDataSetChanged()
        } else {
            mMovieTicketWindow = CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_show_xiala)
                .setBackGroundLevel(1f)
                .setViewOnclickListener(object : CommonPopupWindow.ViewInterface {
                    override fun getChildView(view: View?, layoutResId: Int) {
                        var recyclerVIew = view?.findViewById<RecyclerView>(R.id.rv_list)
                        recyclerVIew?.setLayoutManager(LinearLayoutManager(context))
                        var popXialaAdapter = PopXialaAdapter()
                        recyclerVIew?.adapter = popXialaAdapter
                        popXialaAdapter.setNewData(alarmList)
                        popXialaAdapter.setOnItemClickListener { adapter, view, position ->
                            var data = sampleInformationTitleAdapter.data
                            for (i in 0 until data.size) {
                                if (data[i].isSelect) {
                                    bean.fieldValue = popXialaAdapter.data.get(position)
                                    newSampleInformationAdapter.data.set(currentposition, bean)
                                    newSampleInformationAdapter.notifyDataSetChanged()
                                    mMovieTicketWindow?.dismiss()
                                }
                            }
                        }
                        popXialaAdapter.setOnItemChildClickListener { adapter, view, position ->
                            var data = sampleInformationTitleAdapter.data
                            for (i in 0 until data.size) {
                                if (data[i].isSelect) {
                                    bean.fieldValue = popXialaAdapter.data.get(position)
                                    newSampleInformationAdapter.data.set(currentposition, bean)
                                    newSampleInformationAdapter.notifyDataSetChanged()
                                    mMovieTicketWindow?.dismiss()
                                }
                            }
                        }
                    }

                })
                .setOutsideTouchable(true) //设置外层不可点击
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                .create()
            if (mMovieTicketWindow == null || !mMovieTicketWindow!!.isShowing()) {
                mMovieTicketWindow!!.setFocusable(true) // 这个很重要
                mMovieTicketWindow!!.showAsDropDown(view)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getBodyList(event: ShowSwipListEvent?) {
        getBodyData();
    }

    private fun initPicSelect() {
        val manager = FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        dispose_recycler.setLayoutManager(manager)
        adapter = GridImageAdapter(this, onAddPicClickListener)
        adapter.setList(selectList)
        adapter.setSelectMax(maxSelectNum)
        dispose_recycler.setAdapter(adapter)
        adapter.setOnImageDelClickListener(GridImageAdapter.OnItemImageDelClickListener() { position, v ->
            uploadAddress.removeAt(position)
        })
        adapter.setOnItemClickListener(GridImageAdapter.OnItemClickListener { position, v ->
            if (selectList.size > 0) {
                val media: LocalMedia = selectList.get(position)
                val pictureType = media.pictureType
                val mediaType = PictureMimeType.pictureToVideo(pictureType)
                when (mediaType) {
                    1 ->                             // 预览图片 可自定长按保存路径
                        //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                        PictureSelector.create(this)
                            .externalPicturePreview(position, selectList)
                    2 ->                             // 预览视频
                        PictureSelector.create(this)
                            .externalPictureVideo(media.path)
                    3 ->                             // 预览音频
                        PictureSelector.create(this)
                            .externalPictureAudio(media.path)
                }
            }
        })
    }

    fun getData() {
        val postBean1 = PostBean()
//        BASE_URL + "/lims/sampling/app/getTaskHeadInfo/";//获取采样任务表头信息
        OkGoUtils.doHttpPost(
            this, AccountHelper.getToken(),
            "" + BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/getTaskHeadInfo/" + "" + id, Gson().toJson(postBean1)
        ) { success, response, message, code ->
            if (success) {
                var taskheadinfolistBean = Gson().fromJson<TaskHeadInfoListBean>(
                    response,
                    TaskHeadInfoListBean::class.java
                )
                var list = taskheadinfolistBean.data
                var bean: TaskHeadInfoListBean.DataBean = TaskHeadInfoListBean.DataBean()
                if (list.isNotEmpty()) {
                    for (i in 0 until list.size) {
                        list[i].isSelect = false
                    }
                }
                bean?.groupName = "天次频次"
                bean?.isSelect = true
                list.add(0, bean)
                var bean2: TaskHeadInfoListBean.DataBean = TaskHeadInfoListBean.DataBean()
                bean2?.groupName = "采样记录"
                bean2?.isSelect = false
                list.add(bean2)
                sampleInformationTitleAdapter.setNewData(list)
            } else {
                showToast("" + message)
            }
        }
    }

    private val onAddPicClickListener = onAddPicClickListener {
        //获取写的权限
        val rxPermission = RxPermissions(this)
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { permission ->
                if (permission.granted) { // 用户已经同意该权限
                    //第一种方式，弹出选择和拍照的dialog
                    KeyBoardUtils.hideInputForce(this)
                    showPop()
                    //第二种方式，直接进入相册，但是 是有拍照得按钮的
                    //                                showAlbum();
                } else {
                    Toast.makeText(this, "拒绝", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showPop() {
        val bottomView = View.inflate(this, R.layout.layout_bottom_dialog, null)
        val mAlbum = bottomView.findViewById<TextView>(R.id.tv_album)
        val mCamera = bottomView.findViewById<TextView>(R.id.tv_camera)
        val mCancel = bottomView.findViewById<TextView>(R.id.tv_cancel)
        pop = PopupWindow(bottomView, -1, -2)
        pop.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pop.setOutsideTouchable(true)
        pop.setFocusable(true)
        val lp = window.attributes
        lp.alpha = 1f
        window.attributes = lp
        pop.setOnDismissListener(PopupWindow.OnDismissListener {
            val lp = window.attributes
            lp.alpha = 1f
            window.attributes = lp
        })
        pop.setAnimationStyle(R.style.main_menu_photo_anim)
        pop.showAtLocation(home_f, Gravity.BOTTOM, 0, 0)
        val clickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.tv_album ->                         //相册
                        PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(maxSelectNum)
                            .minSelectNum(1)
                            .imageSpanCount(3)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .forResult(PictureConfig.CHOOSE_REQUEST)
                    R.id.tv_camera ->                         //拍照
                        PictureSelector.create(this)
                            .openCamera(PictureMimeType.ofImage())
                            .forResult(PictureConfig.CHOOSE_REQUEST)
                    R.id.tv_cancel -> {
                    }
                }
                closePopupWindow()
            }
        mAlbum.setOnClickListener(clickListener)
        mCamera.setOnClickListener(clickListener)
        mCancel.setOnClickListener(clickListener)
    }

    fun closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss()
            pop == null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images: List<LocalMedia>
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) { // 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data)
                var list = adapter.list
                upShouldSubmitAddress.clear()
                if (list != null && list.size > 0) {
                    list.addAll(images)
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.setList(images)
                }
                selectList.clear()
                selectList?.addAll(images)
                upShouldSubmitAddress.addAll(images)
                //selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            }
        }
    }


    /*编辑表头信息接口*/
    private fun bodyInfoPost(data1: List<PostBean.DataBean>) {
        showLoading()
        val newList: MutableList<PostBean.DataBean.HeadInfoListDTO> = java.util.ArrayList()

        if (data1 != null) {
            for (m in data1.indices) {
                val dataBean = data1[m]
                val headInfoList = dataBean.headInfoList
                if (dataBean.headInfoList != null && dataBean.headInfoList.size > 0) {
                    for (n in headInfoList.indices) {
                        newList.add(headInfoList[n])
                    }
                }
            }
        }
//        BASE_URL+"/lims/sampling/app/editSamplingHeadInfo";//修改表头信息
        OkGoUtils.doHttpPost(
            this,
            AccountHelper.getToken(),
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/editSamplingHeadInfo",
            Gson().toJson(newList),
        ) { success, response, message, code ->
            dismissLoading()
            if (success) {
                linearLayoutBody.visibility = View.VISIBLE
                linearLayoutOne.visibility = View.GONE
                linearLayoutTwo.visibility = View.GONE
                nescrollview.visibility = View.GONE
                linearlayoutFirstSave.visibility = View.GONE
                linearLayoutWancheng.visibility = View.VISIBLE
                getBodyData()
            } else {
                showToast("" + message)
            }
        }

    }

    /*请求表体信息接口*/
    private fun getBodyData() {
        var postbean = PostBean()
//        postbean.taskId = "" + id
//        postbean.planId = "" + samplingPlanId
//        BASE_URL+"/lims/sampling/app/getRecordList/";//获取采样记录列表
        OkGoUtils.doHttpPost(this,
            AccountHelper.getToken(),
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/getRecordList/" + id,
            Gson().toJson(postbean),
            OkGoUtils.httpCallBack { success, response, message, code ->
                if (success) {
                    var bean = Gson().fromJson(response, getTaskBodyInfoBean::class.java)
                    var list = bean.data
                    if (list != null && list.size > 0) {
                        linearLayoutEmptyView.visibility = View.GONE
                        sampleCourInfoListAdapter.setNewData(list)
                    } else {
                        linearLayoutEmptyView.visibility = View.VISIBLE
                        sampleCourInfoListAdapter.setNewData(ArrayList())

                    }
                }
            })
    }

    private fun uploadFile(file: MutableList<File>, content: String, title: String) {
        showLoading()
//        BASE_URL + "/lims/sampling/app/uploadSamplingFile";//上传文件
        OkGoUtils.upLoadFiles(
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/uploadSamplingFile",
            this,
            null,
            "fileList",
            file,
            object : onUploadFileListener {
                override fun onStart() {}
                override fun onUploadProgress(progress: Progress) {}
                override fun onSuccess(response: Response<String>) {
                    val responseBean = Gson().fromJson(
                        response.body(),
                        UploadBean::class.java
                    )
                    var data = responseBean.data
                    if (data != null && data.size > 0) {
                        for (i in 0 until data.size) {
                            uploadAddress.add(data.get(i))
                        }
                    }

                    dismissLoading()
                }

                override fun onError(response: Response<String>) {
                    dismissLoading()
                    showToast("网络连接不稳定 请重试")

                }

                override fun onFinish() {

                    val strDayTime: String = mEtDayTime.getText().toString()
                    val strPinCiTime: String = mEtPinCiTime.getText().toString()

                    getDatas(strDayTime, strPinCiTime)

                }
            })
    }

    private fun getDatas(strDayTime: String, strPinCiTime: String) {
        if (uploadError.size > 0) {
            showToast("图片上传失败")
            return
        }
        val postBean = PostBean()

        postBean.samplingPlanId = "" + samplingPlanId
        postBean.userId = "" + AccountHelper.getuserId()
        postBean.whichDay = "" + strDayTime
        postBean.whichTime = "" + strPinCiTime
        postBean.attachment = Gson().toJson(uploadAddress)
//        postBean.attachment = "" + picture
        if (!TextUtils.isEmpty(editId)) {
            postBean.id = "" + editId
        }
//           = BASE_URL + "/lims/sampling/app/addAndEditSamplingTask"; //新增或者修改采样任务
        OkGoUtils.normalRequest(
            this, BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/addAndEditSamplingTask",
            "" + Gson().toJson(postBean), "" + AccountHelper.getToken()
        ) { success, response, message, code ->
            if (success) {
                if (!isAdd) {
                    var beans = Gson().fromJson(response, AddEditBean::class.java) as AddEditBean
                    editId = beans.data
                    var data = sampleInformationTitleAdapter.data
                    if (data.size >= 2) {
                        for (i in 0 until data.size) {
                            data[i].isSelect = false
                        }
                        data[1].isSelect = true
                        sampleInformationTitleAdapter.setNewData(data)
                        sampleInformationTitleAdapter.notifyDataSetChanged()

                        linearLayoutOne.visibility = View.GONE
                        linearLayoutTwo.visibility = View.VISIBLE
                        linearLayoutBody.visibility = View.GONE
                        nescrollview.visibility = View.VISIBLE
                        linearlayoutFirstSave.visibility = View.VISIBLE
                        linearLayoutWancheng.visibility = View.GONE
                        var data =
                            sampleInformationTitleAdapter.data.get(1) as TaskHeadInfoListBean.DataBean
                        var headInfoList = data.headInfoList
                        val contents = LinkedHashMap<String, String>()
                        for (m in headInfoList.indices) {
                            val fillContentBean = FillContentBean()
                            fillContentBean.name = "" + headInfoList.get(m).getFieldName()
                            fillContentBean.content = "" + headInfoList.get(m).getFieldValue()
                            contents["" + headInfoList.get(m).getFieldName()] =
                                "" + headInfoList.get(m).getFieldValue()
                        }
                        newSampleInformationAdapter.setMyTens(0, contents)
                        newSampleInformationAdapter.setNewData(headInfoList)
                        newSampleInformationAdapter.notifyDataSetChanged()

                    }
                } else {
                    finish()
                }

            } else {
                showToast("" + message)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_wancheng -> {
                val builder = KyDialogBuilder(this)
                builder.setTitle("完成后采样信息将不允许修改，是否确认？")
                builder.setMessage("")
                builder.setNegativeButton("取消") { builder.dismiss() }
                builder.setPositiveButton(
                    "确定"
                ) {
                    getCompleteData()
                    builder.dismiss()
                }
                builder.show()
            }
            R.id.tv_cancel -> {
                finish()
            }
            R.id.iv_add -> {
                UpdateSampleInformationActivity.goActivity(
                    this,
                    "" + samplingPlanId, "true", id
                )

            }
            R.id.tv_save -> {
                val filePath: MutableList<File> = ArrayList()
                val strDayTime: String = mEtDayTime.getText().toString()
                val strPinCiTime: String = mEtPinCiTime.getText().toString()
                if (TextUtils.isEmpty(strDayTime)) {
                    showToast("请输入天次")
                    return
                }
                if (TextUtils.isEmpty(strPinCiTime)) {
                    showToast("请输入频次")
                    return
                }
                if (isAdd) {
                    if (upShouldSubmitAddress.size > 0) {
                        for (m in upShouldSubmitAddress.indices) {
                            val path = upShouldSubmitAddress[m].path
                            try {
                                val file = CompressHelper.getDefault(this)
                                    .compressToFile(File(path))
                                filePath.add(file)
                            } catch (e: Exception) {
                                val file = File(path)
                                filePath.add(file)
                            }

                        }
                        uploadFile(filePath, strDayTime, strPinCiTime)
                    } else {
                        getDatas(strDayTime, strPinCiTime)
                    }
                } else {
                    var onlyData = sampleInformationTitleAdapter.data
                    for (i in 0 until onlyData.size) {
                        if (onlyData[i].isSelect) {
                            if (i == 0) {
                                if (upShouldSubmitAddress.size > 0) {
                                    for (m in upShouldSubmitAddress.indices) {
                                        val path = upShouldSubmitAddress.get(m).path
                                        try {
                                            val file = CompressHelper.getDefault(this)
                                                .compressToFile(File(path))
                                            filePath.add(file)
                                        } catch (e: Exception) {
                                            val file = File(path)
                                            filePath.add(file)
                                        }
                                    }
                                    uploadError.clear()
                                    uploadFile(filePath, strDayTime, strPinCiTime)
                                } else {
                                    getDatas(strDayTime, strPinCiTime)
                                }
                            } else if (i == onlyData.size - 2) {
                                //数据填写完成
                                //点击 进入下一个 设为true  刷新数据
                                val oldData = sampleInformationTitleAdapter.data
                                val postBean = PostBean()
                                val data1 = postBean.data
                                for (i in oldData.indices) {
                                    if (i == 0) {
                                    } else {
                                        val dataBean = PostBean.DataBean()
                                        dataBean.groupName = "" + oldData[i].groupName
                                        val headInfoList = oldData[i].headInfoList
                                        if (headInfoList != null && headInfoList.size > 0) {
                                            val head: MutableList<PostBean.DataBean.HeadInfoListDTO> =
                                                java.util.ArrayList()
                                            for (m in headInfoList.indices) {
                                                val headInfoListDTO = headInfoList[m]
                                                val headinfo = PostBean.DataBean.HeadInfoListDTO()
                                                if (headInfoListDTO.selectBeans != null && headInfoListDTO.selectBeans.size > 0) {
                                                    var strDataSource = ""
                                                    for (n in headInfoListDTO.selectBeans.indices) {
                                                        if (headInfoListDTO.selectBeans[n].isSelect) {
                                                            strDataSource =
                                                                strDataSource + headInfoListDTO.selectBeans[n].fileValue + ","
                                                        }
                                                    }
                                                    if (strDataSource.endsWith(",")) {
                                                        headinfo.fieldValue =
                                                            strDataSource.substring(
                                                                0, strDataSource.length - 1
                                                            )
                                                    } else {
                                                        headinfo.fieldValue = strDataSource
//
                                                    }
                                                } else {
                                                    headinfo.fieldValue = headInfoListDTO.fieldValue
                                                }
                                                headinfo.fieldName = headInfoListDTO.fieldName
                                                headinfo.fieldType = headInfoListDTO.fieldType
                                                headinfo.groupName = headInfoListDTO.groupName
                                                headinfo.id = headInfoListDTO.id
                                                headinfo.samplingTaskId =
                                                    headInfoListDTO.samplingTaskId
                                                headinfo.serialNum = headInfoListDTO.serialNum
                                                head.add(headinfo)
                                            }
                                            dataBean.headInfoList = head
                                        }
                                        data1.add(dataBean)
                                    }
                                }
                                bodyInfoPost(data1)
                                var data = sampleInformationTitleAdapter.data
                                for (i in 0 until data.size) {
                                    data[i].isSelect = false
                                }
                                data[i + 1].isSelect = true
                                sampleInformationTitleAdapter.setNewData(data)
                                sampleInformationTitleAdapter.notifyDataSetChanged()
                            } else if (i == onlyData.size - 1) {
//                                showToast("哈哈哈哈哈哈哈")
                            } else {
                                var data = sampleInformationTitleAdapter.data
                                if (data.size >= (i + 1)) {
                                    for (i in 0 until data.size) {
                                        data[i].isSelect = false
                                    }
                                    data[i + 1].isSelect = true
                                    sampleInformationTitleAdapter.setNewData(data)
                                    sampleInformationTitleAdapter.notifyDataSetChanged()
                                    linearLayoutOne.visibility = View.GONE
                                    linearLayoutTwo.visibility = View.VISIBLE
                                    linearLayoutBody.visibility = View.GONE
                                    nescrollview.visibility = View.VISIBLE
                                    linearlayoutFirstSave.visibility = View.VISIBLE
                                    linearLayoutWancheng.visibility = View.GONE
                                    var data =
                                        sampleInformationTitleAdapter.data.get(i + 1) as TaskHeadInfoListBean.DataBean
                                    var headInfoList = data.headInfoList
                                    val contents = LinkedHashMap<String, String>()
                                    for (m in headInfoList.indices) {
                                        val fillContentBean = FillContentBean()
                                        fillContentBean.name =
                                            "" + headInfoList.get(m).getFieldName()
                                        fillContentBean.content =
                                            "" + headInfoList.get(m).getFieldValue()
                                        contents["" + headInfoList.get(m).getFieldName()] =
                                            "" + headInfoList.get(m).getFieldValue()
                                    }
                                    newSampleInformationAdapter.setMyTens(i, contents)

                                    newSampleInformationAdapter.setNewData(headInfoList)
                                    newSampleInformationAdapter.notifyDataSetChanged()

                                }
                                return

                            }
                        }
                    }
                }

            }
        }
    }

    private fun getCompleteData() {
        showLoading()
//        BASE_URL+"/lims/sampling/app/completeTask/";//完成采样任务
        OkGoUtils.doHttpPost(this,
            AccountHelper.getToken(),
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/completeTask/" + "" + id,
            "",
            OkGoUtils.httpCallBack { success, response, message, code ->
                dismissLoading()
                if (success) {
                    showToast("采样完成")
                    finish()
                } else {
                    showToast("" + message)
                }
            })
    }


    fun getTimeMinute(date: Date?): String? { //可根据需要自行截取数据显示
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }

    override fun onDestroy() {
        super.onDestroy()

        PrintService.pl.disconnect()
        if(tv_update!=null)
        {
            tvFlag=false
            tv_update!!.interrupt()
        }
    }

    /**
     * 生成二维码 要转换的地址或字符串,可以是中文
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    fun createQRImage(url: String?, width: Int, height: Int): Bitmap? {
        try {
            // 判断URL合法性
            if (url == null || "" == url || url.length < 1) {
                return null
            }
            val hints = Hashtable<EncodeHintType, String?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(
                url,
                BarcodeFormat.QR_CODE, width, height, hints
            )
            val pixels = IntArray(width * height)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = -0x1000000
                    } else {
                        pixels[y * width + x] = -0x1
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            val bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun ShowMsg(msg: String) {
        Toast.makeText(
            applicationContext, msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}