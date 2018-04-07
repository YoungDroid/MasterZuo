package com.oom.masterzuo.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kelin.mvvmlight.messenger.Messenger;
import com.kennyc.view.MultiStateView;
import com.oom.masterzuo.R;
import com.oom.masterzuo.event.RequestErrorEvent;
import com.oom.masterzuo.viewmodel.base.ViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by xiaojunhuang on 16/3/8.
 */
@EFragment
public abstract class BaseFragment extends Fragment {

    public static final String TAG = "YoungDroid";

    protected MultiStateView multiStateView;
    protected View contentView;
    private ViewDataBinding binding;
    protected int SHOWCONTENT = MultiStateView.VIEW_STATE_CONTENT;
    protected int EMPTY = MultiStateView.VIEW_STATE_EMPTY;
    protected int ERROR = MultiStateView.VIEW_STATE_ERROR;
    protected int LOADING = MultiStateView.VIEW_STATE_LOADING;
    private List< Call > calls = new ArrayList<>();

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        initMultiStateView( inflater, container );
        initContentView( inflater, container );
        multiStateView.addView( contentView );
        showState( LOADING );
        return multiStateView;
    }

    public void initMultiStateView( LayoutInflater inflater, ViewGroup container ) {
        multiStateView = ( MultiStateView ) inflater.inflate( R.layout.muiltstateview, container, false );
        multiStateView.setBackgroundColor( Color.WHITE );
        multiStateView.findViewById( R.id.b_retry ).setOnClickListener( retry );
    }

    protected void setMutiStateBackgroundColor( int color ) {
        multiStateView.setBackgroundColor( color );
    }

    public void initContentView( LayoutInflater inflater, ViewGroup container ) {
        binding = DataBindingUtil.inflate( inflater, layoutId(), container, false );
        contentView = binding.getRoot();
    }

    @AfterViews
    public void afterViews() {
        Messenger.getDefault().register( this, ViewModel.LOAD_DATA_FINISH, () -> {
            showState( MultiStateView.VIEW_STATE_CONTENT );
            Messenger.getDefault().unregister( this, ViewModel.LOAD_DATA_ERROR );
        } );
        Messenger.getDefault().register( this, ViewModel.LOAD_DATA_ERROR, () -> {
            showState( MultiStateView.VIEW_STATE_ERROR );
        } );
        refresh();
    }

    protected abstract int layoutId();

    public void refresh() {}

    public ViewDataBinding getBinding() {
        return binding;
    }

    @UiThread
    public void showState( int state ) {
        multiStateView.setViewState( state );
    }

    public void addCall( Call call ) {
        calls.add( call );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for ( Call call : calls ) {
            if ( call.isExecuted() || !call.isCanceled() ) {
                call.cancel();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void responseError( RequestErrorEvent requestErrorEvent ) {
        if ( TextUtils.isEmpty( requestErrorEvent.getMessage() ) ) {
            Log.e( TAG, "responseError3: 未知错误" );
            Toast.makeText( getActivity(), "未知错误", Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( getActivity(), requestErrorEvent.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }

    private View.OnClickListener retry = view -> refresh();

    @Override
    public void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister( this );
    }
}
