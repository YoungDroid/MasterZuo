package com.oom.masterzuo.base;

import android.databinding.BaseObservable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by xiaojunhuang on 16/3/8.
 */
public class BaseResponse extends BaseObservable implements Serializable {
    public static final String TAG = "YoungDroid";

    @JsonProperty(value = "state")
    int state;
    @JsonProperty(value = "msg")
    String message;


    public String getMessage() {
        return message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
