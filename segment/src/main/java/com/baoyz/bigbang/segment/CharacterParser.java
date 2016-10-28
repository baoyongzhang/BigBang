/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public class CharacterParser extends SimpleParser {

    @Override
    public String[] parseSync(String text) throws SegmentException {
        // TODO 应该用正则替换
        text = text.replaceAll(" ", "").replaceAll("\n", "");
        char[] chars = text.toCharArray();
        String[] result = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            result[i] = String.valueOf(chars[i]);
        }
        return result;
    }
}
