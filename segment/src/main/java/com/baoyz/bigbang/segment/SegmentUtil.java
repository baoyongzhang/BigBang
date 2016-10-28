/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public class SegmentUtil {

    public static String filterInvalidChar(String text) {
        return text.replaceAll("\\s*|\t|\r|\n", "");
    }

    public static boolean skipChar(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }
}
