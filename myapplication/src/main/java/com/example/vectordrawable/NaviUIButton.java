package com.example.vectordrawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.example.myapplication.R;
import com.example.utils.UIUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NaviUIButton extends RelativeLayout {

    public static final int[] STATES_SELECTED = new int[]{android.R.attr.state_selected};
    public static final int[] STATES_NO = new int[] {};

    private ImageView imageView;
    private Drawable mDrawable;
    private Drawable mNormalDrawable;
    private Drawable mSelectDrawable;
    private LottieDrawable mAnimaDrawable;

    private TextView textView;
    private TextView reddotText;
    private View reddot;

    /**
     * 是否显示红点
     */
    private boolean showRedDot = false;
    /**
     * 提示信息
     */
    private String remindInfo = null;
    /**
     * 普通的红点宽度
     */
    private float pointWidth;
    /**
     * 红点背景文字宽度
     */
    private float pointTextWidth;
    /**
     * 距离右上角
     */
    private float pointMargin;

    /**
     * 判断是否为双击
     */
    private long firstClick;
    private int count;
    private OnDoubleClickListener onDoubleClickListener;

    private Paint paint;

    public NaviUIButton(Context context) {
        super(context);
        init();
    }

    public NaviUIButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NaviUIButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setClickable(true);

        pointWidth = UIUtils.dip2px(10);
        pointTextWidth = UIUtils.dip2px(12);
        pointMargin = UIUtils.dip2px(10);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textView = new TextView(getContext());
        textView.setClickable(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        LayoutParams textParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        addView(textView, textParams);

        try {
            imageView = new LottieAnimationView(getContext());
            imageView.setClickable(false);
            LayoutParams imageParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            imageParams.addRule(RelativeLayout.ABOVE, textView.getId());
            addView(imageView, imageParams);
        } catch (Exception e) {
            imageView = new ImageView(getContext());
            imageView.setClickable(false);
            LayoutParams imageParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            imageParams.addRule(RelativeLayout.ABOVE, textView.getId());
            addView(imageView, imageParams);
        }

        View midView = new View(getContext());
        LayoutParams mideParams = new LayoutParams(0, 0);
        mideParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        addView(midView, mideParams);

        reddot = new View(getContext());
        reddot.setBackgroundResource(R.drawable.phone_common_reddot_ball);
        reddot.setVisibility(GONE);
        LayoutParams reddotParams = new LayoutParams(UIUtils.dip2px(15), UIUtils.dip2px(15));
        reddotParams.addRule(RelativeLayout.RIGHT_OF, midView.getId());
        reddotParams.leftMargin = UIUtils.dip2px(6f);
        addView(reddot, reddotParams);

        reddotText = new TextView(getContext());
        reddotText.setBackgroundResource(R.drawable.reddot_num_1);
        reddotText.setVisibility(GONE);
        reddotText.setGravity(Gravity.CENTER);
        reddotText.setTextColor(0xFFFFFFFF);
        reddotText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        LayoutParams reddotTextParams = new LayoutParams(UIUtils.dip2px(30), UIUtils.dip2px(24));
        reddotTextParams.addRule(RelativeLayout.RIGHT_OF, midView.getId());
        reddotTextParams.leftMargin = UIUtils.dip2px(4);
        addView(reddotText, reddotTextParams);
    }

    public void setText(String text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    public String getText() {
        if (textView != null) {
            return textView.getText().toString();
        } else {
            return null;
        }
    }

    public void setTextColor(int color) {
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setTextColor(ColorStateList color) {
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setCompoundDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        mDrawable = drawable;
        mAnimaDrawable = null;
        if (mDrawable instanceof StateListDrawable) {
            StateListDrawable stateListDrawable = (StateListDrawable) mDrawable;
            stateListDrawable.setState(STATES_NO);
            mNormalDrawable = stateListDrawable.getCurrent();
            stateListDrawable.setState(STATES_SELECTED);
            mSelectDrawable = stateListDrawable.getCurrent();
        }
    }

    public Drawable getCompoundDrawable() {
        return mDrawable;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mDrawable == null) { // VipNavigationController运营换肤
            return;
        }
        if (textView != null) {
            textView.setSelected(selected);
        }

        if (imageView != null) {
            imageView.setSelected(selected);
            if (mSelectDrawable instanceof LottieDrawable) {
                if (selected) {
                    if (imageView instanceof LottieAnimationView) {
                        LottieAnimationView lottieView = (LottieAnimationView) imageView;
                        if (mAnimaDrawable == null) {
                            LottieComposition lottieComposition = ((LottieDrawable) mSelectDrawable).getComposition();
                            if (lottieComposition == null) {
                                imageView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setSelected(true);
                                    }
                                }, 100);
                                return;
                            }
                            lottieView.setComposition(lottieComposition);
                            if (imageView.getDrawable() instanceof LottieDrawable) {
                                mAnimaDrawable = (LottieDrawable) imageView.getDrawable();
                            }
                        } else {
                            imageView.setImageDrawable(mAnimaDrawable);
                        }
                        lottieView.setProgress(0);
                        lottieView.playAnimation();
                    } else {
                        LottieDrawable lottieDrawable = (LottieDrawable) mSelectDrawable;
                        if (lottieDrawable.getComposition() == null) {
                            imageView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setSelected(true);
                                }
                            }, 100);
                            return;
                        }
                        imageView.setImageDrawable(lottieDrawable);
                        lottieDrawable.setProgress(0);
                        lottieDrawable.playAnimation();
                    }
                } else {
                    imageView.setImageDrawable(mNormalDrawable);
                }
            }
        }
    }

    /**
     * 是否显示红点 默认没有
     */
    public void setShowRedDot(boolean show) {
        showRedDot = show;
        if (showRedDot) {
            reddot.setVisibility(View.VISIBLE);
        } else {
            reddot.setVisibility(View.GONE);
        }
    }

    /**
     * 更新提醒数字
     *
     * @param number 显示信息
     */
    public void setRemindPointText(int number) {
        if (number <= 0) { // 只显示红点
            reddotText.setVisibility(GONE);
            if (showRedDot) {
                reddot.setVisibility(VISIBLE);
            } else {
                reddot.setVisibility(GONE);
            }
        } else {
            if (number < 10) {
                reddotText.setBackgroundResource(R.drawable.reddot_num_1);
                reddotText.setPadding(0, 0, UIUtils.dip2px(7), 0);
            } else if (number < 100) {
                reddotText.setBackgroundResource(R.drawable.reddot_num_2);
                reddotText.setPadding(0, 0, 0, 0);
            } else {
                reddotText.setBackgroundResource(R.drawable.reddot_num_3);
                reddotText.setPadding(0, 0, 0, 0);
                number = 99;
            }
            reddotText.setVisibility(VISIBLE);
            reddotText.setText(String.valueOf(number));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long time = System.currentTimeMillis();
                if (firstClick != 0 && time - firstClick > 600) {
                    count = 0;
                }
                count++;
                if (count == 1) {
                    firstClick = System.currentTimeMillis();
                } else if (count == 2 && time - firstClick < 600 && onDoubleClickListener != null) {
                    onDoubleClickListener.onDoubleClick(this);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /***
     * 双击事件
     */
    public interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

}
