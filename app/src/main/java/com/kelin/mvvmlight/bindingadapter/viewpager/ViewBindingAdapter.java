package com.kelin.mvvmlight.bindingadapter.viewpager;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;

import com.kelin.mvvmlight.command.ReplyCommand;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

/**
 * Created by kelin on 16-6-1.
 */
public class ViewBindingAdapter {
    
    @BindingAdapter({ "autoScroll" })
    public static void autoScroll( ViewPager viewPager, long durations ) {
        if ( Debug ) {
            Log.e( "ViewBindingAdapter", "autoScroll: " + durations );
        }
        if ( durations > 0 ) {
            if ( viewPager == null ) {
                throw new IllegalArgumentException( "viewPager must not be null" );
            } else {
                if ( viewPager.getAdapter() == null ) {
                    throw new IllegalArgumentException( "viewPager's adapter must not be null" );
                } else {
                    if ( viewPager.getAdapter().getCount() <= 0 ) {
                        throw new IllegalArgumentException( "viewPager's child count must not be 0" );
                    } else {
                        if ( viewPager.getAdapter().getCount() > 1 ) {
                            Observable.interval( durations, TimeUnit.MILLISECONDS )
                                    .subscribeOn( Schedulers.io() )
                                    .observeOn( AndroidSchedulers.mainThread() )
                                    .subscribe( aLong -> {
                                        viewPager.setCurrentItem( ( int ) ( aLong % viewPager.getAdapter().getCount()), true );
                            } );
                        }
                    }
                }
            }
        }
    }
    
    @BindingAdapter({ "smoothScrollPosition" })
    public static void setCurrentItem( ViewPager viewPager, int smoothScrollPosition ) {
        Observable.interval( 100, TimeUnit.MILLISECONDS ).take( 1 ).subscribeOn( Schedulers.io() ).observeOn( AndroidSchedulers.mainThread() ).subscribe( aLong -> {
            if ( smoothScrollPosition < 0 ) {
                throw new IllegalArgumentException( "currentPosition = " + smoothScrollPosition + " must >= 0 " );
            } else {
                if ( viewPager == null ) {
                    throw new IllegalArgumentException( "viewPager must not be null" );
                } else {
                    if ( viewPager.getAdapter() == null ) {
                        throw new IllegalArgumentException( "viewPager's adapter must not be null" );
                    } else {
                        if ( viewPager.getAdapter().getCount() > 0 && viewPager.getAdapter().getCount() <= smoothScrollPosition ) {
                            throw new IllegalArgumentException(
                                    "currentPosition = " + smoothScrollPosition + " must < " + viewPager.getAdapter().getCount() );
                        } else {
                            viewPager.setCurrentItem( smoothScrollPosition, true );
                        }
                    }
                }
            }
        } );
    }
    
    @BindingAdapter({ "scroll2Position" })
    public static void setCurrentItemNow( ViewPager viewPager, int smoothScrollPosition ) {
        viewPager.setCurrentItem( smoothScrollPosition, true );
    }
    
    @BindingAdapter({ "viewPagerTransformer" })
    public static void setPageTransformer( ViewPager viewPager, PageTransformer transformer ) {
        viewPager.setPageTransformer( true, transformer );
    }
    
    @BindingAdapter(value = { "onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand" }, requireAll = false)
    public static void onScrollChangeCommand( final ViewPager viewPager, final ReplyCommand< ViewPagerDataWrapper > onPageScrolledCommand, final ReplyCommand< Integer > onPageSelectedCommand, final ReplyCommand< Integer > onPageScrollStateChangedCommand ) {
        viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            private int state;
            
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {
                if ( onPageScrolledCommand != null ) {
                    onPageScrolledCommand.execute( new ViewPagerDataWrapper( position, positionOffset, positionOffsetPixels, state ) );
                }
            }
            
            @Override
            public void onPageSelected( int position ) {
                if ( onPageSelectedCommand != null ) {
                    onPageSelectedCommand.execute( position );
                }
            }
            
            @Override
            public void onPageScrollStateChanged( int state ) {
                this.state = state;
                if ( onPageScrollStateChangedCommand != null ) {
                    onPageScrollStateChangedCommand.execute( state );
                }
            }
        } );
        
    }
    
    public static class ViewPagerDataWrapper {
        
        public float positionOffset;
        public float position;
        public int positionOffsetPixels;
        public int state;
        
        public ViewPagerDataWrapper( float position, float positionOffset, int positionOffsetPixels, int state ) {
            this.positionOffset = positionOffset;
            this.position = position;
            this.positionOffsetPixels = positionOffsetPixels;
            this.state = state;
        }
    }
}
