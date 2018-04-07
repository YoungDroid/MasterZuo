package com.oom.masterzuo.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaojunhuang on 16/3/8.
 */
public class BaseListResponse< T > extends BaseResponse {
    @SerializedName("data")
    private List< T > data;
    @SerializedName("page")
    private int page;

    public int getPage() {
        return page;
    }

    public List< T > getData() {
        return data;
    }
}
