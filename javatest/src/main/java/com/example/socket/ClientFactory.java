package com.example.socket;

/**
 * Created by shisong on 2016/11/14.
 */

public class ClientFactory {

    public static void main(String[] a) {
        new Client(new Client.MsgCallback() {
            @Override
            public void msg(String msg) {
                System.out.println(msg);
            }
        }).connect();
    }

}
