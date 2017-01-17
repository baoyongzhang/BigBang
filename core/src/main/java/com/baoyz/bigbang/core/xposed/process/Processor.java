package com.baoyz.bigbang.core.xposed.process;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by dim on 17/1/18.
 */

public class Processor {

    private List<IProcess> mIProcesses = new ArrayList<>();

    public void addProcess(IProcess iProcess) {
        mIProcesses.add(iProcess);
    }

    public void process(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        for (IProcess iProcess : mIProcesses) {
            if (iProcess.process(loadPackageParam)) break;
        }
    }
}
