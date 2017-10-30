package com.example.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by shisong on 2017/1/10.
 */
@SuppressWarnings("MissingPermission")
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraTool {

    private CameraDevice mCamera;

    private CameraManager mCameraManager;

    private CameraCaptureSession mCameraSession;

    private Surface mSurface;

    private ImageReader imageReader;

    private IGetImage mGetImage;

    private Context mContext;

    public CameraTool(Context context) {
        mContext = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        imageReader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, ImageFormat.JPEG, 1);
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public void openCamera(Surface surface) {
        Log.d("###", "init camera and preview");
        mSurface = surface;
        try {
            String mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
//            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
//                return;
//            }
            mCameraManager.openCamera(mCameraId, deviceStateCallback, null);
        } catch (Exception e) {
            Log.e("###", "open camera failed." + e.getMessage());
        }
    }

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e("###", "DeviceStateCallback:onOpened");
            mCamera = camera;
            try {
                mCamera.createCaptureSession(Arrays.asList(mSurface, imageReader.getSurface()), previewSession, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.e("###", "DeviceStateCallback:onDisconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e("###", "DeviceStateCallback:onError " + error);
            camera.close();
        }
    };

    private CameraCaptureSession.StateCallback previewSession = new CameraCaptureSession.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.e("###", "CameraCaptureSession:onConfigured ");
            mCameraSession = session;
            try {
                CaptureRequest.Builder builder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.addTarget(mSurface);
                mCameraSession.setRepeatingRequest(builder.build(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e("###", "CameraCaptureSession:onConfigureFailed ");
        }
    };

    public void getImage(IGetImage listener) {
        mGetImage = listener;
        try {
            CaptureRequest.Builder builder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(imageReader.getSurface());
            mCameraSession.capture(builder.build(), captureCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            Log.e("###", "CameraCaptureSession:onConfigured ");
            Image image = imageReader.acquireLatestImage();
            Log.e("###", "image.getHeight() " + image.getHeight());
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            buffer.clear();
            image.close();
            if (mGetImage != null) {
                mGetImage.getImage(bitmap);
            }
        }
    };

    public void closeCamera() {
        if (mCamera != null) {
            mCamera.close();
        }
    }

    public interface IGetImage {
        void getImage(Bitmap img);
    }
}
