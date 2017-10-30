package com.example.vectordrawable;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.example.myapplication.R;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * Created by shisong on 2017/5/8.
 *
 */
public class VectorDrawableActivity extends Activity implements View.OnClickListener {
    //id的名字无意义，因在不同模式下，tab的类型不一样
    public static int[] NAVI_VIEW_ID = new int[]{R.id.navi0, R.id.navi1, R.id.navi2, R.id.navi3, R.id.navi4};

    private ImageView img;
    private ImageView img_lottie;
    private LottieAnimationView lottieAnimationView;
    private SimpleDraweeView mSimpleDraweeView;
    private LottieDrawable littieDrawable;
    private String imageUri2 = "http://p5.qhimg.com/t01d0e0384b952ed7e8.gif";
    private NaviUIButton[] naviUIButtons = new NaviUIButton[5];

    private View testInclude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//放在加载布局之前
        setContentView(R.layout.activity_vectordrawable);

        Log.e("######", "" + findViewById(R.id.testIncludeLayoutOut));
        Log.e("######", "" + findViewById(R.id.testIncludeLayout));

        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.frsco_img1);
        loadGifPicInApp(mSimpleDraweeView, R.drawable.phone_navi_find_selected);

        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(this);
        img.performClick();

        img_lottie = (ImageView) findViewById(R.id.img_lottie);
        littieDrawable = new LottieDrawable();
        LottieComposition.Factory.fromAssetFileName(this, "disagree.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                littieDrawable.setComposition(composition);
            }
        });
        img_lottie.setImageDrawable(littieDrawable);
        img_lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                littieDrawable.setProgress(0);
                littieDrawable.playAnimation();
            }
        });

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.img_lottie1);
        lottieAnimationView.setAnimation("data.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottieAnimationView.setProgress(0);
                lottieAnimationView.playAnimation();
            }
        });

        final LottieAnimationView lottieAnimationView2 = (LottieAnimationView) findViewById(R.id.img_lottie2);
        lottieAnimationView2.setAnimation("pp.json");
        lottieAnimationView2.playAnimation();
        lottieAnimationView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottieAnimationView2.setProgress(0);
                lottieAnimationView2.playAnimation();
            }
        });

        for (int i = 0; i < NAVI_VIEW_ID.length; i++) {
            Drawable drawable = getResources().getDrawable(R.drawable.img_selected1);
            naviUIButtons[i] = (NaviUIButton) findViewById(NAVI_VIEW_ID[i]);
            naviUIButtons[i].setText("视频" + i);
            naviUIButtons[i].setCompoundDrawable(drawable);
            naviUIButtons[i].setShowRedDot(true);
            naviUIButtons[i].setRemindPointText(811);
            naviUIButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    ((NaviUIButton)v).setShowRedDot(false);
                    ((NaviUIButton)v).setRemindPointText(0);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                anima();
                if (mSimpleDraweeView != null && mSimpleDraweeView.getController() != null) {
                    Animatable animatable = mSimpleDraweeView.getController().getAnimatable();
                    if (animatable != null) {
                        animatable.start();
                    }
                }
                break;
        }
    }

    private void anima() {
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, R.drawable.va_test);
        if (animatedVectorDrawableCompat == null) {
            return;
        }
        img.setImageDrawable(animatedVectorDrawableCompat);
        if (animatedVectorDrawableCompat.isRunning()) {
            animatedVectorDrawableCompat.stop();
        } else {
            animatedVectorDrawableCompat.start();
        }
    }

    /**
     * 设置自动播放图片
     */
    private void setImage() {
        Uri uri = Uri.parse(imageUri2);
        DraweeController draweeController = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
        mSimpleDraweeView.setController(draweeController);
    }

    /**
     * @param simpleDraweeView
     * @param resId
     */
    private void loadGifPicInApp(@NonNull SimpleDraweeView simpleDraweeView, int resId) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(controllerListener)
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(draweeController);
    }

    private ControllerListener<ImageInfo> controllerListener = new ControllerListener<ImageInfo>() {
        @Override
        public void onSubmit(String id, Object callerContext) {
            Log.d("###", "onSubmit");
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            Log.d("###", "onFinalImageSet");
        }

        @Override
        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            Log.d("###", "onIntermediateImageSet");
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            Log.d("###", "onIntermediateImageFailed");
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            Log.d("###", "onFailure");
        }

        @Override
        public void onRelease(String id) {
            Log.d("###", "onRelease");
        }
    };


    public void setImage(View v) {
        ImageView imageView = (ImageView) findViewById(R.id.testImage);
        imageView.setImageResource(R.drawable.test);
    }
}
