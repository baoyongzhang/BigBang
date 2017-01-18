package com.baoyz.bigbang.core.xposed.process;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.baoyz.bigbang.core.xposed.process.handler.ActivityTouchEvent;
import com.baoyz.bigbang.core.xposed.process.handler.TouchHandler;
import com.baoyz.bigbang.core.xposed.process.handler.ViewTouchEvent;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.baoyz.bigbang.core.xposed.common.XposedUtils.forClassName;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by dim on 17/1/18.
 */

public class WeChatProcess extends AppProcess {

    private final TouchHandler mTouchHandler = new TouchHandler();
    private final List<TextViewFilter> mTextViewFilters = new ArrayList<>();

    @Override
    String getAppPackageName() {
        return "com.tencent.mm";
    }

    @Override
    protected boolean onProcess(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        mTextViewFilters.add(new TextViewFilter.TextViewValidTextViewFilter());
        mTextViewFilters.add(new TextViewFilter.WeChatValidTextViewFilter(loadPackageParam.classLoader));
        //聊天详情中的文字点击事件优化
        Class<?> mmTextView = forClassName(loadPackageParam.classLoader, "com.tencent.mm.ui.base.MMTextView");
        if (mmTextView == null) {
            mmTextView = forClassName(loadPackageParam.classLoader, "com.tencent.mm.ui.widget.MMTextView");
        }
        if (mmTextView != null) {
            findAndHookMethod(mmTextView, "onTouchEvent",
                    MotionEvent.class, new MMTextViewTouchEvent());
        }
        findAndHookMethod(Activity.class, "onTouchEvent", MotionEvent.class, new ActivityTouchEvent(mTouchHandler, mTextViewFilters));
        findAndHookMethod(View.class, "onTouchEvent", MotionEvent.class, new ViewTouchEvent(mTouchHandler, mTextViewFilters));
        return true;

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
                        intercept = interval < TouchHandler.BIG_BANG_RESPONSE_TIME;
                    } else {
                        intercept = false;
                    }
                    break;
            }
            mTouchHandler.hookTouchEvent(view, event, mTextViewFilters, true);
            if (intercept) {
                param.setResult(true);
            }
        }
    }
}
