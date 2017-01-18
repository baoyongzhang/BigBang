package com.baoyz.bigbang.core.xposed.process;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by dim on 17/1/18.
 */

public interface IProcess {

    boolean process(XC_LoadPackage.LoadPackageParam loadPackageParam);

}
