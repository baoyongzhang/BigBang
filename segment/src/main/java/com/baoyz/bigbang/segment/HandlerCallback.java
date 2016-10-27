/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

import android.os.Handler;

/**
 * Created by baoyongzhang on 2016/10/27.
 */
public abstract class HandlerCallback<T> implements Callback<T> {

    private final Handler mHandler;

    public HandlerCallback() {
        mHandler = new Handler();
    }

    @Override
    public void finish(final T result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFinish(result);
            }
        });
    }

    @Override
    public void error(final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onError(e);
            }
        });
    }

    public abstract void onFinish(T result);
    public abstract void onError(Exception e);
}
