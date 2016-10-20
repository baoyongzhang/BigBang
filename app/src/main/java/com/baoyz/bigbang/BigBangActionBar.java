/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by baoyongzhang on 2016/10/20.
 */
public class BigBangActionBar extends ViewGroup {

    ImageView mSearch;
    ImageView mShare;
    ImageView mCopy;
    Drawable mBorder;
    private int mActionGap;
    private int mContentPadding;

    public BigBangActionBar(Context context) {
        this(context, null);
    }

    public BigBangActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigBangActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSubViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BigBangActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSubViews();
    }

    private void initSubViews() {
        Context context = getContext();

        mBorder = ContextCompat.getDrawable(context, R.drawable.bigbang_action_bar_bg);

        mSearch = new ImageView(context);
        mSearch.setImageResource(R.mipmap.bigbang_action_search);
        mShare = new ImageView(context);
        mShare.setImageResource(R.mipmap.bigbang_action_share);
        mCopy = new ImageView(context);
        mCopy.setImageResource(R.mipmap.bigbang_action_copy);

        addView(mSearch, createLayoutParams());
        addView(mShare, createLayoutParams());
        addView(mCopy, createLayoutParams());

        setWillNotDraw(false);

        mActionGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        mContentPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
    }

    private LayoutParams createLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        return params;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(measureSpec, measureSpec);
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height + mContentPadding + mSearch.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        layoutSubView(mSearch, mActionGap, 0);
        layoutSubView(mShare, width - mActionGap * 2 - mShare.getMeasuredWidth() - mCopy.getMeasuredWidth(), 0);
        layoutSubView(mCopy, width - mActionGap - mCopy.getMeasuredWidth(), 0);

        mBorder.setBounds(0, mSearch.getMeasuredHeight() / 2, width, height);
    }

    private void layoutSubView(View view, int l, int t) {
        view.layout(l, t, view.getMeasuredWidth() + l, view.getMeasuredHeight() + t);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBorder.draw(canvas);
    }

    public int getContentPadding() {
        return mContentPadding;
    }
}
