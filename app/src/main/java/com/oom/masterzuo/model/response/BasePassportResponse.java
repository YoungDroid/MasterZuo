package com.oom.masterzuo.model.response;

import java.io.Serializable;

public class BasePassportResponse implements Serializable {
    
    private int sql_id;
    
    private String response_at;
    private String state;
    
    public String getResponse_at() {
        return response_at;
    }
    
    public String getState() {
        return state;
    }
}
