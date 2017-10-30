/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Checkable;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.qiyi.basecore.utils.FloatUtils;
import org.qiyi.widget.R;

@SuppressWarnings("unused")
public class PagerSlidingTabStrip extends HorizontalScrollView {

    /**
     * 从ColorState中获取选中颜色
     */
    protected static final int[] CHECKED_COLOR_ATTR = {
            android.R.attr.state_checked,
            android.R.attr.state_enabled
    };
    /**
     * Log Tag
     */
    private static final String TAG = "PagerSlidingTabStrip";
    /**
     * 使用的系统attrs
     */
    private static final int[] ATTRS = {
            android.R.attr.textSize
    };
    /**
     * 默认的指示器颜色
     */
    private static final int DEFAULT_INDICATOR_COLOR = 0xff0bbe06;

    /**
     * 监听ViewPager滑动实现Tab的滑动
     */
    private final PageListener mPageListener = new PageListener();
    /**
     * 所有RadioButton的RadioGroup
     */
    protected RadioGroup mTabsContainer;
    /**
     * 底部滑动的ViewPager
     */
    protected ViewPager mPager;

    /**
     * tab数量
     */
    protected int mTabCount;
    /**
     * tab颜色列表
     */
    protected SparseArray<ColorStateList> mTabColorArray = new SparseArray<>();
    /**
     * 当前的加粗的Tab位置
     */
    protected int mBoldPosition = 0;
    /**
     * 当前的Tab位置
     */
    protected int mCurrentPosition = 0;
    /**
     * 当前的位移
     */
    protected float mCurrentPositionOffset = 0f;

    /**
     * 指示器的Paint
     */
    protected Paint mIndicatorPaint;
    /**
     * 下划线的Paint
     */
    protected Paint mUnderlinePaint;
    /**
     * 分割线的Paint
     */
    protected Paint mDividerPaint;
    /**
     * 指示器颜色
     */
    protected int mIndicatorColor = DEFAULT_INDICATOR_COLOR;
    /**
     * 下划线颜色
     */
    protected int mUnderlineColor = 0xffe6e6e6;
    /**
     * 分割线颜色
     */
    protected int mDividerColor = Color.TRANSPARENT;
    /**
     * 标签文字颜色
     */
    protected ColorStateList mTabTextColor = ContextCompat.getColorStateList(getContext(), R.color.tab_color);
    /**
     * Tab的默认背景颜色
     */
    protected int mTabBackgroundResId = R.drawable.background_tab;
    /**
     * 指示器高度
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mIndicatorHeight = 3;
    /**
     * 指示器宽度
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mIndicatorWidth = 12;
    /**
     * 下划线高度
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mUnderlineHeight = 0;
    /**
     * 分割线上下的padding
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mDividerPadding = 12;
    /**
     * 分割线宽度
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mDividerWidth = 1;
    /**
     * 标签间的padding
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mTabPadding = 12;
    /**
     * 文字大小
     * 现在用sp表示 会在构造函数时转换为px
     */
    protected int mTabTextSize = 17;
    /**
     * 位移大小
     * 现在用dp表示 会在构造函数时转换为px
     */
    protected int mScrollOffset = 52;
    /**
     * 上次位移距离
     */
    protected int mLastScrollX = 0;
    /**
     * 是否扩展到均匀占满整个区域
     */
    protected boolean mShouldExpand = false;
    /**
     * 标签文字是否都是大写
     */
    protected boolean mTextAllCaps = true;
    /**
     * 标签文字的Typeface
     */
    protected Typeface mTabTypeface = null;
    /**
     * 标签文字Typeface的Style
     */
    protected int mTabTypefaceStyle = Typeface.NORMAL;
    /**
     * Tab被点击时的监听
     */
    protected OnClickListener mTabClickListener;
    /**
     * 是否当前选中滑动屏幕中央
     */
    protected boolean mToCenter = false;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    /**
     * 代理ViewPager切换的Listener
     */
    private OnPageChangeListener mDelegatePageListener;
    /**
     * 文字Tab添加监听事件
     */
    private ITextTabAddListener mTextTabAddListener;
    /**
     * 是通过点击直接选择的还是滑动选择的Tab
     * true: 点击
     * false: 滑动
     */
    private boolean mFromClick = false;
    /**
     * 横向滚动位置
     */
    private int mSaveScrollX;
    /**
     * 重置scrollX
     */
    private Runnable mResetScrollXRunnable = new Runnable() {
        @Override
        public void run() {
            mSaveScrollX = 0;
        }
    };

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        mTabsContainer = new RadioGroup(context);

