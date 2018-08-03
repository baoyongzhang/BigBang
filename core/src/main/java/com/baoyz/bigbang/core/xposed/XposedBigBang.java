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

    /* The constructor is called every time an app is loaded (in background OR foreground or even at system start)
    so we should not do ANYTHING that slows down, no matter how small it is, it could get very expensive. */
    public XposedBigBang() {
        processor = new Processor();
        processor.addProcess(new FilterProcess());
        processor.addProcess(new WeChatProcess());
        processor.addProcess(new CommonAppProcess());
    }

    // Same thing here, try to do all checks in a first place, as fast as possible, so you don't execute too many methods on any app start
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedUtils.setXpoedEnable(loadPackageParam);
        processor.process(loadPackageParam);
        
        
        /* I recommend this
        switch (loadPackageParam.classLoader) {
            case "com.example.something_you_want_to_hook":
                // Note that I did not pass loadPackageParam as an argument, but only the classLoader, small optimization
                new ExampleHooks(loadPackageParam.classLoader); // or add an "init" method if you don't want to execute it with a constructor call:
                // new ExampleHooks(lpparam.classLoader).init();
                break;
            default:
                // Return on any other app, nothing should be hooked, no other calls are made.
                return;
        }
        Log.d("Successfully initialized Hooks for app \"" + loadPackageParam.packageName + "\");
        */
    }
}
