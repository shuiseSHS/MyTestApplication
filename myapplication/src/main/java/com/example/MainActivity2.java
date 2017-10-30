package com.example;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.camera.CameraTool;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity2 extends Activity {
    private TextureView myTexture;
    private CameraTool cameraTool;
    private ImageView imageView;
    private TextView textView;
    private EditText editFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("###", "MainActivity:onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myTexture = (TextureView) findViewById(R.id.textureView1);
        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.text);
        editFee = (EditText) findViewById(R.id.edit_fee);
        editFee.setFilters(new InputFilter[]{ddddddd});
    }

    private InputFilter ddddddd = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            if (dValue.contains(".")) {
                int n = dValue.substring(dValue.indexOf(".") + 1).length();
                if (n >= 2) {
                    return "";
                }
            }
            return source;
        }
    };

    public void showRewardSuccessDialog(View v) {
        final View rootView = LayoutInflater.from(this).inflate(R.layout.layout_payment_success, null);
        ((TextView) rootView.findViewById(R.id.title)).setText("谢谢壕！");
        ((TextView) rootView.findViewById(R.id.words)).setText("淡淡的淡淡的淡淡的淡淡的淡淡的淡淡等等等等等等等等等等等等等等");

        final Dialog finalDialog = new Dialog(this);
        rootView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        finalDialog.setContentView(rootView);
        finalDialog.setCancelable(true);
        finalDialog.show();
        if (finalDialog.getWindow() != null) {
            WindowManager.LayoutParams layoutParams = finalDialog.getWindow().getAttributes();
            layoutParams.width = 870;
            layoutParams.height = 600;
            finalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void initCameraAndPreview(View v) {
        Log.d("linc", "init camera and preview");
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                openCamera();
            }
        } catch (Exception e) {
            Log.e("linc", "open camera failed." + e.getMessage());
        }
    }

    public void closeCamera(View view) {
        if (cameraTool != null) {
            cameraTool.closeCamera();
        }
    }

    private void openCamera() {
        if (cameraTool == null) {
            cameraTool = new CameraTool(this);
        }
        cameraTool.openCamera(new Surface(myTexture.getSurfaceTexture()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }

    public void zoomOut(View v) {
//        int w = myTexture.getWidth();
//        int h = myTexture.getHeight();
//        ViewGroup.LayoutParams lp = myTexture.getLayoutParams();
//        lp.width = (int) (w * 1.1);
//        lp.height = (int) (h * 1.1);
//        myTexture.requestLayout();
        myTexture.setAlpha((float) (myTexture.getAlpha() * 1.1));
    }

    public void zoomIn(View v) {
//        int w = myTexture.getWidth();
//        int h = myTexture.getHeight();
//        ViewGroup.LayoutParams lp = myTexture.getLayoutParams();
//        lp.width = (int) (w * 0.9);
//        lp.height = (int) (h * 0.9);
//        myTexture.requestLayout();
        myTexture.setAlpha((float) (myTexture.getAlpha() * 0.9));
    }

//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        Log.e("###", "onSurfaceTextureUpdated: " + surface);
//    }

    public void getPic(View v) {
        if (cameraTool != null) {
            cameraTool.getImage(new CameraTool.IGetImage() {
                @Override
                public void getImage(Bitmap img) {
                    imageView.setImageBitmap(img);
                }
            });
        }
    }

    public void download(View view) {
        int[] ls = new int[2];
        int[] ls1 = new int[2];
        imageView.getLocationInWindow(ls);
        imageView.getLocationOnScreen(ls1);

        String c = "getLocationInWindow: x=" + ls[0] + " ,y=" + ls[1] + "\n" +
                "getLocationOnScreen: x=" + ls1[0] + " ,y=" + ls1[1];
        textView.setText(c);

        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View v = View.inflate(this, R.layout.popup, null);
        View block = v.findViewById(R.id.block);
//        if (android.os.Build.VERSION.SDK_INT <= 21) { // 5.0及以下版本，PopupWindow被StatusBar挡住
//            Rect rectangle = new Rect();
//            Window window = getWindow();
//            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
//            int statusBarHeight = rectangle.top;
//            ViewGroup.LayoutParams lp = block.getLayoutParams();
//            lp.height = statusBarHeight;
//            block.requestLayout();
//        }

        popupWindow.setContentView(v);
        popupWindow.setTouchable(true);
        popupWindow.setClippingEnabled(true);
        popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });

        popupWindow.showAtLocation(imageView, Gravity.BOTTOM, 0, 0);

    }

    public void bottom_popup(View view) {
        final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = View.inflate(this, R.layout.popup_bottom, null);
        popupWindow.setContentView(v);
        popupWindow.setClippingEnabled(false);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
        popupWindow.showAtLocation(imageView, Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
    }
}
