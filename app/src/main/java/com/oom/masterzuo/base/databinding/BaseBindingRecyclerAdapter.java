package com.oom.masterzuo.base.databinding;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by CcYang on 2016/7/22.
 * Email: 1367496366@qq.com
 */
public abstract class BaseBindingRecyclerAdapter< T > extends RecyclerView.Adapter< BaseBindingHolder > {

    protected List< T > listData;
    protected final int mItemLayoutId;
    protected Activity activity;
    protected FragmentManager fragmentManager;
    protected Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemClick( View view, Object listData, int position );
    }

    public BaseBindingRecyclerAdapter( Context context, int itemLayoutId ) {
        this( null, null, itemLayoutId, null );
        this.context = context;
    }

    public BaseBindingRecyclerAdapter( Activity activity, FragmentManager fragmentManager, int itemLayoutId ) {
        this( activity, fragmentManager, itemLayoutId, null );
    }

    public BaseBindingRecyclerAdapter( Activity activity, FragmentManager fragmentManager, int itemLayoutId, Collection< T > datas ) {
        if ( datas == null ) {
            listData = new ArrayList<>();
        } else if ( datas instanceof List ) {
            listData = ( List< T > ) datas;
        } else {
            listData = new ArrayList<>( datas );
        }
        mItemLayoutId = itemLayoutId;

        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public BaseBindingHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        ViewDataBinding binding = DataBindingUtil.inflate( LayoutInflater.from( parent.getContext() ), mItemLayoutId, parent, false );
        BaseBindingHolder holder = new BaseBindingHolder( binding.getRoot() );
        holder.setBinding( binding );
        return holder;
    }

    public abstract void convert( BaseBindingHolder holder, T item, int position );

    @Override
    public void onBindViewHolder( BaseBindingHolder holder, int position ) {
        convert( holder, listData.get( position ), position );
        holder.itemView.setOnClickListener( getOnClickListener( position ) );
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public T getItem( int position ) {
        if ( position >= listData.size() )
            return null;
        return listData.get( position );
    }

    public List< T > getListData() {
        return listData;
    }

    public void setOnItemClickListener( OnItemClickListener l ) {
        listener = l;
    }

    public View.OnClickListener getOnClickListener( final int position ) {
        return v -> {
            if ( listener != null && v != null ) {
                listener.onItemClick( v, listData.get( position ), position );
            }
        };
    }

    public BaseBindingRecyclerAdapter< T > refresh( Collection< T > datas ) {
        if ( datas == null ) {
            listData = new ArrayList<>();
        } else if ( datas instanceof List ) {
            listData = ( List< T > ) datas;
        } else {
            listData = new ArrayList<>( datas );
        }
        return this;
    }

    public boolean isEnabled( int position ) {
        return position < listData.size();
    }
    
    public void add( T elem ) {
        listData.add( elem );
        notifyDataSetChanged();
    }
    
    public void addAll( List< T > elem ) {
        listData.addAll( elem );
        notifyDataSetChanged();
    }
    
    public void set( T oldElem, T newElem ) {
        set( listData.indexOf( oldElem ), newElem );
    }
    
    public void set( int index, T elem ) {
        listData.set( index, elem );
        notifyDataSetChanged();
    }
    
    public void remove( T elem ) {
        listData.remove( elem );
        notifyDataSetChanged();
    }
    
    public void remove( int index ) {
        listData.remove( index );
        notifyDataSetChanged();
    }
    
    public void replaceAll( List< T > elem ) {
        listData.clear();
        listData.addAll( elem );
        notifyDataSetChanged();
    }
    
    public boolean contains( T elem ) {
        return listData.contains( elem );
    }
    
    /**
     * Clear listData list
     */
    public void clear() {
        listData.clear();
        notifyDataSetChanged();
    }
}
