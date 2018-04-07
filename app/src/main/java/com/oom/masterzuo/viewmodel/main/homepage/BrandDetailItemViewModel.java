package com.oom.masterzuo.viewmodel.main.homepage;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.viewmodel.base.ViewModel;

public class BrandDetailItemViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableField< String > brandDetailName = new ObservableField<>("275 50 20轮胎");
    
    // command
    public final ReplyCommand onClick = new ReplyCommand( () -> {
        // TODO: 2018/4/3 点击事件
    } );
    
    // item viewModel
    
    public BrandDetailItemViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
    }
}
