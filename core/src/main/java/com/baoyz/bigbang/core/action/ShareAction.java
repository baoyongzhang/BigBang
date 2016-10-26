/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.core.action;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class ShareAction implements Action {

    public static ShareAction create() {
        return new ShareAction();
    }

    @Override
    public void start(Context context, String text) {
        if (!TextUtils.isEmpty(text)) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(sharingIntent);
        }
    }
}
