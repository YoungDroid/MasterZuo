package com.oom.masterzuo.base.databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

/**
 * Created by CcYang on 2016/7/27.
 * Email: 1367496366@qq.com
 */
public abstract class BaseSwipeMenuAdapter< T > extends SwipeMenuAdapter< BaseBindingHolder > {
    
    protected List< T > listData;
    protected final int mItemLayoutId;
    protected Context context;
    private ViewDataBinding binding;
    
    public interface OnItemClickListener {
        
        void onItemClick( View view, Object listData, int position );
    }
    
    private OnItemClickListener listener;
    
    public void setOnItemClickListener( OnItemClickListener listener ) {
        this.listener = listener;
    }
    
    public View.OnClickListener getOnClickListener( final int position ) {
        return v -> {
            if ( listener != null && v != null ) {
                listener.onItemClick( v, listData.get( position ), position );
            }
        };
    }
    
    public BaseSwipeMenuAdapter( Context context, int itemLayoutId ) {
        this( context, itemLayoutId, null );
    }
    
    public BaseSwipeMenuAdapter( Context context, int itemLayoutId, Collection< T > datas ) {
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
    
    public T getItem( int position ) {
        if ( position >= listData.size() )
            return null;
        return listData.get( position );
    }
    
    @Override
    public long getItemId( int position ) {
        return 0;
    }
    
    public abstract void convert( ViewDataBinding binding, T item, int position );
    
    @Override
    public int getItemCount() {
        return null == listData ? 0 : listData.size();
    }
    
    @Override
    public View onCreateContentView( ViewGroup parent, int viewType ) {
        binding = DataBindingUtil.inflate( LayoutInflater.from( context ), mItemLayoutId, parent, false );
        return binding.getRoot();
    }
    
    @Override
    public BaseBindingHolder onCompatCreateViewHolder( View realContentView, int viewType ) {
        BaseBindingHolder viewHolder = new BaseBindingHolder( realContentView );
        viewHolder.setBinding( binding );
        if ( Debug ) {
            Log.e( "BaseSwipeMenuAdapter", "onCompatCreateViewHolder: " + (null == realContentView ) );
            Log.e( "BaseSwipeMenuAdapter", "onCompatCreateViewHolder: " + (null == binding ) );
            Log.e( "BaseSwipeMenuAdapter", "onCompatCreateViewHolder: " + (null == viewHolder.getBinding()) );
        }
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder( BaseBindingHolder holder, int position ) {
        if ( Debug ) {
            Log.e( "BaseSwipeMenuAdapter", "onBindViewHolder: " + (null == holder.getBinding()) );
        }
        convert( holder.getBinding(), listData.get( position ), position );
        holder.itemView.setOnClickListener( getOnClickListener( position ) );
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
