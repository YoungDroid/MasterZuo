package com.oom.masterzuo.viewmodel.base.radioButton;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

import com.oom.masterzuo.utils.DensityUtil;

public class ViewBindingAdapter {
    
    @BindingAdapter({ "boundWidth", "boundHeight" })
    public static void onCheckedChange( RadioButton radioButton, int width, int height ) {
        int boundWidth = DensityUtil.dip2px( radioButton.getContext(), width );
        int boundHeight = DensityUtil.dip2px( radioButton.getContext(), height );
        Drawable[] drawables = radioButton.getCompoundDrawables();
        for ( Drawable drawable : drawables ) {
            if ( drawable != null ) {
                int right = drawable.getBounds().right;
                int bottom = drawable.getBounds().bottom;
                int left = 0;
                int top = 0;
                
                if ( right > boundWidth ) {
                    left = ( right - boundWidth ) / 2;
                }
                if ( bottom > boundHeight ) {
                    top = ( bottom - boundHeight );
                }
                drawable.setBounds( left, top, left + boundWidth, top + boundHeight );
            }
        }
        radioButton.requestLayout();
    }
}
