package com.masterzuo.masterzuo.view.base

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.masterzuo.masterzuo.app.MasterZuoApplication
import com.masterzuo.masterzuo.utils.error

/**
 * Created by YoungDroid on 2018/3/21.
 * Email: YoungDroid@163.com
 */
abstract class BaseActivity : AppCompatActivity() {

    private val REQUEST_WRITE_EXTERNAL_STORAGE_CODE = 101
    private val REQUEST_READ_PHONE_STATE_CODE = 102

    abstract fun initViews()

    abstract fun initData()

    abstract fun afterInit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSomeThing()
    }

    /**
     * 初始化一些参数
     */
    private fun initSomeThing() {

        // 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            error( "version: " + Build.VERSION.SDK_INT )
            val hasWriteExternalStoragePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                error("Need permission")
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_EXTERNAL_STORAGE_CODE)
            }
            val hasReadPhoneStatePermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
            if (hasReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                error("Need permission 2")
                requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_READ_PHONE_STATE_CODE)
            }
        }

        // 竖屏锁定
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // 透明ActionBar 和 顶部导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE_CODE ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    val application = application as MasterZuoApplication
                    application.init()
                } else {
                    error("权限不足,请同意权限申请.")
                    System.exit(0)
                }
            REQUEST_READ_PHONE_STATE_CODE ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        permissions[0] == Manifest.permission.READ_PHONE_STATE) {
                    val application = application as MasterZuoApplication
                    application.init()
                } else {
                    error("权限不足,请同意权限申请.")
                    System.exit(0)
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}