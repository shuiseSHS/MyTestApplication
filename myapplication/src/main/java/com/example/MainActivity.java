package com.example;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;

    private SurfaceHolder holder;

    private Paint paint;

    private boolean start = false;

    private float x = 0;

    private float y = 0;

    private CameraManager mCameraManager;

    private String mCameraId;
    private CameraDevice mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("###", "MainActivity:onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.view_canvas);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        paint.setStrokeWidth(1);
        mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start = true;
//        startDraw();
    }

    public void initCameraAndPreview(View v) {
        Log.d("linc", "init camera and preview");
        try {
            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                mCameraManager.openCamera(mCameraId, deviceStateCallback, null);
            }
        } catch (Exception e) {
            Log.e("linc", "open camera failed." + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            try {
                mCameraManager.openCamera(mCameraId, deviceStateCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e("###", "DeviceStateCallback:onOpened");
            mCamera = camera;
            try {
                mCamera.createCaptureSession(Arrays.asList(holder.getSurface()), sessionCallback, null);
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
//
//    private CameraCaptureSession.CaptureCallback listener = new CameraCaptureSession.CaptureCallback() {
//        @Override
//        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
//            super.onCaptureStarted(session, request, timestamp, frameNumber);
//            Log.e("###", "CameraCaptureSession.CaptureCallback");
//        }
//    };

    private CameraCaptureSession.StateCallback sessionCallback = new CameraCaptureSession.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.e("###", "CameraCaptureSession:onConfigured ");
            try {
                CaptureRequest.Builder builder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.addTarget(holder.getSurface());
                session.setRepeatingRequest(builder.build(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e("###", "CameraCaptureSession:onConfigureFailed ");
        }
    };

    private void startDraw() {
        new Thread() {
            public void run() {
                boolean buffer = false;
                while (start) {
                    Canvas canvas = holder.lockCanvas();
                    if (canvas == null) {
                        continue;
                    }
                    float nx = x + 1;
                    float ny = getY(nx);
                    canvas.drawLine(x, y, nx, ny, paint);

                    if (buffer) {
                        x = nx;
                        y = ny;
                        buffer = false;
                    } else {
                        buffer = true;
                    }
                    SystemClock.sleep(50);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }.start();
    }

    private float getY(float x) {
        return x * x / 100;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("###", "surfaceChanged: " + width + " - " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        start = false;
    }

    public void zoomOut(View v) {
        int w = surfaceView.getWidth();
        int h = surfaceView.getHeight();
        ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        lp.width = (int) (w * 1.1);
        lp.height = (int) (h * 1.1);
        surfaceView.requestLayout();
    }

    public void zoomIn(View v) {
        int w = surfaceView.getWidth();
        int h = surfaceView.getHeight();
        ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        lp.width = (int) (w * 0.9);
        lp.height = (int) (h * 0.9);
        surfaceView.requestLayout();
    }

    public void closeCamera(View view) {
        if (mCamera != null) {
            mCamera.close();
        }
    }
}
