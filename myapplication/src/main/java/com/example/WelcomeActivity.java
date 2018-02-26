package com.example;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.Window;

import com.example.dexloader.DexloaderActivity;
import com.example.flowlayout.FlowLayoutActivity;
import com.example.myapplication.R;
import com.example.transition.TransitionActivity;
import com.example.vectordrawable.VectorDrawableActivity;
import com.example.waveview.WaveViewActivity;

/**
 * Created by shisong on 2017/4/1.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置允许通过ActivityOptions.makeSceneTransitionAnimation发送或者接收Bundle
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            //设置使用TransitionManager进行动画，不设置的话系统会使用一个默认的TransitionManager
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        setContentView(R.layout.activity_welcome);
        toActivityWaveView(null);

        ObjectAnimator showAnimator = new ObjectAnimator();
        showAnimator.setIntValues(0, 100);
        showAnimator.setDuration(200);
        showAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        showAnimator.start();
    }

    public void toRecyclerViewTest(View v) {
        startActivity(new Intent(this, RecyclerViewTest.class));
    }

    public void toAnimation(View view) {
        startActivity(new Intent(this, AnimationTest.class));
    }

    public void toFlowLayout(View view) {
        startActivity(new Intent(this, FlowLayoutActivity.class));
    }

    public void toCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, findViewById(R.id.btn_camera), "share_image");
        startActivity(intent, option.toBundle());
    }

    public void toCoordinatorLayout(View view) {
        startActivity(new Intent(this, CoordinatorLayoutActivity.class));
    }

    public void toVectorDrawable(View view) {
        startActivity(new Intent(this, VectorDrawableActivity.class));
    }

    public void toActivityTransition(View view) {
        startActivity(new Intent(this, TransitionActivity.class));
    }

    public void toActivityDexLoader(View view) {
        startActivity(new Intent(this, DexloaderActivity.class));
    }

    public void toActivityWaveView(View view) {
        startActivity(new Intent(this, WaveViewActivity.class));
    }
}
