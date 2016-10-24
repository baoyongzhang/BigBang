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

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final TouchHandler mTouchHandler = new TouchHandler();
        final List<Filter> filters = new ArrayList<>();
        filters.add(new Filter.TextViewValidFilter());
        if ("com.tencent.mm".equals(loadPackageParam.packageName)) {//优化微信 下的体验。

            //朋友圈内容拦截。
            filters.add(new Filter.WeChatValidFilter(loadPackageParam.classLoader));
            //聊天详情中的文字点击事件优化
            XC_MethodHook MMTextViewTouchEvent = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    View view = (View) param.thisObject;
                    //拦截聊天界面快速点击进入信息详情
                    mTouchHandler.hookTouchEvent(view, (MotionEvent) param.args[0], filters);
                    param.setResult(true);

                }
            };
            findAndHookMethod(loadPackageParam.classLoader.loadClass("com.tencent.mm.ui.base.MMTextView"), "onTouchEvent", MotionEvent.class, MMTextViewTouchEvent);
        }

        // installer  不注入。 防止代码出错。进不去installer 中。
        if (!"de.robv.android.xposed.installer".equals(loadPackageParam.packageName)) {
            XC_MethodHook activityTouchEvent = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Activity activity = (Activity) param.thisObject;
                    View view = activity.findViewById(android.R.id.content);
                    MotionEvent event = (MotionEvent) param.args[0];
                    L.d(TAG, "activityTouchEvent: "+event.getAction());
                    mTouchHandler.hookTouchEvent(view,event, filters);
                }
            };
            findAndHookMethod(Activity.class, "onTouchEvent", MotionEvent.class, activityTouchEvent);
            XC_MethodHook viewTouchEvent = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if ((Boolean) param.getResult()) {
                        View view = (View) param.thisObject;
                        MotionEvent event = (MotionEvent) param.args[0];
                        L.d(TAG,view.getClass().getSimpleName());
                        L.d(TAG, "viewTouchEvent: "+event.getAction());
                        mTouchHandler.hookTouchEvent(view,event , filters);
                    }
                }
            };
            findAndHookMethod(View.class, "onTouchEvent", MotionEvent.class, viewTouchEvent);

        }
    }

}