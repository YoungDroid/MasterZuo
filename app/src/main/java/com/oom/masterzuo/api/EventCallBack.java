package com.oom.masterzuo.api;

import com.oom.masterzuo.base.BaseResponse;
import com.oom.masterzuo.event.RequestErrorEvent;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaojunhuang on 16/3/17.
 */
public class EventCallBack< T extends BaseResponse > implements Callback< T > {
    private EventBus mEventBus;

    private Object mSubscribe;

    private boolean autoUnSubscribe;


    @Override
    public void onResponse( Call< T > call, Response< T > response ) {
        T t = response.body();
        if ( t != null && t.getState() > 0 ) {
            if ( response.body().getState() == 2 ) {
                mEventBus.post( new RequestErrorEvent( 233, "账号已在异地登录，需重新登录.", call, call ) );
            }
            mEventBus.post( response.body() );
        } else if ( t != null ) {
            mEventBus.post( new RequestErrorEvent( t.getState(), t.getMessage(), response.body(), call ) );
        } else {
            if ( response.code() == 504 ) {
                mEventBus.post( new RequestErrorEvent( 504, "缓存异常~", call, call ) );
            } else if ( response.code() == 777 ) {
                mEventBus.post( new RequestErrorEvent( 777, "尚未登录", call, call ) );
            } else {
                mEventBus.post( new RequestErrorEvent( 201, "返回数据为空", null, call ) );
            }
        }
        autoUnregister();
    }

    @Override
    public void onFailure( Call< T > call, Throwable t ) {
        t.printStackTrace();
        mEventBus.post( new RequestErrorEvent( 400, "请检查网络", call, call ) );
        autoUnregister();
    }

    private void autoUnregister() {
        if ( autoUnSubscribe ) {
            mEventBus.unregister( mSubscribe );
        }
    }

    public void cancle() {
        mEventBus.unregister( mSubscribe );
    }


    public EventCallBack( EventBus eventBus, Object subscribe, boolean autoUnSubscribe ) {
        this.mEventBus = eventBus;
        this.mSubscribe = subscribe;
        this.autoUnSubscribe = autoUnSubscribe;
        if ( !mEventBus.isRegistered( subscribe ) ) {
            this.mEventBus.register( subscribe );
        }
    }

}
