package com.oom.masterzuo.base;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by xiaojunhuang on 16/3/30.
 */
public class BaseEventCallbackResponse extends BaseResponse {
    @JsonProperty(value = "data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @JsonProperty(value = "int_state")
        private int intState;

        public int getIntState() {
            return intState;
        }
    }
}
