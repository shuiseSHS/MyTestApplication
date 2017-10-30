package com.example;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;

import java.io.IOException;

/**
 * Created by shisong on 2017/1/10.
 * 旧相机
 */
@SuppressWarnings("deprecation")
public class TextureViewTest extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView myTexture;
    private Camera mCamera;
    private ImageView imgView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imgView = (ImageView) findViewById(R.id.img);
        myTexture = (TextureView) findViewById(R.id.textureView1);
        myTexture.setSurfaceTextureListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1, int arg2) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
        // TODO Auto-generated method stub
    }

    public void zoomOut(View v) {
        myTexture.setAlpha((float) (myTexture.getAlpha() * 1.1));
    }

    public void zoomIn(View v) {
        myTexture.setAlpha((float) (myTexture.getAlpha() * 0.9));
    }

    public void initCameraAndPreview(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            return;
        }

        if (mCamera != null) {
            return;
        }

        try {
            mCamera = Camera.open();
            setCameraDisplayOrientation(0, mCamera);
            mCamera.setPreviewTexture(myTexture.getSurfaceTexture());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initCameraAndPreview(null);
        }
    }

    private void setCameraDisplayOrientation(int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void closeCamera(View view) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void getPic(View view) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setJpegQuality(100);
            parameters.setRotation(90);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setPictureSize(1920, 1080);
            mCamera.setParameters(parameters);
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imgView.setImageBitmap(img);
                    mCamera.startPreview();
                }
            });
        }
    }

}
