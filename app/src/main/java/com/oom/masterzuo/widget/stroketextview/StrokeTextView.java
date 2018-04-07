package com.oom.masterzuo.widget.stroketextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oom.masterzuo.R;

/**
 * Created by CcYang on 2016/7/25.
 * Email: 1367496366@qq.com
 */
public class StrokeTextView extends TextView {

    private TextView outlineTextView = null;
    private int strokeWidth;
    private int strokeColor;

    public StrokeTextView( Context context ) {
        this( context, null );
    }

    public StrokeTextView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    public StrokeTextView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );

        setWillNotDraw( false );
        TypedArray typedArray = context.getTheme().obtainStyledAttributes( attrs, R.styleable.StrokeTextView, defStyle, 0 );
        int count = typedArray.getIndexCount();
        for ( int i = 0; i < count; i++ ) {
            int attr = typedArray.getIndex( i );
            switch ( attr ) {
                case R.styleable.StrokeTextView_StrokeTextView_strokeWidth:
                    strokeWidth = typedArray.getDimensionPixelSize(
                            attr,
                            ( int ) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_SP,
                                    16,
                                    getResources().getDisplayMetrics()
                            )
                    );
                    break;
                case R.styleable.StrokeTextView_StrokeTextView_strokeColor:
                    strokeColor = typedArray.getColor( attr, Color.BLACK );
                    break;
            }
        }
        typedArray.recycle();


        outlineTextView = new TextView( context, attrs, defStyle );
        init();
    }

    public void init() {
        TextPaint paint = outlineTextView.getPaint();
        paint.setStrokeWidth( strokeWidth );// 描边宽度
        paint.setStyle( Style.STROKE );
        outlineTextView.setTextColor( strokeColor );// 描边颜色
        outlineTextView.setGravity( getGravity() );
    }

    @Override
    public void setLayoutParams( ViewGroup.LayoutParams params ) {
        super.setLayoutParams( params );
        outlineTextView.setLayoutParams( params );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );

        // 设置轮廓文字
        CharSequence outlineText = outlineTextView.getText();
        if ( outlineText == null || !outlineText.equals( this.getText() ) ) {
            outlineTextView.setText( getText() );
            postInvalidate();
        }
        outlineTextView.measure( widthMeasureSpec, heightMeasureSpec );
    }

    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom ) {
        super.onLayout( changed, left, top, right, bottom );
        outlineTextView.layout( left, top, right, bottom );
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        outlineTextView.draw( canvas );
        super.onDraw( canvas );
    }
}
