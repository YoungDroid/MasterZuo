package com.oom.masterzuo.widget.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
import com.oom.masterzuo.widget.indicator.ImageIndicator.IndicatorItem.Point;

import java.util.ArrayList;
import java.util.List;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

/**
 * Created by YoungDroid on 2017/9/21.
 * Email: YoungDroid@163.com
 */

public class ImageIndicator extends View {
    
    public static final String TAG = "YoungDroid";
    
    private static final int STATUS_INIT = 10000;
    private static final int STATUS_INIT_END = 10001;
    private static final int STATUS_MOVE = 20000;
    private static final int STATUS_MOVE_END = 20001;
    private static final int STATUS_MOVE_COMPLETE = 20002;
    
    private List< IndicatorItem > indicatorItems;
    private int[] indicatorImages;
    private Resources resources;
    
    private IndicatorItem itemFrom;
    private IndicatorItem itemMove;
    private IndicatorItem itemTo;
    
    private int mItemCount;
    private int mItemSize;
    private int mItemSelectedSize;
    private float mItemAlpha;
    private float mItemSelectedAlpha;
    private int mInterval;
    
    private boolean mMoveHalf;
    
    private Paint mItemPaint;
    private Matrix matrix;
    
    private int mStatus;
    
    private int mMoveIndex = 0;
    
    private ValueAnimator mMoveAnimator;
    private long mAnimationDuration = 300;
    private ViewPager mViewPager;
    
    public ImageIndicator( Context context ) {
        this( context, null );
    }
    
    public ImageIndicator( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }
    
