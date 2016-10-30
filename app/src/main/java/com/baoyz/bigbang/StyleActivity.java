package com.baoyz.bigbang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baoyz.bigbang.core.BigBangLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class StyleActivity extends AppCompatActivity {

    private BigBangLayout mBigBang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        mBigBang = (BigBangLayout) findViewById(R.id.bigbang);
        DiscreteSeekBar textSize = (DiscreteSeekBar) findViewById(R.id.textSize);
        DiscreteSeekBar lineSpace = (DiscreteSeekBar) findViewById(R.id.lineSpace);
        DiscreteSeekBar itemSpace = (DiscreteSeekBar) findViewById(R.id.itemSpace);

        String[] testStrings = new String[]{"你", "可以", "调整", "文字", "大小", "、", "行间距", "以及", "块间距", "以", "达到", "最佳的", "操作", "手感", "。"};
        for (String testString : testStrings) {
            mBigBang.addTextItem(testString);
        }

        textSize.setOnProgressChangeListener(new SimpleListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mBigBang.setItemTextSize(value);
            }
        });

        lineSpace.setOnProgressChangeListener(new SimpleListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mBigBang.setLineSpace(value);
            }
        });

        itemSpace.setOnProgressChangeListener(new SimpleListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mBigBang.setItemSpace(value);
            }
        });
    }

    static abstract class SimpleListener implements DiscreteSeekBar.OnProgressChangeListener {

        @Override
        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

        }
    }
}
