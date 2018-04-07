package com.oom.masterzuo.viewmodel.main.membercenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.R;
import com.oom.masterzuo.app.MasterZuoApplication;
import com.oom.masterzuo.app.main.HomeActivity_;
import com.oom.masterzuo.utils.StringUtils;
import com.oom.masterzuo.viewmodel.base.ViewModel;

import java.util.Random;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

public class LoginViewModel extends ViewModel {
    
    public static final int PASSWORD_LENGTH_MIN = 8;
    
    // Context
    
    // SharedPreferences
    private Preference< String > preferenceUserName;
    private Preference< String > preferencePassword;
    
    // 数据绑定（data filed）
    public final ObservableField< String > username = new ObservableField<>();
    public final ObservableField< String > password = new ObservableField<>();
    
    public final ObservableBoolean usernameComplete = new ObservableBoolean();
    public final ObservableBoolean passwordComplete = new ObservableBoolean();
    
    // 命令绑定（command）
    public final ReplyCommand onLogin = new ReplyCommand( () -> {
        // TODO: 2018/4/5 登录
        Toast.makeText( context, "登录", Toast.LENGTH_SHORT ).show();
    } );
    public final ReplyCommand onResetPassword = new ReplyCommand( () -> {
        // TODO: 2018/4/5 忘记密码
        Toast.makeText( context, "忘记密码", Toast.LENGTH_SHORT ).show();
    } );
    
    public final ReplyCommand< String > onUsernameChange = new ReplyCommand<>( s -> {
        username.set( s );
        Observable.just( s ).subscribe( preferenceUserName.asAction() );
    } );
    
    public final ReplyCommand< String > onPasswordChange = new ReplyCommand<>( s -> {
        password.set( s );
        Observable.just( s ).subscribeOn( Schedulers.newThread()).subscribe( preferencePassword.asAction() );
    } );
    
    public final ReplyCommand onClearUserName = new ReplyCommand( () -> {
        onUsernameChange.execute( "" );
    } );
    
    public final ReplyCommand onClearPassword = new ReplyCommand( () -> {
        onPasswordChange.execute( "" );
    } );
    
    public LoginViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create( preferences );
        preferenceUserName = rxSharedPreferences.getString( "login_username" );
        preferencePassword = rxSharedPreferences.getString( "login_password" );
    
        preferenceUserName.asObservable().subscribe( s -> {
            username.set( s );
            Observable.just( username )
                    .flatMap( stringObservableField -> Observable.just( !StringUtils.isEmptyString( stringObservableField.get() ) ) ).
                    subscribe( usernameComplete::set );
        } );
    
        preferencePassword.asObservable().subscribe( s -> {
            Observable.just( password )
                    .flatMap( stringObservableField
                            -> Observable.just( StringUtils.isStringLengthLegal( stringObservableField.get(), PASSWORD_LENGTH_MIN ) ) )
                    .subscribe( passwordComplete::set );
        } );
    }
}
