package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * Created by shisong on 2017/3/24.
 * 搜索热图
 */
public class HotSearchImageAdapter extends RecyclerView.Adapter<HotSearchImageAdapter.ViewHolder> {

    private Context mContext;

    private View.OnClickListener itemClickListener;

    public HotSearchImageAdapter(Activity activity, View.OnClickListener listener) {
        mContext = activity;
        itemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.phone_search_hot_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText("AAAAAAAAAAAA" + i);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.textview);
        }
    }

}
