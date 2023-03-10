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
    ??????????????????
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
                    /*?????? */
                    val builder = KyDialogBuilder(this)
                    builder.setTitle("??????????????????????")
                    builder.setMessage("")
                    builder.setNegativeButton(
                        "??????"
                    ) { builder.dismiss() }
                    builder.setPositiveButton("??????") {
                        builder.dismiss()
                        // ??????isOpened?????????????????????????????????????????????AppInfo??????????????????App????????????
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
                        //?????????
                        var dataSource = bean.dataSource
//                        var dataSource = "G??????P???,?????????"
                        val temp: Array<String> = dataSource.split(",".toRegex())?.toTypedArray()!!
                        selectXiaLa(temp.toMutableList(), position, bean, view, this)
                    } else if (fieldType.equals("5")) {
                        //??????????????????
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
                            .setType(TimePickerView.Type.ALL) //??????????????????
                            .setCancelText("??????") //??????????????????
                            .setSubmitText("??????") //??????????????????
                            .setContentSize(20) //??????????????????
                            .setTitleSize(20) //??????????????????
                            //                        .setTitleText("???????????????")//????????????
                            .setOutSideCancelable(true) //???????????????????????????????????????????????????????????????
                            .isCyclic(true) //??????????????????
                            .setTextColorCenter(Color.parseColor("#000000")) //????????????????????????
                            .setTextColorOut(Color.parseColor("#666666"))
                            .setTitleColor(Color.BLACK) //??????????????????
                            .setSubmitColor(Color.BLUE) //????????????????????????
                            .setCancelColor(Color.BLUE) //????????????????????????
                            .setLoop(false)
                            .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                            //                        .isDialog(true)//??????????????????????????????
                            .build()
                        pvTime.setDate(Calendar.getInstance()) //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

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
                //??????????????????
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
            // ??????
            taskBean = SampleTaskBean()
        } else {
            rvSampleInformationTitle.visibility = View.VISIBLE
            val postBean1 = PostBean()
//            BASE_URL + "/lims/sampling/app/getSamplTask/";//????????????????????????
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

                        //??????8?????????????????????????????????
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
                                    //?????????
                                    PrintService.pl.printImage(createQRImage(printCode, 300, 300))
                                    PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                                } else {
                                    //?????????
                                    val btMap = BarcodeCreater.creatBarcode(
                                        instance,
                                        "" + printCode, 384, 100, true, 1
                                    )
                                    PrintService.pl.printImage(btMap)
                                    PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                                }
                                PrintService.pl.write(byteArrayOf(0x1d, 0x0c))

                            } catch (e: Exception) {
//                                showToast("??????????????????????????? ???????????????")
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
                            Log.e("msg", "????????????")
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
                        XLog.e("????????????", "address===$localAddress")
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
                            showToast("???????????????")
                            return;
                        }
                        try {
                            if (printType.equals("1")) {
                                //?????????
                                PrintService.pl.printImage(createQRImage(printCode, 300, 300))
                                PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                            } else {
                                //?????????
                                val btMap = BarcodeCreater.creatBarcode(
                                    this,
                                    ""+printCode, 384, 100, true, 1
                                )
                                PrintService.pl.printImage(btMap)
                                PrintService.pl.write(byteArrayOf(0x1d, 0x0c))
                            }
                            PrintService.pl.write(byteArrayOf(0x1d, 0x0c))

                        } catch (e: Exception) {
                            showToast("??????????????????????????? ???????????????")
                        }


                    } else {


                        val bluetoothDevices = BtService.mBTService.GetBondedDevice()
                        if (bluetoothDevices.size > 0) {
                            Log.e("??????????????????", "" + Gson().toJson(bluetoothDevices))
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
     * ??????????????????
     *
     * @since 2.5.0
     */
    private fun showMissingPermissionDialog() {
        try {
            val builder = AlertDialog.Builder(instance)
            builder.setTitle("??????")
            builder.setMessage("????????????????????????????????????\\n\\n?????????\\\"??????\\\"-\\\"??????\\\"-??????????????????")

            // ??????, ????????????
            builder.setNegativeButton(
                "??????"
            ) { dialog, which ->
                try {
                    showToast("????????????????????????????????????\\n\\n?????????\\\"??????\\\"-\\\"??????\\\"-??????????????????")
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            builder.setPositiveButton(
                "??????"
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
     * ?????????????????????
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
//        BASE_URL+"/lims/sampling/app/delSamplingRecord/";//??????????????????
        OkGoUtils.doHttpPost(this,
            AccountHelper.getToken(),
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/delSamplingRecord/" + "" + id,
            "",
            OkGoUtils.httpCallBack { success, response, message, code ->
                if (success) {
                    showToast("????????????")
                    getBodyData()
                } else {
                    showToast("" + message)
                }

            })
    }

    /*??????????????????*/
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
                .setOutsideTouchable(true) //????????????????????????
                .setWidthAndHeight(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                .create()
            if (mMovieTicketWindow == null || !mMovieTicketWindow!!.isShowing()) {
                mMovieTicketWindow!!.setFocusable(true) // ???????????????
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
                    1 ->                             // ???????????? ???????????????????????????
                        //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                        PictureSelector.create(this)
                            .externalPicturePreview(position, selectList)
                    2 ->                             // ????????????
                        PictureSelector.create(this)
                            .externalPictureVideo(media.path)
                    3 ->                             // ????????????
                        PictureSelector.create(this)
                            .externalPictureAudio(media.path)
                }
            }
        })
    }

    fun getData() {
        val postBean1 = PostBean()
//        BASE_URL + "/lims/sampling/app/getTaskHeadInfo/";//??????????????????????????????
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
                bean?.groupName = "????????????"
                bean?.isSelect = true
                list.add(0, bean)
                var bean2: TaskHeadInfoListBean.DataBean = TaskHeadInfoListBean.DataBean()
                bean2?.groupName = "????????????"
                bean2?.isSelect = false
                list.add(bean2)
                sampleInformationTitleAdapter.setNewData(list)
            } else {
                showToast("" + message)
            }
        }
    }

    private val onAddPicClickListener = onAddPicClickListener {
        //??????????????????
        val rxPermission = RxPermissions(this)
        rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { permission ->
                if (permission.granted) { // ???????????????????????????
                    //??????????????????????????????????????????dialog
                    KeyBoardUtils.hideInputForce(this)
                    showPop()
                    //????????????????????????????????????????????? ????????????????????????
                    //                                showAlbum();
                } else {
                    Toast.makeText(this, "??????", Toast.LENGTH_SHORT).show()
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
                    R.id.tv_album ->                         //??????
                        PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(maxSelectNum)
                            .minSelectNum(1)
                            .imageSpanCount(3)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .forResult(PictureConfig.CHOOSE_REQUEST)
                    R.id.tv_camera ->                         //??????
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
            if (requestCode == PictureConfig.CHOOSE_REQUEST) { // ????????????????????????
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

                // ?????? LocalMedia ??????????????????path
                // 1.media.getPath(); ?????????path
                // 2.media.getCutPath();????????????path????????????media.isCut();?????????true
                // 3.media.getCompressPath();????????????path????????????media.isCompressed();?????????true
                // ????????????????????????????????????????????????????????????????????????????????????
            }
        }
    }


    /*????????????????????????*/
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
//        BASE_URL+"/lims/sampling/app/editSamplingHeadInfo";//??????????????????
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

    /*????????????????????????*/
    private fun getBodyData() {
        var postbean = PostBean()
//        postbean.taskId = "" + id
//        postbean.planId = "" + samplingPlanId
//        BASE_URL+"/lims/sampling/app/getRecordList/";//????????????????????????
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
//        BASE_URL + "/lims/sampling/app/uploadSamplingFile";//????????????
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
                    showToast("????????????????????? ?????????")

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
            showToast("??????????????????")
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
//           = BASE_URL + "/lims/sampling/app/addAndEditSamplingTask"; //??????????????????????????????
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
                builder.setTitle("?????????????????????????????????????????????????????????")
                builder.setMessage("")
                builder.setNegativeButton("??????") { builder.dismiss() }
                builder.setPositiveButton(
                    "??????"
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
                    showToast("???????????????")
                    return
                }
                if (TextUtils.isEmpty(strPinCiTime)) {
                    showToast("???????????????")
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
                                //??????????????????
                                //?????? ??????????????? ??????true  ????????????
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
//                                showToast("?????????????????????")
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
//        BASE_URL+"/lims/sampling/app/completeTask/";//??????????????????
        OkGoUtils.doHttpPost(this,
            AccountHelper.getToken(),
            BaseUrlManager.getInstance().baseUrl+"/lims/sampling/app/completeTask/" + "" + id,
            "",
            OkGoUtils.httpCallBack { success, response, message, code ->
                dismissLoading()
                if (success) {
                    showToast("????????????")
                    finish()
                } else {
                    showToast("" + message)
                }
            })
    }


    fun getTimeMinute(date: Date?): String? { //???????????????????????????????????????
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
     * ??????????????? ??????????????????????????????,???????????????
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    fun createQRImage(url: String?, width: Int, height: Int): Bitmap? {
        try {
            // ??????URL?????????
            if (url == null || "" == url || url.length < 1) {
                return null
            }
            val hints = Hashtable<EncodeHintType, String?>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // ??????????????????????????????????????????
            val bitMatrix = QRCodeWriter().encode(
                url,
                BarcodeFormat.QR_CODE, width, height, hints
            )
            val pixels = IntArray(width * height)
            // ????????????????????????????????????????????????????????????????????????
            // ??????for????????????????????????????????????
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = -0x1000000
                    } else {
                        pixels[y * width + x] = -0x1
                    }
                }
            }
            // ???????????????????????????????????????ARGB_8888
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