package com.example.shortcuttest;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addShortCut();
    }

    public void toTest(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }

    private void addShortCut() {
        //①、创建动态快捷方式的第一步，创建ShortcutManager
        ShortcutManager scManager = getSystemService(ShortcutManager.class);
        //②、构建动态快捷方式的详细信息
        Intent redIntent = new Intent(this, TestActivity.class);
        redIntent.setAction(Intent.ACTION_VIEW);
        redIntent.putExtra("bg", 0xFFFF0000);
        ShortcutInfo scInfoRed = new ShortcutInfo.Builder(this, "dynamic_red")
                .setShortLabel("red")
                .setLongLabel("test red page")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                .setIntent(redIntent)
                .build();

        Intent blueIntent = new Intent(this, TestActivity.class);
        blueIntent.setAction(Intent.ACTION_VIEW);
        blueIntent.putExtra("bg", 0xFF0000FF);
        ShortcutInfo scInfoBlue = new ShortcutInfo.Builder(this, "dynamic_blue")
                .setShortLabel("blue")
                .setLongLabel("test blue page")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                .setIntent(blueIntent)
                .build();

        Intent greenIntent = new Intent(this, TestActivity.class);
        greenIntent.setAction(Intent.ACTION_VIEW);
        greenIntent.putExtra("bg", 0xFF00FF00);
        ShortcutInfo scInfoGreen = new ShortcutInfo.Builder(this, "dynamic_green")
                .setShortLabel("green")
                .setLongLabel("test green page")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher_round))
                .setIntent(greenIntent)
                .build();

        //③、为ShortcutManager设置动态快捷方式集合
        scManager.setDynamicShortcuts(Arrays.asList(scInfoRed, scInfoBlue, scInfoGreen));

//        //如果想为两个动态快捷方式进行排序，可执行下面的代码
//        ShortcutInfo dynamicWebShortcut = new ShortcutInfo.Builder(this, "dynamic_one")
//                .setRank(0)
//                .build();
//        ShortcutInfo dynamicActivityShortcut = new ShortcutInfo.Builder(this, "dynamic_two")
//                .setRank(1)
//                .build();
//
//        //④、更新快捷方式集合
//        scManager.updateShortcuts(Arrays.asList(dynamicWebShortcut, dynamicActivityShortcut));
    }

}
