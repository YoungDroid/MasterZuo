package com.oom.masterzuo.widget.tabLayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by YoungDroid on 2017/5/5.
 * Email: YoungDroid@163.com
 */

public class TabLayout extends RelativeLayout {
    
    private TabView tabRecommend;
    private TabView tabFlash;
    private TabView tabFollow;

    private ViewPager viewPager;

    public TabLayout( Context context ) {
        this( context, null );
    }
    
    public TabLayout( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }
    
    public TabLayout( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }

    public void setViewPager( ViewPager viewPager ) {
        this.viewPager = viewPager;

        viewPager.addOnPageChangeListener( new OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {

            }

            @Override
            public void onPageSelected( int position ) {
                switch ( position ) {
                    case 0:
                        toRecommend();
                        break;
                    case 1:
                        toFlash();
                        break;
                    case 2:
                        toFollow();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged( int state ) {

            }
        } );
    }

    @Override
    protected void onFinishInflate() {
        init();
    }

    private void init() {

        if ( getChildCount() == 3 ) {
            tabRecommend = ( TabView ) getChildAt( 2 );
            tabFlash = ( TabView ) getChildAt( 1 );
            tabFollow = ( TabView ) getChildAt( 0 );

            tabRecommend.setOnClickListener( v -> {

                toRecommend();
            } );

            tabFlash.setOnClickListener( v -> {

                toFlash();
            } );

            tabFollow.setOnClickListener( v -> {

                toFollow();
            } );
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity( Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("tabView must be 3");
            removeAllViews();
            addView(errorView);
        }
    }

    private void toFollow() {
        removeAllViews();
        addView( tabRecommend );
        addView( tabFlash );
        addView( tabFollow );

        tabRecommend.changeToNextState( 2 );
        tabFlash.changeToNextState( 1 );
        tabFollow.changeToNextState( 0 );

        if ( null != viewPager ) {
            viewPager.setCurrentItem( 2, true );
        }
    }

    private void toFlash() {
        removeAllViews();
        addView( tabRecommend );
        addView( tabFollow );
        addView( tabFlash );

        tabRecommend.changeToNextState( 1 );
        tabFlash.changeToNextState( 0 );
        tabFollow.changeToNextState( 1 );

        if ( null != viewPager ) {
            viewPager.setCurrentItem( 1, true );
        }
    }

    private void toRecommend() {
        removeAllViews();
        addView( tabFollow );
        addView( tabFlash );
        addView( tabRecommend );

        tabRecommend.changeToNextState( 0 );
        tabFlash.changeToNextState( 1 );
        tabFollow.changeToNextState( 2 );

        if ( null != viewPager ) {
            viewPager.setCurrentItem( 0, true );
        }
    }
}
