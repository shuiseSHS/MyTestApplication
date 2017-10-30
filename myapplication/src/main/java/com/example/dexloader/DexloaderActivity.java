package com.example.dexloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.myapplication.R.id.txt_title;

public class DexloaderActivity extends AppCompatActivity {

    private static final String TAG = "DexloaderActivity";
    TextView text;
    private String dexFileName = "target.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dexloader);
        text = (TextView) findViewById(txt_title);
    }

    public void loadHello(View view) {
        text.setText(new Hello_1().getHello());
    }

    public void loadHello1(View view) {
        try {
            // 加载 HelloAndroid 类
            Class clazz = getClassLoader().loadClass("com.example.dexloader.Hello_1");
            // 强转成 ISayHello, 注意 ISayHello 的包名需要和 jar 包中的一致
            Object iSayHello = clazz.newInstance();
            try {
                Method helloMethod = clazz.getMethod("getHello");
                text.setText((String) helloMethod.invoke(iSayHello));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
