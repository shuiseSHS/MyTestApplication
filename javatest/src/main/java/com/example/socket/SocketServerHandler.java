package com.example.socket;

/**
 * Created by shisong on 2016/11/14.
 *
 */

public class SocketServerHandler {

    public static void main(String[] a) {
        Server server = new Server(new Server.MsgCallback() {
            @Override
            public void number(int num) {
                System.out.println("客户端数量" + num);
            }

            @Override
            public void msg(String msg) {
                System.out.println(msg);
            }
        });
        server.init();
    }
}
