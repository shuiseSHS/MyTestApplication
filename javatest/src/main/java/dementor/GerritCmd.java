package dementor;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by shisong on 2019/6/6
 */
public class GerritCmd {

    public static void main(String[] args) {
        System.out.println("---------------------------------------------------------------" + new Date());
        new GerritCmd().getCmdResult("ssh -i c:/dementor/id_rsa.unknown -p 29418 dementor@sci.qiyi.domain gerrit stream-events");
//        new GerritCmd().getCmdResult("ssh -i c:/dementor/id_rsa.unknown -p 29418 dementor@sci.qiyi.domain gerrit stream-events -s comment-added");
        System.out.println("---------------------------------------------------------------" + new Date());
    }

    public void getCmdResult(String cmd) {
        Runtime runtime = Runtime.getRuntime();
        OkHttpClient okHttpClient = new OkHttpClient();
        BufferedReader reader = null;
        Process proc = null;
        while (true) {
            try {
                System.out.println("##################" + cmd);
                proc = runtime.exec(cmd);
                // 采用字符流读取缓冲池内容，腾出空间
                reader = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    String url = "http://localhost:8080/gerrit/save?jsonStr=" + URLEncoder.encode(line, "UTF-8");
                    okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
                    final Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            response.close();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (proc != null) {
                    proc.destroy();
                }
            }
        }
    }

}
