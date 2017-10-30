package com.example.socketclient;

/**
 * Created by shisong on 2016/11/11.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private String server_ip = "192.168.254.136";//服务器地址
    private int server_port = 12345;//服务器端口号

    private DataInputStream input;
    private DataOutputStream out;
    private Socket socket = null;
    private boolean quit = false;
    private MsgCallback msgCallback;

    Client(MsgCallback callback) {
        msgCallback = callback;
    }

    void connect(String ip, int port) {
        showMsg("客户端启动...");
        server_ip = ip;
        server_port = port;

        try {
            if (socket != null && socket.isConnected()) {
                socket.close();
            }
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket();
            socket.connect(new InetSocketAddress(server_ip, server_port), 5000);
            showMsg("连接成功");

            //读取服务器端数据
            input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            out = new DataOutputStream(socket.getOutputStream());
            startRead();
        } catch (Exception e) {
            showMsg("客户端异常:" + e.getMessage());
        }
    }


    private void showMsg(String s) {
        if (msgCallback != null) {
            msgCallback.msg(s);
        }
    }

    private void startRead() {
        try {
            while (!quit) {
                showMsg(input.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void write(final String s) {
        if (socket != null && socket.isConnected()) {
            new Thread() {
                public void run() {
                    try {
                        out.writeUTF(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            showMsg("断了");
        }
    }

    void quit() {
        quit = true;
        try {
            if (out != null) {
                out.close();
            }
            if (input != null) {
                input.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            showMsg("客户端 finally 异常:" + e.getMessage());
        }
    }

    interface MsgCallback {
        void msg(String msg);
    }
}
