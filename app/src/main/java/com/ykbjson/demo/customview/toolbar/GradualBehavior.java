package com.ykbjson.demo.customview.toolbar;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * 包名：com.ykbjson.demo.customview.toolbar
 * 描述：
 * 创建者：yankebin
 * 日期：2016/8/18
 */

public class GradualBehavior extends CoordinatorLayout.Behavior<View> {

    public GradualBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {


        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}
