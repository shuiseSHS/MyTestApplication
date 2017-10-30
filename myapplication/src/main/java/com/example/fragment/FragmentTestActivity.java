package com.example.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.myapplication.R;

/**
 * Created by shisong on 2017/9/28.
 */

public class FragmentTestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
    }
}
