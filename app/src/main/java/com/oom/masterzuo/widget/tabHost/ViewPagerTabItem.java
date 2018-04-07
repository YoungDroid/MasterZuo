package com.oom.masterzuo.widget.tabHost;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by YoungDroid on 2016/12/26.
 * Email: YoungDroid@163.com
 */

public class ViewPagerTabItem extends RadioButton {
    
    public ViewPagerTabItem( Context context ) {
        this( context, null );
    }
    
    public ViewPagerTabItem( Context context, @Nullable AttributeSet attrs ) {
        this( context, attrs, 0 );
    }
    
    public ViewPagerTabItem( Context context, @Nullable AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
        
        init();
    }
    
    private void init() {
        
    }
}
