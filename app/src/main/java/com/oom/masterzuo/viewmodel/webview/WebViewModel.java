package com.oom.masterzuo.viewmodel.webview;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.app.MasterZuoApplication;
import com.oom.masterzuo.viewmodel.base.ViewModel;
import com.oom.masterzuo.widget.callback_dialog.CallBackDialog.Style;
import com.oom.masterzuo.widget.callback_dialog.DialogBuilder;

/**
 * Created by YoungDroid on 2016/8/21.
 * Email: YoungDroid@163.com
 */
public class WebViewModel extends ViewModel {
    
    public static final String WEB_LOAD_FINISH = "web_load_finish" + MasterZuoApplication.sPackageName;
    public static final String WEB_URL_JUMP = "web_url_jump" + MasterZuoApplication.sPackageName;
    
    // Context

    // data filed
    public final ObservableField< String > url = new ObservableField<>();
    
    public final ObservableBoolean loadInside = new ObservableBoolean();
    
    // Command
    public final ReplyCommand onLoadFinish = new ReplyCommand( () -> {
        // load finish
        Messenger.getDefault().sendNoMsg( LOAD_DATA_FINISH );
        Messenger.getDefault().sendNoMsg( WEB_LOAD_FINISH );
        if ( null != callbackDialog && callbackDialog.isShowing() )
            callbackDialog.dismiss();
    } );
    
    public final ReplyCommand< String > webViewClient = new ReplyCommand<>( url -> {
        Messenger.getDefault().send( url, WEB_URL_JUMP );
    } );
    
    public WebViewModel( Context context, Activity activity, FragmentManager fragmentManager, String url, boolean loadInside ) {
        super( context, activity, fragmentManager );
    
        callbackDialog = DialogBuilder.init().createDialog( context )
                .setTitle( "正在加载..." ).setStyle( Style.LOADING ).build();
        callbackDialog.show();
        
        this.url.set( url );
        this.loadInside.set( loadInside );
    }
}
