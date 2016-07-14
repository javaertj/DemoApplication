package com.drivingassisstantHouse.library.widget.floatwindow;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * 包名：com.drivingassisstantHouse.library.widget.floatwindow
 * 描述：悬浮window服务。 显示推送通知、广告
 * 创建者：yankebin
 * 日期：2016/2/29
 */
public class FloatWindowService extends Service {
    private static final int FLAG_ADD_VIEW = 1;

    private WindowManager windowManager;
    private FloatHandler uiHandler;
    private LocalBinder localBinder = new LocalBinder();

    //后期扩展
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    public class LocalBinder extends Binder {
        public FloatWindowService getService() {
            return FloatWindowService.this;
        }
    }

    private static class FloatHandler extends Handler {
        private WeakReference<FloatWindowService> service;

        public FloatHandler(FloatWindowService service) {
            this.service = new WeakReference<>(service);
        }

        private WindowManager.LayoutParams generalParams() {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
//            params.flags |=  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.format = PixelFormat.TRANSPARENT;

            return params;
        }

        @Override
        public void handleMessage(Message msg) {
            WindowManager manager = service.get().windowManager;
            switch (msg.what) {
                case FLAG_ADD_VIEW:
                    FloatView advertView = new FloatView(service.get());
                    advertView.show((String) msg.obj, manager, generalParams());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        uiHandler = new FloatHandler(this);
        registerReceiver(mReceiver, new IntentFilter());
        if (null == windowManager) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void showView(String  url) {
        uiHandler.obtainMessage(FLAG_ADD_VIEW, url).sendToTarget();
    }
}
