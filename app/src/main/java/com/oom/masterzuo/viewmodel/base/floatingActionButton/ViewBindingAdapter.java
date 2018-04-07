package com.oom.masterzuo.viewmodel.base.floatingActionButton;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by YoungDroid on 2017/3/23.
 * Email: YoungDroid@163.com
 */

public class ViewBindingAdapter {

    @BindingAdapter({ "buttonIcon" })
    public static void setButtonIcon( FloatingActionButton floatingActionButton,@DrawableRes int resourceID ) {
        if ( null != floatingActionButton ) {
            floatingActionButton.setImageResource( resourceID );
        }
    }
}
