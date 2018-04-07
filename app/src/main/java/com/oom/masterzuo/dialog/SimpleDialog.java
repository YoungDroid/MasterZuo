package com.oom.masterzuo.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.oom.masterzuo.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by YoungDroid on 2017/5/11.
 * Email: YoungDroid@163.com
 */
@EFragment(R.layout.dialog_simple_dialog)
public class SimpleDialog extends DialogFragment {
    
    @FragmentArg("TITLE")
    String title;
    @FragmentArg("CONTENT")
    String content;
    @FragmentArg("CANCEL")
    String cancel;
    @FragmentArg("CONFIRM")
    String confirm;
    
    @ViewById(R.id.tv_simple_dialog_title)
    TextView tvTitle;
    @ViewById(R.id.tv_simple_dialog_content)
    TextView tvContent;
    @ViewById(R.id.b_simple_dialog_cancel)
    Button bCancel;
    @ViewById(R.id.b_simple_dialog_confirm)
    Button bConfirm;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setStyle( DialogFragment.STYLE_NORMAL, R.style.ShareDialogTheme );
        setCancelable( true );
    }
    
    @AfterViews
    public void afterViews() {
        if ( !TextUtils.isEmpty( title ) ) {
            tvTitle.setText( title );
        }
        if ( !TextUtils.isEmpty( content ) ) {
            tvContent.setText( content );
        }
        if ( !TextUtils.isEmpty( cancel ) ) {
            bCancel.setText( cancel );
        }
        if ( !TextUtils.isEmpty( confirm ) ) {
            bConfirm.setText( confirm );
        }
    }
    
    @Click(R.id.b_simple_dialog_cancel)
    public void cancel() {
        if ( listener != null ) listener.cancelClick();
        dismiss();
    }
    
    @Click(R.id.b_simple_dialog_confirm)
    public void confirm() {
        if ( listener != null ) listener.confirmClick();
        dismiss();
    }
    
    public interface OnDialogClickListener {
        void cancelClick();
        
        void confirmClick();
    }
    
    public OnDialogClickListener listener;
    
    public void setOnDialogClickListener( OnDialogClickListener listener ) {
        this.listener = listener;
    }
}
