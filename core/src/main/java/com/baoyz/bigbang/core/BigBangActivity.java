package com.baoyz.bigbang.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.baoyz.bigbang.segment.HandlerCallback;
import com.baoyz.bigbang.segment.SimpleParser;

public class BigBangActivity extends AppCompatActivity implements BigBangLayout.ActionListener {

    public static final String EXTRA_TEXT = "extra_text";
    private BigBangLayout mLayout;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mLayout.reset();
        handleIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_bang);
        mLayout = (BigBangLayout) findViewById(R.id.bigbang);
        mLayout.setActionListener(this);
        if (BigBang.getItemSpace() > 0) mLayout.setItemSpace(BigBang.getItemSpace());
        if (BigBang.getLineSpace() > 0) mLayout.setLineSpace(BigBang.getLineSpace());
        if (BigBang.getItemTextSize() > 0) mLayout.setItemTextSize(BigBang.getItemTextSize());
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String text = data.getQueryParameter(EXTRA_TEXT);

            if (TextUtils.isEmpty(text)) {
                finish();
                return;
            }

            SimpleParser parser = BigBang.getSegmentParser();
            parser.parse(text, new HandlerCallback<String[]>() {
                @Override
                public void onFinish(String[] result) {
                    mLayout.reset();
                    for (String str : result) {
                        mLayout.addTextItem(str);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(BigBangActivity.this, "分词出错：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String selectedText = mLayout.getSelectedText();
        BigBang.startAction(this, BigBang.ACTION_BACK, selectedText);
    }

    @Override
    public void onSearch(String text) {
        BigBang.startAction(this, BigBang.ACTION_SEARCH, text);
    }

    @Override
    public void onShare(String text) {
        BigBang.startAction(this, BigBang.ACTION_SHARE, text);
    }

    @Override
    public void onCopy(String text) {
        BigBang.startAction(this, BigBang.ACTION_COPY, text);
    }

}
