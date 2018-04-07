package com.oom.masterzuo.widget.speciallinearlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/9/15.
 * Mail: xzlight@outlook.com
 */
public class LinearLayoutToListView extends LinearLayout {

    private int i, j;
    private int viewIndex = 0;
    private BaseAdapter adapter;
    private boolean simpleType = false;
    private DataObserver observer;

    public LinearLayoutToListView(Context context) {
        this(context, null);
    }

    public LinearLayoutToListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutToListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWeightSum(1.0f);
        setGravity(Gravity.CENTER);
        observer = new DataObserver();
    }

    public void setSimpleType(boolean simpleType) {
        this.simpleType = simpleType;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        adapter.registerDataSetObserver(observer);
        updateUI();
    }

    public void updateUI() {
        if (adapter != null) {
            viewIndex = 0;
            removeAllViews();
            for (i = 0; i < adapter.getCount(); i++) {
                if (!simpleType) {
                    LayoutParams spanParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    spanParams.weight = 1f / adapter.getCount();
                    addView(adapter.getView(i, null, null), viewIndex, spanParams);
                } else {
                    addView(adapter.getView(i, null, null), viewIndex);
                }
                viewIndex++;
            }
        }
    }

    public class DataObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            updateUI();
        }

        @Override
        public void onInvalidated() {
        }
    }
}
