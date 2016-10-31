package org.wltea.analyzer.core;

/**
 * Created by baoyongzhang on 2016/10/31.
 */
public class UselessSegmenter implements ISegmenter {

    static final String SEGMENTER_NAME = "USELESS_SEGMENTER";

    @Override
    public void analyze(AnalyzeContext context) {
        if (CharacterUtil.CHAR_USELESS == context.getCurrentCharType()) {
            Lexeme newLexeme = new Lexeme(context.getBufferOffset() , 0 , 1 , Lexeme.TYPE_UNKNOWN);
            context.addLexeme(newLexeme);
            context.lockBuffer(SEGMENTER_NAME);
        }
    }

    @Override
    public void reset() {

    }
}
