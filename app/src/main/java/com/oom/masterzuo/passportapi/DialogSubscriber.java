package com.oom.masterzuo.passportapi;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.oom.masterzuo.event.RequestErrorEvent;
import com.oom.masterzuo.widget.callback_dialog.CallBackDialog;
import com.oom.masterzuo.widget.callback_dialog.CallBackDialog.Style;
import com.oom.masterzuo.widget.callback_dialog.DialogBuilder;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

public class DialogSubscriber< T > extends Subscriber< T > {
    
    public static final String TAG = "YoungDroid";
    
    public static final int NOT_LOGIN = 777;
    public static final int LOGIN_ON_OTHER_DEVICE = 233;
    public static final int NETWORK_ERROR = 444;
    public static final int UNKNOWN_ERROR = 100;
    
    private WeakReference< Context > context;
    private EventBus mEventBus;
    private Object mSubscribe;
    
    private boolean mAutoUnSubscribe;
    private boolean mShouldShowDialog;
    private boolean mShouldLogin;
    
    private String mTitleLoading = "";
    private String mTitleSuccess = "";
    
    private CallBackDialog callbackDialog;
    
    public DialogSubscriber( Context context, Object mSubscribe ) {
        this( context, new EventBus(), mSubscribe, true, false, true, "", "" );
    }
    
    public DialogSubscriber( Context context, Object mSubscribe, boolean mShouldLogin ) {
        this( context, new EventBus(), mSubscribe, true, false, mShouldLogin, "", "" );
    }
    
    public DialogSubscriber( Context context, EventBus mEventBus, Object mSubscribe, boolean mShouldLogin ) {
        this( context, mEventBus, mSubscribe, true, false, mShouldLogin, "", "" );
    }
    
    public DialogSubscriber( Context context, Object mSubscribe, boolean mShouldShowDialog, boolean mShouldLogin ) {
        this( context, new EventBus(), mSubscribe, true, mShouldShowDialog, mShouldLogin, "", "" );
    }
    
    public DialogSubscriber( Context context, Object mSubscribe, String mTitleLoading, String mTitleSuccess ) {
        this( context, new EventBus(), mSubscribe, true, true, true, mTitleLoading, mTitleSuccess );
    }
    
    public DialogSubscriber( Context context, Object mSubscribe, String mTitleLoading, String mTitleSuccess, boolean mShouldLogin ) {
        this( context, new EventBus(), mSubscribe, true, true, mShouldLogin, mTitleLoading, mTitleSuccess );
    }
    
    public DialogSubscriber( Context context, EventBus mEventBus, Object mSubscribe, boolean mAutoUnSubscribe, boolean mShouldShowDialog, boolean mShouldLogin, String mTitleLoading, String mTitleSuccess ) {
        if ( context == null ) {
            throw new NullPointerException( "Context can't be null." );
        }
        
        this.context = new WeakReference<>( context );
        this.mEventBus = mEventBus;
        this.mSubscribe = mSubscribe;
        this.mAutoUnSubscribe = mAutoUnSubscribe;
        this.mShouldShowDialog = mShouldShowDialog;
        this.mShouldLogin = mShouldLogin;
        this.mTitleLoading = mTitleLoading;
        this.mTitleSuccess = mTitleSuccess;
        if ( !mEventBus.isRegistered( mSubscribe ) ) {
            this.mEventBus.register( mSubscribe );
        }
    }
    
    private void autoUnregister() {
        if ( mAutoUnSubscribe ) {
            mEventBus.unregister( mSubscribe );
        }
    }
    
