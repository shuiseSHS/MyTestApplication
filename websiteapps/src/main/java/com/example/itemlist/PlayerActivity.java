package com.example.itemlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "VideoPlayActivity";
    private static final int PLAY_RETURN = 2 * 1000; // 2 seconds
    private static final String KEY_PLAY_POSITON = "paly_position";
    private static final String TOAST_ERROR_PLAY = "Paly error, please check url exist!";
    private static final String DIALOG_TITILE = "奋力加载中，请稍后...";

    private ProgressDialog progressDialog;
    private MediaController mc;
    private VideoView videoView;
    private LinearLayout llMain;
    private ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);

        videoView = new VideoView(this);
        videoView.requestFocus();
        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);

        mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setKeepScreenOn(true);
        mc.setAlpha(0.4f);

        videoView.setMediaController(mc);

        llMain = new LinearLayout(this);
        llMain.setGravity(Gravity.CENTER_VERTICAL);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setLayoutParams(params);

        llMain.addView(videoView, params);
        setContentView(llMain);

        initDialog();

        String url = getIntent().getStringExtra("url");
        loadData(url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int palyPosition = videoView.getCurrentPosition();
        if (palyPosition > PLAY_RETURN) {
            palyPosition -= PLAY_RETURN;
        }
        outState.putInt(KEY_PLAY_POSITON, palyPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        videoView.seekTo(savedInstanceState.getInt(KEY_PLAY_POSITON));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        progressDialog.cancel();
        videoView.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError:" + videoUrl);
        Toast.makeText(getApplicationContext(), TOAST_ERROR_PLAY + "\n" + videoUrl, Toast.LENGTH_LONG).show();
        progressDialog.cancel();
        finish();
        return true;
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(DIALOG_TITILE);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void loadData(final String url) {
        new Thread() {
            public void run() {
                try {
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder().url(url)
                            .header("User-Agent", "OkHttp Headers.java").build();
                    Call call = mOkHttpClient.newCall(request);
                    Response response = null;
                    response = call.execute();

                    String result = response.body().string();
                    videoUrl = result.substring(result.indexOf("video_url") + 12, result.indexOf(".mp4", result.indexOf("video_url")) + 4);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoView.setVideoURI(Uri.parse(videoUrl));
                            videoView.start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PlayerActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

}
