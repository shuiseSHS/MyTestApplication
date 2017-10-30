package com.example.itemlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by shisong on 2017/2/23.
 * 打赏弹窗金额item
 */
public class RewardFeeAdapter extends RecyclerView.Adapter<RewardFeeAdapter.RewardViewHolder> {

    private List<DataItem> datas;

    private Context context;

    RewardFeeAdapter(Activity activity) {
        context = activity;
    }

    public void setData(List<DataItem> d) {
        datas = d;
    }

    @Override
    public RewardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
        RewardViewHolder rv = new RewardViewHolder(convertView);
        return rv;
    }

    @Override
    public void onBindViewHolder(RewardViewHolder rewardViewHolder, int i) {
        rewardViewHolder.data = datas.get(i);
        rewardViewHolder.title.setText(datas.get(i).title);
        rewardViewHolder.duration.setText(datas.get(i).duration);
        rewardViewHolder.rate.setText(datas.get(i).rate);
        rewardViewHolder.add.setText(datas.get(i).add);
        rewardViewHolder.views.setText(datas.get(i).views);
        rewardViewHolder.img.setImageBitmap(null);
        rewardViewHolder.img.setTag(datas.get(i).img);
        ImageLoader.getImage(rewardViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class RewardViewHolder extends RecyclerView.ViewHolder {
        private DataItem data;
        private TextView title;
        private TextView duration;
        private TextView rate;
        private TextView add;
        private TextView views;
        private ImageView img;

        RewardViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            duration = (TextView) itemView.findViewById(R.id.duration);
            rate = (TextView) itemView.findViewById(R.id.rate);
            add = (TextView) itemView.findViewById(R.id.add);
            views = (TextView) itemView.findViewById(R.id.views);
            img = (ImageView) itemView.findViewById(R.id.img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, data.url, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("url", data.url);
                    context.startActivity(intent);
                }
            });
        }
    }
}

class DataItem {
    String title;
    String duration;
    String img;
    String url;
    String rate;
    String views;
    String add;
}
