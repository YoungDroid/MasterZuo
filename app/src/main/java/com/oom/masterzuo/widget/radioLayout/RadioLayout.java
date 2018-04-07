package com.oom.masterzuo.widget.radioLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by YoungDroid on 2017/5/5.
 * Email: YoungDroid@163.com
 */

public class RadioLayout extends LinearLayout {

    private Map< RadioView, Integer > radioViewMaps;
    private int selectedIndex = -1;
    private OnCheckedChangeListener listener;

    public RadioLayout( Context context ) {
        this( context, null );
    }

    public RadioLayout( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    public RadioLayout( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }

    @Override
    protected void onFinishInflate() {
        init();
    }

    private void init() {

        if ( getChildCount() > 0 ) {
            radioViewMaps = new Hashtable<>();
            for ( int i = 0; i < getChildCount(); i++ ) {
                RadioView radioView = ( RadioView ) getChildAt( i );
                radioView.setOnClickListener( v -> {
                    selectedIndex = radioViewMaps.get( v );
                    selectedOne();
                } );
                radioViewMaps.put( radioView, i );
                if ( radioView.isSelected() ) {
                    if ( selectedIndex == -1 ) {
                        selectedIndex = i;
                    } else {
                        radioView.setSelected( false );
                    }
                }
            }
            if ( selectedIndex == -1 ) {
                selectedIndex = 0;
                getChildAt( 0 ).setSelected( true );
            }
        } else {
            TextView errorView = new TextView( getContext() );
            errorView.setClickable( true );
            errorView.setTextColor( 0xffff6600 );
            errorView.setGravity( Gravity.CENTER );
            errorView.setTextSize( 20 );
            errorView.setText( "RadioLayout must have last one child!" );
            removeAllViews();
            addView( errorView );
        }
    }

    public void setListener( OnCheckedChangeListener listener ) {
        this.listener = listener;
    }

    public void setCheckAt( int checkAt ) {
        if ( selectedIndex != checkAt && checkAt >= 0 && checkAt < getChildCount() ) {
            selectedIndex = checkAt;
            selectedOne();
        }
    }

    private void selectedOne() {
        for ( RadioView view : radioViewMaps.keySet() ) {
            view.setSelected( selectedIndex == radioViewMaps.get( view ) );
        }
        if ( listener != null ) {
            listener.onCheckedChanged( this, selectedIndex );
        }
    }

    public interface OnCheckedChangeListener{
        void onCheckedChanged( RadioLayout layout, int selectedIndex );
    }
}
