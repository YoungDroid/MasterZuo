package com.oom.masterzuo.widget.taglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CcYang on 2016/3/28.
 */
public class CcTagLayout extends ViewGroup {
    private LayoutInflater inflater;
    private List< String > tags;
    private ArrayList< ArrayList< View > > viewArray;
    private ArrayList< View > viewSort;
    private ArrayList< View > array;
    private ArrayList< Integer > arrayWidth;

    public CcTagLayout( Context context ) {
        this( context, null );
    }

    public CcTagLayout( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    public CcTagLayout( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        init( context );
    }

    private void init( Context context ) {
        inflater = LayoutInflater.from( context );

        viewArray = new ArrayList<>();
        viewSort = new ArrayList<>();
        array = new ArrayList<>();
        arrayWidth = new ArrayList<>();
    }

    public void setAdapter( List< String > tags, int resource, int id ) {
        this.tags = tags;
        if ( tags != null ) {
            for ( int i = 0; i < tags.size(); i++ ) {
                View convertView = inflater.inflate( resource, null );
                TextView tvTitle = (TextView) convertView.findViewById( id );
                tvTitle.setText( tags.get( i ) );
                if ( listener != null ) {
                    final int finalI = i;
                    tvTitle.setOnClickListener( v -> listener.onTagClickListener( v, finalI ) );
                }
                addView( convertView );
            }
        }
        requestLayout();
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        int widthMode = MeasureSpec.getMode( widthMeasureSpec );
        int widthSize = MeasureSpec.getSize( widthMeasureSpec );
        int heightMode = MeasureSpec.getMode( heightMeasureSpec );
        int heightSize = MeasureSpec.getSize( heightMeasureSpec );

        int width = widthSize, height = heightSize;
        int maxWidth = 0, lineCount = 0, childWidth = 0, childHeight = 0;

        // 计算出所有的childView的宽和高
        measureChildren( width, heightMeasureSpec );

        viewArray = new ArrayList<>();
        viewSort = new ArrayList<>();
        array = new ArrayList<>();
        arrayWidth = new ArrayList<>();

        boolean isAdd = false;
        for ( int i = 0; i < getChildCount(); i++ ) {
            View convertView = getChildAt( i );
            childWidth = convertView.getMeasuredWidth() + ( (MarginLayoutParams) convertView.getLayoutParams() ).leftMargin + ( (MarginLayoutParams) convertView.getLayoutParams() ).rightMargin;
            childHeight = convertView.getMeasuredHeight() + ( (MarginLayoutParams) convertView.getLayoutParams() ).topMargin + ( (MarginLayoutParams) convertView.getLayoutParams() ).bottomMargin;
            for ( int j = 0; j < viewSort.size(); j++ ) {
                if ( childWidth > viewSort.get( j ).getMeasuredWidth() ) {
                    viewSort.add( j, convertView );
                    isAdd = true;
                    break;
                }
            }
            if ( !isAdd ) {
                viewSort.add( convertView );
            }
        }

        for ( int i = 0; i < viewSort.size(); i++ ) {
            isAdd = false;
            View child = viewSort.get( i );
            int cWidth = child.getMeasuredWidth() + ( (MarginLayoutParams) child.getLayoutParams() ).leftMargin + ( (MarginLayoutParams) child.getLayoutParams() ).rightMargin;
            for ( int j = 0; j < arrayWidth.size(); j++ ) {
                if ( arrayWidth.get( j ) + cWidth <= widthSize ) {
                    arrayWidth.set( j, arrayWidth.get( j ) + cWidth );
                    viewArray.get( j ).add( viewSort.get( i ) );
                    isAdd = true;
                    break;
                }
            }

            if ( !isAdd ) {
                array = new ArrayList<>();
                array.add( viewSort.get( i ) );
                arrayWidth.add( cWidth );
                viewArray.add( array );
            }
        }

        height = viewArray.size() * childHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension( width, heightMode == MeasureSpec.EXACTLY ? heightSize > height ? heightSize : height : height );
    }

    @Override
    protected void onLayout( boolean changed, int l, int t, int r, int b ) {
        int maxWidth = 0, maxHeight = 0, lineCount = 0, childWidth = 0, childHeight = 0;
        int layoutLeft = 0, layoutTop = 0, layoutRight = 0, layoutBottom = 0;
        for ( int i = 0; i < viewArray.size(); i++ ) {
            maxWidth = getPaddingLeft();
            for ( int j = 0; j < viewArray.get( i ).size(); j++ ) {
                View child = viewArray.get( i ).get( j );
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
                maxHeight = i * childHeight + ( i + 1 ) * ( (MarginLayoutParams) child.getLayoutParams() ).topMargin + i * ( (MarginLayoutParams) child.getLayoutParams() ).bottomMargin + getPaddingTop();
                layoutLeft = maxWidth + ( (MarginLayoutParams) child.getLayoutParams() ).leftMargin;
                layoutTop = maxHeight;
                layoutRight = layoutLeft + childWidth;
                layoutBottom = layoutTop + childHeight;
                maxWidth = layoutRight + ( (MarginLayoutParams) child.getLayoutParams() ).rightMargin;
                child.layout( layoutLeft, layoutTop, layoutRight, layoutBottom );
            }
        }
    }

    @Override
    protected LayoutParams generateLayoutParams( ViewGroup.LayoutParams p ) {
        return new LayoutParams( p );
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
    }

    @Override
    public LayoutParams generateLayoutParams( AttributeSet attrs ) {
        return new LayoutParams( getContext(), attrs );
    }

    // 继承自margin，支持子视图android:layout_margin属性
    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams( Context c, AttributeSet attrs ) {
            super( c, attrs );
        }

        public LayoutParams( int width, int height ) {
            super( width, height );
        }

        public LayoutParams( ViewGroup.LayoutParams source ) {
            super( source );
        }

        public LayoutParams( MarginLayoutParams source ) {
            super( source );
        }
    }

    public OnTagClickListener listener;

    public interface OnTagClickListener {
        void onTagClickListener( View view, int position );
    }

    public void setOnTagClickListener( OnTagClickListener listener ) {
        this.listener = listener;
    }
}
