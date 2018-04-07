package com.oom.masterzuo.viewmodel.base.multistateview;

import android.databinding.BindingAdapter;

import com.kennyc.view.MultiStateView;

/**
 * Created by YoungDroid on 2016/9/2.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {

    @BindingAdapter({ "state" })
    public static void setState( MultiStateView multiStateView, int state ) {
        if ( multiStateView != null && state != -1 ) {
            multiStateView.setViewState( state );
        }
    }
}
