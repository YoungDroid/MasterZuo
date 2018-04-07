package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.viewmodel.base.ViewModel;

/**
 * Created by CcYang on 2016/11/22.
 * Email: 1367496366@qq.com
 */

public class ErrorViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableBoolean loading = new ObservableBoolean();
    public final ObservableInt contentState = new ObservableInt( MultiStateView.VIEW_STATE_LOADING );
    
    public final ObservableBoolean loadMoreComplete = new ObservableBoolean( false );
    public final ObservableBoolean loadMoreError = new ObservableBoolean( false );
    public final ObservableBoolean loadMoreEmpty = new ObservableBoolean( false );
    public final ObservableBoolean loadMoreHasMore = new ObservableBoolean( true );
    
    // command
    public final ReplyCommand onRefresh = new ReplyCommand( () -> {
        // TODO: 2018/4/2 刷新
    } );
    
    // item viewModel
    
    public ErrorViewModel( Context context, Activity activity, FragmentManager fragmentManager, String flashGory ) {
        super( context, activity, fragmentManager );
        
        Messenger.getDefault().register( activity, LOAD_DATA_ERROR, () -> {
            contentState.set( MultiStateView.VIEW_STATE_CONTENT );
            loading.set( false );
            loadMoreError.set( true );
            loadMoreComplete.set( true );
        } );
        
        loadDataFromNet();
    }
    
    public ErrorViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        this( context, activity, fragmentManager, "" );
    }
    
    private void loadDataFromNet() {
        loading.set( true );
        
        if ( contentState.get() == MultiStateView.VIEW_STATE_ERROR || contentState.get() == MultiStateView.VIEW_STATE_EMPTY )
            contentState.set( MultiStateView.VIEW_STATE_LOADING );
        
        loadMoreComplete.set( false );
    }
}
