/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.content.Context;

import com.baoyz.bigbang.core.BigBang;
import com.baoyz.bigbang.segment.CharacterParser;
import com.baoyz.bigbang.segment.NetworkParser;
import com.baoyz.bigbang.segment.SimpleParser;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/28.
 */
public class SegmentEngine {

    public static final String TYPE_CHARACTER = "character"; // 单字符分词
    public static final String TYPE_NETWORK = "network";   // 网络API分词
    public static final String TYPE_THIRD = "third";     // 第三方库分词

    public static final String[] ENGINE_LIST = new String[]{TYPE_CHARACTER, TYPE_NETWORK, TYPE_THIRD};
    public static final String[] ENGINE_NAME_LIST = new String[]{"单字符", "网络 API", "本地三方库(Beta)"};

    public static SimpleParser getSegmentParser(Context context) {
        String segmentEngine = Treasure.get(context, Config.class).getSegmentEngine();
        switch (segmentEngine) {
            case TYPE_CHARACTER:
                return new CharacterParser();
            case TYPE_NETWORK:
                return new NetworkParser();
            case TYPE_THIRD:
                return new ThirdPartyParser(context);
        }
        return null;
    }

    public static String getSegmentParserType(Context context) {
        return Treasure.get(context, Config.class).getSegmentEngine();
    }

    public static void setup(Context context) {
        SimpleParser parser = SegmentEngine.getSegmentParser(context);
        BigBang.setSegmentParser(parser);
    }

    public static String[] getSupportSegmentEngineNameList() {
        return ENGINE_NAME_LIST;
    }

    public static String[] getSupportSegmentEngineList() {
        return ENGINE_LIST;
    }

    public static String getSegmentEngineName(Context context) {
        String segmentEngine = Treasure.get(context, Config.class).getSegmentEngine();
        switch (segmentEngine) {
            case TYPE_CHARACTER:
                return ENGINE_NAME_LIST[0];
            case TYPE_NETWORK:
                return ENGINE_NAME_LIST[1];
            case TYPE_THIRD:
                return ENGINE_NAME_LIST[2];
        }
        return null;
    }
}
