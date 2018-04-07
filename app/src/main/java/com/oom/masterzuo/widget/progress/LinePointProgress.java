package com.oom.masterzuo.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.oom.masterzuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungDroid on 2017/2/15.
 * Email: YoungDroid@163.com
 */

public class LinePointProgress extends View {
    
    public static final String TAG = "YoungDroid";
    
    private Paint mPaintNormal;
    private Paint mPaintNormalLoading;
    private Paint mPaintFull;
    private Paint mPaintCover;
    private Path mPath;
    
    private int mPointCount;
    private int mPointIndex;
    private int mPointRadius;

    private int mLineStrokeWidth;

    private int mNormalColor;
    private int mNormalLoadingColor;
    private int mFullColor;
    private int mCoverColor;
    
    private List< Circle > mNormalCircle;
    private List< Circle > mNormalLoadingCircle;
    private List< Circle > mFullCircle;

    private ValueAnimator mLoadingAnimator;
    private long mAnimationDuration = 300;
    
    public LinePointProgress( Context context ) {
        this( context, null );
    }
    
    public LinePointProgress( Context context, @Nullable AttributeSet attrs ) {
        this( context, attrs, 0 );
    }
    
    public LinePointProgress( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        
        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.LinePoint, defStyleAttr, 0 );
        int n = array.getIndexCount();
        for ( int i = 0; i < n; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.LinePoint_progress_count:
                    mPointCount = array.getInt( attr, 1 );
                    break;
                case R.styleable.LinePoint_progress_radius:
                    mPointRadius = array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.LinePoint_progress_strokeWidth:
                    mLineStrokeWidth = array.getDimensionPixelSize( attr, ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics() ) );
                    break;
                case R.styleable.LinePoint_progress_normalColor:
                    mNormalColor = array.getColor( attr, Color.WHITE );
                    break;
                case R.styleable.LinePoint_progress_normalLoadingColor:
                    mNormalLoadingColor = array.getColor( attr, Color.WHITE );
                    break;
                case R.styleable.LinePoint_progress_fullColor:
                    mFullColor = array.getColor( attr, Color.BLACK );
                    break;
                case R.styleable.LinePoint_progress_coverColor:
                    mCoverColor = array.getColor( attr, Color.YELLOW );
                    break;
            }
        }
        array.recycle();
        
        init();
    }
    
    private void init() {
        
        mPaintNormal = new Paint();
        mPaintNormalLoading = new Paint();
        mPaintFull = new Paint();
        mPaintCover = new Paint();
        mPath = new Path();
        
        mNormalCircle = new ArrayList<>();
        mNormalLoadingCircle = new ArrayList<>();
        mFullCircle = new ArrayList<>();
        for ( int i = 0; i < mPointCount; i++ ) {
            Circle normalCircle = new Circle( mPointRadius, mLineStrokeWidth );
            mNormalCircle.add( normalCircle );
            Circle normalLoadingCircle = new Circle( mPointRadius, mLineStrokeWidth );
            mNormalLoadingCircle.add( normalLoadingCircle );
            Circle fullCircle = new Circle( 0, 0 );
            mFullCircle.add( fullCircle );
        }
        
        initLoadingCircle();
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
            int desireWidth = 0;
            if ( widthMode == MeasureSpec.AT_MOST ) {
                width = Math.max( desireWidth, widthSize );
            }
        }
        
        if ( heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            int desireHeight = 2 * mPointRadius + getPaddingTop() + getPaddingBottom();
            if ( heightMode == MeasureSpec.AT_MOST ) {
                height = Math.min( desireHeight, heightSize );
            }
        }
        
        setMeasuredDimension( width, height );
    }
    
    @Override
    protected void onDraw( Canvas canvas ) {
        initPaint( mPaintNormal, mNormalColor, Style.STROKE, mLineStrokeWidth );
        initPaint( mPaintNormalLoading, mNormalLoadingColor, Style.STROKE, mLineStrokeWidth );
        initPaint( mPaintFull, mFullColor, Style.FILL, mLineStrokeWidth );
        
        float circleY = getHeight() / 2;
        
        for ( int i = 0; i < mPointCount; i++ ) {
            float circleX = 0;
            if ( mPointCount == 1 ) {
                circleX = getWidth() / 2;
            } else {
                circleX = getPaddingLeft() + mLineStrokeWidth + mPointRadius +
                        i * ( ( float ) ( getWidth() - 2 * (mPointRadius + mLineStrokeWidth) - getPaddingLeft() - getPaddingRight() ) / ( float ) ( mPointCount - 1 ) );
            }
            mNormalCircle.get( i ).setCenter( circleX, circleY );
            mNormalLoadingCircle.get( i ).setCenter( circleX, circleY );
            mFullCircle.get( i ).setCenter( circleX, circleY );
            paintCircle( canvas, mNormalCircle.get( i ), mPaintNormal );
            paintArc( canvas, mNormalLoadingCircle.get( i ), mPaintNormalLoading );
            paintCircle( canvas, mFullCircle.get( i ), mPaintFull );
            if ( i > 0 ) {
                paintLine2Circle( canvas, mNormalCircle.get( i - 1 ), mNormalCircle.get( i ), mFullCircle.get( i - 1 ).isFull && mFullCircle.get( i ).isFull ? mPaintFull : mPaintNormal );
            }
        }
    }
    
    private void initPaint( Paint paint, int color, Style style, int strokeWidth ) {
        paint.setColor( color );
        paint.setStyle( style );
        paint.setStrokeWidth( strokeWidth );
        paint.setAntiAlias( true );
    }
    
    private void paintCircle( Canvas canvas, Circle circle, Paint paint ) {
        canvas.drawCircle( circle.center.x, circle.center.y, circle.radius - circle.strokeWidth, paint );
    }
    
    private void paintLine2Circle( Canvas canvas, Circle start, Circle end, Paint paint ) {
        canvas.drawLine( start.right.x - 1f, start.right.y, end.left.x + 1f, end.left.y, paint );
    }

    private void paintArc( Canvas canvas, Circle circle, Paint paint ) {
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(
                circle.center.x - circle.radius + mLineStrokeWidth,
                circle.center.y - circle.radius + mLineStrokeWidth,
                circle.center.x + circle.radius - mLineStrokeWidth,
                circle.center.y + circle.radius - mLineStrokeWidth);
        canvas.drawArc( oval, circle.startAngle, 60, false, paint );
    }
    
    private void initLoadingCircle() {
        mLoadingAnimator = ValueAnimator.ofObject( new FloatEvaluator(), 0, 1 );
        mLoadingAnimator.addUpdateListener( animation -> {
            for ( int i = 0; i < mNormalLoadingCircle.size(); i++ ) {
                if ( !mFullCircle.get( i ).isFull )
                mNormalLoadingCircle.get( i ).startAngle = ( float ) animation.getAnimatedValue() * 360;
            }
            invalidate();
        } );
        mLoadingAnimator.setRepeatCount( ValueAnimator.INFINITE );
        mLoadingAnimator.setRepeatMode( ValueAnimator.RESTART );
        mLoadingAnimator.setInterpolator( new LinearInterpolator() );
        mLoadingAnimator.setDuration( 3 * mAnimationDuration );
        mLoadingAnimator.start();
    }
    
    public void setPointFull( int pointIndex ) {
        if ( pointIndex < 0 || pointIndex >= mFullCircle.size() ) return;
        
        mFullCircle.get( pointIndex ).setFull( !mFullCircle.get( pointIndex ).isFull );
        ValueAnimator circleAnimator = ValueAnimator.ofObject( new FloatEvaluator(),  0, mPointRadius );
        circleAnimator.addUpdateListener( animation -> {
            mFullCircle.get( pointIndex ).radius = ( float ) animation.getAnimatedValue();
            invalidate();
        } );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator( new LinearInterpolator() );
        animatorSet.play( circleAnimator );
        animatorSet.setDuration( mAnimationDuration );
        animatorSet.start();

        boolean isAllFull = true;
        for ( int i = 0; i < mFullCircle.size(); i++ ) {
            if ( !mFullCircle.get( i ).isFull ) {
                isAllFull = false;
                break;
            }
        }

        if ( isAllFull ) {
            if ( null != mLoadingAnimator )
            mLoadingAnimator.cancel();
        } else {
            if ( mLoadingAnimator != null && !mLoadingAnimator.isRunning() ) {
                mLoadingAnimator.start();
            }
        }
    }
    
    public void setPointCount( int mPointCount ) {
        this.mPointCount = mPointCount;
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @BindingAdapter( {"fullPoint"} )
    public static void setPointFull( LinePointProgress linePointProgress, int pointIndex ) {
        linePointProgress.setPointFull( pointIndex );
    }
    
    @BindingAdapter( { "progress_count" })
    public static void setProgressCount( LinePointProgress linePointProgress, int count ) {
        linePointProgress.setPointCount( count );
    }
    
    private static class Circle {
        
        float radius;
        float strokeWidth;
        
        Point center;
        Point left;
        Point top;
        Point right;
        Point bottom;
        
        int color;
        
        boolean isFull;

        float startAngle;
        
        public Circle( float radius, float strokeWidth ) {
            this.radius = radius;
            this.strokeWidth = strokeWidth;
            
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
        
        public int getColor() {
            return color;
        }
        
        public void setColor( int color ) {
            this.color = color;
        }
    
        public boolean isFull() {
            return isFull;
        }
    
        public void setFull( boolean full ) {
            isFull = full;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public void setStartAngle( float startAngle ) {
            this.startAngle = startAngle;
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
        
        public static class Line {
            
            private Circle circleFrom;
            private Circle circleTo;
            private float progress;
    
            public Line( Circle circleFrom, Circle circleTo ) {
                this.circleFrom = circleFrom;
                this.circleTo = circleTo;
            }
    
            public void setProgress( float progress ) {
                this.progress = progress;
            }
    
//            public void display( Canvas canvas ) {
//                canvas.drawLine( circleFrom. );
//            }
        }
    }
}
