package com.oom.masterzuo.widget.specialscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by YoungDroid on 2016/8/21.
 * Email: YoungDroid@163.com
 */
public class SpecialNoScrollListView extends ListView {

    public SpecialNoScrollListView( Context context ) {
        super( context );
    }

    public SpecialNoScrollListView( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }

    public SpecialNoScrollListView( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }

    @Override
    public void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST );
        super.onMeasure( widthMeasureSpec, expandSpec );
    }
}
