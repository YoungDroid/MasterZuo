package com.oom.masterzuo.viewmodel.base.radiogroup;

import android.databinding.BindingAdapter;
import android.widget.RadioGroup;

import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by YoungDroid on 2016/9/20.
 * Email: YoungDroid@163.com
 */

public class ViewBindingAdapter {
    
    @BindingAdapter( { "onCheckedChange" } )
    public static void onCheckedChange( RadioGroup radioGroup, ReplyCommand< Integer > onCheckedChange) {
        radioGroup.setOnCheckedChangeListener( ( group, checkedId ) -> {
            for ( int i = 0; i < group.getChildCount(); i++ ) {
                if ( checkedId == group.getChildAt( i ).getId() && onCheckedChange != null ) {
                    onCheckedChange.execute( i );
                }
            }
        } );
        
        radioGroup.check( radioGroup.getChildAt( 0 ).getId() );
    }
    
    @BindingAdapter( { "autoCheck" } )
    public static void autoCheck( RadioGroup radioGroup, int autoCheck ) {
        if ( autoCheck >= 0 && autoCheck < radioGroup.getChildCount() )
            radioGroup.check( radioGroup.getChildAt( autoCheck ).getId() );
    }
}
