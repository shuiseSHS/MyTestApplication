package com.rxjava;

/**
 * Created by shisong on 2016/11/4.
 */
public interface Callback<T> {
    void onResult(T result);
    void onError(Exception e);
}