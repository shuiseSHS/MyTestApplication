package com.example.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shisong on 2017/11/15.
 * 自定义关闭图标
 */
public class IconViewClose extends View {

    private RectF mRectF;
    private int iconSize;
    private boolean circleBg;
    private int circleColor;
    private int circleStrokeColor;
    private int circleStrokeWidth;
    private int circlePadding;
    private int strokeWidth;
    private int lineColor;
    private int lineColorPressed;
    private int lineColor1;
    private int lineColor2;
    private int iconPadding;
    Paint paint1;
    Paint paint2;
    Paint paintC;
    Paint paintCS;

    public IconViewClose(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public IconViewClose(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public IconViewClose(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconViewClose(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    protected void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IconViewClose, defStyleAttr, defStyleRes);
        if (a != null) {
            lineColor = a.getColor(R.styleable.IconViewClose_lineColor, 0xFF333333);
            lineColorPressed = a.getColor(R.styleable.IconViewClose_lineColorPressed, lineColor);
            lineColor1 = a.getColor(R.styleable.IconViewClose_lineColor1, lineColor);
            lineColor2 = a.getColor(R.styleable.IconViewClose_lineColor2, lineColor);

            iconSize = a.getDimensionPixelSize(R.styleable.IconViewClose_iconSize, 0);
            strokeWidth = a.getDimensionPixelSize(R.styleable.IconViewClose_iconStrokeWidth, 0);
            circleStrokeWidth = a.getDimensionPixelSize(R.styleable.IconViewClose_circleStrokeWidth, 0);
            circlePadding = a.getDimensionPixelSize(R.styleable.IconViewClose_circlePadding, 0);

            iconPadding = a.getDimensionPixelSize(R.styleable.IconViewClose_iconPadding, 0);
            circleColor = a.getColor(R.styleable.IconViewClose_circleColor, 0xFF333333);
            circleStrokeColor = a.getColor(R.styleable.IconViewClose_circleStrokeColor, 0xFF333333);
            circleBg = a.getBoolean(R.styleable.IconViewClose_circleBg, false);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (circleBg) {
            initPaintC();
            initPaintCS();
            if (mRectF == null) {
                mRectF = new RectF(circlePadding, circlePadding, getWidth() - circlePadding, getHeight() - circlePadding);
            }
            canvas.drawOval(mRectF, paintC);
            canvas.drawOval(mRectF, paintCS);
        }

        initPaint1();
        initPaint2();
        canvas.drawLine(iconPadding, iconPadding, getWidth() - iconPadding, getHeight() - iconPadding, paint1);
        canvas.drawLine(getWidth() - iconPadding, iconPadding, iconPadding, getHeight() - iconPadding, paint2);
    }

    private void initPaintCS() {
        if (paintCS == null) {
            paintCS = new Paint();
            paintCS.setAntiAlias(true);
            paintCS.setDither(true);
            paintCS.setColor(circleStrokeColor);
            paintCS.setStrokeWidth(circleStrokeWidth);
            paintCS.setStyle(Paint.Style.STROKE);
        }
    }

    private void initPaintC() {
        if (paintC == null) {
            paintC = new Paint();
            paintC.setAntiAlias(true);
            paintC.setDither(true);
            paintC.setColor(circleColor);
            paintC.setStrokeWidth(strokeWidth);
            paintC.setStyle(Paint.Style.FILL);
        }
    }

    private void initPaint2() {
        if (paint2 == null) {
            paint2 = new Paint();
            paint2.setStrokeWidth(strokeWidth);
            paint2.setStrokeCap(Paint.Cap.ROUND);
            paint2.setColor(lineColor2);
        }
    }

    private void initPaint1() {
        if (paint1 == null) {
            paint1 = new Paint();
            paint1.setStrokeWidth(strokeWidth);
            paint1.setStrokeCap(Paint.Cap.ROUND);
            paint1.setColor(lineColor1);
        }
    }
}
