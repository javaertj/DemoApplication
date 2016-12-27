package com.ykbjson.demo.customview.ptrheader;

import in.srain.cube.views.ptr.PtrUIHandlerHook;

/**
 * 包名：com.ykbjson.demo.customview.ptrheader
 * 描述：下拉刷新关闭header的hook，可以再这里处理关闭header之前的事情
 * 创建者：yankebin
 * 日期：2016/12/7
 */

public class CustomerPtrUIHandlerHook extends PtrUIHandlerHook {

    public interface OnPtrUIHandlerHookCallback{
        void onPtrUIHandlerHookStart();
    }

    private OnPtrUIHandlerHookCallback handlerHookCallback;

    public CustomerPtrUIHandlerHook(OnPtrUIHandlerHookCallback handlerHookCallback) {
        this.handlerHookCallback = handlerHookCallback;
    }

    @Override
    public void run() {
        if (null != handlerHookCallback) {
            handlerHookCallback.onPtrUIHandlerHookStart();
        }
    }
}
