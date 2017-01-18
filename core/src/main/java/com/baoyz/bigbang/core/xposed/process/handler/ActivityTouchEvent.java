package com.baoyz.bigbang.core.xposed.process.handler;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.baoyz.bigbang.core.xposed.common.L;
import com.baoyz.bigbang.core.xposed.process.TextViewFilter;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by dim on 17/1/18.
 */

public class ActivityTouchEvent extends XC_MethodHook {
    private static final String TAG = "ActivityTouchEvent";
    private final TouchHandler mTouchHandler;
    private final List<TextViewFilter> mTextViewFilters;

    public ActivityTouchEvent(final TouchHandler touchHandler, final List<TextViewFilter> textViewFilters) {
        mTouchHandler = touchHandler;
        mTextViewFilters = textViewFilters;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        Activity activity = (Activity) param.thisObject;
        View view = activity.findViewById(android.R.id.content);
        MotionEvent event = (MotionEvent) param.args[0];
        L.d(TAG, "activityTouchEvent: " + event.getAction());
        mTouchHandler.hookTouchEvent(view, event, mTextViewFilters, false);
    }
}
