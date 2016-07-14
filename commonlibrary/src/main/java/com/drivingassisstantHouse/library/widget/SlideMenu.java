package com.drivingassisstantHouse.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drivingassisstantHouse.library.R;
import com.drivingassisstantHouse.library.tools.SLog;
import com.drivingassisstantHouse.library.tools.ToolUnit;
import com.nineoldandroids.view.ViewHelper;

/**
 * 包名：com.drivingassisstantHouse.library.widget
 * 描述：基于HorizontalScrollView的滑动菜单
 * 创建者：yankebin
 * 日期：2015/12/15
 */
public class SlideMenu extends HorizontalScrollView implements SlideBase {
    private String TAG = getClass().getSimpleName();
    /**
     * 滑动偏移量
     */
    private static final int BASE_SLIDE_BLOCK = 12;

    /**
     * 屏幕像素密度
     */
    private float density;

    /**
     * 菜单效果
     */
    private int mode;
    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;

    /**
     * 左侧布局对象。
     */
    private View leftLayout;

    /**
     * 右侧布局对象。
     */
    private View rightLayout;
    /**
     * 菜单宽度
     */
    private int menuWidth;
    /**
     * 滑动开始时的x坐标
     */
    private float downX;
    /**
     * 滑动开始时的y坐标
     */
    private float downY;
    /**
     * 菜单状态
     */
    private boolean isMenuOpen;

    private boolean slideMenuContent;
    private float menuAlpha;
    private float contentAlpha;
    private float menuScale;
    private float contentScale;
    private float menuMove;

    /**
     * 重写SlidingLayout的构造函数
     *
     * @param context
     */
    public SlideMenu(Context context) {
        this(context, null);
    }

