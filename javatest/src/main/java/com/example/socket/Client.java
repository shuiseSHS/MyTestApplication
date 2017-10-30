package com.example.socket;

/**
 * Created by shisong on 2016/11/11.
 *
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private static final String IP_ADDR = "127.0.0.1";//服务器地址
    private static final int PORT = 12345;//服务器端口号
    DataInputStream input;
    DataOutputStream out;
    Socket socket = null;
    boolean quit = false;
    MsgCallback msgCallback;

    public Client(MsgCallback callback) {
        msgCallback = callback;
    }

    public void connect() {
        showMsg("客户端启动...");
        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket();
            socket.connect(new InetSocketAddress(IP_ADDR, PORT), 5000);
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

    public void write(String s) {
        if (socket != null && socket.isConnected()) {
            try {
                out.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showMsg("断了");
        }
    }

    public void quit() {
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
