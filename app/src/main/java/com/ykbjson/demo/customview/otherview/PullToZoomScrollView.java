package com.ykbjson.demo.customview.otherview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewHelper;
import com.ykbjson.demo.R;
import com.ykbjson.demo.tools.SLog;

/**
 * 包名：com.simpletour.client.widget
 * 描述：滑动缩放视图
 * 创建者：yankebin
 * 日期：2016/5/11
 */
public class PullToZoomScrollView extends ScrollView {
    private View headerView;
    private int headerViewHeight;
    private GestureDetectorCompat mDetector;
    private float scrollY;
    private float downY;

    private final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float input) {
            float f = input - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };


    private class ScrollGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            downY = e.getY();
            return super.onDown(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean isTop = getScrollY() == 0;
            if (isTop) {
                scrollY += Math.abs(distanceY);
                float scaleIn = Math.abs(scrollY) / (headerViewHeight);
                float scale = sInterpolator.getInterpolation(scaleIn);
                SLog.d("scaleIn : " + scaleIn + " scale : " + scale + " distanceY : " + distanceY);
                if (scale < 1) {
                    scale = 1;
                }

                ViewHelper.setScaleX(headerView, scale);
                ViewHelper.setScaleY(headerView, scale);
            } else {
                scrollY = headerViewHeight;
                ViewHelper.setScaleX(headerView, 1.0f);
                ViewHelper.setScaleY(headerView, 1.0f);
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDetector = new GestureDetectorCompat(context, new ScrollGestureDetector());
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToZoomScrollView(Context context) {
        this(context, null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        headerView = findViewById(R.id.scroll_header_view);
        headerViewHeight = headerView.getHeight();
        scrollY = headerViewHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f);
            ObjectAnimator.ofPropertyValuesHolder(headerView, pvhX, pvhY).setDuration(200L).start();
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
