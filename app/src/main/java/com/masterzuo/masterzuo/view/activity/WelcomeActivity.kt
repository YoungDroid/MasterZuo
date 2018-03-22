package com.masterzuo.masterzuo.view.activity

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.masterzuo.masterzuo.R
import com.masterzuo.masterzuo.view.base.BaseActivity

import kotlinx.android.synthetic.main.activity_welcome.*

/**
 * Created by YoungDroid on 2018/3/21.
 * Email: YoungDroid@163.com
 */
class WelcomeActivity : BaseActivity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tv_activity_welcome_app_version.text = "1.0.0.20180321"
        //sdv_activity_welcome_flush.setImageURI(Uri.parse("res:///" + R.mipmap.img_welcome))
    }

    override fun initViews() {

    }

    override fun initData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterInit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}