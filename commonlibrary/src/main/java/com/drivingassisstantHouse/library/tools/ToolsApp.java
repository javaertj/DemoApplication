package com.drivingassisstantHouse.library.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by simpletour on 2015/11/9.
 * application中常用的方法，包括获取app版本号，获取服务是否在运行；获取某个应用的信息；程序在前台还是在后台;app是否包含某个权限
 */
public class ToolsApp {

    private static PackageManager getPackManager(Context context) {
        return context.getPackageManager();
    }

    private static ActivityManager getActManager(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager;
    }

    /*****************************************
     * 获取app信息********************************
     * <p/>
     * /**
     * 获取当前程序版本字符串
     *
     * @param context 上下文呢
     * @return 成功则返回相应的版本字符串, 其他则返回空串
     */
    public static String getAppVersionStr(Context context) {
        String version = "";
        try {
            PackageInfo info = getPackManager(context).getPackageInfo(
                    context.getPackageName(), 0);
            if (info != null) {
                version = info.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 获取当前正在运行的进程列表
     *
     * @param context 上下文
     * @return 当前正在运行的进程列表
     */
    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(Context context) {
        return getActManager(context).getRunningAppProcesses();
    }

    /**
     * 获取当前正在运行的最顶层的应用信息
     *
     * @param context 上下文
     * @return 成功则返回当前最顶层的应用信息，其他则返回null
     */
    public static ApplicationInfo getTopApplicationInfo(Context context) {

        String packageName = getActManager(context).getRunningTasks(1).get(0).topActivity
                .getPackageName();
        try {
            return getPackManager(context).getApplicationInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前正在运行的最顶层的应用的名字
     *
     * @param context 上下文
     * @return 成功则返回应用名称，其他则返回null
     */
    public static String getTopApplicationInfoName(Context context) {
        ApplicationInfo applicationInfo = getTopApplicationInfo(context);
        if (applicationInfo != null) {
            return applicationInfo.loadLabel(getPackManager(context)).toString();
        }
        return null;
    }

    /**
     * 获取当前正在运行的最顶层的应用的pid
     *
     * @return 成功则返回对应的pid，其他则返回-1
     */
    public static int getTopApplicationPid(Context context) {
        String packageName = getActManager(context).getRunningTasks(1).get(0).topActivity
                .getPackageName();
        List<ActivityManager.RunningAppProcessInfo> infos = getRunningAppProcesses(context);
        if (infos != null && !infos.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo info : infos) {
                for (String name : info.pkgList) {
                    if (TextUtils.equals(packageName, name)) {
                        return info.pid;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 判断当前正在运行的activity
      * @param activity
     * @return
     */
    public static boolean isTopActivity(Context context,Activity activity){
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo>  tasksInfo = activityManager.getRunningTasks(1);
        if(tasksInfo.size() > 0){
            System.out.println("---------------包名-----------"+tasksInfo.get(0).topActivity.getPackageName());
            //应用程序位于堆栈的顶层
            if(packageName.equals(tasksInfo.get(0).topActivity.getPackageName())){
                return true;
            }
        }
        return false;
    }

    /********************************************
     * 终止进程************************************
     * <p/>
     * /**
     * 清理指定包名关联的所有后台进程 （杀死后一般会后台重启），若未来某个时间需要该进程，它们将被重启
     * 需要声明权限 android.Manifest.permission.KILL_BACKGROUND_PROCESSES
     *
     * @param context 上下文
     * @param pkgName 包名称
     */
    public static void killPackageBackProcess(Context context, String pkgName) {
        getActManager(context).killBackgroundProcesses(pkgName);
    }

    /**
     * 清理非系统应用的后台进程
     * 需要声明权限 android.Manifest.permission.KILL_BACKGROUND_PROCESSES
     *
     * @return 如清理成功则返回相应的清理个数，其他则返回0
     */
    public static int killAllBackRunningApps(Context context) {
        // PackageManager manager = getPackageManager(context);
        int count = 0;
        List<ActivityManager.RunningAppProcessInfo> list = getRunningAppProcesses(context);
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.uid < 10000)
                continue;
            for (String pkgNmae : info.pkgList) {
                count++;
                killPackageBackProcess(context, pkgNmae);
            }
        }
        return count;
    }

    /**
     * 通过反射强行停止指定包名的App (需要系统签名)
     *
     * @param packageName app所在包的名称
     * @return 失败返回-1，成功返回0.
     */
    public static int stopPackageProcess(Context context, String packageName) {
        int flag = -1;
        try {
            Method forceStopPackage = getActManager(context).getClass().getMethod(
                    "forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(getActManager(context), packageName);
            flag = 0;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /*******************************************
     * app权限相关***********************************
     * <p/>
     * /**
     * 获取APP申请的权限
     *
     * @param pkgName app所在包的名称
     * @param context 上下文
     * @return 成功则返回app的权限列表，其他则返回null
     */
    public static List<String> getPermissionsOfApp(String pkgName, Context context) {
        PackageInfo info = null;
        List<String> permissions = null;
        try {
            info = getPackManager(context).getPackageInfo(pkgName,
                    PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            String[] pInfo = null;
            pInfo = info.requestedPermissions;
            if (pInfo != null && pInfo.length > 0) {
                permissions = new ArrayList<String>(100);
                for (String permissionInfo : pInfo) {
                    permissions.add(permissionInfo);
                }
            }
        }
        return permissions;
    }


    /**
     * 根据包名判断app是否有某个权限
     *
     * @param context 上下文
     * @param pkgName 包名
     * @param perName 权限名称
     * @return 如果确定包含某个权限，则返回true;其他则返回false
     */
    public static boolean isAppHasSafePermission(Context context, String pkgName, String perName) {

        PackageInfo info = null;
        try {
            info = getPackManager(context).getPackageInfo(pkgName,
                    PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        if (info != null) {
            String[] infos = info.requestedPermissions;

            if (infos != null && infos.length > 0) {
                for (String permissionInfo : infos) {
                    if (permissionInfo.equals(perName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 根据uid判断APP是否含有某个权限
     *
     * @param context    上下文
     * @param callingUid app的uid
     * @param perName    要过滤的权限
     * @return 有，返回true;没有，返回false
     */
    public static boolean isAppHasSafePermission(Context context, int callingUid, String perName) {
        List<PackageInfo> packages = getPackManager(context).getInstalledPackages(PackageManager.PERMISSION_GRANTED);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.uid == callingUid)
                    && ("android".equals(packageInfo.packageName) || isAppHasSafePermission(context,
                    packageInfo.packageName, perName))) {

                return true;
            }
        }
        return false;
    }

    /****************************************app签名**********************************************/
    /**
     * 从APK中读取签名
     *
     * @param file apk文件地址(全路径)
     * @return 操作成功，返回签名字符串列表,其他则返回空列表
     * @throws IOException
     */
    public static List<String> getSignaturesFromApk(File file)
            throws IOException {
        List<String> signatures = new ArrayList<String>();
        JarFile jarFile = new JarFile(file);
        try {
            JarEntry je = jarFile.getJarEntry("AndroidManifest.xml");
            byte[] readBuffer = new byte[8192];
            Certificate[] certs = loadCertificates(jarFile, je, readBuffer);
            if (certs != null) {
                for (Certificate c : certs) {
                    String sig = toCharsString(c.getEncoded());
                    signatures.add(sig);
                }
            }
        } catch (Exception ex) {
        }
        return signatures;
    }

    /**
     * 加载签名
     *
     * @param jarFile
     * @param je
     * @param readBuffer
     * @return
     */
    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je,
                                                  byte[] readBuffer) {
        try {
            InputStream is = jarFile.getInputStream(je);
            while (is.read(readBuffer, 0, readBuffer.length) != -1) {
            }
            is.close();
            return (je != null ? je.getCertificates() : null);
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 将签名转成转成可见字符串
     *
     * @param sigBytes
     * @return
     */
    private static String toCharsString(byte[] sigBytes) {
        byte[] sig = sigBytes;
        final int N = sig.length;
        final int N2 = N * 2;
        char[] text = new char[N2];
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            text[j * 2 + 1] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return new String(text);
    }

    /****************************************app对intent处理**********************************************/

    /**
     * 查询手机是否装有含某个ACTION的App
     *
     * @param action 对应Intent里的action
     * @return 如果有匹配的app, 返回true;其他，返回false
     */
    public boolean hasFitIntentApp(Context context, String action) {
        if (TextUtils.isEmpty(action))
            return false;
        Intent intent = new Intent();
        intent.setAction(action);

        List<ResolveInfo> infos = getPackManager(context).queryIntentActivities(intent,
                0);
        if (infos != null && !infos.isEmpty())
            return true;

        return false;
    }

    /**
     * 查询手机是否装有含某个ACTION和CATEGORY的App
     *
     * @param action   对应Intent里的action
     * @param category 对应Intent里的category
     * @return 如果有匹配的app, 返回true;其他，返回false
     */
    public boolean hasFitIntentApp(Context context, String action, String category) {
        if (TextUtils.isEmpty(action) || TextUtils.isEmpty(category))
            return false;
        Intent intent = new Intent();
        intent.setAction(action);
        intent.addCategory(category);
        List<ResolveInfo> infos = getPackManager(context).queryIntentActivities(intent,
                0);
        if (infos != null && !infos.isEmpty())
            return true;

        return false;
    }

    /********************
     * 判断service或app是否运行*************************
     * <p/>
     * /**
     *
     * @param mContext  上下文呢
     * @param className 服务类名臣字符串(通过getClassName()获取类名)
     * @return 如果指定名称的服务正在运行则返回true，其他则返回false
     ***/

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;

        List<ActivityManager.RunningServiceInfo> serviceList = getActManager(mContext)
                .getRunningServices(100);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    /**
     * 判断指定的程序是否是在后台运行着的
     *
     * @param context     上下文
     * @param packageName 程序包名
     * @return 如果指定包名的app在后台运行，则返回true，其他则返回false
     */
    public static boolean isAppBackground(Context context, String packageName) {
        //获取当前设备中运行的app的进程列表
        List<ActivityManager.RunningAppProcessInfo> appProcesses = getActManager(context)
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {

                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
