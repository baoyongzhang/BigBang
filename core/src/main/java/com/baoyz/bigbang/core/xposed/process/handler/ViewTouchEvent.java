package com.baoyz.bigbang.core.xposed.process.handler;

import android.view.MotionEvent;
import android.view.View;

import com.baoyz.bigbang.core.xposed.common.L;
import com.baoyz.bigbang.core.xposed.process.TextViewFilter;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by dim on 17/1/18.
 */

public class ViewTouchEvent extends XC_MethodHook {
    private static final String TAG = "ViewTouchEvent";
    private final TouchHandler mTouchHandler;
    private final List<TextViewFilter> mTextViewFilters;

    public ViewTouchEvent(final TouchHandler touchHandler, final List<TextViewFilter> textViewFilters) {
        mTouchHandler = touchHandler;
        mTextViewFilters = textViewFilters;
    }
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        if ((Boolean) param.getResult()) {
            View view = (View) param.thisObject;
            MotionEvent event = (MotionEvent) param.args[0];
            L.d(TAG, view.getClass().getSimpleName());
            L.d(TAG, "viewTouchEvent: " + event.getAction());
            mTouchHandler.hookTouchEvent(view, event, mTextViewFilters, false);
        }
    }
}
