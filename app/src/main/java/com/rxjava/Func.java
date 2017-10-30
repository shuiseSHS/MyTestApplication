package com.rxjava;

/**
 * Created by shisong on 2016/11/4.
 */

public interface Func<T, R> {
    R call(T t);
}