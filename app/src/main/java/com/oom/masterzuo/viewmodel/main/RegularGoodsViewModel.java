package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.viewmodel.base.ViewModel;

public class RegularGoodsViewModel extends ViewModel {
    
    // model
    
    // data filed
    
    // command
    
    public final ReplyCommand onRefresh = new ReplyCommand( this::loadDataFromNet );
    
    // item viewModel
    
    public RegularGoodsViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        loadDataFromNet();
    }
    
    private void loadDataFromNet() {
        // TODO: 2018/4/2 加载网络数据
    }
}
