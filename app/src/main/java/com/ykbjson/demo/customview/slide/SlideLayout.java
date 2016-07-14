package com.ykbjson.demo.customview.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ykbjson.demo.tools.ToolUnit;


/**
 * 包名：com.drivingassisstantHouse.library.widget
 * 描述：基于RelativeLayout的滑动菜单
 * 创建者：yankebin
 * 日期：2015/12/14
 */
public class SlideLayout extends RelativeLayout implements SlideBase {
    private String TAG = getClass().getSimpleName();

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;

    /**
     * 滚动显示和隐藏左侧布局时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;

    /**
     * 屏幕像素密度
     */
    private float density;

    /**
     * 滑动速度
     */
    private int slideSpeed = 10;
    /**
     * 记录当前的滑动状态
     */
    private int slideState;

    /**
     * 右侧布局最多可以滑动到的左边缘。
     */
    private int leftEdge = 0;

    /**
     * 右侧布局最多可以滑动到的右边缘。
     */
    private int rightEdge = 0;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;

    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;

    /**
     * 记录手指按下时的纵坐标。
     */
    private float yDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手指移动时的纵坐标。
     */
    private float yMove;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;

    /**
     * 左侧布局当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
     */
    private boolean isMenuVisible;

    /**
     * 是否正在滑动。
     */
    private boolean isSliding;
    /**
     * 左侧布局的参数，通过此参数来重新确定左侧布局的宽度，以及更改leftMargin的值。
     */
    private MarginLayoutParams menuParams;

    /**
     * 右侧布局的参数，通过此参数来重新确定右侧布局的宽度。
     */
    private MarginLayoutParams contentParams;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 滑动控制器
     */
    private SlideRunner slideRunner = new SlideRunner();
    /**
     * 滑动时和菜单展开时不能操作的视图
     */
    private View mBindView;
    /**
     * 菜单视图
     */
    private View menu;
    /**
     * 内容视图
     */
    private View content;

    /**
     * 重写SlidingLayout的构造函数
     *
     * @param context
     */
    public SlideLayout(Context context) {
        this(context, null);
    }

    /**
     * 重写SlidingLayout的构造函数
     *
     * @param context
     * @param attrs
     */
    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 重写SlidingLayout的构造函数
     *
     * @param context
     * @param attrs
     * @param defaultStyle
     */
    public SlideLayout(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        density = getResources().getDisplayMetrics().density;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setLayoutParams(new LayoutParams(-1, -1));
        initContainer();
    }

    /**
     * 设置背景布局id
     *
     * @param layoutId
     */
    public void setBackGround(int layoutId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
        setBackGround(view);
    }

    /**
     * 设置背景视图
     *
     * @param layout
     */
    public void setBackGround(View layout) {
        addView(layout, 0);
    }

    @Override
    public void initContainer() {
        menu = createContainer(MENU_CONTAINER_ID);
        content = createContainer(CONTENT_CONTAINER_ID);
        addView(menu);
        addView(content);
    }

