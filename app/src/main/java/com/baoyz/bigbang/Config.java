/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import com.baoyz.treasure.Default;
import com.baoyz.treasure.Preferences;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
@Preferences
public interface Config {

    @Default(SearchEngine.BAIDU)
    String getSearchEngine();
    void setSearchEngine(String searchEngine);

    @Default("false")
    boolean isAutoCopy();
    void setAutoCopy(boolean autoCopy);

    @Default(SegmentEngine.TYPE_NETWORK)
    String getSegmentEngine();
    void setSegmentEngine(String segmentEngine);
}
