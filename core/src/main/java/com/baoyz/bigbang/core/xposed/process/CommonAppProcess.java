package com.baoyz.bigbang.core.xposed.process;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.baoyz.bigbang.core.xposed.process.handler.ActivityTouchEvent;
import com.baoyz.bigbang.core.xposed.process.handler.TouchHandler;
import com.baoyz.bigbang.core.xposed.process.handler.ViewTouchEvent;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by dim on 17/1/18.
 */

public class CommonAppProcess implements IProcess {
    private static final String TAG = "CommonAppProcess";

    private final TouchHandler mTouchHandler = new TouchHandler();
    private final List<TextViewFilter> mTextViewFilters = new ArrayList<>();

    @Override
    public boolean process(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        mTextViewFilters.add(new TextViewFilter.TextViewValidTextViewFilter());
        // installer  不注入。 防止代码出错。进不去installer 中。
        if (!"de.robv.android.xposed.installer".equals(loadPackageParam.packageName)) {
            findAndHookMethod(Activity.class, "onTouchEvent", MotionEvent.class, new ActivityTouchEvent(mTouchHandler, mTextViewFilters));
            findAndHookMethod(View.class, "onTouchEvent", MotionEvent.class, new ViewTouchEvent(mTouchHandler, mTextViewFilters));
        }
        return true;
    }

}
