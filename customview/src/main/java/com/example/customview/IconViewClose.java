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
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shisong on 2017/11/15.
 * 自定义关闭图标
 */
public class IconViewClose extends View {

    private RectF mRectF;
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

    private boolean isPressed;

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

            strokeWidth = a.getDimensionPixelSize(R.styleable.IconViewClose_iconStrokeWidth, -1);
            circleStrokeWidth = a.getDimensionPixelSize(R.styleable.IconViewClose_circleStrokeWidth, -1);
            circlePadding = a.getDimensionPixelSize(R.styleable.IconViewClose_circlePadding, 0);

            iconPadding = a.getDimensionPixelSize(R.styleable.IconViewClose_iconPadding, 0);
            circleColor = a.getColor(R.styleable.IconViewClose_circleColor, 0xFF333333);
            circleStrokeColor = a.getColor(R.styleable.IconViewClose_circleStrokeColor, 0xFF333333);
            circleBg = a.getBoolean(R.styleable.IconViewClose_circleBg, false);
            a.recycle();
        }

        initPaint1();
        initPaint2();
        initPaintC();
        initPaintCS();
        mRectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRectF.set(circlePadding, circlePadding, getWidth() - circlePadding, getHeight() - circlePadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed) {
            return;
        }

        if (circleBg) {
            canvas.drawOval(mRectF, paintC);
            if (circleStrokeWidth > 0) {
                canvas.drawOval(mRectF, paintCS);
            }
        }
        canvas.drawLine(iconPadding, iconPadding, getWidth() - iconPadding, getHeight() - iconPadding, paint1);
        canvas.drawLine(getWidth() - iconPadding, iconPadding, iconPadding, getHeight() - iconPadding, paint2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
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
            paint2.setAntiAlias(true);
            paint2.setDither(true);
            paint2.setStrokeWidth(strokeWidth);
            paint2.setStrokeCap(Paint.Cap.ROUND);
            paint2.setColor(lineColor2);
        }
    }

    private void initPaint1() {
        if (paint1 == null) {
            paint1 = new Paint();
            paint1.setAntiAlias(true);
            paint1.setDither(true);
            paint1.setStrokeWidth(strokeWidth);
            paint1.setStrokeCap(Paint.Cap.ROUND);
            paint1.setColor(lineColor1);
        }
    }

    public void setCircleBg(boolean circleBg) {
        this.circleBg = circleBg;
        rePaint();
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        rePaint();
    }

    public void setCircleStrokeColor(int circleStrokeColor) {
        this.circleStrokeColor = circleStrokeColor;
        rePaint();
    }

    public void setCircleStrokeWidth(int circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
        rePaint();
    }

    public void setCirclePadding(int circlePadding) {
        this.circlePadding = circlePadding;
        rePaint();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        rePaint();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        this.lineColor1 = lineColor;
        this.lineColor2 = lineColor;
        rePaint();
    }

    public void setLineColorPressed(int lineColorPressed) {
        this.lineColorPressed = lineColorPressed;
        rePaint();
    }

    public void setLineColor1(int lineColor1) {
        this.lineColor1 = lineColor1;
        rePaint();
    }

    public void setLineColor2(int lineColor2) {
        this.lineColor2 = lineColor2;
        rePaint();
    }

    public void setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        rePaint();
    }

    public void rePaint() {
        mRectF = null;
        paint1 = null;
        paint2 = null;
        paintC = null;
        paintCS = null;

        initPaint1();
        initPaint2();
        initPaintC();
        initPaintCS();
        mRectF = new RectF(circlePadding, circlePadding, getWidth() - circlePadding, getHeight() - circlePadding);
        invalidate();
    }
}
