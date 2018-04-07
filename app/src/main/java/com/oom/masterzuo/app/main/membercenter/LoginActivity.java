package com.oom.masterzuo.app.main.membercenter;

import android.databinding.ViewDataBinding;
import android.view.KeyEvent;
import android.view.View;

import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.base.BaseActivity;
import com.oom.masterzuo.viewmodel.main.membercenter.LoginViewModel;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    
    @Override
    public String tag() {
        return "LoginActivity";
    }
    
    @Override
    public void afterViews() {
        super.afterViews();
        mToolbar.setVisibility( View.GONE );
        showState( MultiStateView.VIEW_STATE_CONTENT );
        ViewDataBinding binding = getBinding();
        binding.setVariable( BR.viewModel, new LoginViewModel( this, this, getSupportFragmentManager() ) );
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
