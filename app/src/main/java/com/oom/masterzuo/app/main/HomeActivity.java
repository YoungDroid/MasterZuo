package com.oom.masterzuo.app.main;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.kelin.mvvmlight.messenger.Messenger;
import com.oom.masterzuo.BR;
import com.oom.masterzuo.R;
import com.oom.masterzuo.base.BaseActivity;
import com.oom.masterzuo.databinding.ActivityMainHomeBinding;
import com.oom.masterzuo.viewmodel.main.MainViewModel;
import com.oom.masterzuo.widget.callback_dialog.CallBackDialog.Style;
import com.oom.masterzuo.widget.callback_dialog.DialogBuilder;

import org.androidannotations.annotations.EActivity;

import static com.oom.masterzuo.viewmodel.base.ViewModel.LOGIN_ON_OTHER_DEVICE;

@EActivity(R.layout.activity_main_home)
public class HomeActivity extends BaseActivity {
    
    public static final int REQUEST_CALENDAR_PERMISSION_READ_CODE = 102;
    public static final int REQUEST_CALENDAR_PERMISSION_WRITE_CODE = 103;
    
    private ActivityMainHomeBinding binding;
    private MainViewModel viewModel;
    
    @Override
    public String tag() {
        return null;
    }
    
    @Override
    public void afterViews() {
        super.afterViews();
        mToolbar.setVisibility( View.GONE );
    }
    
    @Override
    public void refresh() {
        super.refresh();
        binding = ( ActivityMainHomeBinding ) getBinding();
        viewModel = new MainViewModel( this, this, getSupportFragmentManager() );
        binding.setVariable( BR.viewModel, viewModel );
        
        // TODO: 2018/4/2 监听退出登录
        Messenger.getDefault().register( this, LOGIN_ON_OTHER_DEVICE, () -> {
            if ( viewModel.lastCheck == MainViewModel.MemberCenterModel )
                viewModel.autoCheck.set( MainViewModel.HomePageModel );
        } );
        
        // 申请权限
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions( this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CALENDAR_PERMISSION_WRITE_CODE );
        }
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if ( resultCode == RESULT_OK ) {
            viewModel.autoCheck.set( MainViewModel.MemberCenterModel );
            viewModel.member.get().onRefresh.execute();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if ( null != viewModel && null != viewModel.member.get() ) {
            // TODO: 2018/4/2 回收
        }
        
        if ( null != viewModel ) {
            viewModel.recycle();
            viewModel = null;
        }
    }
    
    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        switch ( requestCode ) {
            case REQUEST_CALENDAR_PERMISSION_READ_CODE:
                // If request is cancelled, the result arrays are empty.
                if ( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED &&
                     permissions[ 0 ].equals( permission.READ_CALENDAR ) ) {
                    // TODO: 2018/4/2 请求日历权限
                } else {
                    DialogBuilder.init().createDialog( this ).setTitle( "权限不足,请同意权限申请." ).setStyle( Style.FAIL ).setAutoDismiss( 1000 );
                }
                break;
            case REQUEST_CALENDAR_PERMISSION_WRITE_CODE:
                // If request is cancelled, the result arrays are empty.
                if ( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED &&
                     permissions[ 0 ].equals( permission.WRITE_CALENDAR ) ) {
                } else {
                    DialogBuilder.init().createDialog( this ).setTitle( "权限不足,请同意权限申请." ).setStyle( Style.FAIL ).setAutoDismiss( 1000 );
                }
                break;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }
}
