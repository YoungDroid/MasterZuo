package com.oom.masterzuo.viewmodel.base.relativeLayout;

import android.databinding.BindingAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Created by YoungDroid on 2016/12/8.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {
    
    @BindingAdapter( {"customHeight"} )
    public static void setHeight( RelativeLayout relativeLayout, int height ) {
        LayoutParams layoutParams = ( LayoutParams ) relativeLayout.getLayoutParams();
        layoutParams.height = height;
    }

    @BindingAdapter( {"layoutWeight"} )
    public static void setLayoutWeight( RelativeLayout relativeLayout, float weight ) {
        LinearLayout.LayoutParams layoutParams = ( LinearLayout.LayoutParams ) relativeLayout.getLayoutParams();
        layoutParams.weight = weight;
    }
}
