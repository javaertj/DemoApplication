package com.ykbjson.demo.customview.otherview;

import android.view.View;
/**
 * 包名：com.ykbjson.demo.customview.otherview
 * 描述：滚动回调接口
 * 创建者：yankebin
 * 日期：2016/5/13
 */
public interface OnScrollCallback {
        void onScroll(View view, boolean isTouchFrom,float scrollX);
    }