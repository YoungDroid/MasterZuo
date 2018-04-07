package com.oom.masterzuo.event;

import retrofit2.Call;

/**
 * Created by xiaojunhuang on 16/3/18.
 */
public class RequestErrorEvent {
    private int code;
    private String message;
    private Object t;
    private Call call;

    public Call getCall() {
        return call;
    }

    public RequestErrorEvent(int code, String message, Object t,Call call) {
        this.code = code;
        this.message = message;
        this.t = t;
        this.call = call;
    }

    public Object getReponse() {
        return t;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
