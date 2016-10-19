package com.baoyz.bigbang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BigBangActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "extra_text";
    private BigBangLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_bang);

        mLayout = (BigBangLayout) findViewById(R.id.bigbang);

        String text = getIntent().getStringExtra(EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get().url("http://192.168.10.42:3000/?text=" + text).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    try {
                        final JSONArray array = new JSONArray(string);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < array.length(); i++) {
                                    try {
                                        mLayout.addTextItem(array.getString(i));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