    @Override
    public View createContainer(int id) {
        LinearLayout container = new LinearLayout(getContext());
        container.setId(id);
        container.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(-1, -1);

        if (MENU_CONTAINER_ID == id) {
            params = new LayoutParams(ToolUnit.dipTopx(300), -1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        //important ：if didn't set this rule,rightMargin will be useless
        else
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        container.setLayoutParams(params);

        return container;
    }

    @Override
    public void setMenu(int layoutId) {
        LinearLayout left = (LinearLayout) findViewById(MENU_CONTAINER_ID);
        if (left.getChildCount() > 0)
            throw new IllegalArgumentException("菜单视图已存在");
        View menu = LayoutInflater.from(getContext()).inflate(layoutId, null, false);
        setMenu(menu);
    }

    @Override
    public void setMenu(View menuView) {
        addChildView(menuView, MENU_CONTAINER_ID);
    }

    @Override
    public void setContent(int layoutId) {
        LinearLayout right = (LinearLayout) findViewById(CONTENT_CONTAINER_ID);
        if (right.getChildCount() > 0)
            throw new IllegalArgumentException("主视图已存在");
        View content = LayoutInflater.from(getContext()).inflate(layoutId, null, false);
        setContent(content);
    }

    @Override
    public void setContent(View contentView) {
        contentView.setClickable(true);
        addChildView(contentView, CONTENT_CONTAINER_ID);
    }

    @Override
    public void addChildView(View view, int id) {
        LinearLayout container = (LinearLayout) findViewById(id);
        if (null == container)
            throw new IllegalArgumentException("视图容器为空");

        if (null == view.getLayoutParams())
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        container.addView(view);
    }

    @Override
    public void onLayoutInit() {
        contentParams = (MarginLayoutParams) content.getLayoutParams();
        contentParams.width = getMeasuredWidth();
        content.setLayoutParams(contentParams);

        menuParams = (MarginLayoutParams) menu.getLayoutParams();
        rightEdge = -menuParams.width;

        loadOnce = true;
    }


    /**
     * 设置滑动时和菜单展开时不能操作的视图
     *
     * @param bindView
     */
    public void setScrollEvent(View bindView) {
        if (null != bindView)
            mBindView = bindView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xDown = event.getRawX();
                yDown = event.getRawY();
                slideState = SlideBase.DO_NOTHING;
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整右侧布局的leftMargin值，从而显示和隐藏左侧布局
                xMove = event.getRawX();
                yMove = event.getRawY();
                int moveDistanceX = (int) (xMove - xDown);
                int moveDistanceY = (int) (yMove - yDown);
                checkSlideState(moveDistanceX, moveDistanceY);
                switch (slideState) {
                    case SlideBase.SHOW_MENU:
                        contentParams.rightMargin = -moveDistanceX;
                        menuParams.leftMargin =rightEdge-moveDistanceX;
                        onSlide();
                        break;
                    case SlideBase.HIDE_MENU:
                        contentParams.rightMargin = rightEdge - moveDistanceX;
                        menuParams.leftMargin = moveDistanceX;
                        onSlide();
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                int upDistanceX = (int) (xUp - xDown);
                if (isSliding) {
                    // 手指抬起时，进行判断当前手势的意图
                    switch (slideState) {
                        case SlideBase.SHOW_MENU:
                            if (isNeedShowMenu()) {
                                smoothShowMenu();
                            } else {
                                smoothShowContent();
                            }
                            break;
                        case SlideBase.HIDE_MENU:
                            if (isNeedShowContent()) {
                                smoothShowContent();
                            } else {
                                smoothShowMenu();
                            }
                            break;
                        default:
                            break;
                    }
                }
                recycleVelocityTracker();
                break;
        }
        if (isEnabled()) {
            if (isSliding) {
                unFocusBindView();
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return onTouchEvent(ev);
    }

    /**
     * 执行滑动过程中的逻辑操作，如边界检查，改变偏移值，可见性检查等。
     */
    private void onSlide() {
        checkSlideBorder();
        content.setLayoutParams(contentParams);
        menu.setLayoutParams(menuParams);
    }

    /**
     * 根据手指移动的距离，判断当前用户的滑动意图，然后给slideState赋值成相应的滑动状态值。
     *
     * @param moveDistanceX 横向移动的距离
     * @param moveDistanceY 纵向移动的距离
     */
    private void checkSlideState(int moveDistanceX, int moveDistanceY) {
        if (isMenuVisible) {
            if (!isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX < 0) {
                isSliding = true;
                slideState = SlideBase.HIDE_MENU;
            }
        } else if (!isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX > 0 && Math.abs(moveDistanceY) <= touchSlop) {
            isSliding = true;
            slideState = SlideBase.SHOW_MENU;
        }
    }

    /**
     * 在滑动过程中检查左侧菜单的边界值，防止绑定布局滑出屏幕。
     */
    private void checkSlideBorder() {
        if (contentParams.rightMargin > leftEdge) {
            contentParams.rightMargin = leftEdge;
        } else if (contentParams.rightMargin < rightEdge) {
            contentParams.rightMargin = rightEdge;
        }
    }

    /**
     * 判断是否应该滚动将左侧布局展示出来。如果手指移动距离大于屏幕的1/6，或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将左侧布局展示出来。
     *
     * @return 如果应该滚动将左侧布局展示出来返回true，否则返回false。
     */
    private boolean isNeedShowMenu() {
        return xUp - xDown > menuParams.width / 6 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将右侧布局展示出来。如果手指移动距离加上leftLayoutPadding大于屏幕的1/6， 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将右侧布局展示出来。
     *
     * @return 如果应该滚动将右侧布局展示出来返回true，否则返回false。
     */
    private boolean isNeedShowContent() {
        return xDown - xUp > menuParams.width / 6 || getScrollVelocity() > SNAP_VELOCITY;
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
    private int getScrollVelocity() {
        if (null == mVelocityTracker)
            return 0;
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 将屏幕滚动到左侧布局界面
     */
    public void smoothShowMenu() {
        slideRunner.start(-getSpeed());
    }

    /**
     * 将屏幕滚动到右侧布局界面
     */
    public void smoothShowContent() {
        slideRunner.start(getSpeed());
    }

    /**
     * 获取一个合适的速度
     *
     * @return
     */
    private float getSpeed() {
        float speed = getScrollVelocity() == 0 ? slideSpeed : getScrollVelocity() / 1000f;
        if (speed < slideSpeed)
            speed = slideSpeed;

        return speed;
    }

    /**
     * 左侧布局是否完全显示出来，或完全隐藏，滑动过程中此值无效。
     *
     * @return 左侧布局完全显示返回true，完全隐藏返回false。
     */
    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    /**
     * 使用可以获得焦点的控件在滑动的时候失去焦点。
     */
    private void unFocusBindView() {
        if (null != mBindView) {
            mBindView.setPressed(false);
            mBindView.setFocusable(false);
            mBindView.setFocusableInTouchMode(false);
        }
    }

    /**
     * 在onLayout中重新设定左侧布局和右侧布局的参数。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            onLayoutInit();
        }
    }

    private class SlideRunner implements Runnable {
        private float mSpeed;
        private boolean run;
        private int rightMargin;

        private SlideRunner() {
        }

        public void stop() {
            this.run = false;
            removeCallbacks(this);
        }

        public void start(float mSpeed) {
            stop();
            this.mSpeed = mSpeed;
            rightMargin = contentParams.rightMargin;
            run = true;
            post(this);
        }

        @Override
        public void run() {
            if (!run)
                return;
            float speeds = mSpeed;
            boolean showContent = speeds < 0;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            if (showContent)
                speeds -= 10;
            else
                speeds += 10;
            //保持不同分辨率手机滑动同步
            rightMargin += speeds * density;

            if (rightMargin < rightEdge || rightMargin > leftEdge) {
                if (rightMargin < rightEdge)
                    rightMargin = rightEdge;
                else
                    rightMargin = leftEdge;

                onChangeState(mSpeed);
                onSlide(rightMargin, mSpeed, false);
                return;
            }
            onSlide(rightMargin, mSpeed, true);
            postDelayed(this, 1);
        }
    }

    /**
     * 改变菜单、内容视图的当前状态
     *
     * @param speed
     */
    private void onChangeState(float speed) {
        if (speed > 0) {
            isMenuVisible = false;
        } else {
            isMenuVisible = true;
        }
        isSliding = false;
    }

    /**
     * 滑动
     *
     * @param rightMargin
     * @param speed
     * @param sliding
     */
    private void onSlide(int rightMargin, float speed, boolean sliding) {
        contentParams.rightMargin = rightMargin;
        content.setLayoutParams(contentParams);
        menuParams.leftMargin =rightEdge -rightMargin;
        menu.setLayoutParams(menuParams);
        if (sliding)
            unFocusBindView();
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isMenuVisible()) {
            smoothShowContent();
        } else {
            smoothShowMenu();
        }
    }
}
