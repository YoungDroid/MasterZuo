package com.oom.masterzuo.viewmodel.main.homepage;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;

import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.viewmodel.base.ViewModel;
import com.oom.masterzuo.viewmodel.main.ToolbarViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

public class GoodsClassifyViewModel extends ViewModel {
    
    public final static String CLEAR_BRAND_SELECT = "clear_brand_select";
    
    String[] brandNames = {
            "固特异", "金宇", "开利", "迈进"
    };
    
    // Context
    
    // data filed
    
    public final ObservableField< ToolbarViewModel > toolbar = new ObservableField<>();
    public final ObservableField< ScanSearchTrolleyViewModel > scanSearchTrolley = new ObservableField<>();
    
    // command
    
    // item viewModel
    public final ObservableArrayList< ViewModel > brandViewModels = new ObservableArrayList<>();
    public final ItemViewSelector< ViewModel > brandItemView = new ItemViewSelector< ViewModel >() {
        @Override
        public void select( ItemView itemView, int position, ViewModel item ) {
            itemView.set( BR.viewModel, R.layout.item_goods_classify_brand );
        }
        
        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    
    public final ObservableArrayList< ViewModel > brandDetailViewModels = new ObservableArrayList<>();
    public final ItemViewSelector< ViewModel > brandDetailItemView = new ItemViewSelector< ViewModel >() {
        @Override
        public void select( ItemView itemView, int position, ViewModel item ) {
            itemView.set( BR.viewModel, R.layout.item_goods_classify_brand_detail );
        }
        
        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    
    public GoodsClassifyViewModel( Context context, Activity activity, FragmentManager fragmentManager ) {
        super( context, activity, fragmentManager );
        
        toolbar.set( new ToolbarViewModel( context, activity, fragmentManager, "分类", true, true ) );
        scanSearchTrolley.set( new ScanSearchTrolleyViewModel( context, activity, fragmentManager ) );
    
        for ( int i = 0; i < brandNames.length; i++ ) {
            brandViewModels.add( new BrandItemViewModel( context, activity, fragmentManager, brandNames[ i ], 0 == i ) );
        }
    
        for ( int i = 0; i < 13; i++ ) {
            brandDetailViewModels.add( new BrandDetailItemViewModel( context, activity, fragmentManager ) );
        }
    }
}
