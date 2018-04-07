package com.oom.masterzuo.base.databinding;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by CcYang on 2016/7/22.
 * Email: 1367496366@qq.com
 */
public class BaseBindingHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;

    public BaseBindingHolder( View itemView ) {
        super( itemView );
    }

    public BaseBindingHolder( View itemView, Context context ) {
        super( itemView );
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding( ViewDataBinding binding ) {
        this.binding = binding;
    }
}
