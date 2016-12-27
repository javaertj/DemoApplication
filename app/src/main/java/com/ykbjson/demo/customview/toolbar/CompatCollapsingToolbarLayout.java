package com.ykbjson.demo.customview.toolbar;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * 包名：com.ykbjson.demo.customview.toolbar
 * 描述：自定义CollapsingToolbarLayout
 * 创建者：yankebin
 * 日期：2016/8/17
 */
public class CompatCollapsingToolbarLayout extends CollapsingToolbarLayout {

    private boolean mLayoutReady;

    public CompatCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public CompatCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompatCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!mLayoutReady) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                if ((getWindowSystemUiVisibility() &
                        (SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)) ==
                        (SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)) {
                    try {
                        Field mLastInsets = CollapsingToolbarLayout.class.getDeclaredField("mLastInsets");
                        mLastInsets.setAccessible(true);
                        mLastInsets.set(this, null);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            mLayoutReady = true;
        }

        super.onLayout(changed, left, top, right, bottom);
    }
}