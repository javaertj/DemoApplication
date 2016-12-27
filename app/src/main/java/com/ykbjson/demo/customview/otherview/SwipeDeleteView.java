package com.ykbjson.demo.customview.otherview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.drivingassisstantHouse.library.tools.SLog;
import com.drivingassisstantHouse.library.tools.ToolToast;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.ykbjson.demo.R;


/**
 * 包名：com.ykbjson.demo.customview.otherview
 * 描述：侧滑视图
 * 创建者：yankebin
 * 日期：2016/12/13
 */

public class SwipeDeleteView extends FrameLayout {
    /**
     * 自动滚动时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;

    private View contentView;
    private View controlView;
    private boolean isSlide;
    private float downX;
    private float firstDownX;
    private float mTouchSlop;
    private float deltaX;
    private ObjectAnimator animator;
    private VelocityTracker mVelocityTracker;
    private float moveX;
    private float moveY;
    private ScrollDirection scrollDirection = ScrollDirection.IDELL;

    private enum ScrollDirection {
        IDELL, LEFT, RIGHT
    }

    public SwipeDeleteView(Context context) {
        this(context, null);
    }

    public SwipeDeleteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        requestFocus();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        animator = ObjectAnimator.ofFloat(contentView, "translationX", 1.0f, 0.f);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolToast.showShort("=============");
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        controlView = findViewById(R.id.swipe_control_id);
        contentView = findViewById(R.id.swipe_content_id);
    }


    /**
     * 分发事件，设置响应左右滑动事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                //动画未执行完，交给父视图处理
                if (null != animator && animator.isRunning()) {
                    return super.dispatchTouchEvent(event);
                }
                firstDownX = downX = moveX = event.getRawX();
                moveY = event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float tempMoveX = moveX;
                final float tempMoveY = moveY;

                moveX = event.getRawX();
                moveY = event.getRawY();

                final float distanceX = moveX - tempMoveX;
                final float distanceY = moveY - tempMoveY;

                if (distanceX > 0) {
                    scrollDirection = ScrollDirection.RIGHT;
                } else if (distanceX < 0) {
                    scrollDirection = ScrollDirection.LEFT;
                } else {
                    scrollDirection = ScrollDirection.IDELL;
                }

                final float xSpeed = Math.abs(getScrollVelocity());
                if (xSpeed >= SNAP_VELOCITY || (Math.abs(distanceX) > mTouchSlop && Math
                        .abs(distanceY) < mTouchSlop * 2)) {
                    isSlide = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        //解決当控制视图内部包含抢占焦点的视图时不想赢touch事件的问题
        if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            // 该事件可能不是我们的
            return super.onInterceptTouchEvent(ev);
        }
        return isSlide;
    }

    /**
     * 处理我们拖动item的逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //滑动状态下，不交给父视图处理
        if (isSlide) {
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_MOVE: {
                    final float translationX = ViewHelper.getTranslationX(contentView);
                    final float controlWidth = controlView.getMeasuredWidth();
                    SLog.e("translationX : "+translationX+" scrollDirection : "+scrollDirection+" deltaX : "+deltaX);
                    //滑到左边显示出控制区域后，不允许继续往左滑动
                    if (translationX == -controlWidth && scrollDirection == ScrollDirection.LEFT) {
                        return super.onTouchEvent(event);
                    }
                    //滑到左边显示出控制区域后，不允许继续往左滑动
                    if (translationX == 0 && scrollDirection == ScrollDirection.RIGHT) {
                        return super.onTouchEvent(event);
                    }

                    if (scrollDirection == ScrollDirection.LEFT) {
                        deltaX -=Math.abs (moveX - downX);
                        if (deltaX < -controlWidth) {
                            deltaX = -controlWidth;
                        }
                    }
                    //往右滑时，如果内容视图已经归位，不在继续滑动，其他时刻往右继续滑动
                    else if (scrollDirection == ScrollDirection.RIGHT) {
                        deltaX +=Math.abs (moveX - downX);
                        if (deltaX > 0) {
                            deltaX = 0;
                        }
                    }
                    downX = moveX;
                    //滑动处理
                    scrollByTouch();
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // 手指离开的时候就不响应左右滚动
                    scroll();
                    if (scrollDirection == ScrollDirection.RIGHT) {
                        deltaX = 0;
                    } else if (scrollDirection == ScrollDirection.LEFT) {
                        deltaX = -controlView.getMeasuredWidth();
                    }
                    isSlide = false;
                    scrollDirection = ScrollDirection.IDELL;
                    break;
            }

            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手指离开后，根据最后的移动速度和方向来滚动内容视图
     */
    private void scroll() {
        scrollAuto();
    }


    /**
     * 根据touch move事件滚动内容视图
     */
    private void scrollByTouch() {
        ViewHelper.setTranslationX(contentView, deltaX);
    }

    /**
     * 往右滑动
     */
    private void scrollRight() {
        float start = ViewHelper.getTranslationX(contentView);
        float end = 0;
        scrollByAnim(start, end);
    }

    /**
     * 向左滑动
     */
    private void scrollLeft() {
        float start = ViewHelper.getTranslationX(contentView);
        float end = -controlView.getMeasuredWidth();
        scrollByAnim(start, end);
    }

    /**
     * 用动画滚动内容视图
     *
     * @param values 起始位置，终止位置
     */
    private void scrollByAnim(float... values) {
        animator.cancel();
        animator.setTarget(contentView);
        animator.setPropertyName("translationX");
        animator.setFloatValues(values);
        animator.setDuration(80L);
        animator.start();
    }


    /**
     * 根据手指滚动的距离来判断是滚动到开始位置还是向左或者向右滚动
     */
    private void scrollAuto() {
        final float translationX = ViewHelper.getTranslationX(contentView);
        final float controlWidth = controlView.getMeasuredWidth();
        //没移动过，不在滑动
        if (deltaX == 0 || translationX == 0 || translationX == -controlWidth) {
            return;
        }
        final float totalDistance = Math.abs(downX - firstDownX);
        //往左
        if (scrollDirection == ScrollDirection.LEFT) {
            if (totalDistance >= SNAP_VELOCITY / 2) {
                scrollLeft();
            } else {
                scrollRight();
            }
        }
        //往右
        else if (scrollDirection == ScrollDirection.RIGHT) {
            if (totalDistance >= SNAP_VELOCITY / 2) {
                scrollRight();
            } else {
                scrollLeft();
            }
        }
    }


    /**
     * 创建VelocityTracker对象，并将触摸事件加入到VelocityTracker当中。
     *
     * @param event 右侧布局监听控件的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在右侧布局的监听View上的滑动速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private float getScrollVelocity() {
        if (null == mVelocityTracker) {
            return 0;
        }
        mVelocityTracker.computeCurrentVelocity(1000);
        return mVelocityTracker.getXVelocity();
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        if (null == mVelocityTracker) {
            return;
        }
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
}
