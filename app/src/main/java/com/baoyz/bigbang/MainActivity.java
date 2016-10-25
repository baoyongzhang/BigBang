package com.baoyz.bigbang;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyz.bigbang.service.BigBangService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView);

        ClipboardManager clipboardService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboardService.getPrimaryClip();
        if (primaryClip != null && primaryClip.getItemCount() > 0) {
            textView.setText(primaryClip.getItemAt(0).getText());
        }

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("bigBang://?extra_text=" + URLEncoder.encode(textView.getText().toString(), "utf-8"))));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        mButton = (Button) findViewById(R.id.weixin);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (BigBangService.isAccessibilitySettingsOn(getApplicationContext())) {
            mButton.setEnabled(false);
            mButton.setText("已开启微信支持");
        } else {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccessibilitySettings();
                }
            });
        }
    }

    private void openAccessibilitySettings() {
        new AlertDialog.Builder(this)
                .setMessage("不需要 root，需要在系统设置中开启权限，前往设置页面，找到 `BigBang`，然后开启。")
                .setNeutralButton("前往设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    }
                }).show();
    }

}
