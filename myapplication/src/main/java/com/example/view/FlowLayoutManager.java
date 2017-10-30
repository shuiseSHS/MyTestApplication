package com.example.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 李冰锋 on 2017/3/11.
 * E-mail:libf@ppfuns.com
 * pers.nelon.flowlayoutmanager
 */

public class FlowLayoutManager extends LinearLayoutManager {
    private static final String TAG = "FlowLayoutManager";

    private LayoutState mLayoutState;

    public FlowLayoutManager(Context context) {
        super(context);
        init();
    }

    private void init() {
        mLayoutState = new LayoutState();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        offsetChildrenVertical(-dy);
        updateLayoutState();
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    private void updateLayoutState() {
        mLayoutState.firstVisableItemPosition = findFirstVisibleItemPosition();
        mLayoutState.lastVisableItemPosition = findLastVisibleItemPosition();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        realLayout(recycler, mLayoutState, state);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void realLayout(RecyclerView.Recycler pRecycler, LayoutState pLayoutState, RecyclerView.State pState) {
        detachAndScrapAttachedViews(pRecycler);

        /*
        需要获取position
         */
        int startPosition = pLayoutState.firstVisableItemPosition - pLayoutState.preLayoutPositionOffset;
        int endPosition = pLayoutState.lastVisableItemPosition + pLayoutState.preLayoutPositionOffset;
        for (int position = startPosition; position < endPosition; position++) {
            View viewForPosition = pRecycler.getViewForPosition(position);
            addView(viewForPosition);

            measureChildWithMargins(viewForPosition, 0, 0);

            // TODO: 2017/3/13 获取LayoutHelper
            LayoutHelper layoutHelper = null;

            int indexInDelegateItem = layoutHelper.getItemIndexInDelegateItem(position);
            int left = layoutHelper.getSelfLeftOffset() + layoutHelper.getItemLeftOffset(indexInDelegateItem);
            int top = layoutHelper.getSelfTopOffset() + layoutHelper.getItemTopOffset(indexInDelegateItem);

            layoutDecoratedWithMargins(viewForPosition, left, top,
                    left + getDecoratedMeasuredWidth(viewForPosition),
                    top + getBottomDecorationHeight(viewForPosition));
        }
    }

    private class LayoutState {
        int firstVisableItemPosition;
        int lastVisableItemPosition;
        int preLayoutPositionOffset;
    }

    public class LayoutHelper {

        public int getItemIndexInDelegateItem(int pPosition) {

            return pPosition;
        }

        public int getItemLeftOffset(int pIndex) {
            return 0;
        }

        public int getItemTopOffset(int pIndex) {
            return 0;
        }

        public int getSelfLeftOffset() {
            return 0;
        }

        public int getSelfTopOffset() {
            return 0;
        }
    }
}