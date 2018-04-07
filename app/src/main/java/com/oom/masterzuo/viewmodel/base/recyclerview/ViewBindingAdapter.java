package com.oom.masterzuo.viewmodel.base.recyclerview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

public class ViewBindingAdapter {
    
    @BindingAdapter({ "snapHelper" })
    public static void setSnapHelper( RecyclerView recyclerView, boolean addSnapHelper ) {
        if ( addSnapHelper ) {
            LinearSnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView( recyclerView );
        }
    }
    
    @BindingAdapter({ "stackFromEnd" })
    public static void setStackFromEnd( RecyclerView recyclerView, boolean stackFromEnd ) {
        if ( recyclerView.getLayoutManager() != null )
            ( ( LinearLayoutManager ) recyclerView.getLayoutManager() ).setStackFromEnd( stackFromEnd );
    }
    
    @BindingAdapter({ "scroll2Position" })
    public static void scroll2Position( RecyclerView recyclerView, int position ) {
        Log.e( "YoungDroid", "scroll2Position: " + position );
        if ( position >= 0 && recyclerView != null && recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() > position ) {
            Log.e( "YoungDroid", "scroll2Position: OK" );
            recyclerView.smoothScrollToPosition( position );
        } else {
            Log.e( "YoungDroid", "scroll2Position: error" );
        }
    }
    
    @BindingAdapter({ "scroll2Page" })
    public static void scroll2Page( RecyclerViewPager recyclerViewPager, int position ) {
        if ( position >= 0 && recyclerViewPager != null && recyclerViewPager.getAdapter() != null &&
             recyclerViewPager.getAdapter().getItemCount() > position )
            recyclerViewPager.smoothScrollToPosition( position );
    }
}
