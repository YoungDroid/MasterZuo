package com.kelin.mvvmlight.bindingadapter.view;

import android.databinding.BindingAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.command.ResponseCommand;
import com.oom.masterzuo.utils.TimeUtils;

/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {
    
    @BindingAdapter({ "backgroundColor" })
    public static void setBackgroundColor( View view, int color ) {
        view.setBackgroundColor( color );
    }
    
    @BindingAdapter({ "backgroundResource" })
    public static void setBackgroundResource( View view, int resource ) {
        view.setBackgroundResource( resource );
    }
    
    @BindingAdapter({ "canEnable" })
    public static void setEnable( Button button, boolean isEnable ) {
        button.setEnabled( isEnable );
    }
    
    @BindingAdapter({ "clickCommand" })
    public static void clickCommand( View view, final ReplyCommand clickCommand ) {
        view.setOnClickListener( v -> {
            if ( TimeUtils.isFastClick() ) {
                Toast.makeText( v.getContext(), "别点辣么快啊~~~~(>_<)~~~~", Toast.LENGTH_SHORT ).show();
                return;
            }
            if ( clickCommand != null ) {
                clickCommand.execute();
            }
        } );
    }
    
    @BindingAdapter({ "clickCommandParam", "params" })
    public static void clickCommandParam( View view, final ReplyCommand< Integer > clickCommand, int params ) {
        view.setOnClickListener( v -> {
            if ( TimeUtils.isFastClick() ) {
                Toast.makeText( v.getContext(), "别点辣么快啊~~~~(>_<)~~~~", Toast.LENGTH_SHORT ).show();
                return;
            }
            if ( clickCommand != null ) {
                clickCommand.execute( params );
            }
        } );
    }
    
    @BindingAdapter({ "longClickCommand" })
    public static void longClickCommand( View view, final ReplyCommand longClickCommand ) {
        view.setOnLongClickListener( v -> {
            if ( longClickCommand != null ) {
                longClickCommand.execute();
                return true;
            }
            return false;
        } );
    }
    
    @BindingAdapter({ "requestFocus" })
    public static void requestFocusCommand( View view, final Boolean needRequestFocus ) {
        if ( needRequestFocus ) {
            view.setFocusableInTouchMode( true );
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }
    
    @BindingAdapter({ "onFocusChangeCommand" })
    public static void onFocusChangeCommand( View view, final ReplyCommand< Boolean > onFocusChangeCommand ) {
        view.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View v, boolean hasFocus ) {
                if ( onFocusChangeCommand != null ) {
                    onFocusChangeCommand.execute( hasFocus );
                }
            }
        } );
    }
    
    @BindingAdapter({ "onTouchCommand" })
    public static void onTouchCommand( View view, final ResponseCommand< MotionEvent, Boolean > onTouchCommand ) {
        view.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                if ( onTouchCommand != null ) {
                    return onTouchCommand.execute( event );
                }
                return false;
            }
        } );
    }
}

