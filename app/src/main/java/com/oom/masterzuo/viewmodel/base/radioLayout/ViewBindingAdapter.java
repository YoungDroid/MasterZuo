package com.oom.masterzuo.viewmodel.base.radioLayout;

import android.databinding.BindingAdapter;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.widget.radioLayout.RadioLayout;

public class ViewBindingAdapter {
    
    @BindingAdapter({ "onCheckedChange" })
    public static void onCheckedChange( RadioLayout radioLayout, ReplyCommand< Integer > onCheckedChange ) {
        radioLayout.setListener( ( layout, selectedIndex ) -> {
            if ( null != onCheckedChange ) {
                onCheckedChange.execute( selectedIndex );
            }
        } );
        
        radioLayout.setCheckAt( 0 );
    }
    
    @BindingAdapter({ "autoCheck" })
    public static void autoCheck( RadioLayout radioLayout, int autoCheck ) {
        if ( autoCheck >= 0 && autoCheck < radioLayout.getChildCount() )
            radioLayout.setCheckAt( autoCheck );
    }
}
