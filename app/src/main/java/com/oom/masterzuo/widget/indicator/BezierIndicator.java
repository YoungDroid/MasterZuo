package com.oom.masterzuo.widget.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.oom.masterzuo.R;
import com.oom.masterzuo.widget.indicator.BezierIndicator.Circle.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.ObserverSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by YoungDroid on 2017/1/21.
 * Email: YoungDroid@163.com
 */

public class BezierIndicator extends View {
    
    public static final String TAG = "YoungDroid";
    
    private static final int STATUS_INIT = 10000;
    private static final int STATUS_INIT_END = 10001;
    private static final int STATUS_MOVE = 20000;
    private static final int STATUS_MOVE_END = 20001;
    private static final int STATUS_MOVE_COMPLETE = 20002;
    
    private List< Circle > circles;
    
    private Circle circleFrom;
    private Circle circleMove;
    private Circle circleTo;
    private Circle circleBezier;
    
    private Point assistPoint;
    
    private int mCircleCount;
    private int mRadius;
    private int mInterval;
    private int mStrokeWidth;
    private int mNormalColor;
    private int mSelectedColor;
    private int mAnimationColor;
    
    private boolean mMoveHalf;
    
    private Paint mPaint;
    private Path mPath;
    
    private int mStatus;
    
    private int mMoveIndex = 0;
    
    private ValueAnimator mMoveAnimator;
    private long mAnimationDuration = 300;
    private ViewPager mViewPager;
    
    private long autoScrollDuration = 0;
    private Observable<Long> observableAutoScroll;
    private Subscriber<Long> subscriberAutoScroll;
    
    public BezierIndicator( Context context ) {
        this( context, null );
    }
    
    public BezierIndicator( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }
    
