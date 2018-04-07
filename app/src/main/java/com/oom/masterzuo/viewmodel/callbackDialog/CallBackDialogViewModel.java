package com.oom.masterzuo.viewmodel.callbackDialog;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;

import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.viewmodel.base.ViewModel;

import static com.oom.masterzuo.widget.callback_dialog.CallBackDialog.UPLOAD_COMPLETE;

public class CallBackDialogViewModel extends ViewModel {
    
    // model
    private boolean[] completeInfo;
    private int fullCount;
    
    // data filed
    public final ObservableField< Uri > image = new ObservableField<>();
    
    public final ObservableField< String > title = new ObservableField<>();
    
    public final ObservableBoolean isLoading = new ObservableBoolean();
    public final ObservableBoolean isUploading = new ObservableBoolean();
    
    public final ObservableInt fullPoint = new ObservableInt( -1 );
    public final ObservableInt allCount = new ObservableInt();
    
    // command
    
    // item viewModel
    
    public CallBackDialogViewModel( Context context ) {
        super( context, null, null );
    }
    
    public void setAllCount( int count ) {
        allCount.set( count );
        completeInfo = new boolean[ count ];
        fullCount = 0;
        updateUI();
    }
    
    public void setCompleteIndex( int index ) {
        if ( !completeInfo[ index ] ) {
            completeInfo[ index ] = true;
            fullPoint.set( index );
            fullCount++;
            updateUI();
        }
    }
    
    public void setError() {
        title.set( "上传图片出错...(已完成" + fullCount + "/" + allCount.get() + ")" );
    }
    
    private void updateUI() {
        title.set( "正在上传图片...(已完成" + fullCount + "/" + allCount.get() + ")" );
        if ( fullCount == allCount.get() ) {
            Messenger.getDefault().sendNoMsg( UPLOAD_COMPLETE );
        }
    }
}
