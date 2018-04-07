package com.oom.masterzuo.app.main.homepage;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.base.BaseActivity;
import com.oom.masterzuo.viewmodel.main.homepage.GoodsClassifyViewModel;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_goods_classify)
public class GoodsClassifyActivity extends BaseActivity {
    
    @Override
    public String tag() {
        return "GoodsClassifyActivity";
    }
    
    @Override
    public void afterViews() {
        super.afterViews();
        mToolbar.setVisibility( View.GONE );
        showState( MultiStateView.VIEW_STATE_CONTENT );
        ViewDataBinding binding = getBinding();
        binding.setVariable( BR.viewModel, new GoodsClassifyViewModel( this, this, getSupportFragmentManager() ) );
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
