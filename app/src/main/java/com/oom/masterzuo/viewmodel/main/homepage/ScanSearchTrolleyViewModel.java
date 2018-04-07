package com.oom.masterzuo.viewmodel.main.homepage;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;

import com.oom.masterzuo.viewmodel.base.ViewModel;

public class ScanSearchTrolleyViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableField< String > content = new ObservableField<>();
    
    // command
    
    // item viewModel
    
    public ScanSearchTrolleyViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
    }
}
