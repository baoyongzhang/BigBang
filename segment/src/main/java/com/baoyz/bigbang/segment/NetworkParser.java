/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.segment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by baoyongzhang on 2016/10/27.
 */
public class NetworkParser extends SimpleParser {

    @Override
    public void parse(String text, final Callback<String[]> callback) {
        if (callback == null) {
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        try {
            request = new Request.Builder().get().url("http://fenci.kitdroid.org:3000/?text=" + URLEncoder.encode(text, "utf-8")).build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.error(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                final JSONArray array;
                try {
                    array = new JSONArray(string);
                    String[] result = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        result[i] = array.getString(i);
                    }
                    callback.finish(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.error(e);
                }
            }
        });
    }
}
