package com.oom.masterzuo.api;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oom.masterzuo.utils.FileUtils;
import com.oom.masterzuo.utils.NetUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

public class ApiManager {
    
    // 文章 & 测试
    public static final String URL = "";//正式服地址
    public static final String URL_STAGING = "";//测试服地址
    
    // 赛事 & 测试
    public static final String MATCH_URL = "";
    public static final String MATCH_URL_STAGING = "";
    
    // 用户中心 & 测试
    public static final String PASSPORT_URL = "";
    public static final String PASSPORT_URL_STAGING = "";
    
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private static ObjectMapper objectMapper;
    
    public static void init( Context context ) {
        objectMapper = new ObjectMapper();
        objectMapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        objectMapper.disable( DeserializationFeature.FAIL_ON_INVALID_SUBTYPE );
        objectMapper.disable( DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES );
        objectMapper.disable( DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS );
        objectMapper.disable( DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES );
        objectMapper.disable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES );
        objectMapper.enable( DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY );
        objectMapper.enable( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY );
        objectMapper.enable( DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT );
        objectMapper.enable( DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT );
        objectMapper.enable( DeserializationFeature.WRAP_EXCEPTIONS );
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel( Debug ? HttpLoggingInterceptor.Level.BODY : Level.NONE );
        
        Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
            Response originalResponse = chain.proceed( chain.request() );
            String cacheControl = originalResponse.header( "Cache-Control" );
            if ( cacheControl == null || cacheControl.contains( "no-store" ) || cacheControl.contains( "no-cache" ) ||
                 cacheControl.contains( "must-revalidate" ) || cacheControl.contains( "max-age=0" ) ) {
                return originalResponse.newBuilder().header( "Cache-Control", "public, max-age=" + 60 ).removeHeader( "Pragma" ).build();
            } else {
                return originalResponse;
            }
        };
        
        Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = chain -> {
            Request request = chain.request();
            if ( !NetUtil.isNetworkAvailable( context ) ) {
                if ( Debug ) {
                    Log.e( "YoungDroid - ApiManager", "init: 当前无网络!" );
                }
                // tolerate 4-weeks stale
                int maxStale = 60 * 60 * 24 * 7 * 4;
                request = request.newBuilder().header( "Cache-Control", "public, only-if-cached" + maxStale ).removeHeader( "Pragma" ).build();
            }
            return chain.proceed( request );
        };
        
        okHttpClient =
                new OkHttpClient.Builder().readTimeout( 10, TimeUnit.SECONDS ).writeTimeout( 10, TimeUnit.SECONDS ).connectTimeout( 10, TimeUnit.SECONDS ).addNetworkInterceptor( REWRITE_RESPONSE_INTERCEPTOR ).addInterceptor( REWRITE_RESPONSE_INTERCEPTOR_OFFLINE ).addInterceptor( interceptor ).cache( new Cache( FileUtils.CACHEFILE,
                        1024 * 1024 * 100 ) ).build();
    }
    
    public static void addInterceptor( Interceptor interceptor ) {
        if ( okHttpClient == null ) {
            okHttpClient = new OkHttpClient.Builder().addInterceptor( interceptor ).build();
        } else {
            okHttpClient = okHttpClient.newBuilder().addInterceptor( interceptor ).build();
        }
    }
    
    public static < T > T getClient( Class< T > t ) {
        
        retrofit =
                new Retrofit.Builder().baseUrl( Debug ? URL_STAGING : URL ).client( okHttpClient ).addConverterFactory( JacksonConverterFactory.create( objectMapper ) ).build();
        return retrofit.create( t );
    }
    
    public static < T > T getClient( Class< T > t, String url ) {
        
        retrofit =
                new Retrofit.Builder().baseUrl( url ).client( okHttpClient ).addConverterFactory( JacksonConverterFactory.create( objectMapper ) ).build();
        return retrofit.create( t );
    }
    
    public static < T > T getApiMatchClient( Class< T > t ) {
        return getClient( t, Debug ? MATCH_URL_STAGING : MATCH_URL );
    }
    
    public static < T > T getApiPassportClient( Class< T > t ) {
        retrofit =
                new Retrofit.Builder().baseUrl( Debug ? PASSPORT_URL_STAGING : PASSPORT_URL ).client( okHttpClient ).addConverterFactory( GsonConverterFactory.create() ).addCallAdapterFactory( RxJavaCallAdapterFactory.create() ).build();
        return retrofit.create( t );
    }
}
