package com.baoyz.bigbang.core.xposed.process;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by dim on 17/1/18.
 */

public abstract class AppProcess implements IProcess {

    abstract String getAppPackageName();

    @Override
    public boolean process(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals(getAppPackageName())) {
            return onProcess(loadPackageParam);
        }
        return false;
    }

    protected abstract boolean onProcess(XC_LoadPackage.LoadPackageParam loadPackageParam);

}
