package com.oom.masterzuo.passportapi;

import com.oom.masterzuo.model.response.BasePassportResponse;

import rx.functions.Func1;

public class PassportCallBack< T extends BasePassportResponse > implements Func1< T, T > {
    
    @Override
    public T call( T t ) {
        
        if ( null != t && !"SUCCESS".equals( t.getState() ) ) {
            throw new ApiException( t.getState() );
        }
        
        return t;
    }
}
