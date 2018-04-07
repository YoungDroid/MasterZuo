package com.oom.masterzuo.base;

import android.Manifest.permission;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kelin.mvvmlight.messenger.Messenger;
import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.R;
import com.oom.masterzuo.app.MasterZuoApplication;
import com.oom.masterzuo.event.RequestErrorEvent;
import com.oom.masterzuo.viewmodel.base.ViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//import com.oom.masterzuo.dialog.SimpleDialog;
//import com.oom.masterzuo.dialog.SimpleDialog.OnDialogClickListener;
//import com.oom.masterzuo.dialog.SimpleDialog_;

/**
 * Created by xiaojunhuang on 16/3/2.
 *
 * Changed by YoungDroid on 17/3/28.
 * Email: YoungDroid@163.com
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_CODE = 101;
    private static final int REQUEST_READ_PHONE_STATE_CODE = 102;
    
    public abstract String tag();
    
    protected String TAG = "YoungDroid";
    private MultiStateView multiStateView;
    private View baseView;
    private ViewDataBinding binding;
    protected Toolbar mToolbar;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        initMultiStateView( getLayoutInflater(), null );
        super.onCreate( savedInstanceState );
        
        // 申请权限
        if ( VERSION.SDK_INT >= VERSION_CODES.M ) {
            int hasWriteExternalStoragePermission = checkSelfPermission( permission.WRITE_EXTERNAL_STORAGE );
            if ( hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions( new String[]{ permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_EXTERNAL_STORAGE_CODE );
            }
            int hasReadPhoneStatePermission = checkSelfPermission( permission.READ_PHONE_STATE );
            if ( hasReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions( new String[]{ permission.READ_PHONE_STATE }, REQUEST_READ_PHONE_STATE_CODE );
            }
        }
        
        // 竖屏锁定
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        
        // 透明ActionBar 和 顶部导航栏
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
        }
    }
    
    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        switch ( requestCode ) {
            case REQUEST_WRITE_EXTERNAL_STORAGE_CODE:
                // If request is cancelled, the result arrays are empty.
                if ( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED &&
                     permissions[ 0 ].equals( permission.WRITE_EXTERNAL_STORAGE ) ) {
                    MasterZuoApplication application = ( MasterZuoApplication ) getApplication();
                    application.init();
                } else {
                    //                    DialogBuilder.init().createDialog( this ).setTitle( "权限不足,请同意权限申请." ).setStyle( CallBackDialog.Style.FAIL ).setAutoDismiss( 1000 );
                    System.exit( 0 );
                }
                break;
            case REQUEST_READ_PHONE_STATE_CODE:
                // If request is cancelled, the result arrays are empty.
                if ( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED &&
                     permissions[ 0 ].equals( permission.READ_PHONE_STATE ) ) {
                    MasterZuoApplication application = ( MasterZuoApplication ) getApplication();
                    application.init();
                } else {
                    //                    DialogBuilder.init().createDialog( this ).setTitle( "权限不足,请同意权限申请." ).setStyle( CallBackDialog.Style.FAIL ).setAutoDismiss( 1000 );
                    System.exit( 0 );
                }
                break;
        }
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }
    
    @Deprecated
    protected void initToolBar() {
        mToolbar.setBackgroundColor( Color.BLACK );
        setSupportActionBar( mToolbar );
        if ( null != getSupportActionBar() )
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
    }
    
    protected void initToolBar( String toolBarTitle ) {
        initToolBar( toolBarTitle, Color.BLACK );
    }
    
    protected void initToolBar( String toolBarTitle, int color ) {
        mToolbar.setBackgroundColor( color );
        mToolbar.setTitle( toolBarTitle );
        setSupportActionBar( mToolbar );
        if ( null != getSupportActionBar() )
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        mToolbar.setNavigationIcon( R.mipmap.nav_icon_back );
        mToolbar.setNavigationOnClickListener( v -> finish() );
    }
    
    @AfterViews
    public void afterViews() {
        Messenger.getDefault().register( this, ViewModel.LOAD_DATA_FINISH, () -> {
            showState( MultiStateView.VIEW_STATE_CONTENT );
            Messenger.getDefault().unregister( this, ViewModel.LOAD_DATA_ERROR );
        } );
        Messenger.getDefault().register( this, ViewModel.LOAD_DATA_ERROR, () -> {
            showState( MultiStateView.VIEW_STATE_CONTENT );
        } );
        //        Messenger.getDefault().register( this, ViewModel.LOGIN_ON_OTHER_DEVICE, RequestErrorEvent.class, this::loginOnOtherDevice );
        refresh();
    }
    
    public ViewDataBinding getBinding() {
        return binding;
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void responseError( RequestErrorEvent requestErrorEvent ) {
        if ( requestErrorEvent.getCode() == 233 ) {
            loginOnOtherDevice( requestErrorEvent );
        } else {
            if ( TextUtils.isEmpty( requestErrorEvent.getMessage() ) ) {
                Log.e( TAG, "responseError: 未知错误" );
                Toast.makeText( this, "未知错误", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( this, requestErrorEvent.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        }
        if ( requestErrorEvent.getCode() == 777 ) {
            // TODO: 2018/4/2 重新登录
        }
    }
    
    private void loginOnOtherDevice( RequestErrorEvent requestErrorEvent ) {
        // TODO: 2018/4/2 判断已经登录
        if ( true ) {
            // TODO: 2018/4/2 退出登录
            //            SimpleDialog simpleDialog = SimpleDialog_.builder().content( requestErrorEvent.getMessage() ).cancel( "稍后登录" ).confirm( "立即登录" ).build();
            //            simpleDialog.setOnDialogClickListener( new OnDialogClickListener() {
            //                @Override
            //                public void cancelClick() {
            //
            //                }
            //
            //                @Override
            //                public void confirmClick() {
            //                    // TODO: 2018/4/2 重新登录
            //                }
            //            } );
            //            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //            ft.add( simpleDialog, null ).commitAllowingStateLoss();
        }
    }
    
    public void initMultiStateView( LayoutInflater inflater, ViewGroup container ) {
        baseView = inflater.inflate( R.layout.layout_baseactivity, container, false );
        multiStateView = ( MultiStateView ) baseView.findViewById( R.id.multiStateView );
        mToolbar = ( Toolbar ) baseView.findViewById( R.id.toolbar );
        multiStateView.setBackgroundColor( Color.WHITE );
        multiStateView.findViewById( R.id.b_retry ).setOnClickListener( retry );
        multiStateView.setViewState( MultiStateView.VIEW_STATE_LOADING );
    }
    
    public void refresh() {
    }
    
    @Override
    public void setContentView( int layoutResID ) {
        binding = DataBindingUtil.inflate( getLayoutInflater(), layoutResID, null, false );
        multiStateView.addView( binding.getRoot() );
        super.setContentView( baseView );
    }
    
    @Override
    public void setContentView( View view ) {
        binding = DataBindingUtil.bind( view );
        multiStateView.addView( binding.getRoot() );
        super.setContentView( baseView );
    }
    
    @UiThread
    public void showState( int state ) {
        multiStateView.setViewState( state );
    }
    
    private View.OnClickListener retry = view -> refresh();
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister( this );
    }
}
