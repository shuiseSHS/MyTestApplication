package com.example.flowlayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2017/6/20.
 */
public class TestListAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(parent.getContext(), R.layout.card_search_recommend, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View convertView = holder.itemView;
        if (position == 11) {
            convertView.setBackgroundColor(0xFFFFFFFF);
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
            RecommendKeywordLayout recFlowLayout = (RecommendKeywordLayout) convertView.findViewById(R.id.layout_recommend);
            recFlowLayout.setWordList(mKeywordList);
        } else {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            convertView.setLayoutParams(lp);
            convertView.setBackgroundColor(0xFFFF0000);
        }
    }

    @Override
    public int getItemCount() {
        return 13;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
