/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.segment_jcseg;

import android.content.Context;
import android.content.res.AssetManager;

import com.baoyz.bigbang.segment.SegmentException;
import com.baoyz.bigbang.segment.SegmentUtil;
import com.baoyz.bigbang.segment.SimpleParser;

import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.ISegment;
import org.lionsoul.jcseg.tokenizer.core.IWord;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.lionsoul.jcseg.tokenizer.core.SegmentFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoyongzhang on 2016/10/27.
 */
public class JcsegParser extends SimpleParser {

    private static final String DIR_NAME = "jcseg";
    private String mConfigPath;
    private String mLexiconPath;
    private final ADictionary mDic;
    private final JcsegTaskConfig mConfig;

    public JcsegParser(Context context) {
        copyAssets(context);
        File dir = context.getExternalFilesDir(DIR_NAME);
        mConfigPath = new File(dir, "jcseg.properties").getAbsolutePath();
        mLexiconPath = dir.getAbsolutePath();
        mConfig = new JcsegTaskConfig(mConfigPath);
        mConfig.setLexiconPath(new String[]{mLexiconPath});
        mDic = DictionaryFactory.createSingletonDictionary(mConfig);
    }

    @Override
    public String[] parseSync(String text) throws SegmentException {
        text = SegmentUtil.filterInvalidChar(text);
        try {
            ISegment segment = SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE, new Object[]{new StringReader(text), mConfig, mDic});
            IWord word;
            List<String> result = new ArrayList<>();
            while ((word = segment.next()) != null) {
                System.out.println(word.getValue());
                result.add(word.getValue());
            }
            return result.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SegmentException(e);
        }
    }

    private void copyAssets(Context context) {
        File filesDir = context.getExternalFilesDir(DIR_NAME);
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(DIR_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (files != null) for (String filename : files) {
            InputStream in;
            OutputStream out;
            try {
                File outFile = new File(filesDir, filename);
                if (outFile.exists()) {
                    continue;
                }
                out = new FileOutputStream(outFile);
                in = assetManager.open(DIR_NAME + "/" + filename);
                copyFile(in, out);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
