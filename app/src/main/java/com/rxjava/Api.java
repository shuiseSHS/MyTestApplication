package com.rxjava;

import android.net.Uri;

import java.util.List;

/**
 * Created by shisong on 2016/11/4.
 */

public interface Api {
    void queryCats(String query, CatsQueryCallback catsQueryCallback);

    Uri store(Cat cat, StoreCallback storeCallback);

    interface CatsQueryCallback {
        void onCatListReceived(List<Cat> cats);

        void onError(Exception e);
    }

    interface StoreCallback {
        void onCatStored(Uri uri);

        void onStoreFailed(Exception e);
    }
}
