package com.oom.masterzuo.viewmodel.base.tabLayout;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by YoungDroid on 2017/1/18.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {
    
    @BindingAdapter( { "tabs" })
    public static void setTabs( TabLayout tabLayout, ObservableArrayList< String > tabs ) {
        
        if ( tabLayout != null ) {
            
            for ( String tab : tabs ) {
                tabLayout.addTab( tabLayout.newTab().setText( tab ) );
            }
        }
    }
    
    @BindingAdapter( { "viewPager" })
    public static void setViewPager( TabLayout tabLayout, ViewPager viewPager ) {
        if ( null != tabLayout && null != viewPager ) tabLayout.setupWithViewPager( viewPager );
    }
}
