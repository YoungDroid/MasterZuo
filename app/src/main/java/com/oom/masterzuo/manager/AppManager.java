package com.oom.masterzuo.manager;

import android.content.Context;
import android.os.Build;

import com.oom.masterzuo.utils.AppUtils;

public class AppManager {
    
    private Context context;
    
    public AppManager( Context context ) {
        this.context = context;
    }
    
    public static AppManager builder( Context context ) {
        return new AppManager( context );
    }
    
    public String phoneAndVersionInfo() {
        String versionInfo = AppUtils.getAppVersionName( context );
        
        // 获取Android手机型号和OS的版本号
        String mobileName = Build.DEVICE;
        String osVersion = Build.VERSION.RELEASE;
        return encodeHeadInfo( "DeviceName:" + mobileName + ";OsVersion:" + osVersion + ";AppVersion:" + versionInfo );
    }
    
    private static String encodeHeadInfo( String headInfo ) {
        StringBuffer stringBuffer = new StringBuffer();
        for ( int i = 0, length = headInfo.length(); i < length; i++ ) {
            char c = headInfo.charAt( i );
            if ( c <= '\u001f' || c >= '\u007f' ) {
                stringBuffer.append( String.format( "\\u%04x", ( int ) c ) );
            } else {
                stringBuffer.append( c );
            }
        }
        return stringBuffer.toString();
    }
}
