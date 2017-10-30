package com.example.rxdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "###";

    private static Map<String, Bitmap> memoryCache = new HashMap<>();

    private File storagePath;

    private static final String imgUrl1 = "http://img2.imgtn.bdimg.com/it/u=2591196462,3184594728&fm=21&gp=0.jpg";
    private static final String imgUrl2 = "http://img0.imgtn.bdimg.com/it/u=929895057,7403865&fm=21&gp=0.jpg";
    private static final String imgUrl3 = "http://img2.imgtn.bdimg.com/it/u=1777204395,35897921&fm=21&gp=0.jpg";
    private static final String imgUrl4 = "http://img2.imgtn.bdimg.com/it/u=811666999,891378235&fm=21&gp=0.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storagePath = getCacheDir();
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                updateImg(imgUrl1);
                break;
            case R.id.btn_2:
                updateImg(imgUrl2);
                break;
            case R.id.btn_3:
                updateImg(imgUrl3);
                break;
            case R.id.btn_4:
                updateImg(imgUrl4);
                break;
        }
    }

    private void updateImg(final String imgUrl) {

        Observable.concat(getImageFromMemCache(imgUrl), getImageFromDisk(imgUrl), getImageFromNet(imgUrl))
                .first()
                .onErrorReturn(new Func1<Throwable, Bitmap>() {
                    @Override
                    public Bitmap call(Throwable throwable) {
                        return null;
                    }
                })
                .doOnNext(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        memoryCache.put(imgUrl, bitmap);
                        File f = new File(storagePath, imgUrl.hashCode() + "");
                        try {
                            if (f.createNewFile()) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(f));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError " + e);
                    }
                    @Override
                    public void onNext(Bitmap bitmap) {
                        Log.e(TAG, "onNext " + bitmap);
                        ((ImageView)findViewById(R.id.img_content)).setImageBitmap(bitmap);
                    }
                });

    }

    Observable<Bitmap> getImageFromMemCache(final String imgUrl) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Log.e(TAG, "getImageFromMemCache");
                Bitmap bitmap = memoryCache.get(imgUrl);
                if (bitmap != null) {
                    subscriber.onNext(bitmap);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    Observable<Bitmap> getImageFromDisk(final String imgUrl) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Log.e(TAG, "getImageFromDisk");
                File f = new File(storagePath, imgUrl.hashCode() + "");
                if (f.exists()) {
                    try {
                        subscriber.onNext(BitmapFactory.decodeStream(new FileInputStream(f)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    Observable<Bitmap> getImageFromNet(final String imgUrl) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Log.e(TAG, "getImageFromNet");
                try {
                    URL url = new URL(imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    subscriber.onNext(BitmapFactory.decodeStream(conn.getInputStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        memoryCache.clear();
    }
}
