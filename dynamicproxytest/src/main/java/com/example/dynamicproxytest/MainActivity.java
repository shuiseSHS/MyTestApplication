package com.example.dynamicproxytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView txt;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.txt);
        btn = findViewById(R.id.btn_5);
        btn.setOnClickListener(DynamicFactory.addLoggerProxy(this));
    }

    public void toDo(View view) {
        ITime iTime = DynamicFactory.addLoggerProxy(new MyTime());
        txt.setText(iTime.getCurTime());
    }

    public void proxy1(View view) {
        InvocationHandler handler = new ProxyHandler1();
        ITime proxyTime = (ITime) Proxy.newProxyInstance(ITime.class.getClassLoader(), new Class[] {ITime.class}, handler);
        txt.setText(proxyTime.getCurTime());
    }

    public void proxy2(View view) {
        InvocationHandler handler = new ProxyHandler1();
        ITime proxyTime = (ITime) Proxy.newProxyInstance(ITime.class.getClassLoader(), new Class[] {ITime.class}, handler);
        txt.setText(proxyTime.getCurTime1());
    }

    public void proxy3(View view) {
        InvocationHandler handler = new ProxyHandler1();
        ITime proxyTime = (ITime) Proxy.newProxyInstance(ITime.class.getClassLoader(), new Class[] {ITime.class}, handler);
        txt.setText(proxyTime.getCurTime2());
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "###", Toast.LENGTH_SHORT).show();
    }
}
