package com.oom.masterzuo.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
public abstract class ZBaseRecyclerAdapter<T> extends RecyclerView.Adapter<ZRecyclerHolder> {
    protected List<T> realDatas;
    protected final int mItemLayoutId;
    protected boolean isScrolling;
    protected Context cxt;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick( View view, Object data, int position );
    }

    public ZBaseRecyclerAdapter( RecyclerView v, Collection<T> datas, int itemLayoutId) {
        if (datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        mItemLayoutId = itemLayoutId;
        cxt = v.getContext();

//        v.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
//                if (!isScrolling) {
//                    notifyDataSetChanged();
//                }
//            }
//        });
    }

    /**
     * Recycler适配器填充方法
     *
     * @param holder      viewholder
     * @param item        javabean
     * @param isScrolling RecyclerView是否正在滚动
     */
    public abstract void convert(ZRecyclerHolder holder, T item, int position, boolean isScrolling);

    @Override
    public ZRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cxt);
        View root = inflater.inflate(mItemLayoutId, parent, false);
        return new ZRecyclerHolder(root, cxt);
    }

    @Override
    public void onBindViewHolder(ZRecyclerHolder holder, int position) {
        convert(holder, realDatas.get(position), position, isScrolling);
        holder.itemView.setOnClickListener(getOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return realDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(@Nullable View v) {
                if (listener != null && v != null) {
                    listener.onItemClick(v, realDatas.get(position), position);
                }
            }
        };
    }

    public ZBaseRecyclerAdapter<T> refresh(Collection<T> datas) {
        if (datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        return this;
    }
}
