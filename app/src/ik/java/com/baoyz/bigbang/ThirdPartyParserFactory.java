/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import com.baoyz.bigbang.segment.SimpleParserFactory;
import com.baoyz.bigbang.segment.SimpleParser;
import com.baoyz.segment_ik.IKSegmenterParser;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public class ThirdPartyParserFactory implements SimpleParserFactory {

    @Override
    public SimpleParser createParser() {
        return new IKSegmenterParser();
    }
}
