package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.viewmodel.base.ViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

import static com.oom.masterzuo.viewmodel.main.MemberCenterViewModel.MEMBER_CENTER_AUTO_REFRESH;

/**
 * Created by YoungDroid on 2016/11/17.
 * Email: YoungDroid@163.com
 */

public class MainViewModel extends ViewModel {
    
    public static final int HomePageModel = 0;
    public static final int RegularGoodsModel = 1;
    public static final int ShoppingTrolleyModel = 2;
    public static final int MemberCenterModel = 3;
    
    // model
    public int lastCheck = 0;
    
    // data filed
    public ObservableField< MemberCenterViewModel > member = new ObservableField<>();
    
    public final ObservableInt scrollPosition = new ObservableInt();
    public final ObservableInt autoCheck = new ObservableInt( -2 );
    
    // command
    public final ReplyCommand< Integer > onItemChecked = new ReplyCommand<>( nowCheck -> {
        
//        if ( nowCheck == MemberCenterModel && autoCheck.get() == -2 ) {
//            // TODO: 2018/4/2 判断已经登录
//            if ( true ) {
//                // TODO: 2018/4/2 重新登录
//                autoCheck.set( lastCheck );
//                return;
//            } else {
//                Messenger.getDefault().sendNoMsg( MEMBER_CENTER_AUTO_REFRESH );
//            }
//        }
        lastCheck = nowCheck;
        autoCheck.set( -2 );
        scrollPosition.set( nowCheck );
    } );
    
    public final ReplyCommand< Integer > onPageSelected = new ReplyCommand<>( integer -> {
        Toast.makeText( activity.get(), "onPageSelected" + integer, Toast.LENGTH_SHORT ).show();
    } );
    
    // item viewModel
    public final ObservableArrayList< ViewModel > viewModels = new ObservableArrayList<>();
    public final ItemViewSelector< ViewModel > itemView = new ItemViewSelector< ViewModel >() {
        @Override
        public void select( ItemView itemView, int position, ViewModel item ) {
            if ( item instanceof HomePageViewModel ) {
                itemView.set( BR.viewModel, R.layout.layout_main_home_page );
            } else if ( item instanceof RegularGoodsViewModel ) {
                itemView.set( BR.viewModel, R.layout.layout_main_regular_goods );
            } else if ( item instanceof ShoppingTrolleyViewModel ) {
                itemView.set( BR.viewModel, R.layout.layout_main_shopping_trolley );
            } else if ( item instanceof MemberCenterViewModel ) {
                itemView.set( BR.viewModel, R.layout.layout_main_member_center );
            }
        }
        
        @Override
        public int viewTypeCount() {
            return 3;
        }
    };
    
    public MainViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        Messenger.getDefault().sendNoMsg( LOAD_DATA_FINISH );
        
        member.set( new MemberCenterViewModel( context, activity, fragmentManager ) );
        
        for ( int i = 0; i < 4; i++ ) {
            if ( i == HomePageModel )
                viewModels.add( new HomePageViewModel( context, activity, fragmentManager ) );
            else if ( i == RegularGoodsModel )
                viewModels.add( new RegularGoodsViewModel( context, activity, fragmentManager ) );
            else if ( i == ShoppingTrolleyModel )
                viewModels.add( new ShoppingTrolleyViewModel( context, activity, fragmentManager ) );
            else if ( i == MemberCenterModel )
                viewModels.add( member.get() );
        }
    }
}
