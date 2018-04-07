package com.oom.masterzuo.viewmodel.base.LinearLayout;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewArg;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by YoungDroid on 2016/9/21.
 * Email: YoungDroid@163.com
 */

public class ViewBindingAdapter {

    @BindingAdapter( { "itemView", "items" })
    public static < T > void setBindingAdapterListView( LinearLayout linearLayoutToListView, ItemViewArg< T > arg, List< T > items ) {

        if ( arg == null ) {
            throw new IllegalArgumentException( "itemView must not be null" );
        }

        if ( arg.getItemView() != null && arg.getSelector() != null && items != null ) {
            linearLayoutToListView.removeAllViews();
            for ( int i = 0; i < items.size(); i++ ) {
                T item = items.get( i );
                arg.select( i, item );
                ViewDataBinding binding = DataBindingUtil.inflate( LayoutInflater.from( linearLayoutToListView.getContext() ), arg.getItemView().layoutRes(), null, false );
                binding.setVariable( arg.bindingVariable(), item );
                binding.executePendingBindings();
                linearLayoutToListView.addView( binding.getRoot() );
            }
        }
    }

    @BindingAdapter( { "itemView", "items", "spanCount" })
    public static < T > void setBindingAdapter( LinearLayout linearLayoutToGridView, ItemViewArg< T > arg, List< T > items, int spanCount ) {
        if ( arg == null ) {
            throw new IllegalArgumentException( "itemView must not be null" );
        }
        
        if ( arg.getItemView() != null && arg.getSelector() != null ) {
            
            linearLayoutToGridView.removeAllViews();

            int sumCount = items.size() / spanCount + ( items.size() % spanCount > 0 ? 1 : 0 );
            boolean notTidy = items.size() % spanCount > 0;
            int numOfNotTidy = items.size() % spanCount;

            int viewIndex = 0;
            for ( int i = 0; i < sumCount; i++ ) {
                LinearLayout linearLayout = new LinearLayout( linearLayoutToGridView.getContext() );
                linearLayout.setOrientation( linearLayoutToGridView.getOrientation() == HORIZONTAL ? VERTICAL : HORIZONTAL );
                LayoutParams layoutParams = new LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                layoutParams.gravity = Gravity.CENTER;
                linearLayout.setLayoutParams( layoutParams );

                for ( int j = 0; j < spanCount; j++ ) {
                    boolean isExtra = false;
                    if ( i == sumCount - 1 ) {
                        if ( notTidy && j >= numOfNotTidy ) {
                            isExtra = true;
                            viewIndex = 0;
                        }
                    }

                    LayoutParams spanParams = new LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                    spanParams.weight = 1;

                    T item = items.get( viewIndex );
                    arg.select( viewIndex, item );
                    ViewDataBinding binding = DataBindingUtil.inflate( LayoutInflater.from( linearLayoutToGridView.getContext() ), arg.getItemView().layoutRes(), null, false );
                    binding.setVariable( arg.bindingVariable(), item );
                    binding.executePendingBindings();
                    View v = binding.getRoot();

                    if ( null == v ) {
                        break;
                    }
                    if ( isExtra ) {
                        v.setVisibility( View.INVISIBLE );
                    }
                    linearLayout.addView( v, spanParams );
                    viewIndex++;
                }

                linearLayoutToGridView.addView( linearLayout, layoutParams );
            }
        }
    }
}