    public BezierIndicator( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        
        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.Indicator, defStyleAttr, 0 );
        int n = array.getIndexCount();
        for ( int i = 0; i < n; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.Indicator_indicator_count:
                    mCircleCount = array.getInt( attr, 1 );
                    break;
                case R.styleable.Indicator_indicator_radius:
                    mRadius = array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.Indicator_indicator_interval:
                    mInterval = array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.Indicator_indicator_strokeWidth:
                    mStrokeWidth = array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.Indicator_indicator_normalColor:
                    mNormalColor = array.getColor( attr, Color.WHITE );
                    break;
                case R.styleable.Indicator_indicator_selectedColor:
                    mSelectedColor = array.getColor( attr, Color.BLACK );
                    break;
            }
        }
        array.recycle();
        
        init();
    }
    
    private void init() {
        circles = new ArrayList<>();
        for ( int i = 0; i < mCircleCount; i++ ) {
            Circle circle = new Circle( mRadius - mStrokeWidth );
            circles.add( circle );
        }
        circleFrom = new Circle( mRadius - mStrokeWidth );
        circleMove = new Circle( mRadius - mStrokeWidth );
        circleTo = new Circle( mRadius - mStrokeWidth );
        circleBezier = new Circle( mRadius - mStrokeWidth );
        assistPoint = new Point();
        mPaint = new Paint();
        mPath = new Path();
        
        mMoveAnimator = new ValueAnimator();
        
        mStatus = STATUS_INIT;
    
    
        observableAutoScroll = Observable.interval( 1000, TimeUnit.MILLISECONDS )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() );
        subscriberAutoScroll = new Subscriber< Long >() {
            @Override
            public void onCompleted() {
        
            }
    
            @Override
            public void onError( Throwable throwable ) {
        
            }
    
            @Override
            public void onNext( Long aLong ) {
                toNext();
            }
        };
        if ( 0 < autoScrollDuration ) {
            if ( !subscriberAutoScroll.isUnsubscribed() ) {
                subscriberAutoScroll.unsubscribe();
            }
            observableAutoScroll.subscribe( subscriberAutoScroll );
        }
    }
    
    public void setCount( int count, long duration ) {
        Log.e( TAG, "setCount: reInit" );
        this.mCircleCount = count;
        this.autoScrollDuration = duration;
        init();
        requestLayout();
    }
    
    public void setViewPager( ViewPager viewPager ) {
        this.mViewPager = viewPager;
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
            int desireWidth = ( 2 * mRadius ) * mCircleCount + ( mCircleCount <= 0 ? 0 : mCircleCount - 1 ) * mInterval + getPaddingLeft() + getPaddingRight();
            if ( widthMode == MeasureSpec.AT_MOST ) {
                width = Math.min( desireWidth, widthSize );
            }
        }
        
        if ( heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            int desireHeight = 2 * mRadius + getPaddingTop() + getPaddingBottom();
            if ( heightMode == MeasureSpec.AT_MOST ) {
                height = Math.min( desireHeight, heightSize );
            }
        }
        
        setMeasuredDimension( width, height );
    }
    
    @Override
    protected void onDraw( Canvas canvas ) {
        
        float circleY = getHeight() / 2;
        mPaint.setStrokeWidth( mStrokeWidth );
        mPaint.setStyle( Style.FILL_AND_STROKE );
        mPaint.setAntiAlias( true );
        
        for ( int i = 0; i < mCircleCount; i++ ) {
            float circleX = getPaddingLeft() + i * ( 2 * mRadius + mInterval ) + mRadius;
            Circle circle = circles.get( i );
            circle.setCenter( circleX, circleY );
            mPaint.setColor( mNormalColor );
            paintCircle( canvas, circle, mPaint );
        }
        mPaint.setColor( mSelectedColor );
        if ( mStatus == STATUS_INIT ) {
            mStatus = STATUS_INIT_END;
            circleMove.radius = mRadius;
            circleMove.setCenter( getPaddingLeft() + mRadius, circleY );
            mPaint.setColor( mSelectedColor );
        } else if ( mStatus == STATUS_MOVE ) {
            mPaint.setColor( mSelectedColor );
            if ( !mMoveHalf ) {
                paintCircle( canvas, circleFrom, mPaint );
            } else {
                mPaint.setColor( mNormalColor );
                paintCircle( canvas, circleFrom, mPaint );
                mPaint.setColor( mSelectedColor );
                paintCircle( canvas, circleTo, mPaint );
            }
            paintBezier( canvas, circleBezier, circleMove );
        } else if ( mStatus == STATUS_MOVE_COMPLETE ) {
            circleMove.radius = mRadius;
            mPaint.setColor( mSelectedColor );
        }
        
        paintCircle( canvas, circleMove, mPaint );
    }
    
    private void paintBezier( Canvas canvas, Circle circleStart, Circle circleEnd ) {
        
        mPath.reset();
        //起点
        mPath.moveTo( circleStart.top.x, circleStart.top.y );
        assistPoint.set( ( circleEnd.center.x + circleStart.center.x ) / 2, circleEnd.center.y );
        //mPath
        mPath.quadTo( assistPoint.x, assistPoint.y, circleEnd.top.x, circleEnd.top.y );
        mPath.lineTo( circleEnd.bottom.x, circleEnd.bottom.y );
        mPath.quadTo( assistPoint.x, assistPoint.y, circleStart.bottom.x, circleStart.bottom.y );
        mPath.close();
        //画path
        canvas.drawPath( mPath, mPaint );
    }
    
    private void paintCircle( Canvas canvas, Circle circle, Paint paint ) {
        canvas.drawCircle( circle.center.x, circle.center.y, circle.radius, paint );
    }
    
    public void toPrevious() {
        if ( mMoveIndex <= 0 ) {
            mMoveIndex = 0;
            return;
        }
        if ( mStatus == STATUS_MOVE ) {
            if ( mMoveAnimator.isRunning() ) mMoveAnimator.cancel();
        }
        circleFrom = circles.get( mMoveIndex );
        circleTo = circles.get( mMoveIndex - 1 );
        circleBezier = circleFrom;
        mMoveIndex--;
        moveAnimation( circleFrom.center, circleTo.center );
    }
    
    public void toNext() {
        if ( mStatus == STATUS_MOVE ) {
            if ( mMoveAnimator.isRunning() ) mMoveAnimator.cancel();
        }
        if ( mMoveIndex > mCircleCount - 1 ) {
            mMoveIndex = 0;
            circleFrom = circles.get( mMoveIndex );
            circleTo = circles.get( mMoveIndex );
            circleBezier = circleFrom;
            moveAnimation( circleFrom.center, circleTo.center );
        } else {
            circleFrom = circles.get( mMoveIndex );
            circleTo = circles.get( mMoveIndex + 1 );
            circleBezier = circleFrom;
            mMoveIndex++;
            moveAnimation( circleFrom.center, circleTo.center );
        }
    }
    
    private void moveAnimation( Point startPoint, Point endPoint ) {
        mMoveAnimator = ValueAnimator.ofObject( new PointEvaluator(), startPoint, endPoint );
        mMoveAnimator.addUpdateListener( animation -> {
            circleMove.setCenter( ( Point ) animation.getAnimatedValue() );
            if ( Math.abs( circleMove.center.x - circleFrom.center.x ) >= mRadius + mInterval / 2 ) {
                mMoveHalf = true;
                circleBezier = circleTo;
                circleMove.radius = ( 1 - Math.abs( circleMove.center.x - circleFrom.center.x ) / Math.abs( circleFrom.center.x - circleTo.center.x ) ) * ( mRadius / 2 ) + mRadius / 2;
            } else {
                mMoveHalf = false;
                circleMove.radius = Math.abs( circleMove.center.x - circleFrom.center.x ) / Math.abs( circleFrom.center.x - circleTo.center.x ) * ( mRadius / 2 ) + mRadius / 2;
            }
            invalidate();
        } );
        mMoveAnimator.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel( Animator animation ) {
                mStatus = STATUS_MOVE_END;
            }
            
            @Override
            public void onAnimationEnd( Animator animation ) {
                mStatus = STATUS_MOVE_COMPLETE;
            }
            
            @Override
            public void onAnimationStart( Animator animation ) {
                mStatus = STATUS_MOVE;
                if ( mViewPager != null ) {
                    mViewPager.setCurrentItem( mMoveIndex, true );
                }
            }
        } );
        ValueAnimator colorAnimator = ValueAnimator.ofObject( new ColorEvaluator(), "#c5a598", "#e7e7e7", "#c5a598" );
        colorAnimator.addUpdateListener( animation -> {
            mAnimationColor = Color.parseColor( ( String ) animation.getAnimatedValue() );
        } );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator( new LinearInterpolator() );
        animatorSet.play( mMoveAnimator ).with( colorAnimator );
        animatorSet.setDuration( mAnimationDuration );
        animatorSet.start();
    }
    
    public void offset( float offsetX ) {
        circleMove = circles.get( mMoveIndex );
        circleBezier = circleFrom;
        circleMove.setCenter( circleMove.center.x + offsetX, circleMove.center.y );
        invalidate();
    }
    
    public void changeIndex( int newIndex ) {
        if ( newIndex < mMoveIndex ) {
            toPrevious();
        } else if ( newIndex > mMoveIndex ) {
            toNext();
        }
    }
    
    public static class Circle {
        
        float radius;
        
        Point center;
        Point left;
        Point top;
        Point right;
        Point bottom;
        
        public Circle( float radius ) {
            this.radius = radius;
            
            center = new Point();
            left = new Point();
            top = new Point();
            right = new Point();
            bottom = new Point();
        }
        
        public void setCenter( float x, float y ) {
            center.set( x, y );
            left.set( ( x - radius ), y );
            top.set( x, ( y - radius ) );
            right.set( ( x + radius ), y );
            bottom.set( x, ( y + radius ) );
        }
        
        public void setCenter( Point point ) {
            setCenter( point.x, point.y );
        }
        
        public static class Point {
            
            private float x;
            private float y;
            
            public Point() {
                x = 0;
                y = 0;
            }
            
            public Point( float x, float y ) {
                this.x = x;
                this.y = y;
            }
            
            public void set( float x, float y ) {
                this.x = x;
                this.y = y;
            }
            
            public float getX() {
                return x;
            }
            
            public float getY() {
                return y;
            }
            
        }
    }
    
    public class PointEvaluator implements TypeEvaluator {
        
        @Override
        public Object evaluate( float fraction, Object startValue, Object endValue ) {
            Point startPoint = ( Point ) startValue;
            Point endPoint = ( Point ) endValue;
            float x = startPoint.x + fraction * ( endPoint.x - startPoint.x );
            float y = startPoint.y + fraction * ( endPoint.y - startPoint.y );
            return new Point( x, y );
        }
    }
    
    public class ColorEvaluator implements TypeEvaluator {
        
        private int mCurrentRed = -1;
        private int mCurrentGreen = -1;
        private int mCurrentBlue = -1;
        
        @Override
        public Object evaluate( float fraction, Object startValue, Object endValue ) {
            String startColor = ( String ) startValue;
            String endColor = ( String ) endValue;
            int startRed = Integer.parseInt( startColor.substring( 1, 3 ), 16 );
            int startGreen = Integer.parseInt( startColor.substring( 3, 5 ), 16 );
            int startBlue = Integer.parseInt( startColor.substring( 5, 7 ), 16 );
            int endRed = Integer.parseInt( endColor.substring( 1, 3 ), 16 );
            int endGreen = Integer.parseInt( endColor.substring( 3, 5 ), 16 );
            int endBlue = Integer.parseInt( endColor.substring( 5, 7 ), 16 );
            // 初始化颜色的值
            if ( mCurrentRed == -1 ) {
                mCurrentRed = startRed;
            }
            if ( mCurrentGreen == -1 ) {
                mCurrentGreen = startGreen;
            }
            if ( mCurrentBlue == -1 ) {
                mCurrentBlue = startBlue;
            }
            // 计算初始颜色和结束颜色之间的差值
            int redDiff = Math.abs( startRed - endRed );
            int greenDiff = Math.abs( startGreen - endGreen );
            int blueDiff = Math.abs( startBlue - endBlue );
            int colorDiff = redDiff + greenDiff + blueDiff;
            if ( mCurrentRed != endRed ) {
                mCurrentRed = getCurrentColor( startRed, endRed, colorDiff, 0, fraction );
            } else if ( mCurrentGreen != endGreen ) {
                mCurrentGreen = getCurrentColor( startGreen, endGreen, colorDiff, redDiff, fraction );
            } else if ( mCurrentBlue != endBlue ) {
                mCurrentBlue = getCurrentColor( startBlue, endBlue, colorDiff, redDiff + greenDiff, fraction );
            }
            // 将计算出的当前颜色的值组装返回
            String currentColor = "#" + getHexString( mCurrentRed ) + getHexString( mCurrentGreen ) + getHexString( mCurrentBlue );
            return currentColor;
        }
        
        /**
         * 根据fraction值来计算当前的颜色。
         */
        private int getCurrentColor( int startColor, int endColor, int colorDiff, int offset, float fraction ) {
            int currentColor;
            if ( startColor > endColor ) {
                currentColor = ( int ) ( startColor - ( fraction * colorDiff - offset ) );
                if ( currentColor < endColor ) {
                    currentColor = endColor;
                }
            } else {
                currentColor = ( int ) ( startColor + ( fraction * colorDiff - offset ) );
                if ( currentColor > endColor ) {
                    currentColor = endColor;
                }
            }
            return currentColor;
        }
        
        /**
         * 将10进制颜色值转换成16进制。
         */
        private String getHexString( int value ) {
            String hexString = Integer.toHexString( value );
            if ( hexString.length() == 1 ) {
                hexString = "0" + hexString;
            }
            return hexString;
        }
    }
    
    @BindingAdapter( { "viewPager" })
    public static void setViewPager( BezierIndicator bezierIndicator, ViewPager viewPager ) {
        if ( null != bezierIndicator ) {
            if ( null != viewPager ) {
                bezierIndicator.setViewPager( viewPager );
                if ( null != viewPager.getAdapter() ) {
                    bezierIndicator.setCount( viewPager.getAdapter().getCount(), 0 );
                } else {
                    viewPager.addOnAdapterChangeListener( ( viewPager1, oldAdapter, newAdapter ) -> {
                        bezierIndicator.setViewPager( viewPager1 );
                        if ( newAdapter != null ) {
                            bezierIndicator.setCount( newAdapter.getCount(), 0 );
                        }
                    } );
                }
                viewPager.addOnPageChangeListener( new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {
                    }
                    
                    @Override
                    public void onPageSelected( int position ) {
                        bezierIndicator.changeIndex( position );
                    }
                    
                    @Override
                    public void onPageScrollStateChanged( int state ) {}
                } );
            }
        }
    }
}
