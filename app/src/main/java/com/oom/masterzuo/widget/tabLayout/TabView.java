package com.oom.masterzuo.widget.tabLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorInflater;
import com.oom.masterzuo.R;

/**
 * Created by YoungDroid on 2017/5/5.
 * Email: YoungDroid@163.com
 */

public class TabView extends View {

    private int zIndex = -1;
    private int xIndex = -1;
    private Drawable drawableTop;
    private Drawable drawableMid;
    private Drawable drawableBottom;

    private int viewWidthMax, viewHeightMax;
    private int viewWidthMin, viewHeightMin;

    public TabView( Context context ) {
        this( context, null );
    }

    public TabView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    public TabView( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );

        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.TabLayout, defStyleAttr, 0 );
        int n = array.getIndexCount();
        for ( int i = 0; i < n; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.TabLayout_tab_z_index:
                    zIndex = array.getInt( attr, 0 );
                    break;
                case R.styleable.TabLayout_tab_x_index:
                    xIndex = array.getInt( attr, 1 );
                    break;
                case R.styleable.TabLayout_tab_top_drawable:
                    drawableTop = array.getDrawable( attr );
                    initWidthAndHeight( drawableTop );
                    break;
                case R.styleable.TabLayout_tab_mid_drawable:
                    drawableMid = array.getDrawable( attr );
                    initWidthAndHeight( drawableMid );
                    break;
                case R.styleable.TabLayout_tab_bottom_drawable:
                    drawableBottom = array.getDrawable( attr );
                    initWidthAndHeight( drawableBottom );
                    break;
            }
        }
        array.recycle();

    }

    private void initWidthAndHeight(Drawable drawable) {
        if ( null != drawable ) {
            if ( drawable.getIntrinsicWidth() > viewWidthMax )
                viewWidthMax = drawable.getIntrinsicWidth();
            if ( drawable.getIntrinsicHeight() > viewHeightMax )
                viewHeightMax = drawable.getIntrinsicHeight();
            if ( drawable.getIntrinsicWidth() < viewWidthMin )
                viewWidthMin = drawable.getIntrinsicWidth();
            if ( drawable.getIntrinsicHeight() < viewHeightMin )
                viewHeightMin = drawable.getIntrinsicHeight();
        }
    }

    public void changeToNextState( int newState ) {
        if ( zIndex == newState )
            return;
        int oldState = zIndex;
        zIndex = newState;
        postInvalidate();
        requestLayout();
        switch ( oldState ) {
            case 0:
                small();
                break;
            case 1:
                if ( newState == 0 ) {
                    big();
                }
                break;
            case 2:
                if ( newState == 0 ) {
                    big();
                }
                break;
        }
    }

    private void big() {
        // 加载动画
        Animator anim = AnimatorInflater.loadAnimator( getContext(), R.anim.scale_large );
        switch ( xIndex ) {
            case 0:
                setPivotX( 0 );
                break;
            case 1:
                setPivotX( viewWidthMax / 2 );
                break;
            case 2:
                setPivotX( viewWidthMax );
                break;
        }
        setPivotY( viewHeightMax );
        //显示的调用invalidate
        anim.setTarget( this );
        anim.start();
    }

    private void small() {
        // 加载动画
        Animator anim = AnimatorInflater.loadAnimator( getContext(), R.anim.scale_small );
        switch ( xIndex ) {
            case 0:
                setPivotX( 0 );
                break;
            case 1:
                setPivotX( viewWidthMax / 2 );
                break;
            case 2:
                setPivotX( viewWidthMax );
                break;
        }
        this.setPivotY( viewHeightMin );
        //显示的调用invalidate
        anim.setTarget( this );
        anim.start();
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {

        int width = 0, height = 0;

        int widthMode = MeasureSpec.getMode( widthMeasureSpec );
        int widthSize = MeasureSpec.getSize( widthMeasureSpec );
        int heightMode = MeasureSpec.getMode( heightMeasureSpec );
        int heightSize = MeasureSpec.getSize( heightMeasureSpec );

        /**
         * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
         * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
         * UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        if ( widthMode == MeasureSpec.EXACTLY ) {
            width = widthSize;
        } else {
            int desireWidth = getPaddingLeft() + getPaddingRight();
            switch ( zIndex ) {
                case 0:
                    desireWidth += drawableTop.getIntrinsicWidth();
                    break;
                case 1:
                    desireWidth += drawableMid.getIntrinsicWidth();
                    break;
                case 2:
                    desireWidth += drawableBottom.getIntrinsicWidth();
                    break;
            }
            if ( widthMode == MeasureSpec.AT_MOST ) {
                width = Math.min( desireWidth, widthSize );
            }
        }

        if ( heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            int desireHeight = getPaddingTop() + getPaddingBottom();
            switch ( zIndex ) {
                case 0:
                    desireHeight += drawableTop.getIntrinsicHeight();
                    break;
                case 1:
                    desireHeight += drawableMid.getIntrinsicHeight();
                    break;
                case 2:
                    desireHeight += drawableBottom.getIntrinsicHeight();
                    break;
            }
            if ( heightMode == MeasureSpec.AT_MOST ) {
                height = Math.min( desireHeight, heightSize );
            }
        }

        setMeasuredDimension( width, height );
    }

    @Override
    public void draw( Canvas canvas ) {
        super.draw( canvas );
        switch ( zIndex ) {
            case 0:
                if ( null != drawableTop ) {
                    Rect drawableRect = new Rect( 0, 0, drawableTop.getIntrinsicWidth(), drawableTop.getIntrinsicHeight() );
                    drawableTop.setBounds( drawableRect );
                    drawableTop.draw( canvas );
                }
                break;
            case 1:
                if ( null != drawableMid ) {
                    Rect drawableRect = new Rect( 0, 0, drawableMid.getIntrinsicWidth(), drawableMid.getIntrinsicHeight() );
                    drawableMid.setBounds( drawableRect );
                    drawableMid.draw( canvas );
                }
                break;
            case 2:
                if ( null != drawableBottom ) {
                    Rect drawableRect = new Rect( 0, 0, drawableBottom.getIntrinsicWidth(), drawableBottom.getIntrinsicHeight() );
                    drawableBottom.setBounds( drawableRect );
                    drawableBottom.draw( canvas );
                }
                break;
        }
    }
}