        setFillViewport(true);
        setWillNotDraw(false);

        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mTabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset, dm);
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
        mUnderlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight, dm);
        mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);
        mIndicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorWidth, dm);
        mTabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabPadding, dm);
        mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, dm);
        mTabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabTextSize, dm);

        // get system attrs (android:textSize)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        mTabTextSize = a.getDimensionPixelSize(0, mTabTextSize);
        a.recycle();

        // get custom attrs
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        mIndicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, mIndicatorColor);
        mToCenter = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsScrollToCenter, mToCenter);
        mUnderlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, mUnderlineColor);
        mDividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, mDividerColor);
        mIndicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, mIndicatorHeight);
        mIndicatorWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorWidth, mIndicatorWidth);
        mUnderlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, mUnderlineHeight);
        mDividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, mDividerPadding);
        mTabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, mTabPadding);
        mTabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, mTabBackgroundResId);
        mShouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, mShouldExpand);
        mScrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, mScrollOffset);
        mTextAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, mTextAllCaps);
        a.recycle();

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        //noinspection SuspiciousNameCombination
        mIndicatorPaint.setStrokeWidth(mIndicatorHeight);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);

        mUnderlinePaint = new Paint();
        mUnderlinePaint.setAntiAlias(true);
        mUnderlinePaint.setStyle(Paint.Style.FILL);
        //noinspection SuspiciousNameCombination
        mUnderlinePaint.setStrokeWidth(mUnderlineHeight);
        mUnderlinePaint.setStrokeCap(Paint.Cap.BUTT);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerWidth);
    }

    /**
     * 通知数据发生变化
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        mTabCount = mPager.getAdapter().getCount();
        for (int i = 0; i < mTabCount; i++) {
            if (mPager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) mPager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, String.valueOf(mPager.getAdapter().getPageTitle(i)));
            }
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mPager == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                updateInGlobalLayoutListener();
            }
        });
    }

    protected void updateInGlobalLayoutListener() {
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        if (currentTab instanceof TextView) {
            restoreColorState(mCurrentPosition, (TextView) currentTab);
            ((TextView) currentTab).setTypeface(mTabTypeface, mTabTypefaceStyle);
        }
        currentTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
        if (currentTab instanceof TextView) {
            restoreColorState(mCurrentPosition + 1, (TextView) currentTab);
        }
        mCurrentPosition = mPager.getCurrentItem();
        if (mTabsContainer.getChildCount() > mCurrentPosition) {
            View tab = mTabsContainer.getChildAt(mCurrentPosition);
            if (tab instanceof Checkable) {
                ((Checkable) tab).setChecked(true);
            }
            setBoldTypeface(mCurrentPosition);
            scrollToChild(mCurrentPosition, 0);
        }
    }

    /**
     * 在某个位置添加新的标签
     *
     * @param position 位置
     * @param tab      新的Tab
     */
    protected void addTab(final int position, View tab) {
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() != position) {
                    mFromClick = true;
                }
                mPager.setCurrentItem(position, false);
                if (mTabClickListener != null) {
                    mTabClickListener.onClick(v);
                }
            }
        });
        if (mShouldExpand) {
            tab.setPadding(0, 0, 0, 0);
        } else {
            tab.setPadding(mTabPadding, 0, mTabPadding, 0);
        }
        mTabsContainer.addView(tab, position, mShouldExpand ? getExpandedTabLayoutParams() : getDefaultTabLayoutParams());
    }

    /**
     * 普通tab的LayoutParams
     */
    protected RadioGroup.LayoutParams getExpandedTabLayoutParams() {
        return new RadioGroup.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    /**
     * 如果{@link #mShouldExpand}为true
     * 则使用此LayoutParams
     */
    protected RadioGroup.LayoutParams getDefaultTabLayoutParams() {
        return new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 保存滚动位置
     */
    public void saveScrollX() {
        mSaveScrollX = mTabsContainer.getChildAt(mCurrentPosition).getLeft();
    }

    /**
     * 滚动到某个位置
     *
     * @param position 位置
     * @param offset   位移
     * @return 当前的View
     */
    public View scrollToChild(int position, int offset) {
        if (mTabCount == 0) {
            return null;
        }
        if (mScreenWidth == 0) {
            mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        }
        int newScrollX;
        if (mSaveScrollX > 0) {
            newScrollX = mSaveScrollX;
            this.postDelayed(mResetScrollXRunnable, 500);
        } else {
            View child = mTabsContainer.getChildAt(position);
            if (child != null) {
                newScrollX = child.getLeft() + offset;
            } else {
                return null;
            }
        }
        if (position > 0 || offset > 0) {
            newScrollX -= mScrollOffset;
        }
        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            if (mToCenter) {
                View child = mTabsContainer.getChildAt(position);
                if (child == null) {
                    return null;
                }
                int childWith = child.getMeasuredWidth();
                int left = child.getLeft();
                smoothScrollTo(left + childWith / 2 - mScreenWidth / 2, 0);
                return child;
            } else {
                scrollTo(newScrollX, 0);
            }
        }
        return null;
    }

    /**
     * 添加文字tab
     *
     * @param position 位置
     * @param title    标题
     */
    private void addTextTab(final int position, String title) {
        RadioButton tab = new RadioButton(getContext());
        tab.setButtonDrawable(new ColorDrawable());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setLines(1);
        tab.setIncludeFontPadding(false);
        tab.setBackgroundColor(Color.TRANSPARENT);
        addTab(position, tab);
        if (mTextTabAddListener != null) {
            mTextTabAddListener.onTextTabAdded(tab, position, title);
        }
        updateTextViewTabStyle(tab, position);
    }

    /**
     * 添加带icon的Tab
     *
     * @param position 位置
     * @param resId    icon的资源id
     */
    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    /**
     * 更新tab的样式
     */
    private void updateTabStyles() {
        int count = mTabsContainer.getChildCount();
        count = Math.min(count, mTabCount);
        for (int i = 0; i < count; i++) {
            View v = mTabsContainer.getChildAt(i);
            if (v == null) {
                return;
            }
            v.setBackgroundResource(mTabBackgroundResId);
            if (v instanceof TextView) {
                updateTextViewTabStyle((TextView) v, i);
            }
        }
    }

    /**
     * 更新给定TextView的样式
     *
     * @param tab      给定的TextView
     * @param position tab位置
     */
    private void updateTextViewTabStyle(TextView tab, int position) {
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
        if (position == mBoldPosition) {
            tab.setTypeface(mTabTypeface, Typeface.BOLD);
        } else {
            tab.setTypeface(mTabTypeface, mTabTypefaceStyle);
        }
        setTabTextColor(tab, position, mTabTextColor);
        if (mTextAllCaps) {
            tab.setAllCaps(true);
        }
    }

    /**
     * 设置Tab文字颜色
     *
     * @param tab      需要设置的Tab
     * @param position Tab所处的位置
     * @param color    颜色
     */
    protected void setTabTextColor(TextView tab, int position, @ColorRes int color) {
        setTabTextColor(tab, position, ContextCompat.getColorStateList(getContext(), color));
    }

    /**
     * 设置Tab文字颜色
     *
     * @param tab      需要设置的Tab
     * @param position Tab所处的位置
     * @param color    颜色
     */
    protected void setTabTextColor(TextView tab, int position, ColorStateList color) {
        tab.setTextColor(color);
        mTabColorArray.put(position, color);
    }

    /**
     * 设置文字颜色渐变
     */
    private void setTabColorGradient() {
        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        if (FloatUtils.floatsEqual(mCurrentPositionOffset, 0) && currentTab instanceof Checkable) {
            ((Checkable) currentTab).setChecked(true);
        }
        if (currentTab instanceof TextView) {
            if (FloatUtils.floatsEqual(mCurrentPositionOffset, 0)) {
                restoreColorState(mCurrentPosition, (TextView) currentTab);
            } else {
                if (mCurrentPositionOffset < 0.8) {
                    setTabColorGradient(mCurrentPosition, (TextView) currentTab, 1 - mCurrentPositionOffset * 1.25f);
                } else if (currentTab instanceof Checkable && ((Checkable) currentTab).isChecked()) {
                    setTabColorGradient(mCurrentPosition, (TextView) currentTab, 0);
                }
            }
        }
        View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
        if (nextTab instanceof TextView) {
            if (mCurrentPositionOffset > 0.2) {
                setTabColorGradient(mCurrentPosition + 1, (TextView) nextTab, mCurrentPositionOffset * 1.25f - 0.25f);
            } else {
                setTabColorGradient(mCurrentPosition + 1, (TextView) nextTab, 0);
            }
        }
    }

    /**
     * 恢复Tab的ColorStateList
     *
     * @param position tab的位置
     * @param tab      tab的View
     */
    private void restoreColorState(int position, TextView tab) {
        if (tab == null) {
            return;
        }
        ColorStateList colorStateList = mTabColorArray.get(position);
        if (colorStateList == null) {
            return;
        }
        tab.setTextColor(colorStateList);
    }

    /**
     * 设置渐变色
     *
     * @param position   tab位置
     * @param currentTab tab对象
     * @param fraction   渐变比例
     */
    private void setTabColorGradient(int position, TextView currentTab, float fraction) {
        if (currentTab == null) {
            return;
        }
        ColorStateList colorStateList = mTabColorArray.get(position);
        if (colorStateList == null) {
            return;
        }
        int emptyColor = colorStateList.getColorForState(EMPTY_STATE_SET, -1);
        if (emptyColor == -1) {
            emptyColor = colorStateList.getColorForState(ENABLED_STATE_SET, 0xff333333);
        }
        int selectionColor = colorStateList.getColorForState(CHECKED_COLOR_ATTR, emptyColor);
        int gradientColor = ColorUtils.blendARGB(emptyColor, selectionColor, fraction);
        currentTab.setTextColor(gradientColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount == 0) {
            return;
        }
        int height = getHeight();
        // draw indicator line
        drawIndicatorLine(canvas, height);
        // draw underline
        drawUnderLine(canvas, height);
        // draw divider
        drawDivider(canvas, height);
    }

    /**
     * 画整体下方的横线
     *
     * @param canvas 画布
     * @param height 控件高度
     */
    private void drawUnderLine(Canvas canvas, int height) {
        if (mUnderlineHeight > 0) {
            mUnderlinePaint.setColor(mUnderlineColor);
            canvas.drawLine(0, height - mUnderlineHeight / 2f, mTabsContainer.getWidth(), height - mUnderlineHeight / 2f,
                    mUnderlinePaint);
        }
    }

    /**
     * 画Tab之间的分割线
     *
     * @param canvas 画布
     * @param height 控件高度
     */
    private void drawDivider(Canvas canvas, int height) {
        mDividerPaint.setColor(mDividerColor);
        for (int i = 0; i < mTabCount - 1; i++) {
            View tab = mTabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), (float) height - mDividerPadding, mDividerPaint);
        }
    }

    /**
     * 画Tab下面的指示器
     * 右侧先移动到目标位置后左侧再移动
     *
     * @param canvas 画布
     * @param height 控件高度
     */
    protected void drawIndicatorLine(Canvas canvas, int height) {
        // 指定指示器的颜色
        mIndicatorPaint.setColor(mIndicatorColor);
        height -= mUnderlineHeight;

        View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
        float center = (currentTab.getLeft() + currentTab.getRight()) / 2f;
        if (FloatUtils.floatsEqual(center, 0)) {
            return;
        }

        float nextCenter;
        View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
        if (nextTab != null) {
            nextCenter = (nextTab.getLeft() + nextTab.getRight()) / 2f;
        } else {
            nextCenter = center;
        }

        float lineLeft;
        float lineRight;
        if (mCurrentPositionOffset <= 0.5f) {
            // 右侧先移动到目标位置
            lineLeft = center - mIndicatorWidth / 2f;
            lineRight = center + mIndicatorWidth / 2f + (nextCenter - center) * mCurrentPositionOffset * 2;
        } else {
            // 左侧再移动
            lineLeft = nextCenter - mIndicatorWidth / 2f - (nextCenter - center) * (1 - mCurrentPositionOffset) * 2;
            lineRight = nextCenter + mIndicatorWidth / 2f;
        }
        float halfEdge = mIndicatorHeight / 2f;
        canvas.drawLine(lineLeft + halfEdge, height - mIndicatorHeight / 2f, lineRight - halfEdge, height - mIndicatorHeight / 2f,
                mIndicatorPaint);
    }

    public ViewPager getViewPager() {
        return mPager;
    }

    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.removeOnPageChangeListener(mPageListener);
        pager.addOnPageChangeListener(mPageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }

    public void setTextTabAddListener(ITextTabAddListener textTabAddListener) {
        this.mTextTabAddListener = textTabAddListener;
    }

    public void setTabClickListener(OnClickListener tabClickListener) {
        this.mTabClickListener = tabClickListener;
    }

    /**
     * 设置是否当前选中滑动屏幕中央
     *
     * @param center 是否当前选中滑动屏幕中央
     */
    public void setSelectTabToCenter(boolean center) {
        this.mToCenter = center;
    }

    public void setIndicatorColorResource(@ColorRes int resId) {
        setIndicatorColor(ContextCompat.getColor(getContext(), resId));
    }

    public int getIndicatorColor() {
        return this.mIndicatorColor;
    }

    /**
     * 设置指示器的颜色
     *
     * @param indicatorColor 颜色
     */
    public void setIndicatorColor(@ColorInt int indicatorColor) {
        if (this.mIndicatorColor != indicatorColor) {
            if (indicatorColor == -1) {
                this.mIndicatorColor = DEFAULT_INDICATOR_COLOR;
            } else {
                this.mIndicatorColor = indicatorColor;
            }
            invalidate();
        }
    }

    public int getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        if (this.mIndicatorWidth != indicatorWidth) {
            this.mIndicatorWidth = indicatorWidth;
            invalidate();
        }
    }

    public int getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        if (this.mIndicatorHeight != indicatorLineHeightPx) {
            this.mIndicatorHeight = indicatorLineHeightPx;
            mIndicatorPaint.setStrokeWidth(mIndicatorHeight);
            invalidate();
        }
    }

    public void setUnderlineColorResource(@ColorRes int resId) {
        setUnderlineColor(ContextCompat.getColor(getContext(), resId));
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public void setUnderlineColor(@ColorInt int underlineColor) {
        if (this.mUnderlineColor != underlineColor) {
            this.mUnderlineColor = underlineColor;
            invalidate();
        }
    }

    public void setDividerColorResource(@ColorRes int resId) {
        setDividerColor(ContextCompat.getColor(getContext(), resId));
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public void setDividerColor(@ColorInt int dividerColor) {
        if (this.mDividerColor != dividerColor) {
            this.mDividerColor = dividerColor;
            invalidate();
        }
    }

    public int getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        if (this.mUnderlineHeight != underlineHeightPx) {
            this.mUnderlineHeight = underlineHeightPx;
            mUnderlinePaint.setStrokeWidth(mUnderlineHeight);
            invalidate();
        }
    }

    public int getDividerPadding() {
        return mDividerPadding;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        if (this.mDividerPadding != dividerPaddingPx) {
            this.mDividerPadding = dividerPaddingPx;
            invalidate();
        }
    }

    public int getScrollOffset() {
        return mScrollOffset;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        if (this.mScrollOffset != scrollOffsetPx) {
            this.mScrollOffset = scrollOffsetPx;
            invalidate();
        }
    }

    public boolean getShouldExpand() {
        return mShouldExpand;
    }

    /**
     * 设置是否扩展tab占满整个区域
     *
     * @param shouldExpand 是否扩展tab占满整个区域
     */
    public void setShouldExpand(boolean shouldExpand) {
        if (this.mShouldExpand != shouldExpand) {
            this.mShouldExpand = shouldExpand;
            requestLayout();
        }
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        if (this.mTextAllCaps != textAllCaps) {
            this.mTextAllCaps = textAllCaps;
            invalidate();
        }
    }

    public int getTextSize() {
        return mTabTextSize;
    }

    public void setTextSize(int textSizePx) {
        if (this.mTabTextSize != textSizePx) {
            this.mTabTextSize = textSizePx;
            updateTabStyles();
        }
    }

    public ColorStateList getTabTextColor() {
        return mTabTextColor;
    }

    /**
     * 设置标签文字颜色
     *
     * @param tabTextColor 颜色
     */
    public void setTabTextColor(ColorStateList tabTextColor) {
        if (mTabTextColor != tabTextColor) {
            mTabTextColor = tabTextColor;
            updateTabStyles();
        }
    }

    public void setTextColorResource(int resId) {
        setTabTextColor(ContextCompat.getColorStateList(getContext(), resId));
    }

    public void setTypeface(Typeface typeface, int style) {
        if (this.mTabTypeface != typeface || this.mTabTypefaceStyle != style) {
            this.mTabTypeface = typeface;
            this.mTabTypefaceStyle = style;
            updateTabStyles();
        }
    }

    public int getTabBackground() {
        return mTabBackgroundResId;
    }

    public void setTabBackground(@DrawableRes int resId) {
        if (this.mTabBackgroundResId != resId) {
            this.mTabBackgroundResId = resId;
            invalidate();
        }
    }

    public int getTabPaddingLeftRight() {
        return mTabPadding;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        if (this.mTabPadding != paddingPx) {
            this.mTabPadding = paddingPx;
            updateTabStyles();
        }
    }

    public LinearLayout getTabsContainer() {
        return mTabsContainer;
    }

    public boolean isFromClick() {
        return mFromClick;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // 恢复滚动位置
        mCurrentPosition = savedState.mCurrentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        // 保存滚动位置
        savedState.mCurrentPosition = mCurrentPosition;
        return savedState;
    }

    private void setBoldTypeface(int position) {
        View tab = mTabsContainer.getChildAt(mBoldPosition);
        if (tab instanceof TextView) {
            ((TextView) tab).setTypeface(mTabTypeface, mTabTypefaceStyle);
        }
        tab = mTabsContainer.getChildAt(position);
        if (tab instanceof TextView) {
            ((TextView) tab).setTypeface(mTabTypeface, Typeface.BOLD);
        }
        mBoldPosition = position;
    }

    /**
     * 是否是Icon的Tab
     */
    public interface IconTabProvider {
        /**
         * 获取icon的资源id
         *
         * @param position tab的位置
         * @return 此位置的icon资源id
         */
        @DrawableRes
        int getPageIconResId(int position);
    }

    /**
     * 文字Tab添加监听事件
     */
    public interface ITextTabAddListener {
        /**
         * 添加文字Tab回调
         *
         * @param tab      被添加的RadioButton
         * @param position Tab位置
         * @param title    Tab标题
         */
        void onTextTabAdded(RadioButton tab, int position, String title);
    }

    private static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        /**
         * 保存的当前滚动位置
         */
        private int mCurrentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mCurrentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mCurrentPosition);
        }
    }

    /**
     * 滚动ViewPager时滑动标签
     */
    @SuppressWarnings("WeakerAccess")
    protected class PageListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
            if (mCurrentPosition != position) {
                if (currentTab instanceof TextView) {
                    restoreColorState(mCurrentPosition, (TextView) currentTab);
                }
                mTabsContainer.clearCheck();
            }
            if (mCurrentPosition + 1 != position && mTabsContainer.getChildAt(mCurrentPosition + 1) instanceof TextView) {
                restoreColorState(mCurrentPosition + 1, (TextView) mTabsContainer.getChildAt(mCurrentPosition + 1));
            }
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            if (!mToCenter && mTabsContainer.getChildAt(position) != null) {
                scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));
            }
            // 设置Tab文字的颜色渐变
            setTabColorGradient();
            invalidate();
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mPager.getCurrentItem(), 0);
            }
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mToCenter) {
                scrollToChild(position, 0);
            }
            setBoldTypeface(position);
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
            mFromClick = false;
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mResetScrollXRunnable != null) {
            removeCallbacks(mResetScrollXRunnable);
        }
    }
}
