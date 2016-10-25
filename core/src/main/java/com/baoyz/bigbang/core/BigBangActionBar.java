/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.core;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
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
class BigBangActionBar extends ViewGroup implements View.OnClickListener {

    ImageView mSearch;
    ImageView mShare;
    ImageView mCopy;
    Drawable mBorder;
    private int mActionGap;
    private int mContentPadding;
    private ActionListener mActionListener;

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
        mBorder.setCallback(this);

        mSearch = new ImageView(context);
        mSearch.setImageResource(R.mipmap.bigbang_action_search);
        mSearch.setOnClickListener(this);
        mShare = new ImageView(context);
        mShare.setImageResource(R.mipmap.bigbang_action_share);
        mShare.setOnClickListener(this);
        mCopy = new ImageView(context);
        mCopy.setImageResource(R.mipmap.bigbang_action_copy);
        mCopy.setOnClickListener(this);

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

        Rect oldBounds = mBorder.getBounds();
        Rect newBounds = new Rect(0, mSearch.getMeasuredHeight() / 2, width, height);

        if (!oldBounds.equals(newBounds)) {
            ObjectAnimator.ofObject(mBorder, "bounds", new RectEvaluator(), oldBounds, newBounds).setDuration(200).start();
        }
    }

    private void layoutSubView(View view, int l, int t) {
        view.layout(l, t, view.getMeasuredWidth() + l, view.getMeasuredHeight() + t);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBorder.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mBorder;
    }

    public int getContentPadding() {
        return mContentPadding;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public void onClick(View v) {
        if (mActionListener == null) {
            return;
        }
        if (v == mSearch) {
            mActionListener.onSearch();
        } else if (v == mShare) {
            mActionListener.onShare();
        } else if (v == mCopy) {
            mActionListener.onCopy();
        }
    }

    interface ActionListener {
        void onSearch();
        void onShare();
        void onCopy();
    }
}
