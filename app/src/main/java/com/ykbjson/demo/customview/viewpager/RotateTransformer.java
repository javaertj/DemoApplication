package com.ykbjson.demo.customview.viewpager;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 包名：com.drivingassisstantHouse.library.widget.viewpager
 * 描述：旋转切换动画
 * 创建者：yankebin
 * 日期：2016/3/4
 */
public class RotateTransformer implements CustomViewPager.PageTransformer {

    private static final float ROT_MAX = 20.0f;

    public void transformPage(View view, float position)
    {
        if (position < -1)
        { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setRotation(view, 0);
            ViewHelper.setAlpha(view,0);

        } else if (position <= 1) // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
        { // [-1,1]
            float mRot;
            // Modify the default slide transition to shrink the page as well
            if (position < 0)
            {

                mRot = (ROT_MAX * position);
                ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(view, view.getMeasuredHeight());
                ViewHelper.setRotation(view, mRot);
            } else
            {

                mRot = (ROT_MAX * position);
                ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(view, view.getMeasuredHeight());
                ViewHelper.setRotation(view, mRot);
            }
            // Scale the page down (between MIN_SCALE and 1)

            // Fade the page relative to its size.

        } else
        { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setRotation(view, 0);
            ViewHelper.setAlpha(view, 0);
        }
    }
}
