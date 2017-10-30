package com.example.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;

/**
 * Created by shisong on 2017/4/17.
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("###", "onLayout " + l + " " + t + " " + r + " " + b);
        int childCount = this.getChildCount();
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        int lineBottom = 0;
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            left += getMarginLeft(child);
            if (left + child.getMeasuredWidth() > getWidth()) {
                left = l;
                top = lineBottom + getMarginTop(child);
            }
            right = left + child.getMeasuredWidth();
            bottom = top + child.getMeasuredHeight();
            if (bottom > lineBottom) {
                lineBottom = bottom;
            }
            child.layout(left, top, right, bottom);

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
        Log.d("###", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.d("###", width + "  " + height);
        Log.d("###", (widthMode == EXACTLY) + "  " + (heighMode == AT_MOST));

        if (heighMode == AT_MOST) {
            int childCount = this.getChildCount();
            int currentWidth = 0;
            int parentHeight = 0;
            for (int i = 0; i < childCount; i++) {
                View child = this.getChildAt(i);
                currentWidth += (child.getMeasuredWidth() + getMarginLeft(child));
                if (parentHeight == 0) {
                    parentHeight = child.getMeasuredHeight();
                }
                if (currentWidth > width) {
                    currentWidth = child.getMeasuredWidth() + getMarginLeft(child);
                    parentHeight += (child.getMeasuredHeight() + getMarginTop(child));
                }
            }
            setMeasuredDimension(width, parentHeight);
        }
    }
}
