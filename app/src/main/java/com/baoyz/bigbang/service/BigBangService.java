/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.net.Uri;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by baoyongzhang on 2016/10/24.
 */
public class BigBangService extends AccessibilityService {

    private CharSequence mWindowClassName;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        CharSequence className = event.getClassName();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                mWindowClassName = event.getClassName();
                break;
            }
            case AccessibilityEvent.TYPE_VIEW_CLICKED: {
                if (isWechatUI() && "android.widget.TextView".equals(className)) {
                    AccessibilityNodeInfo source = event.getSource();
                    CharSequence text = source.getText();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("bigBang://?extra_text=" + text));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    private boolean isWechatUI() {
        return "com.tencent.mm.ui.LauncherUI".equals(mWindowClassName);
    }

    @Override
    public void onInterrupt() {

    }
}
