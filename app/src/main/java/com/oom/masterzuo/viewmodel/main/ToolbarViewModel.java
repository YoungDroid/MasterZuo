package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.oom.masterzuo.viewmodel.base.ViewModel;

public class ToolbarViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableField< String > title = new ObservableField<>();
    
    public final ObservableBoolean hasBack = new ObservableBoolean( false );
    public final ObservableBoolean hasMenu = new ObservableBoolean( false );
    
    // command
    
    // item viewModel
    
    public ToolbarViewModel( Context context, Activity activity, FragmentManager fragmentManager,
            String title, boolean hasBack, boolean hasMenu ) {
        super( context, activity, fragmentManager );
        
        this.title.set( title );
        this.hasBack.set( hasBack );
        this.hasMenu.set( hasMenu );
    }
}
