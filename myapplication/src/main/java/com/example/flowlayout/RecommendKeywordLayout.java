package com.example.flowlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.utils.UIUtils;

import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by shisong on 2017/4/17.
 * 搜索结果页的推荐关键字列表
 */
public class RecommendKeywordLayout extends ViewGroup {

    private OnClickListener mItemClickListener;

    public RecommendKeywordLayout(Context context) {
        super(context);
    }

    public RecommendKeywordLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendKeywordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnItemClickListener(OnClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    private OnClickListener mInnerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onClick(v);
            }
        }
    };

    public void setWordList(List<String> mKeywordList) {
        removeAllViews();
        TextView textView = new TextView(getContext());
        textView.setClickable(true);
        textView.setText("为您推荐：");
        textView.setTextColor(0xFF0BBE06);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        MarginLayoutParams layoutParams = new MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.topMargin = UIUtils.dip2px(3);
        layoutParams.bottomMargin = UIUtils.dip2px(3);
        addView(textView, layoutParams);
        ColorStateList colorStateList = getResources().getColorStateList(R.color.navi_text_color);

        for (int index = 0; index < mKeywordList.size(); index++) {
            String keyword = mKeywordList.get(index);
            textView = new TextView(getContext());
            textView.setClickable(true);
            textView.setText(keyword);
            textView.setTag(keyword);
            textView.setTextColor(colorStateList);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setOnClickListener(mInnerListener);
            layoutParams = new MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            layoutParams.topMargin = UIUtils.dip2px(4);
            layoutParams.bottomMargin = UIUtils.dip2px(4);
            addView(textView, layoutParams);

            if (index < (mKeywordList.size() - 1)) {
                View lineView = new View(getContext());
                lineView.setBackgroundColor(0xFF949494);
                MarginLayoutParams layoutParams1 = new MarginLayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(3));
                layoutParams1.leftMargin = UIUtils.dip2px(5);
                layoutParams1.rightMargin = UIUtils.dip2px(5);
                layoutParams1.topMargin = UIUtils.dip2px(7);
                layoutParams1.bottomMargin = UIUtils.dip2px(6);
                addView(lineView, layoutParams1);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        int curLineTop = 0;
        int nextLineTop = 0;
        for (int position = 0; position < childCount; position++) {
            View child = this.getChildAt(position);
            top = curLineTop + getMarginTop(child);
            bottom = top + child.getMeasuredHeight();
            left += getMarginLeft(child);
            right = left + child.getMeasuredWidth();

            if (child instanceof TextView && right > getWidth()) { // 换行
                TextView oldTxt = (TextView) child;
                String oldStr = oldTxt.getText().toString();

                TextView halfTxt = new TextView(getContext());
                halfTxt.setClickable(true);
                halfTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, oldTxt.getTextSize());
                int index = oldStr.length();
                int newTxtWidth = 0;
                while (index > 0) {
                    newTxtWidth = (int) halfTxt.getPaint().measureText(oldStr.substring(0, index));
                    if (newTxtWidth < getWidth() - left) {
                        break;
                    } else {
                        index--;
                    }
                }

                if (index > 0) {
                    halfTxt.setTextColor(oldTxt.getTextColors());
                    halfTxt.setText(oldStr.substring(0, index));
                    halfTxt.setTag(oldStr);
                    addView(halfTxt, position, oldTxt.getLayoutParams());
                    halfTxt.layout(left, top, left + newTxtWidth, top + child.getMeasuredHeight());
                    connect(oldTxt, halfTxt);

                    position++;
                    childCount++;
                    oldTxt.setText(oldStr.substring(index, oldStr.length()));
                } else {
                    newTxtWidth = 0;
                }

                left = 0;
                right = child.getMeasuredWidth() - newTxtWidth;
                curLineTop = nextLineTop;
                top = curLineTop + getMarginTop(child);
                bottom = top + child.getMeasuredHeight();
            }

            if (bottom + getMarginBottom(child) > nextLineTop) {
                nextLineTop = bottom + getMarginBottom(child);
            }

            if (child instanceof TextView) {
                child.layout(left, top, right, bottom);
            } else {
                child.layout(left, top, right, nextLineTop - getMarginBottom(child));
            }

            left = right + getMarginRight(child);
        }
    }

    private int getMarginLeft(View view) {
        if (view.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = ((MarginLayoutParams) view.getLayoutParams());
            return marginLayoutParams.leftMargin;
        }
        return 0;
    }

    private int getMarginRight(View view) {
        if (view.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = ((MarginLayoutParams) view.getLayoutParams());
            return marginLayoutParams.rightMargin;
        }
        return 0;
    }

    private int getMarginTop(View view) {
        if (view.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = ((MarginLayoutParams) view.getLayoutParams());
            return marginLayoutParams.topMargin;
        }
        return 0;
    }

    private int getMarginBottom(View view) {
        if (view.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = ((MarginLayoutParams) view.getLayoutParams());
            return marginLayoutParams.bottomMargin;
        }
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heighMode == AT_MOST || getLayoutParams().height == WRAP_CONTENT) {
            int childCount = this.getChildCount();
            int lineWidth = 0;
            int lineHeight = 0;
            int parentHeight = 0;
            for (int i = 0; i < childCount; i++) {
                View child = this.getChildAt(i);
                int childWidth = child.getMeasuredWidth() + getMarginLeft(child) + getMarginRight(child);
                int childHeight = child.getMeasuredHeight() + getMarginTop(child) + getMarginBottom(child);
                if (childHeight > lineHeight) {
                    lineHeight = childHeight;
                }
                lineWidth += childWidth;
                if (lineWidth > width) { // 换行
                    parentHeight += lineHeight;
                    lineHeight = childHeight;
                    lineWidth = childWidth;
                }
            }
            parentHeight += lineHeight;
            setMeasuredDimension(width, parentHeight);
        }
    }

    public void connect(TextView textview1, TextView textview2) {
        textview1.setOnTouchListener(new ConnectedTouchListener(textview2));
        textview2.setOnTouchListener(new ConnectedTouchListener(textview1));
    }

    private class ConnectedTouchListener implements OnTouchListener {
        private View connectedView;

        ConnectedTouchListener(View view) {
            connectedView = view;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (connectedView != null && event.getEdgeFlags() != -1) {
                MotionEvent newEvent = MotionEvent.obtain(event);
                newEvent.setLocation(connectedView.getWidth() / 2, connectedView.getHeight() / 2);
                newEvent.setEdgeFlags(-1);
                connectedView.dispatchTouchEvent(newEvent);
                newEvent.recycle();
            }
            return false;
        }
    }
}
