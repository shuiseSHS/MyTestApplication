package dementor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.OkHttpClient;

/**
 * Created by shisong on 2019/6/6
 */

public class GerritAddReviewerMap {

    public static void main(String[] args) {
//        System.out.println("打发大水");
        new GerritAddReviewerMap().getCmdResult();
    }

    public void getCmdResult() {
        OkHttpClient okHttpClient = new OkHttpClient();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("d:/data/reivewerMap.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
//                String owner = line. substring();
//                String reviewer = line.substring();
//                String url = "http://localhost:8080/gerrit/reviewermap_name?owner=" + owner + "&reviewer=" + reviewer;
//                okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
//                final Call call = okHttpClient.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        response.close();
//                    }
//                });
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
        }
    }
}
