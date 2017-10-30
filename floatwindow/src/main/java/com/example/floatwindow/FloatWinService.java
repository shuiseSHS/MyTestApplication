package com.example.floatwindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by shisong on 2017/1/23.
 *
 */
public class FloatWinService extends Service {

    //定义浮动窗口布局
    View mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    Button mFloatView;

    ImageView mImageThumb;

    private static final String TAG = "FxService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreat");
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/

        final LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (Button) mFloatLayout.findViewById(R.id.float_id);
        mImageThumb = (ImageView) mFloatLayout.findViewById(R.id.thumb);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);

        //设置监听浮动窗口的触摸移动
        mFloatLayout.findViewById(R.id.icon_drag).setOnTouchListener(dragListener);

        mFloatView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FloatWinService.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private View.OnTouchListener dragListener = new View.OnTouchListener() {
        float startX;
        float startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                    wmParams.x += (int) (event.getRawX() - startX);
                    Log.i(TAG, "RawX " + event.getRawX());
                    Log.i(TAG, "X " + event.getX());
                    //减25为状态栏的高度
                    wmParams.y += (int) (event.getRawY() - startY);
                    Log.i(TAG, "RawY " + event.getRawY());
                    Log.i(TAG, "Y " + event.getY());
                    //刷新
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);

                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    updateThumb();
                    break;
            }
            return true;  //此处必须返回false，否则OnClickListener获取不到监听
        }
    };

    private void updateThumb() {
//        mWindowManager.getDefaultDisplay().

        View rootView = mFloatLayout.getRootView();
        rootView.buildDrawingCache();
        mImageThumb.setImageBitmap(rootView.getDrawingCache());

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mediaProjectionManager.createScreenCaptureIntent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }
}
