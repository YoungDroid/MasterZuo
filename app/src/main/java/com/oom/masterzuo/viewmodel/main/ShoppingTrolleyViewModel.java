package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableInt;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.app.MasterZuoApplication;
import com.oom.masterzuo.viewmodel.base.ViewModel;

public class ShoppingTrolleyViewModel extends ViewModel {
    
    public static final String COMMUNITY_REFRESH_COMPLETE = "COMMUNITY_REFRESH_COMPLETE" + MasterZuoApplication.sPackageName;
    
    // model
    
    // data filed
    public final ObservableInt communityViewState = new ObservableInt( MultiStateView.VIEW_STATE_LOADING );
    
    // command
    public final ReplyCommand onRefresh = new ReplyCommand( this::loadDataFromNet );
    
    // item viewModel
    
    public ShoppingTrolleyViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        Messenger.getDefault().register( activity, LOAD_DATA_ERROR, () -> {
            communityViewState.set( MultiStateView.VIEW_STATE_CONTENT );
        } );
        
        loadDataFromNet();
    }
    
    private void loadDataFromNet() {
        if ( communityViewState.get() == MultiStateView.VIEW_STATE_ERROR || communityViewState.get() == MultiStateView.VIEW_STATE_EMPTY )
            communityViewState.set( MultiStateView.VIEW_STATE_LOADING );
        
    }
}
