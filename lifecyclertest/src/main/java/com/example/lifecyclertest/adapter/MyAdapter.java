package com.example.lifecyclertest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lifecyclertest.database.Fruit;

import java.util.List;

/**
 * Created by shisong on 2017/11/8.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private List<Fruit> datas;

    public MyAdapter(Context ctx) {
        mContext = ctx;
    }

    public void setDatas(List<Fruit> fruits) {
        datas = fruits;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Fruit getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text;
        if (convertView == null) {
            text = new TextView(mContext);
        } else {
            text = (TextView) convertView;
        }

        text.setText(getItem(position).toString());
        return text;
    }
}
