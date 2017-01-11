package com.ykbjson.demo.customview.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;

import com.drivingassisstantHouse.library.tools.SLog;
import com.ykbjson.demo.R;


/**
 * 包名：com.ykbjson.demo.customview.scrollview
 * 描述：两段ScrollView
 * 创建者：yankebin
 * 日期：2016/12/27
 */

public class PagingScrollView extends ScrollView {
    public interface OnNextPageLoadCallback {
        void onNextPageLoad(boolean isFirstLoad);
    }

    private final String TAG_PAGE_DIVIDER = getResources().getString(R.string.tag_pageDivider);
    private final String TAG_SECOND_PAGE = getResources().getString(R.string.tag_pageSecondView);
    private static final int SCROLL_DURATION = 700;
    private static final int PAGE_NO_FIRST = 1;
    private static final int PAGE_NO_SECOND = PAGE_NO_FIRST + 1;

    private float downY;
    private View dividerView;
    private View secondView;
    private CustomScroller scroller;
    private int dividerY;
    private int secondTop;
    private int dividerHeight;
    private int pageNo = PAGE_NO_FIRST;
    private boolean isInMove;
    private int downScrollY;
    private OnNextPageLoadCallback callback;
    private boolean isFirstLoadNextPage = true;


    public PagingScrollView(Context context) {
        this(context, null);
    }

    public PagingScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new CustomScroller(context);
        scroller.init(SCROLL_DURATION, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dividerView = findViewWithTag(TAG_PAGE_DIVIDER);
        secondView = findViewWithTag(TAG_SECOND_PAGE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!checkHasPaging()) {
            return;
        }
        dividerHeight = dividerView.getMeasuredHeight();
        int dividerTop = getChildTop(dividerView, dividerView.getTop());
        dividerY = dividerTop + dividerHeight - getHeight();
        secondTop = getChildTop(secondView, secondView.getTop());
        if (secondTop <= getHeight() || dividerTop <= getHeight()) {
            throw new IllegalArgumentException("firstPage's  height must be more than the parent's height");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downScrollY = getScrollY();
            downY = ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            isInMove = true;
        } else if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            isInMove = false;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (checkHasPaging()) {
            if (ev.getAction() == MotionEvent.ACTION_UP
                    || ev.getAction() == MotionEvent.ACTION_CANCEL) {
                if (scroller.isFinished() && checkNeedScroll() && handleScroll((int) (ev.getRawY() - downY))) {
                    return true;
                }
            }
        }
        return !scroller.isFinished() || super.onTouchEvent(ev);
    }

    public void setOnNextPageLoadCallback(OnNextPageLoadCallback callback) {
        this.callback = callback;
    }

    /**
     * 检测是否需要分页
     *
     * @return
     */
    private boolean checkHasPaging() {
        return null != dividerView && null != secondView;
    }

    /**
     * 检测是否需要滚动
     *
     * @return
     */
    private boolean checkNeedScroll() {
        int currentScrollY = getScrollY();
//        SLog.e("currentScrollY : " + currentScrollY + " downScrollY : " + downScrollY + " pageNo : " + pageNo);
        //内容滚向尾部
        if (currentScrollY > downScrollY) {
            if (pageNo == PAGE_NO_FIRST) {
                if (currentScrollY > dividerY) {
                    return true;
                }
            }
        }
        //内容滚向头部
        else if (currentScrollY < downScrollY) {
            if (pageNo == PAGE_NO_SECOND) {
                if (currentScrollY < secondTop) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 根据y方向坐标平滑滑动
     *
     * @param y
     */

    private void smoothScrollTo(int y) {
        smoothScrollBy(y - getScrollY());
    }

    /**
     * 根据y方向距离平滑滑动
     *
     * @param dstY
     */
    private void smoothScrollBy(int dstY) {
        scroller.abortAnimation();
        scroller.startScroll(0, getScrollY(), 0, dstY);
    }

    /**
     * 处理滚动
     *
     * @param distance
     */
    private boolean handleScroll(int distance) {
        SLog.d("distance : " + distance + " isFinished : " + scroller.isFinished());
        switch (pageNo) {
            case PAGE_NO_FIRST:
                if (distance < 0) {
                    if (distance <= -dividerHeight) {
                        smoothScrollTo(secondTop);
                        pageNo = PAGE_NO_SECOND;
                        if (null != callback) {
                            callback.onNextPageLoad(isFirstLoadNextPage);
                        }
                        if (isFirstLoadNextPage) {
                            isFirstLoadNextPage = false;
                        }
                    } else {
                        smoothScrollTo(dividerY);
                    }
                    return true;
                }
                break;
            case PAGE_NO_SECOND:
                if (distance > 0) {
                    if (distance >= dividerHeight) {
                        smoothScrollTo(dividerY);
                        pageNo = PAGE_NO_FIRST;
                    } else {
                        smoothScrollTo(secondTop);
                    }
                    return true;
                }
                break;
        }

        return false;
    }

    /**
     * 找到view距离顶部的距离
     *
     * @param view 当前视图
     * @param top  当前视图的顶部距离
     * @return
     */
    private int getChildTop(View view, int top) {
        ViewParent parent = view.getParent();
        if (null == parent || this == parent || !(parent instanceof View)) {
            return top;
        }
        ViewGroup parentView = (ViewGroup) parent;
        top += parentView.getTop();
        return getChildTop(parentView, top);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!checkHasPaging()) {
            return;
        }
        if (t > oldt) {
            if (pageNo == PAGE_NO_SECOND) {
                return;
            }
            if (t > dividerY) {
                if (!isInMove) {
                    smoothScrollTo(dividerY);
                }
            }
        } else if (t < oldt) {
            if (pageNo == PAGE_NO_FIRST) {
                return;
            }
            if (t < secondTop) {
                if (!isInMove) {
                    smoothScrollTo(secondTop);
                }
            }
        }
    }
}
