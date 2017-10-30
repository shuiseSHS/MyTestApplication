package com.rxjava;

import android.net.Uri;

import java.util.Collections;
import java.util.List;

/**
 * Created by shisong on 2016/11/4.
 */

public class CatsHelper {

    ApiWrapper apiWrapper;

    public AsyncJob<Uri> saveTheCutestCat(final String query) {
        final AsyncJob<List<Cat>> catsListAsyncJob  = apiWrapper.queryCats(query);
        final AsyncJob<Cat> cutestCatAsyncJob = catsListAsyncJob.map(new Func<List<Cat>, Cat>() {
            @Override
            public Cat call(List<Cat> cats) {
                return findCutest(cats);
            }
        });

        AsyncJob<Uri> storedUriAsyncJob = cutestCatAsyncJob.flatMap(new Func<Cat, AsyncJob<Uri>>() {
            @Override
            public AsyncJob<Uri> call(Cat cat) {
                return apiWrapper.store(cat);
            }
        });

//
//        AsyncJob<Uri> storedUriAsyncJob = new AsyncJob<Uri>() {
//            @Override
//            public void start(final Callback<Uri> cutestCatCallback) {
//                cutestCatAsyncJob.start(new Callback<Cat>() {
//                    @Override
//                    public void onResult(Cat cutest) {
//                        apiWrapper.store(cutest).start(new Callback<Uri>() {
//                            @Override
//                            public void onResult(Uri result) {
//                                cutestCatCallback.onResult(result);
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                                cutestCatCallback.onError(e);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        cutestCatCallback.onError(e);
//                    }
//                });
//            }
//        };
        return storedUriAsyncJob;
    }

    private Cat findCutest(List<Cat> cats) {
        return Collections.max(cats);
    }
}