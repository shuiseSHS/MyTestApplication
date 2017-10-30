package com.example.greendaotest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shisong on 2017/2/14.
 * 涂抹效果
 */
public class ScratchView_1 extends View {

    private Bitmap mMaskBitmap;

    private Canvas mMaskCanvas;

    private Paint mMaskPaint;

    private Paint mBitmapPaint;

    private Paint mErasePaint;

    private float startX;

    private float startY;

    public ScratchView_1(Context context) {
        super(context);
        init();
    }

    public ScratchView_1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScratchView_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmapPaint = new Paint();
        mMaskPaint = new Paint();
        mMaskPaint.setColor(0xFFCBCBCB);

        mErasePaint = new Paint();
        mErasePaint.setAntiAlias(true);
        mErasePaint.setDither(true);
        mErasePaint.setStrokeWidth(50);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);//设置笔尖形状，让绘制的边缘圆滑
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setColor(0xFF00FF00);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
    }

    /**
     * 创建蒙层
     *
     * @param width
     * @param height
     */
    private void createMasker(int width, int height) {
        mMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mMaskCanvas = new Canvas(mMaskBitmap);
        Rect rect = new Rect(0, 0, width, height);
        mMaskCanvas.drawRect(rect, mMaskPaint);//绘制生成和控件大小一致的遮罩 Bitmap
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createMasker(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mMaskBitmap, 0, 0, mBitmapPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                startX = -1;
                startY = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                erase(startX, startY, event.getX(), event.getY());
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
        }
        return true;
    }

    private void erase(float sx, float sy, float ex, float ey) {
        mMaskCanvas.drawLine(sx, sy, ex, ey, mErasePaint);
        invalidate();
    }
}
