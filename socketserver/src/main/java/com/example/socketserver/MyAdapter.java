package com.example.socketserver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2016/11/14.
 */

public class MyAdapter extends BaseAdapter {

    private List<String> datas;

    private Context mContext;

    public MyAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    public void addData(String s) {
        datas.add(s);
        if (datas.size() > 99) {
            datas.remove(0);
        }
    }

    public void addData(List<String> ss) {
        datas.addAll(ss);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int i) {
        if (datas.size() > i) {
            return datas.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(mContext);
        }

        ((TextView) convertView).setText(getItem(position));
        return convertView;
    }
}
