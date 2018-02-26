package com.example.itemlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shisong on 2017/2/23.
 * 打赏弹窗金额item
 */
public class RewardFeeAdapter extends RecyclerView.Adapter<RewardFeeAdapter.RewardViewHolder> {

    private static final boolean USE_VLC = true;

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
        return new RewardViewHolder(convertView);
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
                    if (data.url.equals(".mp4")) {
                        toPlay(data.url);
                    } else {
                        loadData(data.url);
                    }
                }
            });
        }

        private void loadData(final String url) {
            new Thread() {
                public void run() {
                    try {
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url(url)
                                .header("User-Agent", "OkHttp Headers.java").build();
                        Call call = mOkHttpClient.newCall(request);
                        Response response = call.execute();
                        String result = response.body().string();
                        String videoUrl = result.substring(result.indexOf("video_url") + 12, result.indexOf(".mp4", result.indexOf("video_url")) + 4);
                        toPlay(videoUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void toPlay(String videoUrl) {
        if (USE_VLC) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity");
                intent.setDataAndType(Uri.parse(videoUrl), "video/*");
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "请安装VLC播放器", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, videoUrl, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("url", videoUrl);
            context.startActivity(intent);
        }
    }
}

