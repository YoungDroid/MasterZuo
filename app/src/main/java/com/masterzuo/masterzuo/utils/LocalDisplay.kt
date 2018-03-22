package com.masterzuo.masterzuo.utils

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * Created by YoungDroid on 17/3/20.
 * Email: YoungDroid@163.com
 */
object LocalDisplay {

    var SCREEN_WIDTH_PIXELS: Int = 0
    var SCREEN_HEIGHT_PIXELS: Int = 0
    var SCREEN_DENSITY: Float = 0.toFloat()
    var SCREEN_WIDTH_DP: Int = 0
    var SCREEN_HEIGHT_DP: Int = 0

    fun init(dm: DisplayMetrics) {

        SCREEN_WIDTH_PIXELS = dm.widthPixels
        SCREEN_HEIGHT_PIXELS = dm.heightPixels
        SCREEN_DENSITY = dm.density
        SCREEN_WIDTH_DP = (SCREEN_WIDTH_PIXELS / dm.density).toInt()
        SCREEN_HEIGHT_DP = (SCREEN_HEIGHT_PIXELS / dm.density).toInt()
        debug("INIT APP " + " width: " + SCREEN_WIDTH_PIXELS + " height: " + SCREEN_HEIGHT_PIXELS)
    }

    fun dp2px(dp: Float): Int {
        val scale = SCREEN_DENSITY
        return (dp * scale + 0.5f).toInt()
    }

    fun designedDP2px(designDP: Float): Int {
        if (SCREEN_WIDTH_DP != 320) {
            dp2px(designDP * SCREEN_WIDTH_DP / 320f)
        }
        return dp2px(designDP)
    }

    fun setPadding(view: View, left: Float, top: Float, right: Float, bottom: Float) {
        view.setPadding(designedDP2px(left), dp2px(top), designedDP2px(right), dp2px(bottom))
    }

    fun setMargins(view: View, left: Float, top: Float, right: Float, bottom: Float) {
        val lllp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        lllp.setMargins(designedDP2px(left), top.toInt(), designedDP2px(right), dp2px(bottom))
        view.layoutParams = lllp
    }
}
