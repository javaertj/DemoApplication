package com.drivingassisstantHouse.library.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.drivingassisstantHouse.library.MApplication;
import com.drivingassisstantHouse.library.R;
import com.drivingassisstantHouse.library.config.SysEnv;
import com.drivingassisstantHouse.library.data.DTO;
import com.drivingassisstantHouse.library.tools.ToolAlert;

import java.io.Serializable;

/**
 * 基本的操作共通抽取
 *
 * @author sunji
 * @version 1.0
 */
public class Operation {
    public static final int ENTER_TYPE_TOP = 1;
    public static final int ENTER_TYPE_BOTTOM = 2;
    public static final int ENTER_TYPE_LEFT = 3;
    public static final int ENTER_TYPE_RIGHT = 4;
    public static final int ACTIVITY_FINISH = 100;

    private static final String HANDLER_THREAD_NAME = "Operation_Handler";

    private class OperationHandler extends Handler {
        private OperationHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ACTIVITY_FINISH) {
                if (msg.obj != null) {
                    Activity activity = (Activity) msg.obj;
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }
            super.handleMessage(msg);
        }
    }

    private OperationHandler operationHandler;

    /**
     * 激活Activity组件意图
     **/
    private Intent mIntent = new Intent();
    /***
     * 上下文
     **/
    private Activity mContext = null;
    /***
     * 整个应用Applicaiton
     **/
    private MApplication mApplication = null;

    public Operation(Activity mContext) {
        this.mContext = mContext;
        mApplication = (MApplication) this.mContext.getApplicationContext();
        HandlerThread thread = new HandlerThread(HANDLER_THREAD_NAME);
        thread.start();
        operationHandler = new OperationHandler(thread.getLooper());
    }

    /**
     * 跳转Activity
     *
     * @param activity 需要跳转至的Activity
     */
    public void forward(Class activity) {
        forward(activity.getName());
    }

    /**
     * 跳转Activity
     *
     * @param className 需要跳转至的Activity
     */
    public void forward(String className) {
        forward(className, IBaseActivity.NONE);
    }

    /**
     * 跳转Activity
     *
     * @param className 需要跳转至的Activity
     * @param animaType 动画类型IBaseActivity.LEFT_RIGHT/TOP_BOTTOM/FADE_IN_OUT
     */
    public void forward(String className, int animaType) {
        mIntent.setClassName(mContext, className);
        mIntent.putExtra(IBaseActivity.ANIMATION_TYPE, animaType);
        forward(mIntent, animaType);
    }

    public void forward(Intent intent, int animType) {
        mIntent = intent;
        mIntent.putExtra(IBaseActivity.ANIMATION_TYPE, animType);
        mContext.startActivity(mIntent);
        switch (animType) {
            case IBaseActivity.LEFT_RIGHT:
                int inLFAnim = BaseView.gainResId(mApplication, BaseView.ANIM, "base_slide_right_in");
                int outLFAnim = BaseView.gainResId(mApplication, BaseView.ANIM, "base_slide_remain");
                mContext.overridePendingTransition(inLFAnim, outLFAnim);
                break;
            case IBaseActivity.TOP_BOTTOM:
                int inTBAnim = BaseView.gainResId(mApplication, BaseView.ANIM, "base_push_up_in");
                int outTBAnim = BaseView.gainResId(mApplication, BaseView.ANIM, "base_push_up_out");
                mContext.overridePendingTransition(inTBAnim, outTBAnim);
                break;
            case IBaseActivity.FADE_IN_OUT:
                int inFAnim = BaseView.gainResId(mApplication, BaseView.ANIM, "base_fade_in");
                int outFAnim = BaseView.gainResId(mApplication, BaseView.ANIM, "base_fade_out");
                mContext.overridePendingTransition(inFAnim, outFAnim);
                break;
            default:
                break;
        }
    }

    /**
     * @param activity
     * @param cls
     * @param enterType
     * @param isFinish
     */
    public void startActivity(Activity activity, Class<?> cls, int enterType, Boolean isFinish, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
        switch (enterType) {
            case ENTER_TYPE_TOP:
                activity.overridePendingTransition(R.anim.enter_toptobuttom, R.anim.exit_toptobuttom);
                break;
            case ENTER_TYPE_BOTTOM:
                activity.overridePendingTransition(R.anim.enter_buttomtotop, R.anim.exit_buttomtotop);
                break;
            case ENTER_TYPE_LEFT:
                activity.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                break;
            case ENTER_TYPE_RIGHT:
                activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            default:
                break;
        }

        if (isFinish) {
            Message msg = new Message();
            msg.what = ACTIVITY_FINISH;
            msg.obj = activity;
            operationHandler.sendMessage(msg);
        }
    }


    public void startActivity(Activity activity, int enterType, Boolean isFinish, Intent intent) {
        activity.startActivity(intent);
        switch (enterType) {
            case ENTER_TYPE_TOP:
                activity.overridePendingTransition(R.anim.enter_toptobuttom, R.anim.exit_toptobuttom);
                break;
            case ENTER_TYPE_BOTTOM:
                activity.overridePendingTransition(R.anim.enter_buttomtotop, R.anim.exit_buttomtotop);
                break;
            case ENTER_TYPE_LEFT:
                activity.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                break;
            case ENTER_TYPE_RIGHT:
                activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            default:
                break;
        }

        if (isFinish) {
            Message msg = new Message();
            msg.what = ACTIVITY_FINISH;
            msg.obj = activity;
            operationHandler.sendMessage(msg);
        }
    }

    public void startActivity(Activity activity, Class<?> cls, int enterType, boolean isFinish, Bundle bundle, boolean FLAG_ACTIVITY_FORWARD_RESULT) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (FLAG_ACTIVITY_FORWARD_RESULT) {
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        }

        activity.startActivity(intent);
        switch (enterType) {
            case ENTER_TYPE_TOP:
                activity.overridePendingTransition(R.anim.enter_toptobuttom, R.anim.exit_toptobuttom);
                break;
            case ENTER_TYPE_BOTTOM:
                activity.overridePendingTransition(R.anim.enter_buttomtotop, R.anim.exit_buttomtotop);
                break;
            case ENTER_TYPE_LEFT:
                activity.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                break;
            case ENTER_TYPE_RIGHT:
                activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            default:
                break;
        }


        if (isFinish) {
            Message msg = new Message();
            msg.what = ACTIVITY_FINISH;
            msg.obj = activity;
            operationHandler.sendMessage(msg);
        }
    }

    /**
     * @param activity
     * @param cls
     * @param enterType
     */
    public void startActivityForResult(Activity activity, Class<?> cls, int enterType, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        activity.startActivityForResult(intent, requestCode);
        switch (enterType) {
            case ENTER_TYPE_TOP:
                activity.overridePendingTransition(R.anim.enter_toptobuttom, R.anim.exit_toptobuttom);
                break;
            case ENTER_TYPE_BOTTOM:
                activity.overridePendingTransition(R.anim.enter_buttomtotop, R.anim.exit_buttomtotop);
                break;
            case ENTER_TYPE_LEFT:
                activity.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                break;
            case ENTER_TYPE_RIGHT:
                activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            default:
                break;
        }
    }

    public void finishActivity(Activity activity, int enterType, int resultCode, Intent intent) {

        if (intent != null) {
            activity.setResult(resultCode, intent);
        }
        activity.finish();
        switch (enterType) {
            case ENTER_TYPE_TOP:
                activity.overridePendingTransition(R.anim.enter_toptobuttom, R.anim.exit_toptobuttom);
                break;
            case ENTER_TYPE_BOTTOM:
                activity.overridePendingTransition(R.anim.enter_buttomtotop, R.anim.exit_buttomtotop);
                break;
            case ENTER_TYPE_LEFT:
                activity.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                break;
            case ENTER_TYPE_RIGHT:
                activity.overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
                break;
            default:
                break;
        }
    }

    /**
     * 设置传递参数
     *
     * @param value 数据传输对象
     */

    public void addParameter(DTO value) {
        mIntent.putExtra(SysEnv.ACTIVITY_DTO_KEY, value);
    }

    /**
     * 设置传递参数
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameter(String key, DTO value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 设置传递参数
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameter(String key, Bundle value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 设置传递参数
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameter(String key, Serializable value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 设置传递参数
     *
     * @param key   参数key
     * @param value 数据传输对象
     */
    public void addParameter(String key, String value) {
        mIntent.putExtra(key, value);
    }

    /**
     * 获取跳转时设置的参数
     *
     * @param key
     * @return
     */
    public Object getParameters(String key) {
        DTO parms = getParameters();
        if (null != parms) {
            return parms.get(key);
        } else {
            parms = new DTO();
            parms.put(key, mContext.getIntent().getExtras().get(key));
        }
        return parms;
    }

    /**
     * 获取跳转参数集合
     *
     * @return
     */
    public DTO getParameters() {
        DTO parms = (DTO) mContext.getIntent().getExtras().getSerializable(SysEnv.ACTIVITY_DTO_KEY);
        return parms;
    }

    /**
     * 设置全局Application传递参数
     *
     * @param strKey 参数key
     * @param value  数据传输对象
     */
    public void addGloableAttribute(String strKey, Object value) {
        mApplication.assignData(strKey, value);
    }

    /**
     * 获取跳转时设置的参数
     *
     * @param strKey
     * @return
     */
    public Object getGloableAttribute(String strKey) {
        return mApplication.gainData(strKey);
    }

    /**
     * 弹出等待对话框
     *
     * @param message 提示信息
     */
    public void showLoading(String message) {
        ToolAlert.loading(mContext, message);
    }

    /**
     * 弹出等待对话框
     *
     * @param message  提示信息
     * @param listener 按键监听器
     */
    public void showLoading(String message, ToolAlert.ILoadingOnKeyListener listener) {
        ToolAlert.loading(mContext, message, listener);
    }

    /**
     * 更新等待对话框显示文本
     *
     * @param message 需要更新的文本内容
     */
    public void updateLoadingText(String message) {
        ToolAlert.updateProgressText(message);
    }

    /**
     * 关闭等待对话框
     */
    public void closeLoading() {
        ToolAlert.closeLoading();
    }

    public void destroy() {
        if (null != operationHandler && null != operationHandler.getLooper()) {
            operationHandler.getLooper().quit();
        }
    }
}

