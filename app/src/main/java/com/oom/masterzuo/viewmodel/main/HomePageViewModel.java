package com.oom.masterzuo.viewmodel.main;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import com.kelin.mvvmlight.command.ReplyCommand;
import com.kelin.mvvmlight.messenger.Messenger;
import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.app.main.HomeActivity_;
import com.oom.masterzuo.app.main.homepage.GoodsClassifyActivity_;
import com.oom.masterzuo.viewmodel.base.ViewModel;
import com.oom.masterzuo.viewmodel.main.homepage.ModelHeaderViewModel;
import com.oom.masterzuo.viewmodel.main.homepage.NewGoodsRecommendItemViewModel;
import com.oom.masterzuo.viewmodel.main.homepage.RecommendGoodsItemViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

public class HomePageViewModel extends ViewModel {
    
    // model
    
    // data filed
    public final ObservableBoolean loading = new ObservableBoolean();
    public final ObservableInt contentState = new ObservableInt( MultiStateView.VIEW_STATE_LOADING );
    
    public final ObservableField< ToolbarViewModel > toolbar = new ObservableField<>();
    
    public final ObservableField< ModelHeaderViewModel > goodsClassify = new ObservableField<>();
    public final ObservableField< ModelHeaderViewModel > goodsRecommend = new ObservableField<>();
    
    // command
    public final ReplyCommand onRefresh = new ReplyCommand( () -> {
        // TODO: 2018/4/2 刷新
        
    } );
    
    public final ReplyCommand< Integer > onClick = new ReplyCommand<>( integer -> {
        switch ( integer ) {
            case 0:
                GoodsClassifyActivity_.intent( activity.get() ).start();
                break;
            default:
                break;
        }
    } );
    
    // item viewModel
    public final ObservableArrayList< ViewModel > viewModels = new ObservableArrayList<>();
    public final ItemViewSelector< ViewModel > itemView = new ItemViewSelector< ViewModel >() {
        @Override
        public void select( ItemView itemView, int position, ViewModel item ) {
            itemView.set( BR.viewModel, R.layout.item_home_page_recommend_goods );
        }
        
        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    
    public final ObservableArrayList< ViewModel > newGoodsRecommendViewModels = new ObservableArrayList<>();
    public final ItemViewSelector< ViewModel > newGoodsRecommendItemView = new ItemViewSelector< ViewModel >() {
        @Override
        public void select( ItemView itemView, int position, ViewModel item ) {
            itemView.set( BR.viewModel, R.layout.item_home_page_new_goods_recommend );
        }
        
        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    
    public HomePageViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        Messenger.getDefault().register( activity, LOAD_DATA_ERROR, () -> {
            contentState.set( MultiStateView.VIEW_STATE_CONTENT );
        } );
        
        toolbar.set( new ToolbarViewModel( context, activity, fragmentManager, "首页", true, true ) );
        goodsClassify.set( new ModelHeaderViewModel( context, activity, fragmentManager, Uri.parse(
                "res:///" + R.mipmap.home_btn_commodity ), "商品", "分类", false ) );
        goodsRecommend.set( new ModelHeaderViewModel( context, activity, fragmentManager, Uri.parse(
                "res:///" + R.mipmap.home_btn_news ), "新品", "推荐", true ) );
        
        loadDataFromNet();
    }
    
    private void loadDataFromNet() {
        // TODO: 2018/4/2 加载网络数据
        for ( int i = 0; i < 4; i++ ) {
            viewModels.add( new RecommendGoodsItemViewModel( context, activity.get(), fragmentManager.get() ) );
            newGoodsRecommendViewModels.add( new NewGoodsRecommendItemViewModel( context, activity.get(), fragmentManager.get() ) );
        }
    }
}
