package com.drivingassisstantHouse.library.widget.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
*des:禁止用手势
*verison:1.0
*author:SunJi
*create time:2016/4/5 17:31
*/
public class ForbidViewPager extends ViewPager {

    private boolean isLock = true;
//
//    public ForbidViewPager(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public ForbidViewPager(Context context) {
//        super(context);
//    }
//
    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }
//
//    @Override
//    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent arg0) {
//        /* return false;//super.onTouchEvent(arg0); */
//        if (isLock)
//            return false;
//        else
//            return super.onTouchEvent(arg0);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        if (isLock)
//            return false;
//        else
//            return super.onInterceptTouchEvent(arg0);
//    }
//
//    @Override
//    public void setCurrentItem(int item, boolean smoothScroll) {
//        super.setCurrentItem(item, smoothScroll);
//    }
//
//    @Override
//    public void setCurrentItem(int item) {
//        super.setCurrentItem(item);
//    }

    private boolean isCanScroll = true;

    public ForbidViewPager(Context context) {
        super(context);
    }

    public ForbidViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }




    @Override
    public void scrollTo(int x, int y){
        if (isLock){
            super.scrollTo(x, y);
        }
    }
}
