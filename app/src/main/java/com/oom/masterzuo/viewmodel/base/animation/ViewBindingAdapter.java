package com.oom.masterzuo.viewmodel.base.animation;

import android.databinding.BindingAdapter;
import android.view.View;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YoungDroid on 2016/8/20.
 * Email: YoungDroid@163.com
 */
public class ViewBindingAdapter {

    public static final String[] animationPropertyName = new String[]{ "alpha", "pivotX", "pivotY", "translationX", "translationY", "rotation", "rotationX", "rotationY", "scaleX", "scaleY", "scrollX", "scrollY", "x", "y" };

    public static final String[] specialAnimationPropertyName = new String[]{ "pivot", "translation", "scale", "scroll" };
    
    
    @BindingAdapter(value = { "showMvp" }, requireAll = false)
    public static void matchHomeMvpAnimation( View view, boolean showMvp ) {
    
        if ( showMvp ) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether( ObjectAnimator.ofFloat( view, "alpha", 0.0f, 1.0f ),
                    ObjectAnimator.ofFloat( view, "translationX", view.getWidth(), 0.0f ) );
            animatorSet.addListener( new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart( Animator animation ) {
                    view.setAlpha( 0.0f );
                    view.setVisibility( View.VISIBLE );
                }
            } );
            animatorSet.start();
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether( ObjectAnimator.ofFloat( view, "alpha", 1.0f, 0.0f ),
                    ObjectAnimator.ofFloat( view, "translationX", 0.0f, view.getWidth() ) );
            animatorSet.addListener( new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd( Animator animation ) {
                    view.setVisibility( View.INVISIBLE );
                }
            } );
            animatorSet.start();
        }
    }
    
    @BindingAdapter(value = { "hideData" }, requireAll = false)
    public static void matchHomeDataAnimation( View view, boolean hideData ) {
        
        if ( !hideData ) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether( ObjectAnimator.ofFloat( view, "alpha", 0.0f, 1.0f ),
                    ObjectAnimator.ofFloat( view, "translationX", -view.getWidth(), 0.0f ) );
            animatorSet.addListener( new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart( Animator animation ) {
                    view.setAlpha( 0.0f );
                    view.setVisibility( View.VISIBLE );
                }
            } );
            if ( view.getVisibility() == View.INVISIBLE )
                animatorSet.start();
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether( ObjectAnimator.ofFloat( view, "alpha", 1.0f, 0.0f ),
                    ObjectAnimator.ofFloat( view, "translationX", 0.0f, -view.getWidth() ) );
            animatorSet.addListener( new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd( Animator animation ) {
                    view.setVisibility( View.INVISIBLE );
                }
            } );
            animatorSet.start();
        }
    }
    
    @BindingAdapter(value = { "propertyName", "duration", "start", "end", "onAnimation", "clickTrigger" }, requireAll = false)
    public static void animation( View view, String propertyName, int duration, float start, float end, ReplyCommand replyCommand, boolean clickTrigger ) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether( getAnimator( view, propertyName, start, end ) );
        if ( duration != -1 ) animatorSet.setDuration( duration );
        animatorSet.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd( Animator animation ) {
                if ( null != replyCommand )
                    replyCommand.execute();
            }
        } );
        if ( clickTrigger ) {
            view.setOnClickListener( v -> animatorSet.start() );
        } else {
            animatorSet.start();
        }
    }

    @BindingAdapter(value = { "propertyNames", "duration", "start", "end", "onAnimation", "clickTrigger" }, requireAll = false)
    public static void animations( View view, String[] propertyNames, int duration, float[] start, float[] end, ReplyCommand replyCommand, boolean clickTrigger ) {
        AnimatorSet animatorSet = new AnimatorSet();
        List< Animator > animators = new ArrayList<>();
        for ( int i = 0; i < propertyNames.length; i++ ) {
            animators.addAll( getAnimator( view, propertyNames[ i ], start[ i ], end[ i ] ) );
        }
        animatorSet.playTogether( animators );
        if ( duration != -1 ) animatorSet.setDuration( duration );
        animatorSet.addListener( new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd( Animator animation ) {
                replyCommand.execute();
            }
        } );
        if ( clickTrigger ) {
            view.setOnClickListener( v -> animatorSet.start() );
        } else {
            animatorSet.start();
        }
    }

    private static List< Animator > getAnimator( View view, String propertyName, float start, float end ) {
        List< Animator > animators = new ArrayList<>();
        if ( Arrays.asList( animationPropertyName ).contains( propertyName ) || Arrays.asList( specialAnimationPropertyName ).contains( propertyName ) ) {
            if ( Arrays.asList( animationPropertyName ).contains( propertyName ) ) {
                animators.add( ObjectAnimator.ofFloat( view, propertyName, start, end ) );
            } else {
                String animationX = propertyName + "X";
                animators.add( ObjectAnimator.ofFloat( view, animationX, start, end ) );
                String animationY = propertyName + "Y";
                animators.add( ObjectAnimator.ofFloat( view, animationY, start, end ) );
            }
        }
        return animators;
    }
}
