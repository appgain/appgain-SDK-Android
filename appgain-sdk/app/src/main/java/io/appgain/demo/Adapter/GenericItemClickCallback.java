package io.appgain.demo.Adapter;

/**
 * Created by Ahmed on 10/25/2017.
 */

public interface GenericItemClickCallback<T> {
    void onItemClicked(T item);
}
