package com.example;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.myapplication.R;

import java.util.Random;

import static android.animation.ObjectAnimator.ofFloat;

/**
 * Created by shisong on 2017/4/6.
 */

public class AnimationTest extends Activity {

    private ImageView imgAnima;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        imgAnima = (ImageView) findViewById(R.id.img_anima);
        startAnimation();
    }

    private int[] animaIcos = new int[] {
            R.drawable.search_by_image_anima1,
            R.drawable.search_by_image_anima2,
            R.drawable.search_by_image_anima3,
            R.drawable.search_by_image_anima4,
            R.drawable.search_by_image_anima5,
    };
    private boolean stopAnimation = false;

    private void startAnimation() {
        if (imgAnima != null) {
            Random random = new Random(System.currentTimeMillis());
            imgAnima.setImageResource(animaIcos[random.nextInt(animaIcos.length)]);
            imgAnima.setVisibility(View.VISIBLE);
            imgAnima.setAlpha(1.0f);

            AnimatorSet anims1 = new AnimatorSet();
            anims1.playTogether(
                    ofFloat(imgAnima, "translationY", 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -50, getResources().getDisplayMetrics())),
                    ofFloat(imgAnima, "rotation", 0, random.nextInt(2) == 0 ? 45 : -45),
                    ofFloat(imgAnima, "scaleX", 1, 1.2f),
                    ofFloat(imgAnima, "scaleY", 1, 1.2f)
            );
            anims1.setInterpolator(new DecelerateInterpolator());
            anims1.setDuration(299);

            ObjectAnimator anima2 = ofFloat(imgAnima, "alpha", 1.0f, 0);
            anima2.setInterpolator(new DecelerateInterpolator());
            anima2.setDuration(699);

            AnimatorSet anims = new AnimatorSet();
            anims.playSequentially(anims1, anima2);
            anims.start();
            anims.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (stopAnimation) {
                        imgAnima.setVisibility(View.GONE);
                        return;
                    }
                    startAnimation();
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                }
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
                @Override
                public void onAnimationStart(Animator animation) {
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAnimation = true;
    }
}
