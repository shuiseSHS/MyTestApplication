package com.rxjava;

import android.net.Uri;

import java.util.List;

/**
 * Created by shisong on 2016/11/4.
 */
public class ApiWrapper {

    Api api;

    public AsyncJob<List<Cat>> queryCats(final String query){
        return new AsyncJob<List<Cat>>() {
            @Override
            public void start(final Callback<List<Cat>> callback) {
                api.queryCats(query, new Api.CatsQueryCallback() {
                    @Override
                    public void onCatListReceived(List<Cat> cats) {
                        callback.onResult(cats);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }

    public AsyncJob<Uri> store(final Cat cat){
        return new AsyncJob<Uri>() {
            @Override
            public void start(final Callback<Uri> uriCallback) {
                api.store(cat, new Api.StoreCallback() {
                    @Override
                    public void onCatStored(Uri uri) {
                        uriCallback.onResult(uri);
                    }

                    @Override
                    public void onStoreFailed(Exception e) {
                        uriCallback.onError(e);
                    }
                });
            }
        };
    }
}