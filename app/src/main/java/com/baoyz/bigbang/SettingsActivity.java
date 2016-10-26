/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.baoyz.bigbang.core.BigBang;
import com.baoyz.bigbang.core.action.CopyAction;
import com.baoyz.treasure.Treasure;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class SettingsActivity extends AppCompatActivity {

    private TextView mSearchEngineText;
    private SwitchCompat mAutoCopySwitch;
    private Config mConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

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

        mAutoCopySwitch = (SwitchCompat) findViewById(R.id.auto_copy_switch);
        mAutoCopySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConfig.setAutoCopy(isChecked);
                BigBang.registerAction(BigBang.ACTION_BACK, mConfig.isAutoCopy() ? CopyAction.create() : null);
                updateUI();
            }
        });

        mConfig = Treasure.get(this, Config.class);
        updateUI();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateUI() {
        mSearchEngineText.setText(mConfig.getSearchEngine());
        mAutoCopySwitch.setChecked(mConfig.isAutoCopy());
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
