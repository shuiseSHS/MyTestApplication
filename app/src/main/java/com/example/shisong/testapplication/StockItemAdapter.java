package com.example.shisong.testapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shisong on 2016/8/17.
 *
 */

public class StockItemAdapter extends BaseAdapter {

    private Context mCtx;
    private List<StockItem> itemList;

    public StockItemAdapter(Context ctx, List<StockItem> list) {
        mCtx = ctx;
        itemList = list;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public StockItem getItem(int position) {
        return itemList == null ? null : itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("###", "getView: " + position);
        if (convertView == null) {
            convertView = new TextView(mCtx);
        }
        TextView textView = ((TextView)convertView);
        StockItem stock = getItem(position);
        if (stock.percent > 0) {
            textView.setTextColor(0xFFFF0000);
            if (stock.percent > 0.099) {
                textView.setBackgroundColor(0x77FF0000);
            } else {
                textView.setBackgroundColor(0x00000000);
            }
        } else {
            textView.setTextColor(0xFF00DD33);
            if (stock.percent < -0.099) {
                textView.setBackgroundColor(0x7700FF00);
            } else {
                textView.setBackgroundColor(0x00000000);
            }
        }
        ((TextView)convertView).setText(stock.toString());
        return convertView;
    }
}
