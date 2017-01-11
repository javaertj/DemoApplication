package com.ykbjson.demo.customview.scrollview;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * 包名：com.ykbjson.demo.customview.scrollview
 * 描述：自定义Scroller
 * 创建者：yankebin
 * 日期：2016/12/27
 */

public class CustomScroller extends OverScroller {
    private int mScrollDuration;

    public CustomScroller(Context context) {
        this(context, new AccelerateDecelerateInterpolator());
    }

    public CustomScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public CustomScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        super(context, interpolator, bounceCoefficientX, bounceCoefficientY);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
    }

    public void init(int scrollDuration, ScrollView scrollView) {
        setScrollDuration(scrollDuration);
        try {
            Field mScroller = ScrollView.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(scrollView, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
