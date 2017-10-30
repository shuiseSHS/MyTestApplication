package com.example.scannet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shisong on 2016/12/12.
 */

public class ScanNetData {

    //    public static String rootUrl = "https://www.114sihu.com/Html/100/17875.html";
//    private static String mp4Url = "https://d1.xia12345.com/down/201611/30/s";
//    private static String downurl = "https://d1.xia12345.com/down/201611/30/s203.mp4";

    public static void main(String[] a) throws Exception {
//        down(downurl);
//        testByteRead();

//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
//
//        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sc.getSocketFactory(), new TrustAnyTrustManager())
//                .hostnameVerifier(new TrustAnyHostnameVerifier()).build();
//
//        Request request;
//        for (int i = 200; i < 300; i ++) {
//            String url = mp4Url + i + ".mp4";
//            request = new Request.Builder().url(url).build();
//            Call call = mOkHttpClient.newCall(request);
//            Response response = call.execute();
//            System.out.println(url + "        " + (response.body().contentLength() >> 20) + " M");
//            response.close();
//        }

//        String result = response.body().string();
//        System.out.println(result);
//
//        Document doc = Jsoup.parse(result);
//        Elements elements = doc.getElementsByClass("no-hover-underline");
    }

    private static void down(String url) throws Exception {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sc.getSocketFactory(), new TrustAnyTrustManager())
                .hostnameVerifier(new TrustAnyHostnameVerifier()).build();

        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        long length = response.body().contentLength();
        InputStream is = response.body().byteStream();
        File f = new File("f:/dddd.mp4");
        f.createNewFile();
        FileOutputStream fw = new FileOutputStream(f);
        long len = 0;
        long sum = 0;
        byte[] bytes = new byte[4 * 1024];
        long ss = System.currentTimeMillis();
        int time;
        while ((len = is.read(bytes)) > 0) {
            fw.write(bytes);
            sum += len;
            time = (int) ((System.currentTimeMillis() - ss));
            System.out.println(len + "     " + sum + " " + (sum * 100 / length) + "%      " + (sum / time) + "KB/s");
        }
        is.close();
        fw.close();
        response.close();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
