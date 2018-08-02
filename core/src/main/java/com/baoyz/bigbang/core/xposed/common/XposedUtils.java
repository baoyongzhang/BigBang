package com.baoyz.bigbang.core.xposed.common;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by dim on 17/1/17.
 */

public class XposedUtils {
    
    public static void setXpoedEnable(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        if (loadPackageParam.packageName.startsWith("com.baoyz.bigbang")) {/* This should be handled on your main HookManager (The class which implements IXposedHookLoadPackage)
        I recommend a switch (loadPackageParam.packageName).... statement for this kind of checks
        
        Replace LoadPackageParam with ClassLoader in the parameters of this method (obviously you call it with loadPackageParam.classLoader) if you remove the check (to optimize it a bit)
        */
            findAndHookMethod("com.baoyz.bigbang.core.xposed.XposedEnable", loadPackageParam.classLoader, "isEnable", XC_MethodReplacemet.returnConstant(true));
        }
    }
    
    // Have a look at the "findClassIfExists" method in the XposedHelpers class. As far as I know, it does the exact same thing as this
    public static Class forClassName(ClassLoader classLoader, String className) { 
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
