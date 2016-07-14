package com.drivingassisstantHouse.library.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.drivingassisstantHouse.library.tools.SLog;

/**
 * android 系统中的四大组件之一Service基类<br>
 * Android Service的生命周期 http://www.cnbSLogs.com/mengdd/archive/2013/03/24/2979944.html
 *
 * @author sunji
 * @version 1.0
 */
public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        SLog.d("BaseService-->onCreate()");
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        SLog.d("BaseService-->onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.d("BaseService-->onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        SLog.d("BaseService-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        SLog.d("BaseService-->onBind()");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        SLog.d("BaseService-->onUnbind()");
        return super.onUnbind(intent);
    }
}
