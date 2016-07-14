package com.ykbjson.demo.customview.viewpager;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 包名：com.drivingassisstantHouse.library.widget.viewpager
 * 描述：旋转、缩放、透明结合在一起的动画
 * 创建者：yankebin
 * 日期：2016/3/4
 */
public class MultiTransformer implements CustomViewPager.PageTransformer {

    private static final float ROT_MAX = 20.0f;
    //zoom
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position)
    {
        //zoom
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
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
            //zoom
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
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
