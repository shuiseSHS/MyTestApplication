package com.example.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by shisong on 2016/11/11.
 *
 */
public class Server {

    private static final int PORT = 12345;//监听的端口号
    private List<Socket> clients;
    private ServerSocket serverSocket;
    private MsgCallback msgCallback;

    public Server(MsgCallback callback) {
        clients= new ArrayList<>();
        msgCallback = callback;
    }

    public void init() {
        new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress(getLocalIPList().get(0), PORT));
                    showMsg("服务器启动-----" + serverSocket.getLocalSocketAddress());
                    while (true) {
                        // 一旦有堵塞, 则表示服务器与客户端获得了连接
                        Socket socket = serverSocket.accept();
                        // 处理这次连接
                        new HandlerThread(socket);
                        for (Socket socket1 : clients) {
                            if (!socket.isConnected()) {
                                showMsg("断了一个");
                                clients.remove(socket1);
                            }
                        }
                        clients.add(socket);
                        if (msgCallback != null) {
                            msgCallback.number(clients.size());
                        }

                    }
                } catch (Exception e) {
                    showMsg("服务器异常: " + e.getMessage());
                }
            }
        }.start();
    }

    private class HandlerThread implements Runnable {
        private Socket socket;

        HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            while (true) {
                try {
                    // 读取客户端数据
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                    // 处理客户端数据
                    showMsg(clientInputStr);

                    // 向客户端回复信息
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("你说：" + clientInputStr);
                } catch (SocketException e) {
                    break;
                } catch (Exception e) {
                    showMsg("服务器 run 异常: " + e.getMessage());
                }
            }
        }
    }

    private void showMsg(String clientInputStr) {
        if (msgCallback != null) {
            msgCallback.msg(clientInputStr);
        }
    }

    public void quit() {
        try {
            for (Socket client : clients) {
                client.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface MsgCallback {
        void number(int num);
        void msg(String msg);
    }

    private static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }}