package com.example.transition;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.myapplication.R;

/**
 * Created by shisong on 2017/6/2.
 * 过场动画
 */
public class TransitionActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //设置允许通过ActivityOptions.makeSceneTransitionAnimation发送或者接收Bundle
//            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//            //设置使用TransitionManager进行动画，不设置的话系统会使用一个默认的TransitionManager
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        }
//        getWindow().setExitTransition(new Fade());
//        getWindow().setReenterTransition(new Explode());
        setContentView(R.layout.activity_transition);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.f_container, new TransitionFragment());
        fragmentTransaction.commit();
    }
}
