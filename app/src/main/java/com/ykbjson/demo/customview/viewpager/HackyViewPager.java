package com.ykbjson.demo.customview.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ykbjson.demo.tools.SLog;

/**
 * 包名：com.drivingassisstantHouse.library.widget.viewpager
 * 描述：解决滑动冲突的ViewPager
 * 创建者：yankebin
 * 日期：2016/3/21
 */
public class HackyViewPager extends ViewPager {

    private boolean isLocked;

    public HackyViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isLocked && super.onTouchEvent(event);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void setCurrentItem(int item) {
        if (item == getCurrentItem()) {
            return;
        }
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (item == getCurrentItem()) {
            SLog.d("same currentItem");
            return;
        }
        super.setCurrentItem(item, smoothScroll);
    }
}
