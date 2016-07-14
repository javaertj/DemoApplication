package com.ykbjson.demo.hook;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.ykbjson.demo.tools.SLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 包名：com.drivingassisstantHouse.library.tools.test
 * 描述：
 * 创建者：yankebin
 * 日期：2016/3/9
 */
public class BinderHookHandler implements InvocationHandler {
    private static final String TAG = "BinderHookHandler";

    private enum SERVICES {
        ACTIVITY,
        PACKAGE,
        CLIP
    }

    // 原始的Service对象 (IInterface)
    private Object mBase;
    private SERVICES type = SERVICES.CLIP;

    public BinderHookHandler(Object base, String stubName, String serviceName) {
        if (TextUtils.equals(serviceName, BinderHookHelper.ACTIVITY_SERVICE)) {
            type = SERVICES.ACTIVITY;
        } else if (TextUtils.equals(serviceName, BinderHookHelper.PACKAGE_SERVICE)) {
            type = SERVICES.PACKAGE;
        }
        try {
            Class<?> stubClass = Class.forName(stubName);
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            this.mBase = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            throw new RuntimeException("Hooked failed!",e);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        SLog.d(TAG + " methodName : " + method.getName());

        switch (type) {
            case CLIP:
                // 把剪切版的内容替换为 "you are hooked"
                if ("getPrimaryClip".equals(method.getName())) {
                    SLog.d(TAG + " hook getPrimaryClip");
                    return ClipData.newPlainText(null, "you are hooked");
                }
                // 欺骗系统,使之认为剪切版上一直有内容
                if ("hasPrimaryClip".equals(method.getName())) {
                    return true;
                }

                break;
            case ACTIVITY:
                SLog.d(TAG + " hook ActivityService");
                break;

            case PACKAGE:
                SLog.d(TAG + " hook PackageService");
                break;
            default:
                SLog.d(TAG + " hook null");
                break;
        }

        return method.invoke(mBase, args);
    }
}
