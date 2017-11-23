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
 * 自定义箭头图标
 */
public class IconViewArrow extends View {

    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int RIGHT = 3;
    public static final int DOWN = 4;

    private RectF mRectF;
    private boolean circleBg;
    private int circleColor;
    private int circleStrokeColor;
    private int circleStrokeWidth;
    private int circlePadding;
    private int strokeWidth;
    private int lineColor;
    private int iconPadding;
    private int iconWidth;
    private int arrowDirection;
    Paint paint;
    Paint paintC;
    Paint paintCS;

    private boolean isPressed;

    public IconViewArrow(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public IconViewArrow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public IconViewArrow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconViewArrow(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    protected void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IconViewArrow, defStyleAttr, defStyleRes);
        if (a != null) {
            lineColor = a.getColor(R.styleable.IconViewArrow_lineColor, 0xFF333333);
            arrowDirection = a.getInt(R.styleable.IconViewArrow_arrowDirection, 1);

            strokeWidth = a.getDimensionPixelSize(R.styleable.IconViewArrow_iconStrokeWidth, -1);
            circleStrokeWidth = a.getDimensionPixelSize(R.styleable.IconViewArrow_circleStrokeWidth, -1);
            circlePadding = a.getDimensionPixelSize(R.styleable.IconViewArrow_circlePadding, 0);

            iconPadding = a.getDimensionPixelSize(R.styleable.IconViewArrow_iconPadding, 0);
            iconWidth = a.getDimensionPixelSize(R.styleable.IconViewArrow_iconWidth, 0);
            circleColor = a.getColor(R.styleable.IconViewArrow_circleColor, 0xFF333333);
            circleStrokeColor = a.getColor(R.styleable.IconViewArrow_circleStrokeColor, 0xFF333333);
            circleBg = a.getBoolean(R.styleable.IconViewArrow_circleBg, false);
            a.recycle();
        }

        initPaint();
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

        switch (arrowDirection) {
            case LEFT:
                canvas.drawLine(iconPadding, getHeight() / 2, iconPadding + iconWidth, getHeight() / 2 - iconWidth, paint);
                canvas.drawLine(iconPadding, getHeight() / 2, iconPadding + iconWidth, getHeight() / 2 + iconWidth, paint);
                break;
            case UP:
                canvas.drawLine(getWidth() / 2, iconPadding, getWidth() / 2 - iconWidth, iconPadding + iconWidth, paint);
                canvas.drawLine(getWidth() / 2, iconPadding, getWidth() / 2 + iconWidth, iconPadding + iconWidth, paint);
                break;
            case RIGHT:
                canvas.drawLine(getWidth() - iconPadding, getHeight() / 2, getWidth() - iconPadding - iconWidth, getHeight() / 2 - iconWidth, paint);
                canvas.drawLine(getWidth() - iconPadding, getHeight() / 2, getWidth() - iconPadding - iconWidth, getHeight() / 2 + iconWidth, paint);
                break;
            case DOWN:
                canvas.drawLine(getWidth() / 2, getHeight() - iconPadding, getWidth() / 2 - iconWidth, getHeight() - iconPadding - iconWidth, paint);
                canvas.drawLine(getWidth() / 2, getHeight() - iconPadding, getWidth() / 2 + iconWidth, getHeight() - iconPadding - iconWidth, paint);
                break;
        }
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

    private void initPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setStrokeWidth(strokeWidth);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(lineColor);
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
        rePaint();
    }

    public void setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        rePaint();
    }

    public void setArrowDirection(int direction) {
        arrowDirection = direction;
        rePaint();
    }

    public void rePaint() {
        mRectF = null;
        paintC = null;
        paintCS = null;

        initPaint();
        initPaintC();
        initPaintCS();
        mRectF = new RectF(circlePadding, circlePadding, getWidth() - circlePadding, getHeight() - circlePadding);
        invalidate();
    }
}