    /**
     * 重写SlidingLayout的构造函数
     *
     * @param context
     * @param attrs
     */
    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 重写SlidingLayout的构造函数
     *
     * @param context
     * @param attrs
     * @param defaultStyle
     */
    public SlideMenu(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        initAttributeSet(attrs);
        density = getResources().getDisplayMetrics().density;
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideMenu);
        mode = typedArray.getInteger(typedArray.getIndex(R.styleable.SlideMenu_slide_mode), MODE_NORMAL);
        menuWidth = typedArray.getDimensionPixelSize(R.styleable.SlideMenu_slide_menu_width, ToolUnit.dipTopx(300));
        slideMenuContent = typedArray.getBoolean(R.styleable.SlideMenu_slide_menu_content_enabled, true);
        menuAlpha = typedArray.getFloat(R.styleable.SlideMenu_menu_alpha_coefficient, 0.6f);
        contentAlpha = typedArray.getFloat(R.styleable.SlideMenu_content_alpha_coefficient, 1f);
        menuScale = typedArray.getFloat(R.styleable.SlideMenu_menu_scale_coefficient, 0.3f);
        contentScale = typedArray.getFloat(R.styleable.SlideMenu_content_scale_coefficient, 0.8f);
        menuMove = typedArray.getFloat(R.styleable.SlideMenu_menu_content_move_coefficient, 0.3f);
        typedArray.recycle();
        initContainer();
    }

    /**
     * 初始化一些属性
     *
     * @param attrs
     */
    private void initAttributeSet(AttributeSet attrs) {
        if (getChildCount() > 0) {
            throw new IllegalArgumentException("不允许在布局文件中添加子视图");
        }
        if (null == attrs) {
            setLayoutParams(new RelativeLayout.LayoutParams(-2, -1));
        }
        //删除ScrollView边界阴影
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
        //删除ScrollView拉到尽头（顶部、底部、左侧、右侧），然后继续拉出现的阴影效果
        setOverScrollMode(OVER_SCROLL_NEVER);
        setClickable(true);
    }

    /**
     * @param layoutResId
     */
    public void setBackGround(int layoutResId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
        setBackGround(view);
    }

    /**
     * @param view
     */
    public void setBackGround(View view) {
        FrameLayout bgLayout = (FrameLayout) findViewById(BACKGROUND_CONTAINER_ID);
        bgLayout.addView(view, 0);
    }

    /**
     * @param layoutResId
     */
    public void setMenuBackGround(int layoutResId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
        setMenuBackGround(view);
    }

    /**
     * @param view
     */
    public void setMenuBackGround(View view) {
        FrameLayout bgLayout = (FrameLayout) findViewById(MENU_CONTAINER_ID);
        bgLayout.addView(view, 0);
    }

    @Override
    public void initContainer() {
        ViewGroup bgLayout = (ViewGroup) createContainer(BACKGROUND_CONTAINER_ID);
        addView(bgLayout);
        LinearLayout container = (LinearLayout) createContainer(MAIN_CONTAINER_ID);
        bgLayout.addView(container);
        View menu = createContainer(MENU_CONTAINER_ID);
        View content = createContainer(CONTENT_CONTAINER_ID);
        container.addView(menu);
        container.addView(content);

    }

    @Override
    public View createContainer(int id) {
        View container = new LinearLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -1);
        if (id == MENU_CONTAINER_ID) {
            container = new FrameLayout(getContext());
            params.width = menuWidth;

        } else if (id == BACKGROUND_CONTAINER_ID) {
            container = new FrameLayout(getContext());
        } else if (id == CONTENT_CONTAINER_ID) {
            ((LinearLayout) container).setOrientation(LinearLayout.VERTICAL);
            params.width = getResources().getDisplayMetrics().widthPixels;
        } else {
            ((LinearLayout) container).setOrientation(LinearLayout.HORIZONTAL);
        }

        //防止手机休眠唤醒后或其他情况引起scroll自动滚动
        container.setFocusable(true);
        container.setFocusableInTouchMode(true);
        container.setId(id);
        container.setLayoutParams(params);

        return container;
    }

    @Override
    public void setMenu(int layoutId) {
        ViewGroup left = (ViewGroup) findViewById(MENU_CONTAINER_ID);
        if (left.getChildCount() > 1) {
            throw new IllegalArgumentException("菜单视图已存在");
        }
        View menu = LayoutInflater.from(getContext()).inflate(layoutId, null, false);
        setMenu(menu);
    }

    @Override
    public void setMenu(View menuView) {
        addChildView(menuView, MENU_CONTAINER_ID);
    }

    @Override
    public void setContent(int layoutId) {
        ViewGroup right = (ViewGroup) findViewById(CONTENT_CONTAINER_ID);
        if (right.getChildCount() > 0) {
            throw new IllegalArgumentException("内容视图已存在");
        }
        View content = LayoutInflater.from(getContext()).inflate(layoutId, null, false);
        setContent(content);
    }

    @Override
    public void setContent(View contentView) {
        addChildView(contentView, CONTENT_CONTAINER_ID);
    }

    @Override
    public void addChildView(View view, int id) {
        ViewGroup container = (ViewGroup) findViewById(id);
        if (null == container) {
            throw new NullPointerException("视图容器为空");
        }

        if (null == view.getLayoutParams()) {
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        }

        container.addView(view);
    }


    @Override
    public void onLayoutInit() {
        loadOnce = true;
        // 获取左侧布局对象
        leftLayout = findViewById(MENU_CONTAINER_ID);
        // 获取右侧布局对象
        rightLayout = findViewById(CONTENT_CONTAINER_ID);
        scrollTo(menuWidth, 0);
    }

    /**
     * 设置菜单动画模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
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

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //l是当前scroller的相对（scrollview起始位置）坐标，这样计算出来的系数刚好是menu的显示宽度的占比
        float scale = l * 1.0f / menuWidth;
        switch (mode) {
            case MODE_CONTENT_SCROLL_ONLY:
                ViewHelper.setTranslationX(leftLayout, menuWidth * scale);
                if (slideMenuContent) {
                    ViewGroup menu = ((ViewGroup) leftLayout);
                    View menuContent = menu.getChildAt(menu.getChildCount() - 1);
                    //控制menu的顶层视图不滑动，但速度和scroller的速度不等，实现层次移动效果
                    ViewHelper.setTranslationX(menuContent, -menuWidth * scale * menuMove);
                }
                break;
            case MODE_SCROLL_ALL_WITH_SCALE:
                float leftScale = 1 - menuScale * scale;
                float rightScale = contentScale + scale * (1 - contentScale);
                ViewHelper.setScaleX(leftLayout, leftScale);
                ViewHelper.setScaleY(leftLayout, leftScale);
                ViewHelper.setAlpha(leftLayout, menuAlpha + (1 - menuAlpha) * (1 - scale));

                ViewHelper.setPivotX(rightLayout, 0);
                ViewHelper.setPivotY(rightLayout, rightLayout.getHeight() / 2);
                ViewHelper.setScaleX(rightLayout, rightScale);
                ViewHelper.setScaleY(rightLayout, rightScale);
                break;
            default:
                break;
        }
        ViewHelper.setAlpha(rightLayout, contentAlpha + (1 - contentAlpha) * (1 - scale));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                float scrollX = ev.getRawX() - downX;
                float scrollY = ev.getRawY() - downY;
                SLog.d("scrollX : " + scrollX + " scrollY : " + scrollY);
                int slideWidth = menuWidth / BASE_SLIDE_BLOCK;
                if (scrollX == 0 || scrollX == menuWidth) {
                    if (isMenuOpen) {
                        smoothScrollTo(0, 0);
                    } else {
                        smoothScrollTo(menuWidth, 0);
                    }
                    downX = 0;
                    downY = 0;
                    return true;
                }
                float slideX = Math.abs(scrollX);
                //右滑
                if (scrollX > 0) {
                    if (slideX >= slideWidth) {
                        smoothScrollTo(0, 0);
                        isMenuOpen = true;
                    } else {
                        if (isMenuOpen) {
                            smoothScrollTo(0, 0);
                        } else {
                            smoothScrollTo(menuWidth, 0);
                        }
                    }
                }
                //左滑
                else if (scrollX < 0) {
                    if (slideX >= slideWidth) {
                        smoothScrollTo(menuWidth, 0);
                        isMenuOpen = false;
                    } else {
                        if (isMenuOpen) {
                            smoothScrollTo(0, 0);
                        } else {
                            smoothScrollTo(menuWidth, 0);
                        }
                    }
                }
                //未滑动
                else {
                    if (!isMenuOpen) {
                        smoothScrollTo(menuWidth, 0);
                    } else {
                        smoothScrollTo(0, 0);
                    }
                }
                downX = 0;
                downY = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (downX == 0) {
                    downX = ev.getRawX();
                }
                if (downY == 0) {
                    downY = ev.getRawY();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isMenuOpen) {
            return;
        }

        smoothScrollTo(0, 0);
        isMenuOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isMenuOpen) {
            return;
        }

        smoothScrollTo(menuWidth, 0);
        isMenuOpen = false;
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}
