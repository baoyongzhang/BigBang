package com.baoyz.bigbang.core.xposed.setting;

import android.content.pm.ApplicationInfo;

/**
 * Created by dim on 16/11/4.
 */

public class AppInfo {

    public boolean enable;
    public ApplicationInfo applicationInfo;
    public String packageName;
    public String appName;

    public AppInfo(ApplicationInfo applicationInfo, String appName, String packageName, boolean enable) {
        this.applicationInfo = applicationInfo;
        this.packageName = packageName;
        this.appName = appName;
        this.enable = enable;
    }
}
