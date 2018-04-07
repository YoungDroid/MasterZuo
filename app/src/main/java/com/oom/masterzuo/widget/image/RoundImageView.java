package com.oom.masterzuo.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.oom.masterzuo.R;

public class RoundImageView extends android.support.v7.widget.AppCompatImageView {
    
    private Paint paint;
    
    private Drawable drawableBorder;
    private Drawable drawableMask;
    
    private Bitmap bitmapBorder;
    private Bitmap bitmapMask;
    
    private PorterDuffXfermode duffXfermode;
    
    private int actualWidth, actualHeight;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    
    /**
     * view的宽度
     */
    private RectF mRoundRect;
    
    public RoundImageView( Context context ) {
        this( context, null );
    }
    
    public RoundImageView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }
    
    public RoundImageView( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        
        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.RoundImageView, defStyleAttr, 0 );
        int n = array.getIndexCount();
        for ( int i = 0; i < n; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.RoundImageView_round_border:
                    drawableBorder = array.getDrawable( attr );
                    break;
                case R.styleable.RoundImageView_round_mask:
                    drawableMask = array.getDrawable( attr );
                    break;
            }
        }
        array.recycle();
        
        if ( null != drawableBorder ) {
            bitmapBorder = drawable2Bitmap( drawableBorder );
        }
        if ( null != drawableMask ) {
            bitmapMask = drawable2Bitmap( drawableMask );
        }
        
        paint = new Paint();
        paint = new Paint();
        paint.setAntiAlias( true );
        mMatrix = new Matrix();
        duffXfermode = new PorterDuffXfermode( PorterDuff.Mode.SRC_IN );
    }
    
    public void setParams( int width, int height ) {
        actualWidth = width;
        actualHeight = height;
        Log.e( "YoungDroid", "params: " + width + "\t" + height );
        requestLayout();
    }
    
    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        
        int width = 0, height = 0;
        
        int widthMode = MeasureSpec.getMode( widthMeasureSpec );
        int widthSize = MeasureSpec.getSize( widthMeasureSpec );
        int heightMode = MeasureSpec.getMode( heightMeasureSpec );
        int heightSize = MeasureSpec.getSize( heightMeasureSpec );
        
        /*
          EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
          AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
          UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        if ( widthMode == MeasureSpec.EXACTLY ) {
            width = widthSize;
        } else {
            int desireWidth = getPaddingLeft() + getPaddingRight();
            if ( widthMode == MeasureSpec.AT_MOST ) {
                width = Math.min( desireWidth, widthSize );
            }
        }
        
        if ( heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            int desireHeight = getPaddingTop() + getPaddingBottom();
            if ( heightMode == MeasureSpec.AT_MOST ) {
                height = Math.min( desireHeight, heightSize );
            }
        }
        
        if ( actualWidth > 0 && actualHeight > 0 ) {
            width = actualWidth;
            height = actualHeight;
        }
        
        setMeasuredDimension( width, height );
    }
    
    @Override
    protected void onDraw( Canvas canvas ) {
        
        // 准备底层的图案样式
        drawableBorder.setBounds( 0, 0, actualWidth, actualHeight );
        drawableBorder.draw( canvas );
        int saveFlags = Canvas.ALL_SAVE_FLAG;
        canvas.saveLayer( 0, 0, actualWidth, actualHeight, null, saveFlags );
        
        drawableMask.setBounds( 0, 0, actualWidth, actualHeight );
        drawableMask.draw( canvas );
        
        paint.setXfermode( duffXfermode );
        Drawable drawable = getDrawable();
        if ( drawable == null ) {
            Log.e( "YoungDroid", "drawable null " );
            return;
        }
        // 绘制上层图片使用
        //        Bitmap bitmap = drawable2Bitmap( drawable );
        //        canvas.drawBitmap( bitmap, 0, 0, paint );
        //        paint.setXfermode( null );
        //        canvas.restore();
        
        Bitmap bitmap = drawable2Bitmap( drawable );
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader( bitmap, TileMode.CLAMP, TileMode.CLAMP );
        float scale = Math.max( getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight() );
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale( scale, scale );
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix( mMatrix );
        // 设置shader
        paint.setShader( mBitmapShader );
        canvas.drawRoundRect( mRoundRect, 0, 0, paint );
        paint.setXfermode( null );
        canvas.restore();
    }
    
    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh ) {
        super.onSizeChanged( w, h, oldw, oldh );
        // 圆角图片的范围
        mRoundRect = new RectF( 0, 0, getWidth(), getHeight() );
    }
    
        public Bitmap drawable2Bitmap( Drawable drawable ) {
            Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565 );
            Canvas canvas = new Canvas( bitmap );
            drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
            drawable.draw( canvas );
            return bitmap;
        }
//    private Bitmap drawable2Bitmap( Drawable drawable ) {
//        if ( drawable instanceof BitmapDrawable ) {
//            BitmapDrawable bd = ( BitmapDrawable ) drawable;
//            return bd.getBitmap();
//        }
//        int w = drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight();
//        Bitmap bitmap = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
//        Canvas canvas = new Canvas( bitmap );
//        drawable.setBounds( 0, 0, w, h );
//        drawable.draw( canvas );
//        return bitmap;
//    }
    
    // Bitmap转换成Drawable
    public Drawable bitmap2Drawable( Bitmap bitmap ) {
        BitmapDrawable bd = new BitmapDrawable( bitmap );
        Drawable d = ( Drawable ) bd;
        return d;
    }
}