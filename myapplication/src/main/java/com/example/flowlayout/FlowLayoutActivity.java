package com.example.flowlayout;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.view.FlowLayoutManager;
import com.example.view.FlowRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2017/4/17.
 */
@SuppressWarnings("ResourceType")
public class FlowLayoutActivity extends Activity {

    private FlowLayout flowLayout;
//    private RecommendKeywordLayout recFlowLayout;
    private RecyclerView recyclerView;
    private FlowRecyclerAdapter adapter;
    private RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flowlayout);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new TestListAdapter());

        flowLayout = (FlowLayout) findViewById(R.id.layout_flow);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new FlowLayoutManager(this));
        adapter = new FlowRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

//        recFlowLayout = (RecommendKeywordLayout) findViewById(R.id.layout_recommend);

        String ss = "Where name is the name of the attribute set, the main purpose is to identify the attribute set. Where will it be used? Mainly in the third step. See? When an attribute identifier is obtained";
        String[] strings = ss.split(" ");
        ColorStateList colorStateList = getResources().getColorStateList(R.color.color_txt);
        for (String string : strings) {
            TextView textView = new TextView(this);
            textView.setText(string);
            textView.setTextSize(25);
            textView.setClickable(true);
            textView.setTextColor(colorStateList);
            textView.setBackgroundColor(0x33000000);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 10;
            layoutParams.topMargin = 10;
            flowLayout.addView(textView, layoutParams);
        }

        List<String> mKeywordList = new ArrayList<>();
        mKeywordList.add("奔跑吧兄弟迪丽热巴第5季");
        mKeywordList.add("奔跑吧兄弟迪丽热巴第5季第1期");
        mKeywordList.add("奔跑吧兄弟第4季迪丽热巴");
        mKeywordList.add("奔跑吧兄弟迪丽热巴");
        mKeywordList.add("迪丽热巴跑男");
        mKeywordList.add("奔跑吧兄弟大理站");
//        mKeywordList.add("思美人演员表");
//        mKeywordList.add("思美人易烊千玺");
//        mKeywordList.add("思美人发布会直播");
//        mKeywordList.add("思美人花絮");
//        recFlowLayout.setWordList(mKeywordList);
    }

    public void addData(View view) {
        List<String> mKeywordList = adapter.getData();
        if (mKeywordList == null) {
            mKeywordList = new ArrayList<>();
            adapter.setData(mKeywordList);
        }
        mKeywordList.add("DDDDDDDDDD");
        adapter.setData(mKeywordList);
        adapter.notifyDataSetChanged();
    }
}
