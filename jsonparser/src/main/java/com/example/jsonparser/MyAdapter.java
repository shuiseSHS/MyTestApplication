package com.example.jsonparser;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shisong on 2017/10/30.
 */
public class MyAdapter extends BaseAdapter {

    private List<DataItem> mDatas;
    private Context mContext;

    MyAdapter(Context ctx, List<DataItem> datas) {
        mDatas = datas;
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public DataItem getItem(int position) {
        return (mDatas == null || mDatas.size() <= position) ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
        } else {
            textView = (TextView) convertView;
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setPadding(20, 20, 20, 20);
        textView.setText(getItem(position).name);
        textView.setTag(getItem(position).url);
        return textView;
    }
}
