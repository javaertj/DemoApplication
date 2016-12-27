package com.drivingassisstantHouse.library.data;

import android.content.Context;
import android.os.Bundle;

import com.drivingassisstantHouse.library.base.BaseActivity;

import java.io.Serializable;

/**
 * 包名：com.drivingassisstantHouse.library.data
 * 描述：intent内容携带者
 * 创建者：yankebin
 * 日期：2016/8/9
 */

public class IntentBean<T extends BaseActivity> implements Serializable {
    private Class<T> distaionClass;
    private Context context;
    private String action;
    private int msgId;
    private Bundle extra;

    public Class<T> getDistaionClass() {
        return distaionClass;
    }

    public void setDistaionClass(Class<T> distaionClass) {
        this.distaionClass = distaionClass;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Bundle getExtra() {
        return extra;
    }

    public void setExtra(Bundle extra) {
        this.extra = extra;
    }
}
