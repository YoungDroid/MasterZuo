package com.oom.masterzuo.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.R;
import com.oom.masterzuo.app.MasterZuoApplication;
import com.oom.masterzuo.app.main.HomeActivity_;
import com.oom.masterzuo.viewmodel.base.ViewModel;

import java.util.Random;

import rx.Observable;

public class WelcomeViewModel extends ViewModel {
    
    public static final String BG_ANIMATION_FINISH = "bg_animation_finish" + MasterZuoApplication.sPackageName;
    
    // Context
    
    private Preference< Boolean > preferenceIsFirstStart;
    private Preference< Boolean > preferenceShowNews;
    
    // Background Resource
    int[] resArray = new int[]{
            R.mipmap.icon_splash_1
    };
    
    // 数据绑定（data filed）
    public final ObservableField< Uri > backgroundUri = new ObservableField<>();
    
    // 命令绑定（command）
    public final ReplyCommand onAnimation = new ReplyCommand( () -> {
        HomeActivity_.intent( activity.get() ).start();
        activity.get().finish();
    } );
    
    public WelcomeViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        Observable< Integer > backgroundSource = Observable.create( subscriber -> {
            subscriber.onNext( new Random().nextInt( 100 ) % resArray.length );
        } );
        
        backgroundSource.map( integer -> Uri.parse( "res:///" + resArray[ integer ] ) ).subscribe( backgroundUri::set );
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( activity );
        RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create( preferences );
        preferenceIsFirstStart = rxSharedPreferences.getBoolean( "isFirstStart", true );
        preferenceShowNews = rxSharedPreferences.getBoolean( "showNews", false );
        
        if ( preferenceIsFirstStart == null || preferenceIsFirstStart.get() == null || preferenceIsFirstStart.get() ) {
            preferenceIsFirstStart.set( false );
            preferenceShowNews.set( false );
        }
    }
}
