package com.example.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * Created by shisong on 2017/5/9.
 * 一个神奇的效果
 */
public class WhatButton extends RelativeLayout implements View.OnClickListener {
    private OnClickListener clickListener;
    private ImageView img_c;
    private TextView txt;
    private View btn;
    private int width;

    public WhatButton(Context context) {
        super(context);
        init();
    }

    public WhatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WhatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.what_view, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        addView(view, lp);
        img_c = (ImageView) view.findViewById(R.id.img_c);
        txt = (TextView) view.findViewById(R.id.txt);
        btn = view;
        btn.setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        clickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (btn.isSelected()) {
            btn.setSelected(false);
            txt.setText("+ 订阅");
            txt.setTextColor(0xFFFFFFFF);
            txt.setAlpha(1.0f);
        } else {
            runAnimation1();
        }
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }

    private void hideTextAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(txt, "alpha", 1.0f, 0.0f).setDuration(300);
        objectAnimator.start();
    }

    private void showTextAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(txt, "alpha", 0.0f, 1.0f).setDuration(300);
        objectAnimator.start();
    }

    private void runAnimation1() {
        hideTextAnimation();
        btn.setEnabled(false);
        width = btn.getWidth();
        int height = btn.getHeight();
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(1.0f * width, 0.8f * height, 1.2f * height, 0.9f * height, 1.0f * height).setDuration(600);
        valueAnimator1.addUpdateListener(listener);
        valueAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runAnimation2();
            }
        });
        valueAnimator1.start();
    }

    private void runAnimation2() {
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.va_ico1);
        if (animatedVectorDrawableCompat == null) {
            return;
        }
        img_c.setVisibility(View.VISIBLE);
        img_c.setImageDrawable(animatedVectorDrawableCompat);
//        animatedVectorDrawableCompat.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
//            @Override
//            public void onAnimationEnd(Drawable drawable) {
//                img_c.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        img_c.setVisibility(View.GONE);
//                        runAnimation3();
//                    }
//                }, 300);
//            }
//        });
        if (animatedVectorDrawableCompat.isRunning()) {
            animatedVectorDrawableCompat.stop();
        } else {
            animatedVectorDrawableCompat.start();
        }
                img_c.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img_c.setVisibility(View.GONE);
                        runAnimation3();
                    }
                }, 600);
    }

    private void runAnimation3() {
        btn.setSelected(true);
        txt.setText("查看订阅");
        txt.setTextColor(0xFF00BE06);
        showTextAnimation();
        ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(btn.getHeight(), 1.1f * width, 0.95f * width, 1.05f * width, 1.0f * width).setDuration(600);
        valueAnimator1.addUpdateListener(listener);
        valueAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btn.setEnabled(true);
                btn.getLayoutParams().width = width;
                btn.requestLayout();
            }
        });
        valueAnimator1.start();
    }

    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float wv = (float) animation.getAnimatedValue();
            btn.getLayoutParams().width = (int) wv;
            btn.requestLayout();
        }
    };
}
