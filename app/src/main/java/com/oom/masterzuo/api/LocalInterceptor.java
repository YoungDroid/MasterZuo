package com.oom.masterzuo.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LocalInterceptor implements Interceptor {
    
    private String deviceID;
    private String userToken;
    private String appVersion;
    
    public LocalInterceptor( String deviceID, String userToken, String appVersion ) {
        this.deviceID = deviceID;
        this.userToken = userToken;
        this.appVersion = appVersion;
    }
    
    @Override
    public Response intercept( Chain chain ) throws IOException {
        final HttpUrl modifiedUrl =
                chain.request().url().newBuilder().addQueryParameter( "facility", deviceID ).addQueryParameter( "User_token", userToken ).addQueryParameter( "version", appVersion ).build();
        Request request = chain.request().newBuilder().url( modifiedUrl ).build();
        
        return chain.proceed( request );
    }
}
