/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.content.Context;

import com.baoyz.bigbang.segment.NetworkParserFactory;
import com.baoyz.bigbang.segment.SimpleParserFactory;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public class SegmentEngine {

    public static final String TYPE_CHARACTER = "character"; // 单字符分词
    public static final String TYPE_NETWORK = "network";   // 网络API分词
    public static final String TYPE_THIRD = "third";     // 第三方库分词

    public static SimpleParserFactory getSegmentParserFactory(Context context) {
        String segmentEngine = Treasure.get(context, Config.class).getSegmentEngine();
        switch (segmentEngine) {
            case TYPE_CHARACTER:
                // TODO
                return null;
            case TYPE_NETWORK:
                return new NetworkParserFactory();
            case TYPE_THIRD:
                return new ThirdPartyParserFactory();
        }
        return null;
    }
}
