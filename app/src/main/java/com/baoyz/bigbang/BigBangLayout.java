/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoyongzhang on 2016/10/19.
 */
public class BigBangLayout extends ViewGroup {

    private int mLineSpace;
    private int mItemSpace;
    private Item mTargetItem;
    private List<Line> mLines;
    private int mActionBarTopHeight;
    private int mActionBarBottomHeight;
    private BigBangActionBar mActionBar;

    public BigBangLayout(Context context) {
        super(context);
    }

    public BigBangLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigBangLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttribute(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BigBangLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttribute(attrs);
    }

    private void readAttribute(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BigBangLayout);
            mItemSpace = typedArray.getDimensionPixelSize(R.styleable.BigBangLayout_itemSpace, getResources().getDimensionPixelSize(R.dimen.big_bang_default_item_space));
            mLineSpace = typedArray.getDimensionPixelSize(R.styleable.BigBangLayout_lineSpace, getResources().getDimensionPixelSize(R.dimen.big_bang_default_line_space));
            typedArray.recycle();
            mActionBarBottomHeight = mLineSpace;
            mActionBarTopHeight = getResources().getDimensionPixelSize(R.dimen.big_bang_action_bar_height);
        }

        // TODO 暂时放到这里
        mActionBar = new BigBangActionBar(getContext());

        addView(mActionBar, 0);
    }

    public void addTextItem(String text) {
        TextView view = new TextView(getContext());
        view.setText(text);
        view.setBackgroundResource(R.drawable.item_background);
        view.setGravity(Gravity.CENTER);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int contentWidthSize = widthSize - mActionBar.getContentPadding();
        int heightSize = 0;

        int childCount = getChildCount();

        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        mLines = new ArrayList<>();
        Line currentLine = null;
        int currentLineWidth = contentWidthSize;
        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);

            if (mActionBar == child) {
                continue;
            }

            child.measure(measureSpec, measureSpec);

            if (currentLineWidth > 0) {
                currentLineWidth += mItemSpace;
            }
            currentLineWidth += child.getMeasuredWidth();
            if (mLines.size() == 0 || currentLineWidth > contentWidthSize) {
                heightSize += child.getMeasuredHeight();
                currentLineWidth = child.getMeasuredWidth();
                currentLine = new Line(mLines.size());
                mLines.add(currentLine);
            }
            Item item = new Item(currentLine);
            item.view = child;
            item.index = i;
            item.width = child.getMeasuredWidth();
            item.height = child.getMeasuredHeight();
            currentLine.addItem(item);
        }

        Line firstSelectedLine = findFirstSelectedLine();
        Line lastSelectedLine = findLastSelectedLine();
        if (firstSelectedLine != null && lastSelectedLine != null) {
            int selectedLineHeight = (lastSelectedLine.index - firstSelectedLine.index + 1) * (firstSelectedLine.getHeight() + mLineSpace);
            mActionBar.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(selectedLineHeight, MeasureSpec.UNSPECIFIED));
        }

        int size = heightSize + getPaddingTop() + getPaddingBottom() + (mLines.size() - 1) * mLineSpace + mActionBarTopHeight + mActionBarBottomHeight;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top;
        int left;
        int offsetTop;

        Line lastSelectedLine = findLastSelectedLine();
        Line firstSelectedLine = findFirstSelectedLine();

        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            List<Item> items = line.getItems();
            left = getPaddingLeft() + mActionBar.getContentPadding();

            if (firstSelectedLine != null && firstSelectedLine.index > line.index) {
                offsetTop = -mActionBarTopHeight;
            } else if (lastSelectedLine != null && lastSelectedLine.index < line.index) {
                // TODO
                offsetTop = mActionBarBottomHeight;
            } else {
                offsetTop = 0;
            }

            for (int j = 0; j < items.size(); j++) {
                Item item = items.get(j);
                top = getPaddingTop() + i * (item.height + mLineSpace) + offsetTop + mActionBarTopHeight;
                View child = getChildAt(item.index);
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left += child.getMeasuredWidth() + mItemSpace;
            }
        }

        if (firstSelectedLine != null && lastSelectedLine != null) {

            int actionBarTop = firstSelectedLine.index * (firstSelectedLine.getHeight() + mLineSpace) + getPaddingTop();
            mActionBar.layout(getPaddingLeft(), actionBarTop, getPaddingLeft() + mActionBar.getMeasuredWidth(), actionBarTop + mActionBar.getMeasuredHeight());
        }
    }

    private Line findLastSelectedLine() {
        Line result = null;
        for (Line line : mLines) {
            if (line.hasSelected()) {
                result = line;
            }
        }
        return result;
    }

    private Line findFirstSelectedLine() {
        for (Line line : mLines) {
            if (line.hasSelected()) {
                return line;
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                Item item = findItemFromPoint((int) event.getX(), (int) event.getY());
                if (mTargetItem != item) {
                    mTargetItem = item;
                    if (item != null) {
                        item.setSelected(!item.isSelected());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                requestLayout();
                invalidate();
                break;
        }
        return true;
    }

    private Item findItemFromPoint(int x, int y) {
        for (Line line : mLines) {
            List<Item> items = line.getItems();
            for (Item item : items) {
                if (item.getRect().contains(x, y)) {
                    return item;
                }
            }
        }
        return null;
    }

    private View findChildFromPoint(int x, int y) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect rect = new Rect();
            child.getHitRect(rect);
            if (rect.contains(x, y)) {
                return child;
            }
        }
        return null;
    }

    static class Line {
        int index;
        List<Item> items;

        public Line(int index) {
            this.index = index;
        }

        void addItem(Item item) {
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(item);
        }

        List<Item> getItems() {
            return items;
        }

        boolean hasSelected() {
            for (Item item : items) {
                if (item.isSelected()) {
                    return true;
                }
            }
            return false;
        }

        int getHeight() {
            List<Item> items = getItems();
            if (items != null && items.size() > 0) {
                return items.get(0).view.getMeasuredHeight();
            }
            return 0;
        }

    }

    static class Item {
        Line line;
        int index;
        int height;
        int width;
        View view;

        public Item(Line line) {
            this.line = line;
        }

        Rect getRect() {
            Rect rect = new Rect();
            view.getHitRect(rect);
            return rect;
        }

        boolean isSelected() {
            return view.isSelected();
        }

        void setSelected(boolean selected) {
            view.setSelected(selected);
        }
    }
}
