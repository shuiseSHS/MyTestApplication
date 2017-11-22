package com.example.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.customview.utils.UIUtils;

/**
 * Created by shisong on 2017/11/22.
 */
public class BackgroundView extends View {

    private Paint paint1;
    private int rectSize;

    public BackgroundView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public BackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public BackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int i) {
        paint1 = new Paint();
        paint1.setColor(0xFFD2D2D2);
        rectSize = UIUtils.dip2px(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 255, 255, 255);
        float left, top, right, bottom;
        int row = 0, column = 0;
        while (true) {
            left = column * rectSize;
            right = (column + 1) * rectSize;
            top = row * rectSize;
            bottom = (row + 1) * rectSize;
            canvas.drawRect(left, top, right, bottom, paint1);

            column += 2;
            if (column * rectSize > getWidth()) {
                row += 1;
                if (row * rectSize > getHeight()) {
                    break;
                }
                column = row % 2;
            }
        }
    }
}
