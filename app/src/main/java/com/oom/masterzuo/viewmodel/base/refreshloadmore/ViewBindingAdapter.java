package com.oom.masterzuo.viewmodel.base.refreshloadmore;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.oom.masterzuo.utils.DensityUtil;

import java.util.concurrent.TimeUnit;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class ViewBindingAdapter {
    
    @BindingAdapter(value = { "refresh", "autoComplete", "checkCanDoRefresh", "headerColor" }, requireAll = false)
    public static void refresh( PtrFrameLayout ptrFrameLayout, ReplyCommand refreshFinish, long autoComplete, ReplyCommand checkCanDoRefresh, String headerColor ) {
        StoreHouseHeader header = new StoreHouseHeader( ptrFrameLayout.getContext() );
        header.setBackgroundColor( Color.WHITE );
        if ( !TextUtils.isEmpty( headerColor ) )
            header.setBackgroundColor( Color.parseColor( headerColor ) );
        header.setTextColor( Color.BLACK );
        header.setPadding( 0, DensityUtil.dip2px( ptrFrameLayout.getContext(), 15 ), 0, DensityUtil.dip2px( ptrFrameLayout.getContext(), 15 ) );
        header.initWithString( "PENTAQ" );
        
        ptrFrameLayout.setHeaderView( header );
        ptrFrameLayout.addPtrUIHandler( header );
        
        ptrFrameLayout.setPtrHandler( new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh( PtrFrameLayout frame, View content, View header ) {
                if ( checkCanDoRefresh != null && PtrDefaultHandler.checkContentCanBePulledDown( frame, content, header ) ) {
                    checkCanDoRefresh.execute();
                }
                return PtrDefaultHandler.checkContentCanBePulledDown( frame, content, header );
            }
            
            @Override
            public void onRefreshBegin( PtrFrameLayout frame ) {
                if ( null != refreshFinish ) {
                    refreshFinish.execute();
                }
                if ( autoComplete != 0 && ptrFrameLayout.isRefreshing() ) {
                    Observable.timer( autoComplete, TimeUnit.MILLISECONDS ).subscribe( duration -> {
                        ptrFrameLayout.refreshComplete();
                    } );
                }
            }
        } );
    }
    
    @BindingAdapter(value = { "refresh", "autoComplete", "headerColor" }, requireAll = false)
    public static void refresh( SwipeRefreshLayout swipeRefreshLayout, ReplyCommand refreshStart, long autoComplete, String headerColor ) {
        swipeRefreshLayout.setOnRefreshListener( () -> {
            if ( null != refreshStart ) {
                refreshStart.execute();
            }
            if ( autoComplete > 500 ) {
                Log.e( TAG, "refresh1: " + System.currentTimeMillis() );
                Observable.interval( autoComplete, TimeUnit.MILLISECONDS ).take( 1 ).subscribeOn( Schedulers.io() ).unsubscribeOn( Schedulers.io() ).observeOn( AndroidSchedulers.mainThread() ).subscribe( duration -> {
                    swipeRefreshLayout.setRefreshing( false );
                    Log.e( TAG, "refresh2: " + System.currentTimeMillis() );
                } );
            }
        } );
        if ( !TextUtils.isEmpty( headerColor ) )
            swipeRefreshLayout.setColorSchemeColors( Color.parseColor( headerColor ) );
    }
}