    @Override
    public void onStart() {
        if ( mShouldShowDialog ) {
            callbackDialog = DialogBuilder.init().createDialog( context.get() ).setStyle( Style.LOADING ).setTitle( mTitleLoading ).build();
            try {
                callbackDialog.show();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onCompleted() {
        if ( mShouldShowDialog ) {
            if ( null == callbackDialog || !callbackDialog.isShowing() ) {
                callbackDialog = DialogBuilder.init().createDialog( context.get() ).setStyle( Style.SUCCESS ).build();
                try {
                    callbackDialog.show();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
            
            callbackDialog.setStyle( Style.SUCCESS, mTitleSuccess );
            if ( "登录成功".equals( mTitleSuccess ) || "修改成功".equals( mTitleSuccess ) || "注册成功".equals( mTitleSuccess ) ) {
                Toast.makeText( context.get(), mTitleSuccess, Toast.LENGTH_SHORT ).show();
                try {
                    callbackDialog.dismiss();
                } catch ( IllegalArgumentException e ) {
                    e.printStackTrace();
                }
            } else {
                callbackDialog.setAutoDismiss( 1000 );
            }
        }
        
        autoUnregister();
    }
    
    @Override
    public void onError( Throwable e ) {
        if ( mShouldShowDialog ) {
            if ( null == callbackDialog || !callbackDialog.isShowing() ) {
                callbackDialog = DialogBuilder.init().createDialog( context.get() ).setStyle( Style.FAIL ).build();
                try {
                    callbackDialog.show();
                } catch ( Exception err ) {
                    err.printStackTrace();
                }
            }
            
            if ( e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException ) {
                if ( Debug ) {
                    Log.e( "DialogSubscriber", "onError: network error, 网络异常" );
                }
                callbackDialog.setStyle( Style.FAIL, "请检查网络" );
                mEventBus.post( new RequestErrorEvent( NETWORK_ERROR, "请检查网络.", null, null ) );
            } else {
                String message = e.getMessage();
                if ( "缺少 access_token".equals( message ) ) {
                    message = "尚未登录";
                    if ( mShouldLogin ) {
                        mEventBus.post( new RequestErrorEvent( NOT_LOGIN, "尚未登录", null, null ) );
                    }
                } else {
                    mEventBus.post( new RequestErrorEvent( UNKNOWN_ERROR, message, null, null ) );
                }
                callbackDialog.setStyle( Style.FAIL, message );
                if ( Debug ) {
                    Log.e( "DialogSubscriber", "onError: " + message );
                }
            }
            
            callbackDialog.setAutoDismiss( 1000 );
        } else {
            if ( e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException ) {
                if ( Debug ) {
                    Log.e( "DialogSubscriber", "onError: network error, 网络异常" );
                }
                mEventBus.post( new RequestErrorEvent( NETWORK_ERROR, "请检查网络.", null, null ) );
            } else if ( e instanceof ApiException ) {
                if ( TextUtils.isEmpty( e.getMessage() ) ) {
                    if ( Debug ) {
                        Log.e( "DialogSubscriber", "onError: 未知错误" );
                    }
                    mEventBus.post( new RequestErrorEvent( UNKNOWN_ERROR, "未知错误.", null, null ) );
                } else {
                    switch ( e.getMessage() ) {
                        case "缺少 access_token":
                            if ( mShouldLogin ) {
                                mEventBus.post( new RequestErrorEvent( NOT_LOGIN, "尚未登录", null, null ) );
                            }
                            break;
                        case "ACCESS_TOKEN_NOT_EXIST":
                            mEventBus.post( new RequestErrorEvent( LOGIN_ON_OTHER_DEVICE, "账号已在异地登录,需重新登录.", null, null ) );
                            break;
                        default:
                            mEventBus.post( new RequestErrorEvent( UNKNOWN_ERROR, e.getMessage(), null, null ) );
                            break;
                    }
                }
            } else {
                if ( Debug ) {
                    Log.e( "DialogSubscriber", "onError: 未知错误" );
                }
                mEventBus.post( new RequestErrorEvent( UNKNOWN_ERROR, "未知错误.", null, null ) );
                e.printStackTrace();
            }
        }
        
        autoUnregister();
    }
    
    @Override
    public void onNext( T t ) {
        if ( Debug ) {
            Log.e( "DialogSubscriber", "onNext:" );
        }
        mEventBus.post( t );
    }
}
