package com.example.itemlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shisong on 2017/3/14.
 */

public class ImageLoader {

    public static void getImage(ImageView imageView) {
        if (!(imageView.getTag() instanceof String)) {
            return;
        }
        String url = (String) imageView.getTag();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new MyCallback(url, imageView));
    }

    private static class MyCallback implements Callback {
        private String url;
        private ImageView imageView;

        MyCallback(String urlStr, ImageView image) {
            url = urlStr;
            imageView = image;
        }

        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (imageView.getTag() instanceof String) {
                if (!url.equals(imageView.getTag())) {
                    return;
                }
            }

            InputStream is = response.body().byteStream();
            final Bitmap bm = BitmapFactory.decodeStream(is);
            is.close();
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bm);
                }
            });
        }
    }
}
