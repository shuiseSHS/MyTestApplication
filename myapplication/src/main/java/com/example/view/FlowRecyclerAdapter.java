package com.example.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

/**
 * Created by shisong on 2017/6/2.
 * 本地搜索
 */
public class FlowRecyclerAdapter extends RecyclerView.Adapter<FlowRecyclerAdapter.FlowViewHolder> {

    private List<String> data;

    private Context mContext;

    public FlowRecyclerAdapter(Activity activity) {
        mContext = activity;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    @Override
    public FlowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_localsearch, null);
        return new FlowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlowViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class FlowViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        FlowViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
