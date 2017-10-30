package com.example.greendaotest;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.greendaotest.model.DaoMaster;
import com.example.greendaotest.model.item.Item;
import com.example.greendaotest.model.user.User;
import com.example.greendaotest.model.user.UserDao;

import java.util.List;

public class MainActivity extends Activity {

    private TextView name;

    private DaoMaster dbMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (TextView) findViewById(R.id.text);
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "test", null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        dbMaster = new DaoMaster(db);

        insertDB();
        listDB();
    }

    private void listDB() {
        UserDao userDao = dbMaster.newSession().getUserDao();
        List<User> users = userDao.loadAll();
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.getName() + " id: " + user.getId() + "    " + user.getItem().getId()).append("\n");
        }
        name.setTag(sb.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText(name.getTag().toString());
            }
        });
    }

    private void insertDB() {
        UserDao userDao = dbMaster.newSession().getUserDao();
        long num = userDao.count() + 1;

        Item item = new Item();
        dbMaster.newSession().getItemDao().insert(item);

        User user = new User();
        user.setName("user" + num);
        user.setAge(20);
        user.setItem(item);
        userDao.insert(user);
        Log.e("###", "num:" + num);
    }
}
