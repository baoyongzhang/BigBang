/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.core.action;

import android.net.Uri;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class GoogleSearchAction extends SearchAction {

    public static GoogleSearchAction create() {
        return new GoogleSearchAction();
    }

    @Override
    public Uri createSearchUriWithEncodedText(String encodedText) {
        return Uri.parse("https://www.google.com/search?q=" + encodedText);
    }
}
