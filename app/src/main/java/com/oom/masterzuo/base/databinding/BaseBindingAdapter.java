package com.oom.masterzuo.base.databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by CcYang on 2016/7/27.
 * Email: 1367496366@qq.com
 */
public abstract class BaseBindingAdapter< T > extends BaseAdapter {
    protected List< T > listData;
    protected final int mItemLayoutId;
    protected Context context;

    public interface OnItemClickListener {
        void onItemClick(int position, Object item);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener( OnItemClickListener listener ) {
        this.listener = listener;
    }

    public BaseBindingAdapter( Context context, int itemLayoutId ) {
        this( context, itemLayoutId, null );
    }

    public BaseBindingAdapter( Context context, int itemLayoutId, Collection< T > datas ) {
        if ( datas == null ) {
            listData = new ArrayList<>();
        } else if ( datas instanceof List ) {
            listData = ( List< T > ) datas;
        } else {
            listData = new ArrayList<>( datas );
        }
        mItemLayoutId = itemLayoutId;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public T getItem( int position ) {
        if ( position >= listData.size() ) return null;
        return listData.get( position );
    }

    @Override
    public long getItemId( int position ) {
        return 0;
    }

    public abstract void convert( ViewDataBinding binding, T item, int position );

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        ViewDataBinding binding;
        if ( convertView == null ) {
            binding = DataBindingUtil.inflate( LayoutInflater.from( context ), mItemLayoutId, parent, false );
            convertView = binding.getRoot();
            convertView.setTag( binding );
        } else {
            binding = ( ViewDataBinding ) convertView.getTag();
        }

        convert( binding, getItem( position ), position );

        if ( listener != null ) {
            convertView.setOnClickListener( v -> listener.onItemClick( position, getItem( position ) ) );
        }

        return convertView;
    }

    public boolean isEnabled( int position ) {
        return position < listData.size();
    }

    public void clear() {
        listData.clear();
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
}
