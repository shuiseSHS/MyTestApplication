package com.example.floatwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    //开启悬浮窗的Service
    Intent floatWinIntent;
    private MediaProjectionManager mMediaProjectionManager;
    private Intent intent = null;
    private int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatWinIntent = new Intent(this, FloatWinService.class);

        mMediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startIntent();
    }

    private void startIntent() {
        if (intent != null && result != 0) {
            ((ShotApplication) getApplication()).setResult(result);
            ((ShotApplication) getApplication()).setIntent(intent);
            Intent intent = new Intent(getApplicationContext(), Service1.class);
            startService(intent);
        } else {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            ((ShotApplication) getApplication()).setMediaProjectionManager(mMediaProjectionManager);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                    //启动FxService
                    startService(floatWinIntent);
                }
            }
        }

        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                result = resultCode;
                intent = data;
                ((ShotApplication) getApplication()).setResult(resultCode);
                ((ShotApplication) getApplication()).setIntent(data);
                Intent intent = new Intent(getApplicationContext(), Service1.class);
                startService(intent);
                finish();
            }
        }
    }

    /**
     * 按下begin按钮
     */
    public void begin(View v) {
        //开启悬浮框前先请求权限
        askForPermission();
    }

    /**
     * 按下end按钮
     */
    public void end(View v) {
        //关闭悬浮框
        stopService(floatWinIntent);
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                startService(floatWinIntent);
            }
        } else {
            startService(floatWinIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
