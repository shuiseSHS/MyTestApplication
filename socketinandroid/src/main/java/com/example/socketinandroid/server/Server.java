package com.example.socketinandroid.server;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by shisong on 2016/11/11.
 */
public class Server {

    private static final int PORT = 8899;//监听的端口号
    private List<Socket> clients;
    private ServerSocket serverSocket;
    private MsgCallback msgCallback;

    public Server(MsgCallback callback) {
        clients = new ArrayList<>();
        msgCallback = callback;
    }

    public void init() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress(PORT));
                    showMsg("服务器启动-----" + getNetIp());
                    while (true) {
                        // 一旦有堵塞, 则表示服务器与客户端获得了连接
                        Socket client = serverSocket.accept();
                        showMsg(client.getInetAddress() + ":" + client.getPort() + "   已连接");
                        // 处理这次连接
                        new HandlerThread(client);
                        for (Socket socket : clients) {
                            if (!socket.isConnected()) {
                                Log("断了一个");
                                clients.remove(socket);
                            }
                        }
                        clients.add(client);
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

    public void boardcast(String s) {
        showMsg("广播: " + s);
        for (Socket socket : clients) {
            try {
                // 向客户端回复信息
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("广播： " + s);
            } catch (Exception e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                clients.remove(socket);
                break;
            }
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        private String getClientName() {
            return socket.getInetAddress().toString() + ":" + socket.getPort();
        }

        public void run() {
            while (true) {
                try {
                    // 读取客户端数据
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                    // 处理客户端数据
                    showMsg(getClientName() + "说： " + clientInputStr);

                    // 向客户端回复信息
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("你说： " + clientInputStr);
                } catch (Exception e) {
                    showMsg(getClientName() + "走了");
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    clients.remove(socket);
                    break;
                }
            }
        }
    }

    private void showMsg(String clientInputStr) {
        if (msgCallback != null) {
            msgCallback.msg(clientInputStr);
        }
    }

    private void Log(String s) {
        Log.e("###", s);
    }

    public void quit() {
        try {
            for (Socket client : clients) {
                client.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface MsgCallback {
        void number(int num);

        void msg(String msg);
    }

    private static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
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
    }

    //获取外网的IP(要访问Url，要放到后台线程里处理)
    private String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String line = "";
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    strber.append(line).append("\n");
                }
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        line = jsonObject.optString("cip");
                        Log.d("servr", line);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}