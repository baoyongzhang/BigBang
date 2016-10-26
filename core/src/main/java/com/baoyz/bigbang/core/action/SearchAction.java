/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.core.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public abstract class SearchAction implements Action {

    @Override
    public void start(Context context, String text) {
        if (!TextUtils.isEmpty(text)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, createSearchUri(text));
            context.startActivity(intent);
        }
    }

    public Uri createSearchUri(String text) {
        String encode = null;
        try {
            encode = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return createSearchUriWithEncodedText(encode);
    }

    public abstract Uri createSearchUriWithEncodedText(String encodedText);

}
