package com.nostra13.universalimageloader.sample.aspect;


import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by 50567 on 17/5/17.
 */

@Aspect
public class AspectTest {

    private static final String TAG = "xuyisheng";
    long Tstart=0,Tend=0;
    @Before("execution(* com.nostra13.universalimageloader.sample.fragment.ImagePagerFragment.ImageAdapter.instantiateItem(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        IMonitorService service = new MonitorServiceImpl();
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
        String key = joinPoint.getSignature().toString();
        Log.e(TAG, "MAER_AOP====BEFORE====AOP--MAER ");
        Log.e(TAG, "MAER_AOP----onActivityMethodBefore: " + key);
        Log.e(TAG, "MAER_AOP----cpu占有率=" + monitorInfo.getCpuRatio());
        Log.e(TAG, "MAER_AOP----可使用内存=" + monitorInfo.getTotalMemory());
        Log.e(TAG, "MAER_AOP----剩余内存=" + monitorInfo.getFreeMemory());
        Log.e(TAG, "MAER_AOP----最大可使用内存=" + monitorInfo.getMaxMemory());
        Log.e(TAG, "MAER_AOP----操作系统=" + monitorInfo.getOsName());
        Log.e(TAG, "MAER_AOP----总的物理内存=" + monitorInfo.getTotalMemorySize() + "kb");
        Log.e(TAG, "MAER_AOP----剩余的物理内存=" + monitorInfo.getFreeMemory() + "kb");
        Log.e(TAG, "MAER_AOP----已使用的物理内存=" + monitorInfo.getUsedMemory() + "kb");
        Log.e(TAG, "MAER_AOP----线程总数=" + monitorInfo.getTotalThread() + "kb");
        Log.e(TAG, "\n\n");


        Tstart = System.currentTimeMillis();
        //sleep(1000);
    }
    @After("execution(* com.nostra13.universalimageloader.sample.fragment.ImagePagerFragment.ImageAdapter.instantiateItem(..))")
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        IMonitorService service = new MonitorServiceImpl();
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
        Log.e(TAG, "MAER_AOP====AFTER====AOP--MAER ");
        Log.e(TAG, "MAER_AOP----cpu占有率=" + monitorInfo.getCpuRatio());
        Log.e(TAG, "MAER_AOP----可使用内存=" + monitorInfo.getTotalMemory());
        Log.e(TAG, "MAER_AOP----剩余内存=" + monitorInfo.getFreeMemory());
        Log.e(TAG, "MAER_AOP----最大可使用内存=" + monitorInfo.getMaxMemory());
        Log.e(TAG, "MAER_AOP----操作系统=" + monitorInfo.getOsName());
        Log.e(TAG, "MAER_AOP----总的物理内存=" + monitorInfo.getTotalMemorySize() + "kb");
        Log.e(TAG, "MAER_AOP----剩余的物理内存=" + monitorInfo.getFreeMemory() + "kb");
        Log.e(TAG, "MAER_AOP----已使用的物理内存=" + monitorInfo.getUsedMemory() + "kb");
        Log.e(TAG, "MAER_AOP----线程总数=" + monitorInfo.getTotalThread() + "kb");
        Log.e(TAG, "\n\n");

        Tend = System.currentTimeMillis();
        Log.e(TAG, "MAER----Time----运行时间为: " + (Tend-Tstart)+" 毫秒;");
    }
}