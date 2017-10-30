package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by shisong on 2017/3/23.
 */

public class ImageUpload {

//    private static final String BASE_URL = "http://10.110.22.78/common_upload";
//    private static final String BASE_URL = "http://upload.iqiyi.com/common_upload";
    private static final String TEST_URL = "http://10.110.22.78/common_upload?qichuan_bizid=image_search&file_type=jpg&file_size=123&auth_token=abHBWb1SMlUyuCE4DW1OnoX0Mq8UDpRnXy3Am2xHk0DJWnA6IGfpUXEV0iSngcrvm33n3e&business_type=image&share_type=external";

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");


    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new ProgressRequestBody(RequestBody.create(MEDIA_TYPE_STREAM, getBytes(new File("f:/b.png"))), progressRequestListener);

        RequestBody requestBody1 = createCustomRequestBody(MEDIA_TYPE_STREAM, getBytes(new File("f:/b.png")), progressRequestListener);

        Request request = new Request.Builder()
                .url(TEST_URL)//地址
                .post(requestBody1)//添加请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("上传照片成功：response = " + response.body().string());
            }
        });
    }

    static ProgressRequestListener progressRequestListener = new ProgressRequestListener() {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
            System.out.println("onRequestProgress:  " + bytesWritten + "  " + contentLength);
        }
    };

    public static RequestBody createCustomRequestBody(final MediaType contentType, final byte[] bytes, final ProgressRequestListener listener) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return bytes.length;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(new ByteArrayInputStream(bytes));
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = 0L;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onRequestProgress(contentLength(), remaining += readCount, remaining == contentLength());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static byte[] getBytes(File file) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileInputStream is = new FileInputStream(file);
        byte[] bytes = new byte[8 * 1024];
        int len = 0;
        while (true) {
            len = is.read(bytes);
            if (len > 0) {
                os.write(bytes, 0, len);
            } else {
                break;
            };
        }
        is.close();
        return os.toByteArray();
    }
}
