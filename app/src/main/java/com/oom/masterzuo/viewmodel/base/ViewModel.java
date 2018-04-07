package com.oom.masterzuo.viewmodel.base;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.app.MasterZuoApplication;
//import com.oom.masterzuo.dialog.SimpleDialog;
//import com.oom.masterzuo.dialog.SimpleDialog.OnDialogClickListener;
//import com.oom.masterzuo.dialog.SimpleDialog_;
import com.oom.masterzuo.event.RequestErrorEvent;
import com.oom.masterzuo.passportapi.DialogSubscriber;
import com.oom.masterzuo.widget.callback_dialog.CallBackDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

/**
 * Created by YoungDroid on 2016/8/20.
 * Email: YoungDroid@163.com
 */
public abstract class ViewModel {
    
    public static final String TAG = "YoungDroid";
    public static final String LOAD_DATA_FINISH = "load_data_finish" + MasterZuoApplication.sPackageName;
    public static final String LOAD_DATA_ERROR = "load_data_error" + MasterZuoApplication.sPackageName;
    public static final String LOGIN_ON_OTHER_DEVICE = "login_on_other_device" + MasterZuoApplication.sPackageName;
    public static final String NOT_LOGIN = "not_login" + MasterZuoApplication.sPackageName;
    public static final String NETWORK_ERROR = "network_error" + MasterZuoApplication.sPackageName;
    
    // Context
    protected Context context;
    protected WeakReference< Activity > activity;
    protected WeakReference< FragmentManager > fragmentManager;
    protected SQLiteDatabase database;
    protected CallBackDialog callbackDialog;
    
    public ViewModel( Activity activity ) {
        this( null, activity, null );
    }
    
    public ViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        this( context, activity, fragmentManager, false );
    }
    
    public ViewModel( Activity activity, FragmentManager fragmentManager ) {
        this( null, activity, fragmentManager, false );
    }
    
    public ViewModel( Activity activity, FragmentManager fragmentManager, boolean useDataBase ) {
        this( null, activity, fragmentManager, useDataBase );
    }
    
    public ViewModel( Context context, Activity activity, FragmentManager fragmentManager, boolean useDataBase ) {
        this.context = context;
        this.activity = new WeakReference<>( activity );
        this.fragmentManager = new WeakReference<>( fragmentManager );
    }
    
    public void recycle() {
        context = null;
        activity = null;
        fragmentManager = null;
        database = null;
        if ( callbackDialog != null && callbackDialog.isShowing() ) {
            try {
                callbackDialog.dismiss();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            callbackDialog = null;
        }
    }
    
    // Error Subscriber
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void errorResponse( RequestErrorEvent errorEvent ) {
        if ( errorEvent.getCode() == DialogSubscriber.NOT_LOGIN ) {
            Messenger.getDefault().sendNoMsg( NOT_LOGIN );
            // TODO: 2018/4/2 重新登录
        } else if ( errorEvent.getCode() == DialogSubscriber.LOGIN_ON_OTHER_DEVICE ) {
            Messenger.getDefault().sendNoMsg( LOGIN_ON_OTHER_DEVICE );
            // TODO: 2018/4/2 判断已登录
            if ( true ) {
                if ( fragmentManager != null ) {
//                    SimpleDialog simpleDialog = SimpleDialog_.builder().content( errorEvent.getMessage() ).cancel( "稍后登录" ).confirm( "立即登录" ).build();
//                    simpleDialog.setOnDialogClickListener( new OnDialogClickListener() {
//                        @Override
//                        public void cancelClick() {
//                        }
//
//                        @Override
//                        public void confirmClick() {
//                            // TODO: 2018/4/2 重新登录
//                        }
//                    } );
//                    simpleDialog.show( fragmentManager.get(), "" );
                }
            }
            // TODO: 2018/4/2 退出登录
        } else {
            Messenger.getDefault().sendNoMsg( LOAD_DATA_ERROR );
            Toast.makeText( activity.get(), errorEvent.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }
}
