package com.oom.masterzuo.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.oom.masterzuo.R;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CcYang on 2016/4/8.
 */
public class StringUtils {
    public static boolean isBlank( String str ) {
        int strLen;
        if ( str == null || ( strLen = str.length() ) == 0 ) {
            return true;
        }
        for ( int i = 0; i < strLen; i++ ) {
            if ( ( Character.isWhitespace( str.charAt( i ) ) == false ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证邮箱
     * @param email 邮件地址
     * @return true: 合法邮件地址, false: 非法邮件地址
     */
    public static boolean isEmailUrl(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号
     * @param mobiles 手机号码
     * @return true: 合法手机号码, false: 非法手机号码
     */
    public static boolean isMobileNumber( String mobiles ) {
        Pattern p = Pattern.compile( "^((1[3-9][0-9]))\\d{8}$" );
        Matcher m = p.matcher( mobiles );
        return m.matches();
    }


    public static String getRandomString( int length ) {
        //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTXYZ0123456789";
        //生成字符串从此序列中取
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < length; i++ ) {
            int number = random.nextInt( base.length() );
            sb.append( base.charAt( number ) );
        }
        return sb.toString();
    }

    public static String ToDBC( String input ) {
        char[] c = input.toCharArray();
        for ( int i = 0; i < c.length; i++ ) {
            if ( c[ i ] == 12288 ) {
                c[ i ] = ( char ) 32;
                continue;
            }
            if ( c[ i ] > 65280 && c[ i ] < 65375 ) c[ i ] = ( char ) ( c[ i ] - 65248 );
        }
        return new String( c );
    }

    /**
     * 计算字符串的权值
     *
     * @return 返回权值
     */
    public static int calculateWeight( String str ) {
        int weight = 0;
        for ( int i = 0; i < str.length(); i++ ) {
            weight += str.charAt( i );
        }
        return weight;
    }

    public static String millisToString( long millis, boolean text ) {
        boolean negative = millis < 0;
        millis = Math.abs( millis );
        int mini_sec = ( int ) millis % 1000;
        millis /= 1000;
        int sec = ( int ) ( millis % 60 );
        millis /= 60;
        int min = ( int ) ( millis % 60 );
        millis /= 60;
        int hours = ( int ) millis;

        String time;
        DecimalFormat format = ( DecimalFormat ) NumberFormat.getInstance( Locale.US );
        format.applyPattern( "00" );

        DecimalFormat format2 = ( DecimalFormat ) NumberFormat.getInstance( Locale.US );
        format2.applyPattern( "0" );
        if ( text ) {
            if ( millis > 0 ) time = ( negative ? "-" : "" ) + hours + "h" + format.format( min ) + "min";
            else if ( min > 0 ) time = ( negative ? "-" : "" ) + min + "min";
            else time = ( negative ? "-" : "" ) + sec + "s";
        } else {
            if ( millis > 0 )
                time = ( negative ? "-" : "" ) + hours + ":" + format.format( min ) + ":" + format.format( sec )/* + ":" + format2.format(mini_sec)*/;
            else time = ( negative ? "-" : "" ) + min + ":" + format.format( sec )/* + ":" + format2.format(mini_sec)*/;
        }
        return time;
    }

    public static String getRandomLengthRandomString( int length ) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int randomLength = random.nextInt( length );
        for ( int i = 0; i < randomLength; ++i ) {
            int number = random.nextInt( 62 );//[0,62)
            sb.append( str.charAt( number ) );
        }
        return sb.toString();
    }

    public static int stringToNumber( String string ) {
        return string.equals( "" ) ? -1 : Integer.parseInt( string );
    }

    public static void copy( String content, Context context ) {
        // 得到剪贴板管理器
        ClipboardManager cmb = ( ClipboardManager ) context.getSystemService( Context.CLIPBOARD_SERVICE );
        ClipData clipData = ClipData.newPlainText( "text", content );
        cmb.setPrimaryClip( clipData );
    }

    public static String arrayListToString( ArrayList< String > arrayList ) {
        String str = "";
        int i = 0;
        for ( String string : arrayList ) {
            str += string;
            if ( i < arrayList.size() - 1 ) {
                str += ",";
            }
            i++;
        }
        return str;
    }

    public static Uri stringToURI( String uri ) {
        if ( uri == null || uri.equals( "" ) ) return Uri.parse( "res:///" + R.mipmap.icon_app_default );
        return Uri.parse( uri );
    }

    public static boolean isEmptyString( String string ) {
        return string == null || string.equals( "" );
    }

    public static boolean isStringLengthLegal( String string, int limitLength) {
        return string != null && string.length() >= limitLength;
    }

    public static boolean isZeroNumber(int number) {
        return number == 0;
    }

    public static boolean is2StringEquals(String a, String b) {
        return null != a && null != b && a.equals( b );
    }

    public static void reverseString(String s) {
        char[] charA = s.toCharArray();
        char[] charB = new char[s.length()];

        for( int i = 0; i < s.length(); i++ ) {
            charB[i] = charA[s.length() - 1 - i];
        }

        String x = String.valueOf( charB );
    }
}
