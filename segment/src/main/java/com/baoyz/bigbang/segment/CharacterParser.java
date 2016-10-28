/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public class CharacterParser extends SimpleParser {

    @Override
    public String[] parseSync(String text) throws SegmentException {
        char[] chars = text.toCharArray();
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                builder.append(c);
                continue;
            }
            if (builder.length() > 0) {
                result.add(builder.toString());
                builder.delete(0, builder.length());
            }
            if (SegmentUtil.skipChar(c)) {
                continue;
            }
            result.add(String.valueOf(c));
        }
        return result.toArray(new String[0]);
    }
}
