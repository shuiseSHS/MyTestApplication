package com.example.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;
import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shisong on 2017/7/5.
 * 自定义相机
 */
public class CustomCameraActivity extends Activity {

    private JCameraView jCameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_camera);

        jCameraView = (JCameraView) findViewById(R.id.jcameraview);
        //设置视频保存路径
//        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);

        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorLisenter() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
            }
        });

        jCameraView.setJCameraLisenter(new JCameraLisenter() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());

                File fileDir = ddd(CustomCameraActivity.this, "test");
                File tmpFile = new File(fileDir, "temp.jpg");
                saveBitmap(tmpFile.getAbsolutePath(), bitmap);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Log.i("CJT", "url = " + url);
            }

            @Override
            public void quit() {
                //退出按钮
                finish();
            }
        });
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

    /**
     * 将bitmap写入文件
     *
     * @param path
     * @param img
     */
    public static void saveBitmap(String path, Bitmap img) {
        FileOutputStream ops = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ops = new FileOutputStream(path);
            img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            ops.write(baos.toByteArray());
            ops.flush();
        } catch (IOException e) {
        } finally {
        }
    }

}
