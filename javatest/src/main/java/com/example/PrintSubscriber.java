package com.example;

import rx.Subscriber;

/**
 * Created by shisong on 2016/11/8.
 */

public class PrintSubscriber extends Subscriber {

    private String TAG;

    PrintSubscriber(String tag) {
        TAG = tag;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {
        System.out.println(TAG + o);
    }
}
