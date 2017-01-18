package com.baoyz.bigbang.core.xposed;


import com.baoyz.bigbang.core.xposed.common.XposedUtils;
import com.baoyz.bigbang.core.xposed.process.CommonAppProcess;
import com.baoyz.bigbang.core.xposed.process.FilterProcess;
import com.baoyz.bigbang.core.xposed.process.Processor;
import com.baoyz.bigbang.core.xposed.process.WeChatProcess;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Created by dim on 16/10/23.
 */

public class XposedBigBang implements IXposedHookLoadPackage {

    private Processor processor;

    public XposedBigBang() {
        processor = new Processor();
        processor.addProcess(new FilterProcess());
        processor.addProcess(new WeChatProcess());
        processor.addProcess(new CommonAppProcess());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedUtils.setXpoedEnable(loadPackageParam);
        processor.process(loadPackageParam);
    }
}