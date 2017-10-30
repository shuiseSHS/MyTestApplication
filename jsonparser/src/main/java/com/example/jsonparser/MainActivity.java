package com.example.jsonparser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String SP_ROOT = "SP_ROOT";
    private static final String SP_PATH = "SP_PATH";
    private static final String SP_KEYS = "SP_KEYS";

    private static String PATH = "data,info";
    private static String KEYS = "name:user_nicename,url:pull";
    private static String ROOT = "http://jk.cai-kongjian.com/api/public/?service=Home.getNew";

    private ListView listView;
    private EditText editText;
    private EditText txtPath;
    private EditText txtKeys;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.error);
        textView.setVisibility(View.GONE);

        txtPath = (EditText) findViewById(R.id.txt_path);
        txtPath.setText(SharedPreferenceHelper.getParam(this, SP_PATH, PATH).toString());
        txtKeys = (EditText) findViewById(R.id.txt_keys);
        txtKeys.setText(SharedPreferenceHelper.getParam(this, SP_KEYS, KEYS).toString());

        editText = (EditText) findViewById(R.id.txt_url);
        editText.setText(SharedPreferenceHelper.getParam(this, SP_ROOT, ROOT).toString());

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                c.setPrimaryClip(ClipData.newPlainText(null, view.getTag().toString()));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setClassName("org.videolan.vlc", "org.videolan.vlc.gui.video.VideoPlayerActivity");
                    intent.setDataAndType(Uri.parse(view.getTag().toString()), "video/*");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "请安装VLC播放器", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestUrl(String url) {
        textView.setVisibility(View.GONE);

        SharedPreferenceHelper.setParam(this, SP_ROOT, url);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = StringEscapeUtils.unescapeJava(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferenceHelper.setParam(MainActivity.this, SP_PATH, txtPath.getText().toString());
                            SharedPreferenceHelper.setParam(MainActivity.this, SP_KEYS, txtKeys.getText().toString());

                            String[] paths = txtPath.getText().toString().split(",");
                            String[] keys = txtKeys.getText().toString().split(",");
                            JSONObject root = new JSONObject(result);
                            JSONArray jsonArray = null;
                            JSONObject jsonObject = root;

                            for (String key : paths) {
                                Object obj = jsonObject.get(key.trim());
                                if (obj instanceof JSONArray) {
                                    jsonArray = (JSONArray) obj;
                                    break;
                                } else {
                                    jsonObject = (JSONObject) obj;
                                }
                            }

                            List<DataItem> dataItems = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject info = (JSONObject) jsonArray.get(i);
                                DataItem dataItem = new DataItem();

                                for (int index = 0; index < keys.length; index++) {
                                    String action = keys[index].trim();
                                    String name = action.substring(0, action.indexOf(":"));
                                    String key = action.substring(action.indexOf(":") + 1);

                                    Field field = DataItem.class.getField(name.trim());
                                    field.set(dataItem, info.getString(key.trim()));
                                }
                                dataItems.add(dataItem);
                            }
                            listView.setAdapter(new MyAdapter(MainActivity.this, dataItems));
                            textView.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            textView.setText(e.toString());
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void requestUrl(View v) {
        requestUrl(editText.getText().toString());
    }
}
