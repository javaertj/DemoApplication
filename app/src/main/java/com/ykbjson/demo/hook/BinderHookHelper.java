package com.ykbjson.demo.hook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 包名：com.drivingassisstantHouse.library.tools.test
 * 描述：
 * 创建者：yankebin
 * 日期：2016/3/9
 */
public class BinderHookHelper {
    public static final String ACTIVITY_SERVICE = "activity";
    public static final String PACKAGE_SERVICE = "package";
    public static final String CLIP_SERVICE = "clipboard";

    public static final String SERVICE_MANAGER_NAME = "android.os.ServiceManager";
    public static final String SERVICE_MANAGER_METHOD_NAME = "getService";

    private static Class<?> serviceManager;

    private static void initServiceManager() {
        if (null == serviceManager) {
            synchronized (BinderHookHelper.class) {
                if (null == serviceManager) {
                    try {
                        serviceManager = Class.forName(SERVICE_MANAGER_NAME);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void hookClipboardService() {
        initServiceManager();
        try {
            Method getService = serviceManager.getDeclaredMethod(SERVICE_MANAGER_METHOD_NAME, String.class);
            IBinder rawBinder = ((IBinder) getService.invoke(null, CLIP_SERVICE));
            // Hook 掉这个Binder代理对象的 queryLocalInterface 方法
            // 然后在 queryLocalInterface 返回一个IInterface对象, hook掉我们感兴趣的方法即可.
            IBinder hookedBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
                    new Class<?>[]{IBinder.class},
                    new BinderProxyHookHandler(rawBinder, "android.content.IClipboard$Stub", "android.content.IClipboard",CLIP_SERVICE));

            // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
            // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
            Field cacheField = serviceManager.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> cache = (Map) cacheField.get(null);
            cache.put(CLIP_SERVICE, hookedBinder);
        } catch (Exception e) {
            throw new RuntimeException("Hook Failed", e);
        }
    }

    public static void hookActivityManager() {
        initServiceManager();
        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            // 获取 gDefault 这个字段, 想办法替换它
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);

            Object gDefault = gDefaultField.get(null);

            // 4.x以上的gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            // ActivityManagerNative 的gDefault对象里面原始的 IActivityManager对象
            Object rawIActivityManager = mInstanceField.get(gDefault);

            // 创建一个这个对象的代理对象, 然后替换这个字段, 让我们的代理对象帮忙干活
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(serviceManager.getClassLoader(),
                    new Class<?>[]{iActivityManagerInterface}, new BinderProxyHookHandler(rawIActivityManager, "android.app.IActivityManager$Stub", "android.app.IActivityManager",ACTIVITY_SERVICE));
            mInstanceField.set(gDefault, proxy);

        } catch (Exception e) {
            throw new RuntimeException("Hook Failed", e);
        }

    }

    public static void hookPackageManager(Context context) {
        initServiceManager();
        try {
            // 获取全局的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的 sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    new BinderProxyHookHandler(sPackageManager,"android.content.pm.IPackageManager$Stub","android.content.pm.IPackageManager",PACKAGE_SERVICE));

            // 1. 替换掉ActivityThread里面的 sPackageManager 字段
            sPackageManagerField.set(currentActivityThread, proxy);

            // 2. 替换 ApplicationPackageManager里面的 mPm对象
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
        } catch (Exception e) {
            throw new RuntimeException("hook failed", e);
        }
    }
}
