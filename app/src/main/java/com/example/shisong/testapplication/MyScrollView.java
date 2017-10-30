package com.example.shisong.testapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by shisong on 2016/8/17.
 */

public class MyScrollView extends ScrollView {

    private float lastY;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //true List不能滚动
    //false List能滚动
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.e("######", getScrollY() + "  " + canScrollVertically(1) + "   " + canScrollVertically(-1));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = (ev.getRawY() - lastY);
                lastY = ev.getRawY();
                if (dy > 0) { //下拉，显示top
                    if (canScrollVertically(1)) { // 不能继续下拉
                        Log.e("######", ev.getAction() + " false ");
                        return false;
                    } else {
                        return true;
                    }
                } else { // 上滑，隐藏top
                    if (canScrollVertically(-1)) { // 不能继续上滑
                        Log.e("######", ev.getAction() + " false ");
                        return false;
                    } else {
                        return true;
                    }
                }
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getRawY();
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
