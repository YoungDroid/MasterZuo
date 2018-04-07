package com.oom.masterzuo.viewmodel.base.toolbar;

import android.databinding.BindingAdapter;
import android.support.v7.widget.Toolbar;

import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by YoungDroid on 2016/12/07.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {

    @BindingAdapter( { "navigationAction" })
    public static void setNavigationAction( Toolbar toolbar, ReplyCommand onAction ) {
        if ( null != toolbar ) {
            toolbar.setNavigationOnClickListener( v -> {
                if ( null != onAction ) {
                    onAction.execute();
                }
            } );
        }

    }
}

