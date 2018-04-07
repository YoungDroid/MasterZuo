package com.oom.masterzuo.widget.tabHost;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewArg;

import static com.oom.masterzuo.base.BaseResponse.TAG;

/**
 * Created by YoungDroid on 2016/12/26.
 * Email: YoungDroid@163.com
 */
public class ViewPagerTab extends RadioGroup {
    
    public ViewPagerTab( Context context ) {
        this( context, null );
    }
    
    public ViewPagerTab( Context context, AttributeSet attrs ) {
        super( context, attrs );
        init();
    }
    
    private void init() {
    }
    
    @BindingAdapter( { "itemView", "items" })
    public static < T > void setBindingAdapterViewPagerTab( ViewPagerTab viewPagerTab, ItemViewArg< T > arg, List< T > items ) {
        
        if ( arg == null ) {
            throw new IllegalArgumentException( "itemView must not be null" );
        }
        
        if ( arg.getItemView() != null && arg.getSelector() != null && items != null ) {
            viewPagerTab.setGravity( Gravity.CENTER );
            viewPagerTab.removeAllViews();
            for ( int i = 0; i < items.size(); i++ ) {
                T item = items.get( i );
                arg.select( i, item );
                ViewDataBinding binding = DataBindingUtil.inflate( LayoutInflater.from( viewPagerTab.getContext() ), arg.getItemView().layoutRes(), null, false );
                binding.setVariable( arg.bindingVariable(), item );
                binding.executePendingBindings();
                RadioButton radioButton = ( RadioButton ) binding.getRoot();
                LinearLayout.LayoutParams spanParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                spanParams.weight = 1f / items.size();
                Log.e( TAG, "setBindingAdapterViewPagerTab: " + spanParams.weight );
                viewPagerTab.addView( radioButton, -1, spanParams );
            }
        }
    }
}
