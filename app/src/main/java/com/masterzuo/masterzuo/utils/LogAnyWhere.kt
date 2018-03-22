package com.masterzuo.masterzuo.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by YoungDroid on 2017/3/20.
 * Email: YoungDroid@163.com
 */
inline fun <reified T> T.debug(log: Any) {
    Log.d(T::class.simpleName, log.toString())
}

inline fun <reified T> T.error(log: Any) {
    Log.e(T::class.simpleName, log.toString())
}

inline fun <reified T> T.toast(context: Context, log: CharSequence) {
    Toast.makeText(context, log, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> T.toast(context: Context, log: Int) {
    Toast.makeText(context, log, Toast.LENGTH_SHORT).show()
}