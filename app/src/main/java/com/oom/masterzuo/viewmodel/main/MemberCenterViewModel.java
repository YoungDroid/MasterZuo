package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.app.MasterZuoApplication;
import com.oom.masterzuo.viewmodel.base.ViewModel;

/**
 * Created by YoungDroid on 2016/8/19.
 * Email: YoungDroid@163.com
 */
public class MemberCenterViewModel extends ViewModel {
    
    public static final String MEMBER_CENTER_INFO_FINISH = "member_center_info_finish" + MasterZuoApplication.sPackageName;
    public static final String MEMBER_CENTER_LOGOUT = "member_center_logout" + MasterZuoApplication.sPackageName;
    public static final String MEMBER_CENTER_REFRESH_FINISH = "member_center_refresh_finish" + MasterZuoApplication.sPackageName;
    public static final String MEMBER_CENTER_AUTO_REFRESH = "member_center_auto_refresh" + MasterZuoApplication.sPackageName;
    
    //（model）
    
    //（data filed）
    public final ObservableBoolean isSelf = new ObservableBoolean( true );
    
    //（command）
    public final ReplyCommand onBack = new ReplyCommand( () -> {
        activity.get().finish();
    } );
    
    public final ReplyCommand onSetting = new ReplyCommand( () -> {
        // Setting
        // TODO: 2018/4/2 设置界面
    } );
    
    public final ReplyCommand onRefresh = new ReplyCommand( () -> {
        // TODO: 2018/4/2 刷新
    } );
    
    // item viewModel
    
    public MemberCenterViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        this( context, activity, fragmentManager, true, "" );
    }
    
    public MemberCenterViewModel( Context context, Activity activity, FragmentManager fragmentManager, boolean isSelf, String uID ) {
        super( context, activity, fragmentManager );
        
        this.isSelf.set( isSelf );
        
        Messenger.getDefault().sendNoMsg( LOAD_DATA_FINISH );
    }
    
}
