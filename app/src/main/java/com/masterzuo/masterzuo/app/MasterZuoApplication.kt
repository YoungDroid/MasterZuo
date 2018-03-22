package com.masterzuo.masterzuo.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.masterzuo.masterzuo.utils.CrashHandlerUtils
import com.masterzuo.masterzuo.utils.FileUtils

/**
 * Created by YoungDroid on 2018/3/22.
 * Email: YoungDroid@163.com
 */
class MasterZuoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        init()
    }

    fun init() {

        // 异常文件
        val crashHandler = CrashHandlerUtils.instance
        crashHandler.init(this)
//
//        FileUtils.init()

//        val diskCacheConfig = DiskCacheConfig
//                .newBuilder(this)
//                .setBaseDirectoryPath(FileUtils.THUMBNAIL_FILE)
//                .build()
//
//        val config = ImagePipelineConfig.newBuilder(this)
//                .setMainDiskCacheConfig(diskCacheConfig)
//                .setBitmapsConfig(Bitmap.Config.RGB_565)
//                .setSmallImageDiskCacheConfig(diskCacheConfig)
//                .build()
//        Fresco.initialize(this, config)
//
//        // local display
//        val dm = DisplayMetrics()
//        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        wm.defaultDisplay.getMetrics(dm)
//        LocalDisplay.init(dm)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        val TAG = "MasterZuo"
        var packgeNameString: String = ""
    }
}