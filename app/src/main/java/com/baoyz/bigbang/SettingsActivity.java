/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.baoyz.bigbang.core.BigBang;
import com.baoyz.bigbang.core.action.CopyAction;
import com.baoyz.bigbang.core.xposed.XposedEnable;
import com.baoyz.bigbang.core.xposed.setting.XposedAppManagerActivity;
import com.baoyz.bigbang.service.ListenClipboardService;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class SettingsActivity extends AppCompatActivity {

    private TextView mSearchEngineText;
    private TextView mSegmentEngineText;
    private SwitchCompat mAutoCopySwitch;
    private SwitchCompat mListenClipboardSwitch;
    private Config mConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // 默认分词引擎
        findViewById(R.id.segment_engine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this).setItems(SegmentEngine.getSupportSegmentEngineNameList(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConfig.setSegmentEngine(SegmentEngine.getSupportSegmentEngineList()[which]);
                        SegmentEngine.setup(getApplicationContext());
                        updateUI();
                        if (SegmentEngine.TYPE_THIRD.equals(SegmentEngine.getSegmentParserType(getApplicationContext()))) {
                            new AlertDialog.Builder(SettingsActivity.this).setMessage("本地分词第一次使用的会比较慢，需要将字典加载到内存，会额外占用一部分内存空间。")
                                    .setPositiveButton("确定", null).show();
                        }
                    }
                }).show();
            }
        });

        mSegmentEngineText = (TextView) findViewById(R.id.segment_engine_text);

        // 默认搜索引擎
        findViewById(R.id.search_engine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this).setItems(SearchEngine.getSupportSearchEngineList(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mConfig.setSearchEngine(SearchEngine.getSupportSearchEngineList()[which]);
                        BigBang.registerAction(BigBang.ACTION_SEARCH, SearchEngine.getSearchAction(getApplicationContext()));
                        updateUI();
                    }
                }).show();
            }
        });

        mSearchEngineText = (TextView) findViewById(R.id.search_engine_text);

        // 返回自动复制
        mAutoCopySwitch = (SwitchCompat) findViewById(R.id.auto_copy_switch);
        mAutoCopySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfig.setAutoCopy(isChecked);
                BigBang.registerAction(BigBang.ACTION_BACK, mConfig.isAutoCopy() ? CopyAction.create() : null);
                updateUI();
            }
        });

        // 返回自动复制
        mListenClipboardSwitch = (SwitchCompat) findViewById(R.id.listen_clipboard_switch);
        mListenClipboardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfig.setListenClipboard(isChecked);
                if (mConfig.isListenClipboard()) {
                    ListenClipboardService.start(getApplicationContext());
                } else {
                    ListenClipboardService.stop(getApplicationContext());
                }
                updateUI();
            }
        });

        findViewById(R.id.bigbang_style).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, StyleActivity.class));
            }
        });
        findViewById(R.id.xposed_app_manger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, XposedAppManagerActivity.class));
            }
        });

        if (XposedEnable.isEnable()) {
            findViewById(R.id.xposed_app_manger).setVisibility(View.VISIBLE);
        }
        mConfig = Treasure.get(this, Config.class);
        updateUI();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateUI() {
        mSearchEngineText.setText(mConfig.getSearchEngine());
        mSegmentEngineText.setText(SegmentEngine.getSegmentEngineName(getApplicationContext()));
        mAutoCopySwitch.setChecked(mConfig.isAutoCopy());
        mListenClipboardSwitch.setChecked(mConfig.isListenClipboard());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
