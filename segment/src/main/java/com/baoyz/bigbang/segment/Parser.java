/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

/**
 * Created by baoyongzhang on 2016/10/27.
 */
public interface Parser<T> {

    void parse(String text, Callback<T> callback);

}
