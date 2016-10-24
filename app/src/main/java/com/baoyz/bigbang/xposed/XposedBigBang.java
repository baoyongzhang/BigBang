package com.baoyz.bigbang.xposed;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by dim on 16/10/23.
 */

public class XposedBigBang implements IXposedHookLoadPackage {

    private static final String TAG = "XposedBigBang";

    private final TouchHandler mTouchHandler = new TouchHandler();
    private final List<Filter> mFilters = new ArrayList<>();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        mFilters.add(new Filter.TextViewValidFilter());

        //优化微信 下的体验。
        if ("com.tencent.mm".equals(loadPackageParam.packageName)) {
            //朋友圈内容拦截。
            mFilters.add(new Filter.WeChatValidFilter(loadPackageParam.classLoader));
            //聊天详情中的文字点击事件优化
            findAndHookMethod(loadPackageParam.classLoader.loadClass("com.tencent.mm.ui.base.MMTextView"), "onTouchEvent",
                    MotionEvent.class, new MMTextViewTouchEvent());
        }

        // installer  不注入。 防止代码出错。进不去installer 中。
        if (!"de.robv.android.xposed.installer".equals(loadPackageParam.packageName)) {
            findAndHookMethod(Activity.class, "onTouchEvent", MotionEvent.class, new ActivityTouchEvent());
            findAndHookMethod(View.class, "onTouchEvent", MotionEvent.class, new ViewTouchEvent());
        }
    }


    private class MMTextViewTouchEvent extends XC_MethodHook {

        private boolean intercept = false;

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            //拦截聊天界面快速点击进入信息详情
            View view = (View) param.thisObject;
            MotionEvent event = (MotionEvent) param.args[0];

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    long preClickTimeMillis = mTouchHandler.getClickTimeMillis(view);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (preClickTimeMillis != 0) {
                        long interval = currentTimeMillis - preClickTimeMillis;
                        if (interval < TouchHandler.BIG_BANG_RESPONSE_TIME) {
                            intercept = true;
                        } else {
                            intercept = false;
                        }
                    } else {
                        intercept = false;
                    }
                    break;
            }
            mTouchHandler.hookTouchEvent(view, event, mFilters);
            if (intercept) {
                param.setResult(true);
            }

        }
    }


    private class ActivityTouchEvent extends XC_MethodHook {

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Activity activity = (Activity) param.thisObject;
            View view = activity.findViewById(android.R.id.content);
            MotionEvent event = (MotionEvent) param.args[0];
            L.d(TAG, "activityTouchEvent: " + event.getAction());
            mTouchHandler.hookTouchEvent(view, event, mFilters);
        }
    }


    private class ViewTouchEvent extends XC_MethodHook {

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            if ((Boolean) param.getResult()) {
                View view = (View) param.thisObject;
                MotionEvent event = (MotionEvent) param.args[0];
                L.d(TAG, view.getClass().getSimpleName());
                L.d(TAG, "viewTouchEvent: " + event.getAction());
                mTouchHandler.hookTouchEvent(view, event, mFilters);
            }
        }
    }

}