package com.oom.masterzuo.viewmodel.main.homepage;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.viewmodel.base.ViewModel;

public class BrandItemViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableField< String > brandName = new ObservableField<>( "固特异" );
    
    public final ObservableBoolean isSelected = new ObservableBoolean( false );
    
    // command
    public final ReplyCommand onSelect = new ReplyCommand( () -> {
        if ( !isSelected.get() ) {
            isSelected.set( true );
            Messenger.getDefault().send( brandName.get(), GoodsClassifyViewModel.CLEAR_BRAND_SELECT );
        }
    } );
    
    // item viewModel
    
    public BrandItemViewModel( Context context, Activity activity, FragmentManager fragmentManager,
            String brandNameParam, boolean isSelect ) {
        super( context, activity, fragmentManager );
        
        this.brandName.set( brandNameParam );
        this.isSelected.set( isSelect );
    
        Messenger.getDefault().register( context, GoodsClassifyViewModel.CLEAR_BRAND_SELECT, String.class, sendBrandName -> {
            if ( !brandName.get().equals( sendBrandName ) && isSelected.get() ) {
                isSelected.set( false );
            }
        });
    }
}
