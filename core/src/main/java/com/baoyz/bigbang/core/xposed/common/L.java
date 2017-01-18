package com.baoyz.bigbang.core.xposed.common;

import android.util.Log;

import com.baoyz.bigbang.core.BuildConfig;

/**
 * Created by dim on 16/10/24.
 */

public class L {

    private static boolean sEnable = BuildConfig.DEBUG;
    private static final String TAG = "XposedBigBang";

    public static void e(String tag, String msg) {
        if (sEnable) {
            Log.e(TAG + ":" + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sEnable) {
            Log.d(TAG + ":" + tag, msg);
        }
    }

    public static void logClass(String tag, Class c) {
        if(sEnable) {
            d(tag, "class: " + c.getName());
            if (c.getSuperclass() != null) {
                logClass(tag,c.getSuperclass());
            }
        }
    }
}
