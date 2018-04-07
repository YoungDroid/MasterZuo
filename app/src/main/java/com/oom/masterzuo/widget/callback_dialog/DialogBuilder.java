package com.oom.masterzuo.widget.callback_dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;

import com.oom.masterzuo.R;
import com.oom.masterzuo.databinding.CallBackDialogBinding;
import com.oom.masterzuo.widget.callback_dialog.CallBackDialog.Style;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

/**
 * Created by YoungDroid on 2016/10/9.
 * Email: YoungDroid@163.com
 */

public class DialogBuilder {

    private static DialogBuilder dialogBuilder;
    private CallBackDialog callBackDialog;

    private CallBackDialogBinding callBackBinding;

    public static DialogBuilder init() {
        if ( Debug ) {
            Log.e( "DialogBuilder", "init:" );
        }
        if ( null == dialogBuilder ) {
            if ( Debug ) {
                Log.e( "DialogBuilder", "init: Create" );
            }
            dialogBuilder = new DialogBuilder();
        }
        return dialogBuilder;
    }

    public DialogBuilder createDialog( Context context ) {
        if ( Debug ) {
            Log.e( "DialogBuilder", "createDialog: " );
        }
        if ( context == null ) {
            throw new NullPointerException( "Context can't be null." );
        }
        
        if ( null == callBackDialog || !callBackDialog.isShowing() ) {
            callBackDialog = new CallBackDialog( context, R.style.DialogCallBack );
            callBackBinding = DataBindingUtil.inflate( LayoutInflater.from( context ), R.layout.call_back_dialog, null, false );
            callBackDialog.setContentView( callBackBinding );
        }

        if ( null != callBackDialog.getWindow() ) {
            callBackDialog.getWindow().setBackgroundDrawableResource( android.R.color.transparent );
            callBackDialog.getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        }

        return dialogBuilder;
    }

    public DialogBuilder canCancelOutside( boolean canCancelOutside ) {
        callBackDialog.setCanceledOnTouchOutside( canCancelOutside );
        return dialogBuilder;
    }

    public DialogBuilder setStyle( Style style ) {
        callBackDialog.setStyle( style );
        return dialogBuilder;
    }

    public DialogBuilder setTitle( String title ) {
        callBackDialog.setTitle( title );
        return dialogBuilder;
    }

    public DialogBuilder setUploadCount( int count ) {
        callBackDialog.setUploadCount( count );
        return dialogBuilder;
    }

    public DialogBuilder changeStyleAndTitle( Style style, String title ) {
        callBackDialog.setStyle( style, title );
        return dialogBuilder;
    }

    public DialogBuilder setAutoDismiss( long durations ) {
        callBackDialog.setAutoDismiss( durations );
        return dialogBuilder;
    }

    public CallBackDialog build() {
        return callBackDialog;
    }

    public void updateCompleteUploadIndex( int index ) {
        if ( callBackDialog != null && callBackDialog.isShowing() ) {
            callBackDialog.updateCompleteUploadIndex( index );
        }
    }
}
