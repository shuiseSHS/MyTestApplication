package com.example.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.R;

/**
 * Created by shisong on 2017/4/17.
 * 测试
 */
public class FlowView extends View {

    private int value_b;
    private boolean value_a;
    private String value_c;
    private Paint rectPaint;
    private Paint textPaint;
    private Rect rect;

    public FlowView(Context context) {
        super(context);
    }

    public FlowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        readAttrs(context, attrs);
    }

    public FlowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);
    }

    private void readAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.shsitem, 0, 0);
        value_a = t.getBoolean(R.styleable.shsitem_value_a, false);
        value_b = t.getInt(R.styleable.shsitem_value_b, 0);
        value_c = t.getString(R.styleable.shsitem_value_c);
        t.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rectPaint == null) {
            rectPaint = new Paint();
            rectPaint.setStrokeJoin(Paint.Join.ROUND);
            rectPaint.setStrokeWidth(20);
            rectPaint.setStyle(Paint.Style.STROKE);
            rectPaint.setColor(0xFF000000);
        }
        if (rect == null) {
            rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        }
        canvas.drawRect(rect, rectPaint);

        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setStrokeJoin(Paint.Join.ROUND);
            textPaint.setStrokeWidth(1);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setColor(0xFF000000);
            textPaint.setTextSize(value_b);
        }
        canvas.drawText(value_c, 50, 50, textPaint);
    }
}
