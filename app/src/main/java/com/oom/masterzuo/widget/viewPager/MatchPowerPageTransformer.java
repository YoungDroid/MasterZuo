package com.oom.masterzuo.widget.viewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

public class MatchPowerPageTransformer implements ViewPager.PageTransformer {
    
    private static float defaultScale = ( float ) 9 / ( float ) 10;
    
    @Override
    public void transformPage( View view, float position ) {
    
        int diffWidth = view.getWidth() / 15;
        
        if ( position < -1 ) { // [-Infinity,-1)
            view.setScaleX( defaultScale );
            view.setScaleY( defaultScale );
            view.setTranslationX(diffWidth);
        } else if ( position <= 0 ) { // [-1,0]
            view.setScaleX( ( float ) 1 + position / ( float ) 10 );
            view.setScaleY( ( float ) 1 + position / ( float ) 10 );
            view.setTranslationX((0 - position) * diffWidth);
        } else if ( position <= 1 ) { // (0,1]
            view.setScaleX( ( float ) 1 - position / ( float ) 10 );
            view.setScaleY( ( float ) 1 - position / ( float ) 10 );
            view.setTranslationX((0 - position) * diffWidth);
        } else { // (1,+Infinity]  
            view.setScaleX( defaultScale );
            view.setScaleY( defaultScale );
            view.setTranslationX(-diffWidth);
        }
    }
}