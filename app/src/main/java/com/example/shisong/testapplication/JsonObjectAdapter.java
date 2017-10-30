package com.example.shisong.testapplication;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shisong on 2016/8/17.
 *
 */
public class JsonObjectAdapter extends BaseAdapter {

    private Context mCtx;
    private List<KV> itemList = new ArrayList<>();

    private class KV {
        boolean open = false;
        KV parent = null;
        int layer = 0;
        String name;
        Object value;

        KV(String n, Object v, KV p) {
            name = n;
            value = v;
            parent = p;
            layer = p == null ? 0 : p.layer + 1;
        }
    }

    public JsonObjectAdapter(Context ctx, JSONObject jsonObject) {
        mCtx = ctx;
        JSONArray jsonArray = jsonObject.names();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                String name = jsonArray.getString(i);
                Object value = jsonObject.get(name);
                itemList.add(new KV(name, value, null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public KV getItem(int position) {
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
            convertView.setOnClickListener(onClick);
            ((TextView) convertView).setSingleLine();
            ((TextView) convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }

        KV kv = getItem(position);
        convertView.setTag(kv);
        String str = "";
        if (kv.value instanceof JSONArray) {
            if (kv.open) {
                str += "_";
            } else {
                str += "+";
            }
            str += kv.name + ":[" + ((JSONArray) kv.value).length() + "]";
        } else if (kv.value instanceof JSONObject) {
            if (kv.open) {
                str += "_";
            } else {
                str += "+";
            }
            JSONArray names = ((JSONObject) kv.value).names();
            str += kv.name + ":{ " + (names == null ? 0 : names.length()) + "}";
        } else {
            str += "  ";
            str += kv.name + ":" + kv.value;
        }
        for (int i = 0; i < kv.layer; i ++) {
            str = "  " + str;
        }
        ((TextView) convertView).setText(str);
        return convertView;
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                return;
            }

            KV kv = (KV) v.getTag();
            if (kv.open) {
                closeKV(kv);
            } else {
                openKV(kv);
            }
            notifyDataSetChanged();
        }
    };

    private void openKV(KV kv) {
        try {
            if (kv.value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) kv.value;
                List<KV> addList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object obj = jsonArray.get(i);
                    addList.add(new KV(i + "", obj, kv));
                }
                itemList.addAll(itemList.indexOf(kv) + 1, addList);
            } else if (kv.value instanceof JSONObject) {
                JSONArray names = ((JSONObject) kv.value).names();
                List<KV> addList = new ArrayList<>();
                if (names == null) {
                    addList.add(new KV("Empty object", null, kv));
                } else {
                    for (int i = 0; i < names.length(); i++) {
                        String name = (String) names.get(i);
                        addList.add(new KV(name, ((JSONObject) kv.value).get(name), kv));
                    }
                }
                itemList.addAll(itemList.indexOf(kv) + 1, addList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        kv.open = true;
    }

    private void closeKV(KV kv) {
        if (!kv.open) {
            return;
        }

        List<KV> removeList = new ArrayList<>();
        removeList.add(kv);

        while (!removeList.isEmpty()) {
            KV mkv = removeList.remove(0);
            for (int i = 0; i < itemList.size(); i ++) {
                KV skv = itemList.get(i);
                if (mkv.equals(skv.parent)) {
                    itemList.remove(skv);
                    i --;
                    removeList.add(skv);
                }
            }
        }
        kv.open = false;
    }
}
