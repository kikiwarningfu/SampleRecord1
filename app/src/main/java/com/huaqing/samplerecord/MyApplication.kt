package com.huaqing.samplerecord

//import com.drake.brv.utils.BRV
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.os.Process
import com.elvishew.xlog.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.PatternFlattener
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter
import com.elvishew.xlog.formatter.message.xml.DefaultXmlFormatter
import com.elvishew.xlog.formatter.stacktrace.DefaultStackTraceFormatter
import com.elvishew.xlog.formatter.thread.DefaultThreadFormatter
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.huaqing.samplerecord.base.BaseApplication
import com.huaqing.samplerecord.bean.Global
import com.huaqing.samplerecord.urlmanager.BaseUrlManager
import com.huaqing.samplerecord.urlmanager.bean.UrlInfo
import com.huaqing.samplerecord.utlis.ToastUtils
import com.huaqing.samplerecord.utlis.UserUtil
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import java.util.*

//1、进入项目地址，通过命令git init将项目初始化成git本地仓库
//        git init
//        2、将项目内所有文件都添加到暂存区
//        git add .
//        3、该命令会将git add .存入暂存区修改内容提交至本地仓库中，若文件未添加至暂存区，则提交时不会提交任何修改。
//        git commit -m 'xxx'   //xxx是备注名
//        4、在github上新建一个仓库，复制仓库地址，然后使用命令将本地仓库与远程仓库建立连接
//        （4.1）git remote add origin  xxx       //xxx是git仓库的地址
//        （4.2）4.1这个步骤可能出现错误 提示fatal: remote origin already exists
//        执行：git remote rm origin命令
//        （4.3）再执行（4.1）的命令
//        5、把暂存区的代码推到远程仓库
//        git push -u origin master
// 平板适配方案 https://github.com/JessYanCoding/AndroidAutoSize  implementation 'me.jessyan:autosize:0.9.5'
//https://juejin.cn/post/6844903661819133960
class MyApplication : BaseApplication() {
    /**
     * 是否为主线程
     */
    private val isMainThread = Looper.getMainLooper() == Looper.myLooper()
    private val mUrls = arrayOf("http://39.101.149.15:9080","http://192.168.21.31:8812")

    /**
     * 是否为主进程
     */
    private var isMainProcess = false
    override fun onCreate() {
        super.onCreate()
        myApplication = this
        context = this.applicationContext
//        BRV.modelId = BR.m
        //        BRV.INSTANCE.getModelId()=BRV.INSTANCE.
        //判断是否在主进程  只初始化一次
        isMainProcess = applicationContext.packageName == currentProcessName
        if (isMainProcess) {
            ToastUtils.init(context)
            initXLog()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "通知消息"
                val channelMaxName = "系统消息"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val max = NotificationManager.IMPORTANCE_MAX //优先级
                createNotificationChannel(CHANNELID, channelName, importance)
                createNotificationChannel(CHANNELMAX, channelMaxName, max)
            }
        }
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context: Context?, layout: RefreshLayout? -> MaterialHeader(context) }
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context: Context?, layout: RefreshLayout? -> ClassicsFooter(context) }

        //初始化BaseUrlManager
        val baseUrlManager: BaseUrlManager = BaseUrlManager.getInstance()


        //-------------------------- 下面这一段可以完全不需要，主要用来演示
        if (baseUrlManager.count === 0) {
            val list: MutableList<UrlInfo> = ArrayList<UrlInfo>()
            val length: Int = mUrls.size
            for (i in 0 until length) {
                list.add(UrlInfo(mUrls.get(i)))
            }
            //你也可以提前把可能需要的环境提前加到BaseUrlManager
            baseUrlManager.addUrlInfo(list)
            //设置baseUrl
//            baseUrlManager.setUrlInfo();
        }
    }


    /**
     * 获取当前进程名
     */
    private val currentProcessName: String
        private get() {
            val pid = Process.myPid()
            var processName = ""
            val manager = applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            for (process in manager.runningAppProcesses) {
                if (process.pid == pid) {
                    processName = process.processName
                }
            }
            return processName
        }

    /**
     * 初始化日志
     */
    private fun initXLog() {
        val config = LogConfiguration.Builder()
            .logLevel(if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.INFO) // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
            .tag("Okhttp") // Specify TAG, default: "X-LOG"
            //.t()                                                            // Enable thread info, disabled by default
            .st(Global.Constants.LOG_CLASS_DEPTH) // Enable stack trace info with depth 2, disabled by default
            // .b()                                                            // Enable border, disabled by default
            .jsonFormatter(DefaultJsonFormatter()) // Default: DefaultJsonFormatter
            .xmlFormatter(DefaultXmlFormatter()) // Default: DefaultXmlFormatter
            .throwableFormatter(DefaultThrowableFormatter()) // Default: DefaultThrowableFormatter
            .threadFormatter(DefaultThreadFormatter()) // Default: DefaultThreadFormatter
            .stackTraceFormatter(DefaultStackTraceFormatter()) // Default: DefaultStackTraceFormatter
            .build()
        val androidPrinter: Printer =
            AndroidPrinter() // Printer that print the log using android.util.Log
        val flatPattern = "{d yy/MM/dd HH:mm:ss} {l}|{t}: {m}"
        val filePrinter: Printer =
            FilePrinter.Builder(UserUtil.appLogFolderPath(this)) // Specify the path to save log file
                .fileNameGenerator(DateFileNameGenerator()) // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(
                        FileSizeBackupStrategy(
                                Global.Constants.APP_LOG_SIZE
                        )
                ) // Default: FileSizeBackupStrategy(1024 * 1024)
                .cleanStrategy(
                        FileLastModifiedCleanStrategy(
                                Global.Constants.LOG_DURATION
                        )
                )
                .flattener(PatternFlattener(flatPattern)) // Default: DefaultFlattener
                .build()
        XLog.init( // Initialize XLog
                config,  // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
                androidPrinter,
                filePrinter
        )
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }

    companion object {
        private var myApplication: MyApplication? = null

        //    //com.huaqing.air.MyApplication.getInstance().getContext()获取全局context
        var context: Context? = null
            private set
        val instance: MyApplication?
            get() = if (myApplication != null && myApplication is MyApplication) {
                myApplication
            } else {
                myApplication = MyApplication()
                myApplication!!.onCreate()
                myApplication
            }
        var CHANNELID = "message"
        var CHANNELMAX = "system"
    }
}