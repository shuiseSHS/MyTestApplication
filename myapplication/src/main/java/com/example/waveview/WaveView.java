package com.example.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 波浪动效
 */
public class WaveView extends View {
    private static final int MAX_RMS = 6000;
    private static final int MIN_AMPLITUDE = UIUtils.dip2px(7);

    private final static int SPEED1 = UIUtils.dip2px(10);
    private final static int SPEED2 = UIUtils.dip2px(7);

    private int[] colors1 = new int[]{0x004CEBF8, 0xFF4CEBF8, 0xFF4CEBF8, 0x004CEBF8};
    private int[] colors2 = new int[]{0x0049FB27, 0xFF49FB27, 0xFF49FB27, 0x0049FB27};
    private float[] postions = new float[]{0.1f, 0.4f, 0.6f, 0.9f};
    private Wave wave1;
    private Wave wave2;

    private Paint mPaint1;
    private Path mPath1;
    private Paint mPaint2;
    private Path mPath2;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeWidth(UIUtils.dip2px(3));
        mPath1 = new Path();

        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(UIUtils.dip2px(4));
        mPath2 = new Path();

        wave1 = new Wave(SPEED1, 10, 1.8f);
        wave2 = new Wave(SPEED2, 9, 1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        wave1.updatePath(mPath1);
        wave2.updatePath(mPath2);
        canvas.drawPath(mPath1, mPaint1);
        canvas.drawPath(mPath2, mPaint2);
        postDelayed(runnable, 1000 / 60);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth();
        int maxHeight = getHeight() / 2;
        mPaint1.setShader(new LinearGradient(0, maxHeight, width, maxHeight, colors1, postions, Shader.TileMode.CLAMP));
        mPaint2.setShader(new LinearGradient(0, maxHeight, width, maxHeight, colors2, postions, Shader.TileMode.CLAMP));
        wave1.onLayout(width, maxHeight);
        wave2.onLayout(width, maxHeight);
        invalidate();
    }

    public void updateAmplitudeByRms(int rms) {
        if (wave1 != null) {
            wave1.updateRms(rms);
        }
        if (wave2 != null) {
            wave2.updateRms(rms);
        }
    }

    private class Wave {
        private int maxHeight;
        private int amplitude = MIN_AMPLITUDE;
        private float delta;

        private List<WaveNode> waveNodes;

        Wave(int speed, int nodeCount, float d) {
            delta = d;
            waveNodes = new ArrayList<>();
            for (int i = 0; i < nodeCount; i++) {
                waveNodes.add(new WaveNode(speed));
            }
        }

        void onLayout(int width, int maxH) {
            maxHeight = maxH;
            int waveWidth1 = width / (waveNodes.size() - 4);
            int x = -2 * waveWidth1;
            for (int i = 0; i < waveNodes.size(); i++) {
                x += waveWidth1;
                int ctrlX = x - waveWidth1 / 2;
                waveNodes.get(i).init(waveWidth1, maxHeight, ctrlX, i % 2 == 0 ? -1 * delta : delta);
            }
        }

        void updateRms(int rms) {
            if (rms > MAX_RMS) {
                amplitude = maxHeight;
            } else {
                amplitude = rms * maxHeight / MAX_RMS;
            }
            if (amplitude < MIN_AMPLITUDE) {
                amplitude = MIN_AMPLITUDE;
            }
        }

        void updatePath(Path path) {
            path.reset();
            for (WaveNode wave : waveNodes) {
                wave.quadTo(path, amplitude);
            }
        }
    }

    private class WaveNode {
        int startX1;
        float controlX;
        float objectX;
        float objectY;

        int speed;
        int waveWidth;
        int maxHeight;
        float delta;

        WaveNode(int speed) {
            this.speed = speed;
        }

        void init(int width, int height, int cx, float d) {
            waveWidth = width;
            maxHeight = height;
            controlX = cx;
            delta = d;

            objectX = controlX + waveWidth / 2;
            objectY = maxHeight;
            startX1 = waveWidth * -2;
        }

        void quadTo(Path path, int amplitude) {
            int controlY = (int) (maxHeight + delta * amplitude);
            path.quadTo(controlX + startX1, controlY, objectX + startX1, objectY);
            startX1 += speed;
            if (startX1 > 0) {
                startX1 -= 2 * waveWidth;
            }
        }
    }
}
