package com.oom.masterzuo.app;

import android.databinding.ViewDataBinding;
import android.view.KeyEvent;
import android.view.View;

import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.base.BaseActivity;
import com.oom.masterzuo.viewmodel.WelcomeViewModel;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {
    
    @Override
    public String tag() {
        return "WelcomeActivity";
    }
    
    @Override
    public void afterViews() {
        super.afterViews();
        mToolbar.setVisibility( View.GONE );
        showState( MultiStateView.VIEW_STATE_CONTENT );
        ViewDataBinding binding = getBinding();
        binding.setVariable( BR.viewModel, new WelcomeViewModel( this, this, getSupportFragmentManager() ) );
    }
    
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            //启动界面禁止返回按键
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
