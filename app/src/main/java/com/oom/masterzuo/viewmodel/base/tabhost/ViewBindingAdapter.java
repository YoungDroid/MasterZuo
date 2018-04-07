package com.oom.masterzuo.viewmodel.base.tabhost;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager.OnPageChangedListener;

/**
 * Created by YoungDroid on 2016/8/26.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {
    
    @BindingAdapter( {
            "onPageChange"
    } )
    public static void initTabHost( RecyclerViewPager recyclerViewPager, 
                                    OnPageChangedListener onPageChangedListener ) {

        if ( recyclerViewPager == null || onPageChangedListener == null ) return;

        recyclerViewPager.addOnPageChangedListener( onPageChangedListener );

        recyclerViewPager.addOnScrollListener( new OnScrollListener() {
            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                int childCount = recyclerViewPager.getChildCount();
                int width = recyclerViewPager.getChildAt( 0 ).getWidth();
                int padding = ( recyclerViewPager.getWidth() - width ) / 2;
                for ( int j = 0; j < childCount; j++ ) {
                    View v = recyclerView.getChildAt( j );
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if ( v.getLeft() <= padding ) {
                        if ( v.getLeft() >= padding - v.getWidth() ) {
                            rate = ( padding - v.getLeft() ) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY( 1 - rate * 0.1f );
                        v.setScaleX( 1 - rate * 0.1f );
                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if ( v.getLeft() <= recyclerView.getWidth() - padding ) {
                            rate = ( recyclerView.getWidth() - padding - v.getLeft() ) * 1f / v.getWidth();
                        }
                        v.setScaleY( 0.9f + rate * 0.1f );
                        v.setScaleX( 0.9f + rate * 0.1f );
                    }
                }
            }
        } );

        recyclerViewPager.addOnLayoutChangeListener( ( v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ) -> {
            if ( recyclerViewPager.getChildCount() < 3 ) {
                if ( recyclerViewPager.getChildAt( 1 ) != null ) {
                    if ( recyclerViewPager.getCurrentPosition() == 0 ) {
                        View v1 = recyclerViewPager.getChildAt( 1 );
                        v1.setScaleY( 0.9f );
                        v1.setScaleX( 0.9f );
                    } else {
                        View v1 = recyclerViewPager.getChildAt( 0 );
                        v1.setScaleY( 0.9f );
                        v1.setScaleX( 0.9f );
                    }
                }
            } else {
                if ( recyclerViewPager.getChildAt( 0 ) != null ) {
                    View v0 = recyclerViewPager.getChildAt( 0 );
                    v0.setScaleY( 0.9f );
                    v0.setScaleX( 0.9f );
                }
                if ( recyclerViewPager.getChildAt( 2 ) != null ) {
                    View v2 = recyclerViewPager.getChildAt( 2 );
                    v2.setScaleY( 0.9f );
                    v2.setScaleX( 0.9f );
                }
            }
        } );
    }
}
