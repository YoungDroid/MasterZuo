package com.oom.masterzuo.widget.specialscrollview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by CcYang on 2016/7/26.
 * Email: 1367496366@qq.com
 */
public class SpecialNestedScrollView extends NestedScrollView {

    private int downX;
    private int downY;
    private int mTouchSlop;

    public SpecialNestedScrollView( Context context ) {
        super( context );
        mTouchSlop = ViewConfiguration.get( context ).getScaledTouchSlop();
    }

    public SpecialNestedScrollView( Context context, AttributeSet attrs ) {
        super( context, attrs );
        mTouchSlop = ViewConfiguration.get( context ).getScaledTouchSlop();
    }

    public SpecialNestedScrollView( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        mTouchSlop = ViewConfiguration.get( context ).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent( MotionEvent e ) {
        int action = e.getAction();
        switch ( action ) {
            case MotionEvent.ACTION_DOWN:
                downX = ( int ) e.getRawX();
                downY = ( int ) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = ( int ) e.getRawY();
                if ( Math.abs( moveY - downY ) > mTouchSlop ) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent( e );
    }
}
