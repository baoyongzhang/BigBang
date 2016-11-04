/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.baoyz.bigbang.widget.FloatingView;

/**
 * Created by baoyongzhang on 2016/11/1.
 */
public final class ListenClipboardService extends Service {

    private ClipboardManager mClipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            showAction();
        }
    };
    private FloatingView mFloatingView;

    public static void start(Context context) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);
    }

    public static void stop(Context context) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.stopService(serviceIntent);
    }

    private void showAction() {
        ClipData primaryClip = mClipboardManager.getPrimaryClip();
        if (primaryClip != null && primaryClip.getItemCount() > 0 && !"BigBang".equals(primaryClip.getDescription().getLabel())) {
            CharSequence text = primaryClip.getItemAt(0).coerceToText(this);
            if (text != null) {
                mFloatingView.setText(text.toString());
                mFloatingView.show();
            }
        }
    }

    @Override
    public void onCreate() {
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);

        mFloatingView = new FloatingView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClipboardManager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}