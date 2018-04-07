package com.oom.masterzuo.widget.speciallinearlayout;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewArg;
import me.tatarka.bindingcollectionadapter.factories.BindingAdapterViewFactory;

/**
 * Created by CcYang on 2015/9/15.
 * Mail: xzlight@outlook.com
 */
public class LinearLayoutToGridView extends LinearLayout {

    private int viewIndex = 0;

    public LinearLayoutToGridView( Context context ) {
        super( context );
    }

    public LinearLayoutToGridView( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }

    public LinearLayoutToGridView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
    }

    private int i, j;

    public void setAdapter( BaseAdapter adapter, int spanCount ) {
        viewIndex = 0;
        removeAllViews();

        int sumCount = adapter.getCount() / spanCount + ( adapter.getCount() % spanCount > 0 ? 1 : 0 );
        boolean notTidy = adapter.getCount() % spanCount > 0;
        int numOfNotTidy = adapter.getCount() % spanCount;

        for ( i = 0; i < sumCount; i++ ) {
            LinearLayout linearLayout = new LinearLayout( getContext() );
            linearLayout.setOrientation( HORIZONTAL );
            LayoutParams layoutParams = new LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            layoutParams.gravity = Gravity.CENTER;
            linearLayout.setLayoutParams( layoutParams );

            for ( int j = 0; j < spanCount; j++ ) {
                boolean isExtra = false;
                if ( i == sumCount - 1 ) {
                    if ( notTidy && j >= numOfNotTidy  ) {
//                        break;
                        isExtra = true;
                        viewIndex = 0;
                    }
                }

                LayoutParams spanParams = new LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                spanParams.weight = 1;
                View v = adapter.getView( viewIndex, null, null );
                if ( null == v ) {
                    break;
                }
                if ( isExtra ) {
                    v.setVisibility( INVISIBLE );
                }
                linearLayout.addView( v, spanParams );
                viewIndex++;
            }

            addView( linearLayout, layoutParams );
        }
    }
}
