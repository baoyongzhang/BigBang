package com.baoyz.bigbang;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String text = data.getQueryParameter(EXTRA_TEXT);

            if (!TextUtils.isEmpty(text)) {
                OkHttpClient client = new OkHttpClient();
                Request request = null;
                try {
                    request = new Request.Builder().get().url("http://fenci.kitdroid.org:3000/?text=" + URLEncoder.encode(text, "utf-8")).build();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BigBangActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String string = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mLayout.reset();
                                    final JSONArray array = new JSONArray(string);
                                    for (int i = 0; i < array.length(); i++) {
                                        mLayout.addTextItem(array.getString(i));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        } else {
            this.finish();
        }
    }

    @Override
    public void onSearch(String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com/s?wd=" + URLEncoder.encode(text, "utf-8")));
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShare(String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(sharingIntent);
    }

    @Override
    public void onCopy(String text) {
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager service = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            service.setPrimaryClip(ClipData.newPlainText("BigBang", text));
            Toast.makeText(this, "已复制", Toast.LENGTH_SHORT).show();
        }
    }
}