    public ImageIndicator( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        
        this.resources = getResources();
        
        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.Indicator, defStyleAttr, 0 );
        int n = array.getIndexCount();
        for ( int i = 0; i < n; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.Indicator_indicator_count:
                    mItemCount = array.getInt( attr, 1 );
                    break;
                case R.styleable.Indicator_indicator_itemSize:
                    mItemSize =
                            array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.Indicator_indicator_itemSelectedSize:
                    mItemSelectedSize =
                            array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.Indicator_indicator_itemAlpha:
                    mItemAlpha = array.getFloat( attr, 1.0f );
                    break;
                case R.styleable.Indicator_indicator_itemSelectedAlpha:
                    mItemSelectedAlpha = array.getFloat( attr, 1.0f );
                    break;
                case R.styleable.Indicator_indicator_interval:
                    mInterval =
                            array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
            }
        }
        array.recycle();
        
        init();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resources = null;
        for ( IndicatorItem indicatorItem : indicatorItems ) {
            if ( indicatorItem.bitmap != null ) {
                indicatorItem.bitmap.recycle();
                indicatorItem.bitmap = null;
            }
        }
    }
    
    private void init() {
        if ( null == resources )
            Log.e(ImageIndicator.TAG, "resources null");
        
        indicatorItems = new ArrayList<>();
        matrix = new Matrix();
        
        for ( int i = 0; i < mItemCount; i++ ) {
            IndicatorItem item;
            if ( null == indicatorImages || indicatorImages.length <= 0 ) {
                item = new IndicatorItem( resources, mItemSize, i, -1 );
            } else {
                item = new IndicatorItem( resources, mItemSize, i, indicatorImages[i] );
            }
            indicatorItems.add( item );
        }
        itemFrom = new IndicatorItem( mItemSize );
        itemMove = new IndicatorItem( mItemSize );
        itemTo = new IndicatorItem( mItemSize );
        
        mItemPaint = new Paint();
        
        mItemPaint.setAntiAlias( true );
        
        mMoveAnimator = new ValueAnimator();
        
        mStatus = STATUS_INIT;
    }
    
    public void setCount( int count, int[] indicatorImages ) {
        this.mItemCount = count;
        this.indicatorImages = indicatorImages;
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
        
        /*
         * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
         * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
         * UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        if ( widthMode == MeasureSpec.EXACTLY ) {
            width = widthSize;
        } else {
            int desireWidth = ( 2 * mItemSelectedSize ) * mItemCount + (
                                                                               mItemCount <= 0 ? 0 : mItemCount - 1
                                                                       ) * mInterval + getPaddingLeft() + getPaddingRight();
            if ( widthMode == MeasureSpec.AT_MOST ) {
                width = Math.min( desireWidth, widthSize );
            }
        }
        
        if ( heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            int desireHeight = 2 * mItemSelectedSize + getPaddingTop() + getPaddingBottom();
            if ( heightMode == MeasureSpec.AT_MOST ) {
                height = Math.min( desireHeight, heightSize );
            }
        }
        
        setMeasuredDimension( width, height );
    }
    
    @Override
    protected void onDraw( Canvas canvas ) {
        
        float itemY = getHeight() / 2;
        mItemPaint.setStyle( Style.FILL_AND_STROKE );
        mItemPaint.setAntiAlias( true );
        
        for ( int i = 0; i < mItemCount; i++ ) {
            float itemX = getPaddingLeft() + i * ( 2 * mItemSelectedSize + mInterval ) + mItemSelectedSize;
            IndicatorItem item = indicatorItems.get( i );
            item.setCenter( itemX, itemY );
            matrix.reset();
            matrix.postScale( ( float ) mItemSize / mItemSelectedSize, ( float ) mItemSize / mItemSelectedSize );
            matrix.postTranslate( item.left.x, item.top.y );
            mItemPaint.setAlpha( ( int ) ( mItemAlpha * 255) );
            
//            if ( i != mMoveIndex )
                paintItem( canvas, item, mItemPaint, matrix );
        }
        if ( mStatus == STATUS_INIT ) {
            mStatus = STATUS_INIT_END;
            itemMove.setCenter( indicatorItems.get( mMoveIndex ).center );
            itemMove.bitmap = indicatorItems.get( mMoveIndex ).bitmap;
            itemMove.setSize( mItemSelectedSize );
            matrix.reset();
            matrix.postScale( 1.0f, 1.0f );
            matrix.postTranslate( itemMove.left.x, itemMove.top.y );
            mItemPaint.setAlpha( ( int ) ( mItemSelectedAlpha * 255) );
            paintItem( canvas, itemMove, mItemPaint, matrix );
        } else if ( mStatus == STATUS_MOVE ) {
//            if ( !mMoveHalf ) {
//                paintItem( canvas, itemFrom, mItemPaint );
//            } else {
//                paintItem( canvas, itemFrom, mItemPaint );
//                paintItem( canvas, itemTo, mItemPaint );
//            }
            //            paintBezier( canvas, itemBezier, itemMove );
        } else if ( mStatus == STATUS_MOVE_COMPLETE ) {
            itemMove.setCenter( indicatorItems.get( mMoveIndex ).center );
            itemMove.bitmap = indicatorItems.get( mMoveIndex ).bitmap;
            itemMove.setSize( mItemSelectedSize );
            matrix.reset();
            matrix.postScale( 1.0f, 1.0f );
            matrix.postTranslate( itemMove.left.x, itemMove.top.y );
            mItemPaint.setAlpha( ( int ) ( mItemSelectedAlpha * 255) );
            paintItem( canvas, itemMove, mItemPaint, matrix );
        }
    }
    
    private void paintItem( Canvas canvas, IndicatorItem item, Paint paint, Matrix matrix ) {
        
        if ( null != item.bitmap )
            canvas.drawBitmap( item.bitmap, matrix, paint );
        else
            canvas.drawCircle( item.center.x, item.center.y, item.size, paint );
    }
    
    public void toPrevious() {
        if ( mMoveIndex <= 0 ) {
            mMoveIndex = 0;
            return;
        }
        if ( mStatus == STATUS_MOVE ) {
            if ( mMoveAnimator.isRunning() )
                mMoveAnimator.cancel();
        }
        itemFrom = indicatorItems.get( mMoveIndex );
        itemTo = indicatorItems.get( mMoveIndex - 1 );
        mMoveIndex--;
        moveAnimation( itemFrom.center, itemTo.center );
    }
    
    public void toNext() {
        if ( mMoveIndex >= mItemCount - 1 ) {
            mMoveIndex = mItemCount - 1;
            return;
        }
        if ( mStatus == STATUS_MOVE ) {
            if ( mMoveAnimator.isRunning() )
                mMoveAnimator.cancel();
        }
        itemFrom = indicatorItems.get( mMoveIndex );
        itemTo = indicatorItems.get( mMoveIndex + 1 );
        mMoveIndex++;
        moveAnimation( itemFrom.center, itemTo.center );
    }
    
    private void moveAnimation( Point startPoint, Point endPoint ) {
        mMoveAnimator = ValueAnimator.ofObject( new PointEvaluator(), startPoint, endPoint );
        mMoveAnimator.addUpdateListener( animation -> {
            itemMove.setCenter( ( Point ) animation.getAnimatedValue() );
            if ( Math.abs( itemMove.center.x - itemFrom.center.x ) >= mItemSelectedSize + mInterval / 2 ) {
                mMoveHalf = true;
                //                itemMove.radius =
                //                        ( 1 - Math.abs( itemMove.center.x - itemFrom.center.x ) / Math.abs( itemFrom.center.x - itemTo.center.x ) ) *
                //                        ( mRadius / 2 ) + mRadius / 2;
            } else {
                mMoveHalf = false;
                //                itemMove.radius = Math.abs( itemMove.center.x - itemFrom.center.x ) / Math.abs( itemFrom.center.x - itemTo.center.x ) *
                //                                    ( mRadius / 2 ) + mRadius / 2;
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
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator( new LinearInterpolator() );
        animatorSet.play( mMoveAnimator );
        animatorSet.setDuration( mAnimationDuration );
        animatorSet.start();
    }
    
    public void changeIndex( int newIndex ) {
        if ( newIndex < mMoveIndex ) {
            toPrevious();
        } else if ( newIndex > mMoveIndex ) {
            toNext();
        }
    }
    
    public static class IndicatorItem {
        
        int index;
        
        float size;
        int imageResourceID;
        Bitmap bitmap;
        
        Point center;
        Point left;
        Point top;
        Point right;
        Point bottom;
        
        IndicatorItem( Resources res, float size, int index, int resourceID ) {
            this( size );
            this.index = index;
            this.imageResourceID = resourceID;
            if ( -1 != resourceID )
                this.bitmap = BitmapFactory.decodeResource( res, resourceID );
        }
    
        IndicatorItem( IndicatorItem item ) {
            this.index = item.index;
            this.size = item.size;
            this.imageResourceID = item.imageResourceID;
            this.bitmap = item.bitmap;
            
            center = item.center;
            left = item.left;
            top = item.top;
            right = item.right;
            bottom = item.bottom;
        }
        
        
        IndicatorItem( float size ) {
            this.size = size;
            
            center = new Point();
            left = new Point();
            top = new Point();
            right = new Point();
            bottom = new Point();
        }
        
        void setCenter( float x, float y ) {
            center.set( x, y );
            left.set( ( x - size ), y );
            top.set( x, ( y - size ) );
            right.set( ( x + size ), y );
            bottom.set( x, ( y + size ) );
        }
    
        void setSize( float size ) {
            if ( this.size > 0 && null != center ) {
                this.size = size;
                setCenter( center.x, center.y );
            }
        }
        
        public void setIndex( int index ) {
            this.index = index;
        }
        
        public void setCenter( Point point ) {
            setCenter( point.x, point.y );
        }
        
        public static class Point {
            
            private float x;
            private float y;
            
            Point() {
                x = 0;
                y = 0;
            }
            
            Point( float x, float y ) {
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
    
    @BindingAdapter({ "previous", "next" })
    public static void setActionButton( ImageIndicator indicator, Button previous, Button next ) {
        if ( null != indicator ) {
            if ( null != previous ) {
                previous.setOnClickListener( v -> indicator.toPrevious() );
            }
            if ( null != next ) {
                next.setOnClickListener( v -> indicator.toNext() );
            }
        }
    }
    
    @BindingAdapter(value = { "viewPager", "indicatorImages" })
    public static void setViewPager( ImageIndicator indicator, ViewPager viewPager, int[] indicatorImages ) {
        if ( null != indicator ) {
            if ( null != viewPager ) {
                indicator.setViewPager( viewPager );
                if ( null != viewPager.getAdapter() ) {
                    indicator.setCount( viewPager.getAdapter().getCount(), indicatorImages );
                    if ( Debug ) {
                        Log.e( "ImageIndicator", "setViewPager: " + indicatorImages.length );
                        for ( int i : indicatorImages ) {
                            Log.e( "ImageIndicator", "setViewPager: " + i );
                        }
                    }
                } else {
                    viewPager.addOnAdapterChangeListener( ( viewPager1, oldAdapter, newAdapter ) -> {
                        indicator.setViewPager( viewPager1 );
                        if ( newAdapter != null ) {
                            indicator.setCount( newAdapter.getCount(), indicatorImages );
                        }
                    } );
                }
                viewPager.addOnPageChangeListener( new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {
                    }
                    
                    @Override
                    public void onPageSelected( int position ) {
                        indicator.changeIndex( position );
                    }
                    
                    @Override
                    public void onPageScrollStateChanged( int state ) {
                    }
                } );
            }
        }
    }
}
