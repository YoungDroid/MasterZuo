package com.oom.masterzuo.viewmodel.main.homepage;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.viewmodel.base.ViewModel;

public class ModelHeaderViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableField< Uri > headerImageUri = new ObservableField<>();
    
    public final ObservableField< String > redColorWords = new ObservableField<>();
    public final ObservableField< String > blackColorWords = new ObservableField<>();
    
    public final ObservableBoolean hasMoreMenu = new ObservableBoolean( false );
    
    // command
    
    // item viewModel
    
    public ModelHeaderViewModel( Context context, Activity activity, FragmentManager fragmentManager,
            Uri headerImageUri, String redColorWords, String blackColorWords, boolean hasMoreMenu ) {
        super( context, activity, fragmentManager );
        
        if ( null != headerImageUri )
            this.headerImageUri.set( headerImageUri );
        this.redColorWords.set( redColorWords );
        this.blackColorWords.set( blackColorWords );
        this.hasMoreMenu.set( hasMoreMenu );
    }
}
