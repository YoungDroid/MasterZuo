package com.oom.masterzuo.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.oom.masterzuo.api.ApiManager;
import com.oom.masterzuo.manager.AppManager;
import com.oom.masterzuo.utils.AppUtils;
import com.oom.masterzuo.utils.CrashHandler;
import com.oom.masterzuo.utils.FileUtils;
import com.oom.masterzuo.utils.LocalDisplay;
import com.oom.masterzuo.utils.TimeUtils;

import org.androidannotations.annotations.EApplication;

import java.util.Iterator;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by YoungDroid on 16/3/3.
 * Email: YoungDroid@163.com
 */
@EApplication
public class MasterZuoApplication extends Application {
    
    public static final String TAG = "YoungDroid";
    public static final boolean Debug = true;
    
    public static String sPackageName = "YoungDroid";
    public Context appContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        init();
    }
    
    public void init() {
        appContext = this;
        
        initCrash();
        
        //兼容7.0
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy( builder.build() );
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 ) {
            builder.detectFileUriExposure();
        }
        
        FileUtils.init();
        ApiManager.init( this );
        ApiManager.addInterceptor( mTokenInterceptor );
        
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder( this ).setBaseDirectoryPath( FileUtils.THUMBNAILFILE ).build();
        
        ImagePipelineConfig config =
                ImagePipelineConfig.newBuilder( this )
                        .setMainDiskCacheConfig( diskCacheConfig )
                        .setBitmapsConfig( Config.ARGB_8888 )
                        .setDownsampleEnabled( false )
                        .setResizeAndRotateEnabledForNetwork( true )
                        .setSmallImageDiskCacheConfig( diskCacheConfig )
                        .build();
        Fresco.initialize( this, config );
        
        // local display
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = ( WindowManager ) getSystemService( Context.WINDOW_SERVICE );
        wm.getDefaultDisplay().getMetrics( dm );
        LocalDisplay.init( dm );
    }
    
    private void initCrash() {
        // 异常文件
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init( this );
    }
    
    Interceptor mTokenInterceptor = chain -> {
        final HttpUrl modifiedUrl =
                chain.request().url().newBuilder().addQueryParameter( "user_token", "user_token" ).addQueryParameter( "access_token", "access_token" ).addQueryParameter( "user_id", "user_id" ).addQueryParameter( "facility", AppUtils.getPhoneImeiID( getApplicationContext() ) ).addQueryParameter( "version", AppUtils.getAppVersionName( getApplicationContext() ) ).addQueryParameter( "time", TimeUtils.getLocalTime() ).build();
        Request request =
                chain.request().newBuilder().url( modifiedUrl ).removeHeader( "User-Agent" ).addHeader( "User-Agent", AppManager.builder( getApplicationContext() ).phoneAndVersionInfo() ).build();
        
        return chain.proceed( request );
    };
    
    private String getAppName( int pID ) {
        String processName = null;
        ActivityManager am = ( ActivityManager ) this.getSystemService( ACTIVITY_SERVICE );
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while ( i.hasNext() ) {
            ActivityManager.RunningAppProcessInfo info = ( ActivityManager.RunningAppProcessInfo ) ( i.next() );
            try {
                if ( info.pid == pID ) {
                    processName = info.processName;
                    return processName;
                }
            } catch ( Exception e ) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
    
    @Override
    protected void attachBaseContext( Context base ) {
        super.attachBaseContext( base );
        try {
            MultiDex.install( this );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
