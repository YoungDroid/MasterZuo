package com.oom.masterzuo.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaojunhuang on 16/3/29.
 */
public class AppUtils {
    public static void copy( Context context, String text ) {
        ClipboardManager cmb = ( ClipboardManager ) context.getSystemService( Context.CLIPBOARD_SERVICE );
        ClipData clipData = ClipData.newPlainText( "text", text );
        cmb.setPrimaryClip( clipData );
    }

    //获取用户手机的imei和imsi
    public static String getPhoneImeiID( Context context ) {
        String imeiString = "";
        TelephonyManager telephonyManager;
        telephonyManager = ( TelephonyManager ) context.getSystemService( Context.TELEPHONY_SERVICE );
        imeiString = telephonyManager.getDeviceId();
        return imeiString;
    }

    public static String getPhoneImsiID( Context context ) {
        String imsiString = "";
        TelephonyManager telephonyManager;
        telephonyManager = ( TelephonyManager ) context.getSystemService( Context.TELEPHONY_SERVICE );
        imsiString = telephonyManager.getSubscriberId();
        return imsiString;
    }

    /**
     * 取APP版本名
     *
     * @return
     */
    public static String getAppVersionName( Context context ) {
        try {
            PackageManager mPackageManager = context.getPackageManager();
            PackageInfo _info = mPackageManager.getPackageInfo( context.getPackageName(), 0 );
            return _info.versionName;
        } catch ( NameNotFoundException e ) {
            return null;
        }
    }

    public static String getLocalUserToken( Context context ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences( "user", Context.MODE_PRIVATE );
        String userBase64 = sharedPreferences.getString("user", "");
        String user_token = "";
        if (!TextUtils.isEmpty(userBase64)) {
            try {
                JSONObject data = new JSONObject(new String( Base64.decode(userBase64, Base64.DEFAULT)));
                user_token = data.getString( "user_token" );
            } catch (JSONException e) {
                user_token = "";
            }
        }
        return user_token;
    }
}
