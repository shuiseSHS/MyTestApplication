package com.example;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.camera.CustomCameraActivity;
import com.example.utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by shisong on 2017/4/24.
 */

public class CameraActivity extends Activity {

    private static final int RESULT_CAPTURE_IMAGE = 2;
    private static final String TAG = "###";
    private ImageView img;
    private TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置允许通过ActivityOptions.makeSceneTransitionAnimation发送或者接收Bundle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            //设置使用TransitionManager进行动画，不设置的话系统会使用一个默认的TransitionManager
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        setContentView(R.layout.activity_camera_test);
        text = (TextView) findViewById(R.id.text);
        img = (ImageView) findViewById(R.id.img);
    }

    public void toCamera(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 99);
        } else {
            fsdfsdfsd();
        }
    }

    public void reload(View v) {
        File fileDir = ddd(this, "test");
        File tmpFile = new File(fileDir, "temp.jpg");
        Uri uri = Uri.fromFile(tmpFile);
        Log.d(TAG, uri.toString());

        ContentResolver cr = getContentResolver();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            img.setImageBitmap(bitmap);
            text.setText(bitmap.getWidth() + " X " + bitmap.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fsdfsdfsd();
    }

    private void fsdfsdfsd() {
        File fileDir = ddd(this, "test");
        File tmm = new File(fileDir, "temp.jpg");
        Uri uri = Uri.fromFile(tmm);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }

    private File ddd(Context mContext, String subFolder) {

        //扫卡超时了或扫不出卡(权限问题或File.canWrite调用返回错误)
        try {
            /**
             * mContext.getExternalFilesDir() sometimes will throw java.lang.NullPointerException
             * at android.os.Parcel.readException(Parcel.java:1471)
             * at android.os.Parcel.readException(Parcel.java:1419)
             * at android.os.storage.IMountService$Stub$Proxy.mkdirs(IMountService.java:750)
             * <href>https://code.google.com/p/android/issues/detail?id=62119</href>
             */
            File file = mContext.getExternalFilesDir(subFolder);
            if (file != null && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //非空且是挂载状态，透传系统API
                return file;
            }
        } catch (Exception e) {
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == RESULT_CAPTURE_IMAGE) {
            if (data != null && data.getData() != null) {
                Log.d(TAG, data.getData().toString());
            } else {
                File fileDir = ddd(this, "test");
                File tmpFile = new File(fileDir, "temp.jpg");
//                BitmapUtils.resetImageDegree(tmpFile.getAbsoluteFile());

                Uri uri = Uri.fromFile(tmpFile);
                Log.d(TAG, uri.toString());
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    BitmapUtils.compressBitmapToFile(bitmap, tmpFile, 244, 50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void customCamera(View view) {
        startActivity(new Intent(this, CustomCameraActivity.class));
    }
}
