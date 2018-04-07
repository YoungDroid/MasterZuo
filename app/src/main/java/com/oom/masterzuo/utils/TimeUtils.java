package com.oom.masterzuo.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

/**
 * Created by xiaojunhuang on 16/3/15.
 */
public class TimeUtils {

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( Debug ) {
            Log.e( "TimeUtils", "isFastClick: "+ lastClickTime + "\t" + time + "\t" + (time - lastClickTime) );
        }
        if ( time - lastClickTime < 100) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static long lastActionTime;
    public synchronized static boolean isFastAction(long duration) {
        long time = System.currentTimeMillis();
        if ( Debug ) {
            Log.e( "TimeUtils", "isFastAction: "+ lastActionTime + "\t" + time + "\t" + (time - lastActionTime) );
        }
        if ( time - lastActionTime < duration) {
            return true;
        }
        lastActionTime = time;
        return false;
    }

    public static String timeLongToString(long time, String type) {
        Date d = new Date(time * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        return simpleDateFormat.format(d);
    }

    public static String getLocalTime() {
        return String.valueOf( System.currentTimeMillis() / 1000 );
    }
}
