package com.drivingassisstantHouse.library.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.drivingassisstantHouse.library.MApplication;
import com.drivingassisstantHouse.library.R;
import com.drivingassisstantHouse.library.data.OnNetWorkEvent;
import com.drivingassisstantHouse.library.tools.SLog;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMSocialService;
import com.umeng.message.PushAgent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;


/**
 * android 系统中的四大组件之一Activity基类
 *
 * @author sunji
 * @version 1.0
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity {
    /***
     * 整个应用Applicaiton
     **/
    private MApplication mApplication = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    /**
     * 当前Activity渲染的视图View
     **/
    protected View mContextView = null;
    /**
     * 动画类型
     **/
    private int mAnimationType = NONE;
    /**
     * 是否运行截屏
     **/
    private boolean isCanScreenshot = true;
    /**
     * 共通操作
     **/
    protected Operation mOperation = null;
    /**
     * 友盟分享sdk
     */
    protected UMSocialService mController;

    protected BaseHandler baseHandler = new BaseHandler(this);

    protected Toolbar toolbar;

    protected static class BaseHandler extends Handler {
        private WeakReference<IBaseActivity> baseActivity;

        public BaseHandler(IBaseActivity baseActivity) {
            this.baseActivity = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            baseActivity.get().handleMessage(msg);
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.d("BaseActivity-->onCreate()");
        // 获取应用Application
        mApplication = (MApplication) getApplicationContext();
        // 将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);

        // 实例化共通操作
        mOperation = new Operation(this);

        // 初始化参数
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAnimationType = bundle.getInt(ANIMATION_TYPE, NONE);
        } else {
            bundle = new Bundle();
        }
        initShareSDK();
        mContextView = getLayoutInflater().inflate(bindLayout(), null, false);
        // 设置渲染视图View
        setContentView(mContextView);
        ButterKnife.bind(this);//使用bufferKnife
        initParms(bundle);
        // 初始化控件

        initView(mContextView);

        // 业务操作
        doBusiness(this);

        // 显示VoerFlowMenu
        displayOverflowMenu(getContext());

        // 是否可以截屏
        if (!isCanScreenshot) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SLog.d("BaseActivity-->onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        SLog.d("BaseActivity-->onStart()");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mContextView) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(mContextView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(getClass().getName());
        SLog.d("BaseActivity-->onResume()");
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(getClass().getName());
        SLog.d("BaseActivity-->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SLog.d("BaseActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {
        destroy();
        ButterKnife.unbind(this);
        super.onDestroy();
        mApplication.removeTask(context);
        SLog.d("BaseActivity-->onDestroy()");
    }

    /**
     * 显示Actionbar菜单图标
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);// 显示
                } catch (Exception e) {
                    SLog.e("onMenuOpened-->" + e.getMessage());
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 显示OverFlowMenu按钮
     *
     * @param mContext 上下文Context
     */
    public void displayOverflowMenu(Context mContext) {
        try {
            ViewConfiguration config = ViewConfiguration.get(mContext);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);// 显示
            }
        } catch (Exception e) {
            SLog.e(e.getMessage());
        }
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    protected Activity getContext() {
        if (null != context)
            return context.get();
        else
            return null;
    }

    /**
     * 获取共通操作机能
     */
    public Operation getOperation() {
        return this.mOperation;
    }

    /**
     * 设置是否可截屏
     *
     * @param isCanScreenshot
     */
    public void setCanScreenshot(boolean isCanScreenshot) {
        this.isCanScreenshot = isCanScreenshot;
    }

    /**
     * Actionbar点击返回键关闭事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mOperation.finishActivity(this, Operation.ENTER_TYPE_LEFT, RESULT_CANCELED, null);
    }

    public void finish() {
        super.finish();
        switch (mAnimationType) {
            case IBaseActivity.LEFT_RIGHT:
//                overridePendingTransition(0, BaseView.gainResId(mApplication, BaseView.ANIM, "base_slide_right_out"));
                overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
                break;
            case IBaseActivity.TOP_BOTTOM:
                overridePendingTransition(0, BaseView.gainResId(mApplication, BaseView.ANIM, "base_push_up_out"));
                break;
            case IBaseActivity.FADE_IN_OUT:
                overridePendingTransition(0, BaseView.gainResId(mApplication, BaseView.ANIM, "base_fade_out"));
                break;
            default:
                break;
        }
        mAnimationType = NONE;
    }



    @BusReceiver
    public void onMainNetWorkEvent(OnNetWorkEvent event) {
        onNetWorkChanged(event.connected);
    }

    @Override
    public void onNetWorkChanged(boolean connected) {

    }


    /**
     * 初始化友盟分享sdk
     */
//    @Override
    protected void initShareSDK() {
//        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//        mController.getConfig().setPlatforms(SHARE_MEDIA.TENCENT, SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.SMS, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
//        // 添加短信
//        SmsHandler smsHandler = new SmsHandler();
//        smsHandler.addToSocialSDK();
//        // 添加email
//        EmailHandler emailHandler = new EmailHandler();
//        emailHandler.addToSocialSDK();
//        // 添加有道云笔记平台
//        UMYNoteHandler yNoteHandler = new UMYNoteHandler(this);
//        yNoteHandler.addToSocialSDK();
//        // 添加易信平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
//        UMYXHandler yixinHandler = new UMYXHandler(this,
//                "yxc0614e80c9304c11b0391514d09f13bf");
//        // 关闭分享时的等待Dialog
//        yixinHandler.enableLoadingDialog(false);
//        // 把易信添加到SDK中
//        yixinHandler.addToSocialSDK();
//
//        // 易信朋友圈平台,参数1为当前activity, 参数2为在易信开放平台申请到的app id
//        UMYXHandler yxCircleHandler = new UMYXHandler(this,
//                "yxc0614e80c9304c11b0391514d09f13bf");
//        yxCircleHandler.setToCircle(true);
//        yxCircleHandler.addToSocialSDK();
//        //添加qq空间分享相关
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, Constant.VALUE.SHARE_QQ_APPID, Constant.VALUE.SHARE_QQ_APPKEY);
//        qZoneSsoHandler.addToSocialSDK();
//        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, Constant.VALUE.SHARE_QQ_APPID, Constant.VALUE.SHARE_QQ_APPKEY);
//        qqSsoHandler.addToSocialSDK();
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(this, Constant.VALUE.SHARE_WX_APPID, Constant.VALUE.SHARE_WX_APPKEY);
//        wxHandler.addToSocialSDK();
//        // 添加微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(this, Constant.VALUE.SHARE_WX_APPID, Constant.VALUE.SHARE_WX_APPKEY);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
    }
}
