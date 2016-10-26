/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.app.Application;

import com.baoyz.bigbang.core.BigBang;
import com.baoyz.bigbang.core.action.BaiduSearchAction;
import com.baoyz.bigbang.core.action.CopyAction;
import com.baoyz.bigbang.core.action.ShareAction;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BigBang.registerAction(BigBang.ACTION_SEARCH, BaiduSearchAction.create());
        BigBang.registerAction(BigBang.ACTION_COPY, CopyAction.create());
        BigBang.registerAction(BigBang.ACTION_SHARE, ShareAction.create());
    }
}
