package com.oom.masterzuo.viewmodel.base.textview;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by YoungDroid on 2016/9/2.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {
    
    @BindingAdapter( { "textColor" })
    public static void setTextColor( TextView textView, int color ) {
        textView.setTextColor( color );
    }
    
    @BindingAdapter( { "text" })
    public static void setText( TextView textView, CharSequence words ) {
        textView.setText( words );
    }
    
    @BindingAdapter( { "fonts" })
    public static void setFonts( TextView textView, String fontsName ) {
        textView.setTypeface( Typeface.createFromAsset( textView.getContext().getAssets(), "fonts/" + fontsName + ".ttf" ) );
    }
    
    @BindingAdapter(( "linkMovementMethod" ))
    public static void setMovementMethod( TextView textView, boolean shouldOpenLik ) {
        // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        textView.setMovementMethod( LinkMovementMethod.getInstance() );
        // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        textView.setHighlightColor( Color.parseColor( "#7B00FF" ) );
    }
}
