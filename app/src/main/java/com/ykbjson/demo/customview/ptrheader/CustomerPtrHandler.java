package com.ykbjson.demo.customview.ptrheader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ykbjson.demo.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 包名：com.ykbjson.demo.customview.ptrheader
 * 描述：自定义下拉刷新头部
 * 创建者：yankebin
 * 日期：2016/4/25
 */
public class CustomerPtrHandler extends FrameLayout implements PtrUIHandler,
        CustomerPtrUIHandlerHook.OnPtrUIHandlerHookCallback, PullLoadingView.OnRefreshCompleteCallback {
    // 下拉图标
    private PullLoadingView pullLoadingView;
    private CustomerPtrUIHandlerHook handlerHook;

    public CustomerPtrHandler(Context context) {
        this(context, null);
    }

    public CustomerPtrHandler(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerPtrHandler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        inflate(context, R.layout.item_refresh_header, this);
        pullLoadingView = (PullLoadingView) findViewById(R.id.iv_rotate);
        pullLoadingView.setCompleteCallback(this);
    }

    public void setLoadingColor(int loadingColor) {
        if (checkNotNull(pullLoadingView)) {
            pullLoadingView.setLoadingColor(loadingColor);
        }
    }

    private boolean checkNotNull(Object o) {
        return null != o;
    }

    @Override
    public void onUIReset(PtrFrameLayout ptrFrameLayout) {
        if (checkNotNull(pullLoadingView)) {
            pullLoadingView.setMode(PullLoadingView.MODE_INIT);
        }
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout ptrFrameLayout) {
        if (!checkNotNull(handlerHook)) {
            handlerHook = new CustomerPtrUIHandlerHook(this);
            ptrFrameLayout.setRefreshCompleteHook(handlerHook);
            if (checkNotNull(pullLoadingView)) {
                pullLoadingView.setMaxPullY(ptrFrameLayout.getOffsetToRefresh());
            }
            //修改loading最少驻留时间，不要一闪而过
            ptrFrameLayout.setLoadingMinTime(2000);
            //header关闭时间
            ptrFrameLayout.setDurationToCloseHeader(1500);
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        if (checkNotNull(pullLoadingView)) {
            pullLoadingView.setMode(PullLoadingView.MODE_LOADING);
        }
    }

    @Override
    public void onUIRefreshComplete(final PtrFrameLayout ptrFrameLayout) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        if (!isUnderTouch || status == PtrFrameLayout.PTR_STATUS_LOADING || status == PtrFrameLayout.PTR_STATUS_COMPLETE) {
            return;
        }
        final int currentPos = ptrIndicator.getCurrentPosY();
        if (checkNotNull(pullLoadingView)) {
            pullLoadingView.onUIPositionChange(currentPos);
        }
    }

    @Override
    public void onPtrUIHandlerHookStart() {
        if (checkNotNull(pullLoadingView)) {
            pullLoadingView.setMode(PullLoadingView.MODE_LOADING_COMPLETE);
        }
        //如果没有pullLoadingView，强制hook resume，不然会导致刷新视图无法还原
        else {
            onRefreshComplete();
        }
    }

    @Override
    public void onRefreshComplete() {
        if (checkNotNull(handlerHook)) {
            handlerHook.resume();
        }
    }
}