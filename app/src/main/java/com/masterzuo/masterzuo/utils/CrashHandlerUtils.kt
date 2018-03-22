package com.masterzuo.masterzuo.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.os.Environment
import android.os.Looper

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.UncaughtExceptionHandler
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.

 * @author way
 */
class CrashHandlerUtils
/**
 * 保证只有一个CrashHandler实例
 */
private constructor() : UncaughtExceptionHandler {
    var mDefaultHandler: UncaughtExceptionHandler? = null
    var mContext: Context? = null
    val info = HashMap<String, String>()
    val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

    /**
     * 初始化
     * @param context
     */
    fun init(context: Context) {
        mContext = context
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                // 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    fun handleException(ex: Throwable?): Boolean {
        if (ex == null)
            return false
        object : Thread() {
            override fun run() {
                Looper.prepare()
                error("很抱歉,程序出现异常,即将退出")
                mContext?.let {
                    toast(mContext!!, "很抱歉,程序出现异常,即将退出")
                }
                Looper.loop()
            }
        }.start()
        // 收集设备参数信息
        collectDeviceInfo(mContext!!)
        // 保存日志文件
        saveCrashInfo2File(ex)
        return true
    }

    /**
     * 收集设备参数信息

     * @param context
     */
    fun collectDeviceInfo(context: Context) {
        try {
            val pm = context.packageManager// 获得包管理器
            val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)// 得到该应用的信息，即主Activity
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                info.put("versionName", versionName)
                info.put("versionCode", versionCode)
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        val fields = Build::class.java.declaredFields// 反射机制
        for (field in fields) {
            try {
                field.isAccessible = true
                info.put(field.name, field.get("").toString())
                debug(field.name + ":" + field.get(""))
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveCrashInfo2File(ex: Throwable): String? {
        val sb = StringBuffer()
        for ((key, value) in info) {
            sb.append(key + "=" + value + "\r\n")
        }
        val writer = StringWriter()
        val pw = PrintWriter(writer)
        ex.printStackTrace(pw)
        var cause: Throwable? = ex.cause
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw)
            cause = cause.cause
        }
        pw.close()// 记得关闭
        val result = writer.toString()
        sb.append(result)
        debug("CrashHandler\n" + sb.toString())
        // 保存文件
        val timetamp = System.currentTimeMillis()
        val time = format.format(Date())
        val fileName = "crash-$time-$timetamp.log"
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            try {
                val dir = FileUtils.CRASH_FILE
                error("CrashHandler" + dir.toString())
                if (!dir.exists())
                    dir.mkdir()
                val fos = FileOutputStream(File(dir, fileName))
                fos.write(sb.toString().toByteArray())
                fos.close()
                return fileName
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return null
    }

    companion object {

        private val TAG = "CrashHandler"
        /**
         * 获取CrashHandler实例 ,单例模式
         */
        val instance = CrashHandlerUtils()
    }
}