/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.List;

/**
 * Created by baoyongzhang on 2016/10/27.
 */
public class JiebaParser extends SimpleParser {



    @Override
    public void parse(String text, Callback<String[]> callback) {
        try {
            JiebaSegmenter segmenter = new JiebaSegmenter();
            List<SegToken> result = segmenter.process(text, JiebaSegmenter.SegMode.INDEX);
            callback.finish(result.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
            callback.error(e);
        }
    }
}
