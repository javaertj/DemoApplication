package com.drivingassisstantHouse.library.tools;

import android.util.Log;

/**
 * 包名：com.drivingassisstantHouse.library.tools
 * 描述：日志工具
 * 创建者：yankebin
 * 日期：2015/12/9
 */
public class SLog {
    public static boolean DEVELOP_MODE= true;
    private static String tag = "[AppName]";
    private static int logLevel = Log.DEBUG;//Log.ERROR
    private static final String mClassName="简途旅行";

    static {
        if (!DEVELOP_MODE) {
            logLevel = Log.ERROR;
        }
    }

    private SLog(){

    }

    /**
     * 日志开关
     *
     * @return
     */
    private static boolean isLogFlag() {
        return DEVELOP_MODE;
    }

    /**
     * Get The Current Function Name
     *
     * @return
     */
    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(SLog.class.getName())) {
                continue;
            }
            tag = st.getFileName();
            return mClassName + "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * The Log Level:i
     *
     * @param str
     */
    public static void i(Object str) {
        if (isLogFlag()) {
            if (logLevel <= Log.INFO) {
                String name = getFunctionName();
                if (name != null) {
                    Log.i(tag + "", name + " : " + str);
                } else {
                    Log.i(tag, str.toString());
                }
            }
        }

    }

    /**
     * The Log Level:d
     *
     * @param str
     */
    public static void d(Object str) {
        if (isLogFlag()) {
            if (logLevel <= Log.DEBUG) {
                String name = getFunctionName();
                if (name != null) {
                    Log.d(tag, name + " : " + str);
                } else {
                    Log.d(tag, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:V
     *
     * @param str
     */
    public static void v(Object str) {
        if (isLogFlag()) {
            if (logLevel <= Log.VERBOSE) {
                String name = getFunctionName();
                if (name != null) {
                    Log.v(tag, name + " : " + str);
                } else {
                    Log.v(tag, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:w
     *
     * @param str
     */
    public static void w(Object str) {
        if (isLogFlag()) {
            if (logLevel <= Log.WARN) {
                String name = getFunctionName();
                if (name != null) {
                    Log.w(tag, name + " : " + str);
                } else {
                    Log.w(tag, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param str
     */
    public static void e(Object str) {
        if (isLogFlag()) {
            if (logLevel <= Log.ERROR) {
                String name = getFunctionName();
                if (name != null) {
                    Log.e(tag, name + " : " + str);
                } else {
                    Log.e(tag, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param ex
     */
    public static void e(Exception ex) {
        if (isLogFlag()) {
            if (logLevel <= Log.ERROR) {
                Log.e(tag, "error", ex);
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    public static void e(String log, Throwable tr) {
        if (isLogFlag()) {
            String line = getFunctionName();
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"
                    + "[" + mClassName + line + ":] " + log + "\n", tr);
        }
    }
}