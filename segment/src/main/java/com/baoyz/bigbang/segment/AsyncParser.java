/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public abstract class AsyncParser<T> implements Parser<T> {

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void parse(final String text, final Callback<T> callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.finish(parseSync(text));
                } catch (SegmentException e) {
                    e.printStackTrace();
                    callback.error(e);
                }
            }
        });
    }

    public T parseSync(String text) throws SegmentException {
        throw new SegmentException("Not yet implemented");
    }
}
