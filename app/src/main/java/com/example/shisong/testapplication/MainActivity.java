package com.example.shisong.testapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.textView);
////        insertRecord("MyUser");
////        displayRecords();
//        StringBuilder sb = new StringBuilder();
//        sb.append("curId ").append(getTaskId()).append("\n");
//
//        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
//        for (ActivityManager.RunningTaskInfo task : taskInfoList) {
//            sb.append(task.id).append("  ").append(task.baseActivity).append("\n");
//            if (getTaskId() == task.id) {
//                sb.append(task.numActivities).append("\n");
//            }
//        }
//
//        textView.setText(sb.toString());
//
        File root = Environment.getExternalStorageDirectory();
        System.out.println("########## " + root);
        ZipCRCUtils.checksumByCRC(new File(root, "Android_bottom_theme_1208.zip"), "Android_bottom_theme_1208");
        ZipCRCUtils.checksumByCRC(new File(root, "11111.zip"), "1111");
    }

    public void toSearch(View view) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("iqiyi://mobile/search"));
        startActivity(intent);
    }

    PopupWindow popupWindow;

    public void showDialog(View view) {
        if (popupWindow == null) {
            View cv = View.inflate(this, R.layout.popup_offline_guide, null);
            popupWindow = new PopupWindow(cv, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.playRecordBottomTip);
//            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
//            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        }
//        popupWindow.showAsDropDown(view);
//        popupWindow.showAsDropDown(view, 220, 0);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.RIGHT, 0, 138);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
//        if (bgAlpha == 1.0f) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        } else {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        }
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
//        lp.dimAmount = 1.0f;
        getWindow().setAttributes(lp);
    }

    public void toStock(View v) {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
    }

    public void toList(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    /**
     * dip转换成像素值
     */
    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    private void insertRecord(String userName) {
        ContentValues values = new ContentValues();
        values.put(MyUsers.User.USER_NAME, userName);
        getContentResolver().insert(MyUsers.User.CONTENT_URI, values);
    }

    private void displayRecords() {
        String columns[] = new String[]{MyUsers.User._ID, MyUsers.User.USER_NAME};
        Uri myUri = MyUsers.User.CONTENT_URI;
        CursorLoader cl = new CursorLoader(this, myUri, columns, null, null, null);
        Cursor cur = cl.loadInBackground();
        if (cur.moveToFirst()) {
            String id = null;
            String userName = null;
            do {
                id = cur.getString(cur.getColumnIndex(MyUsers.User._ID));
                userName = cur.getString(cur.getColumnIndex(MyUsers.User.USER_NAME));
                Toast.makeText(this, id + " " + userName, Toast.LENGTH_LONG).show();
            } while (cur.moveToNext());
        }
    }
}
