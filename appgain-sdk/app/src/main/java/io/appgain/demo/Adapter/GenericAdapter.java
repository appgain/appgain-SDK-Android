package io.appgain.demo.Adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class GenericAdapter<T extends Object> extends RecyclerView.Adapter {

    private List<T> items;
    private GenericItemClickCallback<T> adapterItemClickCallbacks;


    public GenericAdapter(List<T> items)
    {
        setItems(items);
    }
    public GenericAdapter(List<T> items,
                          GenericItemClickCallback<T> adapterItemClickCallbacks)
    {
        setItems(items);
        this.adapterItemClickCallbacks = adapterItemClickCallbacks ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(adapterItemClickCallbacks != null){
            final T item = getItem(position);
            if(item != null){
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       adapterItemClickCallbacks.onItemClicked(item);
                   }
               });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    public void removeItems(@Nullable List<T> itemsToRemove){
        if(items != null && !items.isEmpty()){
            if(itemsToRemove != null && !itemsToRemove.isEmpty()){
                items.removeAll(itemsToRemove);
                notifyDataSetChanged();
            }
        }
    }

    public void removeAllWithAnimation(){
        if(items != null && !items.isEmpty()){
            int start = 0;
            int count = items.size();
            items = new ArrayList<>();
            notifyItemRangeRemoved(start, count);
        }
    }

    public void updateData(List<T> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    protected void setItems(List<T> is){
        if (is!=null){
            this.items = new ArrayList<>() ;
            this.items.addAll(is) ;
        }
    }

    @Nullable
    public Integer getItemId(T item){
        if(items == null || items.isEmpty()){
            return null;
        }
        return items.indexOf(item);
    }

    public List<T> getItems() {
        return items;
    }

    public GenericItemClickCallback<T> getAdapterItemClickCallbacks() {
        return adapterItemClickCallbacks;
    }

    @Nullable
    public T getItem(int position){
        if(items != null && position >= 0 && position < items.size()){
            return items.get(position);
        }
        return null;
    }

    public boolean isDataSetEmpty(){
        return items == null || items.isEmpty();
    }

    public void  push(T object){
        if (items==null){
            items = new ArrayList<>() ;
        }
        items.add(0,object) ;
        this.notifyItemInserted(0);

    }

    public void  add(T object){
        if (items==null){
            items = new ArrayList<>() ;
        }
        items.add(object) ;
        this.notifyItemInserted(items.size()-1);

    }

    public  void updateItem(int pos  , T object ) {
        items.set(pos , object) ;
        notifyItemChanged(pos);
    }

    protected View inflate(int viewResId, ViewGroup parent){
        return LayoutInflater.from(parent.getContext())
                .inflate(viewResId, parent, false);
    }


    public  void removeItem(int position){
        if (!isDataSetEmpty()){
            items.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void removeItem(T item) {
        if (!isDataSetEmpty()){
            int position  =  items.indexOf(item) ;
            Log.e("removeItem" ,      items.remove(item) + " position " + position) ;
            notifyItemRemoved(position);
        }
    }
}
