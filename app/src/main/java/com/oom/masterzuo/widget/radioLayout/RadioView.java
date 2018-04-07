package com.oom.masterzuo.widget.radioLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.oom.masterzuo.R;

public class RadioView extends View {

    private boolean mSelected = false;
    private Drawable drawableNormal;
    private Drawable drawableSelected;
    private int maxWidth = -1, maxHeight = -1;

    public RadioView( Context context ) {
        this( context, null );
    }

    public RadioView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    public RadioView( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );

        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.RadioLayout, defStyleAttr, 0 );
        int n = array.getIndexCount();
        for ( int i = 0; i < n; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.RadioLayout_radio_selected:
                    mSelected = array.getBoolean( attr, false );
                    setSelected( mSelected );
                    break;
                case R.styleable.RadioLayout_radio_normal_drawable:
                    drawableNormal = array.getDrawable( attr );
                    break;
                case R.styleable.RadioLayout_radio_selected_drawable:
                    drawableSelected = array.getDrawable( attr );
                    break;
            }
        }
        array.recycle();
    }

    private void initDrawable( Drawable drawable ) {
        if ( getWidth() <= 0 || getHeight() <= 0 ) return;
        maxWidth = Math.min( drawable.getIntrinsicWidth(), getWidth() );
        maxHeight = Math.min( drawable.getIntrinsicHeight(), getHeight() );

        if ( drawable.getIntrinsicWidth() > getWidth() ) {
            maxHeight *= ( getWidth() / ( float ) drawable.getIntrinsicWidth() );
        }
        if ( drawable.getIntrinsicHeight() > getHeight() ) {
            maxWidth *= ( getHeight() / ( float ) drawable.getIntrinsicHeight() );
        }
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public void setSelected( boolean selected ) {
        super.setSelected( selected );
        this.mSelected = selected;
        postInvalidate();
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

        initDrawable( drawableNormal );
        setMeasuredDimension( width, height );
    }

    @Override
    public void draw( Canvas canvas ) {
        super.draw( canvas );
        if ( maxWidth == -1 || maxHeight == -1 ) {
            initDrawable( drawableNormal );
        }
        if ( !mSelected ) {
            Rect drawableRect = new Rect( (getWidth() / 2) - (maxWidth / 2), 0, (getWidth() / 2) + (maxWidth / 2), maxHeight );
            drawableNormal.setBounds( drawableRect );
            drawableNormal.draw( canvas );
        } else {
            Rect drawableRect = new Rect( (getWidth() / 2) - (maxWidth / 2), 0, (getWidth() / 2) + (maxWidth / 2), maxHeight );
            drawableSelected.setBounds( drawableRect );
            drawableSelected.draw( canvas );
        }
    }
}
