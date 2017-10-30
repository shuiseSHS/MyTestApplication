package com.example.transition;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

/**
 * Created by shisong on 2017/6/6.
 */

public class TransitionFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transition, null);
        root.findViewById(R.id.btn_tran).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), Transition1Activity.class);
        Pair<View, String> pair = Pair.create(getActivity().findViewById(R.id.btn_tran), "XXXX");
        Pair<View, String> pair1 = Pair.create(getActivity().findViewById(R.id.phoneSearchKeyword), "bbbb");
        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pair, pair1);
        startActivity(intent, option.toBundle());
    }
}
