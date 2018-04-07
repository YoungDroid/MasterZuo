package com.oom.masterzuo.widget.callback_dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

//import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.app.MasterZuoApplication;
//import com.oom.masterzuo.databinding.CallBackDialogBinding;
import com.oom.masterzuo.viewmodel.callbackDialog.CallBackDialogViewModel;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

public class CallBackDialog extends Dialog {
    
    public static final String TAG = "YoungDroid";
    public static final String UPLOAD_COMPLETE = "upload_complete" + MasterZuoApplication.sPackageName;
    public static final String UPLOAD_ERROR = "UPLOAD_ERROR" + MasterZuoApplication.sPackageName;
    
    private CallBackDialogViewModel callBackDialogViewModel;
    private WeakReference< Context > context;
    
    private int delayCount = 0;
    
    public enum Style {
        LOADING, SUCCESS, FAIL, UPLOADING
    }
    
    public CallBackDialog( Context context ) {
        super( context );
    }
    
    public CallBackDialog( Context context, int themeResId ) {
        super( context, themeResId );
        this.context = new WeakReference<>( context );
    }
    
    public void setContentView( ViewDataBinding binding ) {
        
//        CallBackDialogBinding callBackBinding = ( CallBackDialogBinding ) binding;
//        callBackDialogViewModel = new CallBackDialogViewModel( context.get() );
//        callBackBinding.setVariable( BR.viewModel, callBackDialogViewModel );
        
//        setContentView( callBackBinding.getRoot() );
    }
    
    public void setStyle( Style style ) {
        if ( null == callBackDialogViewModel )
            return;
        switch ( style ) {
            case LOADING:
                setCanceledOnTouchOutside( false );
                callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_comming ) );
                callBackDialogViewModel.isLoading.set( true );
                callBackDialogViewModel.isUploading.set( false );
                if ( TextUtils.isEmpty( callBackDialogViewModel.title.get() ) )
                    callBackDialogViewModel.title.set( "%>_<% 努力加载ing" );
                break;
            case SUCCESS:
                callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_success ) );
                callBackDialogViewModel.isLoading.set( false );
                callBackDialogViewModel.isUploading.set( false );
                if ( TextUtils.isEmpty( callBackDialogViewModel.title.get() ) )
                    callBackDialogViewModel.title.set( "O(∩_∩)O~成功" );
                break;
            case FAIL:
                callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_fail ) );
                callBackDialogViewModel.isLoading.set( false );
                callBackDialogViewModel.isUploading.set( false );
                if ( TextUtils.isEmpty( callBackDialogViewModel.title.get() ) )
                    callBackDialogViewModel.title.set( "~~~~(>_<)~~~~失败" );
                break;
            case UPLOADING:
                setCanceledOnTouchOutside( false );
                callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_loading ) );
                callBackDialogViewModel.isLoading.set( false );
                callBackDialogViewModel.isUploading.set( true );
                if ( TextUtils.isEmpty( callBackDialogViewModel.title.get() ) )
                    callBackDialogViewModel.title.set( "%>_<% 正在上传..." );
                break;
        }
    }
    
    public void setStyle( Style style, String title ) {
        
        Observable.interval( 150, TimeUnit.MILLISECONDS ).take( 1 ).subscribeOn( Schedulers.io() ).unsubscribeOn( Schedulers.io() ).observeOn( AndroidSchedulers.mainThread() ).subscribe( aLong -> {
            switch ( style ) {
                case LOADING:
                    setCanceledOnTouchOutside( false );
                    callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.iv_read ) );
                    callBackDialogViewModel.isLoading.set( true );
                    callBackDialogViewModel.isUploading.set( false );
                    break;
                case SUCCESS:
                    callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_success ) );
                    callBackDialogViewModel.isLoading.set( false );
                    callBackDialogViewModel.isUploading.set( false );
                    break;
                case FAIL:
                    callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_fail ) );
                    callBackDialogViewModel.isLoading.set( false );
                    callBackDialogViewModel.isUploading.set( false );
                    break;
                case UPLOADING:
                    setCanceledOnTouchOutside( false );
                    callBackDialogViewModel.image.set( Uri.parse( "res:///" + R.mipmap.call_back_loading ) );
                    callBackDialogViewModel.isLoading.set( false );
                    callBackDialogViewModel.isUploading.set( true );
                    break;
            }
            
            if ( !TextUtils.isEmpty( title ) )
                callBackDialogViewModel.title.set( title );
        } );
    }
    
    public void setTitle( String title ) {
        if ( null == callBackDialogViewModel )
            return;
        
        if ( !TextUtils.isEmpty( title ) )
            callBackDialogViewModel.title.set( title );
    }
    
    public void setUploadCount( int count ) {
        callBackDialogViewModel.setAllCount( count );
    }
    
    public void updateCompleteUploadIndex( int index ) {
        callBackDialogViewModel.setCompleteIndex( index );
    }
    
    public void uploadError() {
        if ( Debug ) {
            Log.e( "CallBackDialog", "uploadError: 上传图片出错,请重试." );
        }
        setStyle( Style.FAIL, "上传图片出错,请重试." );
        setAutoDismiss( 1500 );
    }
    
    public void setAutoDismiss( long durations ) {
        Observable.interval( durations, TimeUnit.MILLISECONDS ).take( 1 ).subscribeOn( Schedulers.io() ).unsubscribeOn( Schedulers.io() ).observeOn( AndroidSchedulers.mainThread() ).subscribe( aLong -> {
            try {
                dismiss();
            } catch ( IllegalArgumentException e ) {
                e.printStackTrace();
            }
        } );
    }
    
    @Override
    public void dismiss() {
        context = null;
        super.dismiss();
    }
}