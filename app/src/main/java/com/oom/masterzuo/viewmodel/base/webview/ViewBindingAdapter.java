package com.oom.masterzuo.viewmodel.base.webview;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by YoungDroid on 2016/8/21.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {
    
    @BindingAdapter(value = { "webUrl", "loadFinishCommand", "webViewClientCommand", "loadInside" }, requireAll = false)
    public static void loadFinishCommand(
            WebView webView,
            String webUrl,
            ReplyCommand loadFinishCommand,
            ReplyCommand< String > webViewClientCommand,
            boolean loadInside ) {
        webView.loadUrl( webUrl );
        
        webView.getSettings().setJavaScriptEnabled( true );
        webView.getSettings().setBuiltInZoomControls( false );
        webView.getSettings().setBlockNetworkImage( true );
        //设置 缓存模式
        webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        webView.getSettings().setDomStorageEnabled( true );
        webView.getSettings().setDatabaseEnabled( true );
        webView.getSettings().setDisplayZoomControls( false );
        
        webView.setWebViewClient( new WebViewClient() {
            @Override
            public void onPageFinished( WebView view, String url ) {
                super.onPageFinished( view, url );
                // 释放阻塞的图片请求
                view.getSettings().setBlockNetworkImage( false );
                if ( loadFinishCommand != null ) loadFinishCommand.execute();
            }
    
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, String url ) {
                if ( loadInside ) {
                    if ( null != webViewClientCommand ) webViewClientCommand.execute( url );
                }
                else
                    view.getContext().startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
                return true;
            }
    
            @RequiresApi(api = VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, WebResourceRequest request ) {
                if ( loadInside ) {
                    if ( null != webViewClientCommand ) webViewClientCommand.execute( String.valueOf( request.getUrl() ) );
                }
                else
                    view.getContext().startActivity( new Intent( Intent.ACTION_VIEW, request.getUrl() ) );
                return true;
            }
        } );
    }

    @BindingAdapter(value = { "imageUrl", "loadImageFinish" }, requireAll = false)
    public static void loadBigImage(
            WebView webView,
            String webUrl,
            ReplyCommand<Integer> loadFinishCommand ) {
        webView.loadUrl( webUrl );

        webView.getSettings().setJavaScriptEnabled( true );
        webView.getSettings().setBuiltInZoomControls( false );
        webView.getSettings().setBlockNetworkImage( true );
        //设置 缓存模式
        webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        webView.getSettings().setDomStorageEnabled( true );
        webView.getSettings().setDatabaseEnabled( true );
        webView.getSettings().setDisplayZoomControls( false );
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort( true );
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient( new WebViewClient() {
            @Override
            public void onPageFinished( WebView view, String url ) {
                super.onPageFinished( view, url );
                // 释放阻塞的图片请求
                view.getSettings().setBlockNetworkImage( false );
                if ( loadFinishCommand != null ) loadFinishCommand.execute( 0 );
            }
        } );
    }
}
