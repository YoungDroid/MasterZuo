package com.oom.masterzuo.viewmodel.main.homepage;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.viewmodel.base.ViewModel;

public class RecommendGoodsItemViewModel extends ViewModel {
    
    // model
    
    // data filed
    
    // command
    public final ReplyCommand onItemClick = new ReplyCommand( () -> {
        // TODO: 2018/4/3 点击事件
    } );
    
    // item viewModel
    
    public RecommendGoodsItemViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
    }
}
