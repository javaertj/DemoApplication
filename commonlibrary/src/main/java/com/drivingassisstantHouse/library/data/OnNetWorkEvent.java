package com.drivingassisstantHouse.library.data;

import java.io.Serializable;

/**
 * 包名：com.simpletour.client.bean
 * 描述：网络变化的事件
 * 创建者：yankebin
 * 日期：2016/4/19
 */
public class OnNetWorkEvent implements Serializable {
    public boolean connected;

    public OnNetWorkEvent(boolean connected) {
        this.connected = connected;
    }
}
