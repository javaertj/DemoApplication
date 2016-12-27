package com.ykbjson.demo.customview.toolbar;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.drivingassisstantHouse.library.widget.scrollview.ObservableScrollView;
import com.ykbjson.demo.R;

/**
 * 包名：com.ykbjson.demo.customview.toolbar
 * 描述：
 * 创建者：yankebin
 * 日期：2016/8/18
 */

public class CollapsingLayout extends FrameLayout implements ObservableScrollView.ScrollViewListener {

    public interface OnCollapsingCallback {
        public void onCollapsing(float coefficient);
    }

    private ObservableScrollView scrollContentParent;

    private LinearLayout scrollContent;

    private LinearLayout collapsingLayout;

    private LinearLayout headerLayout;

    private ImageView fillingStatusView;

    private OnCollapsingCallback collapsingCallback;

    public CollapsingLayout(Context context) {
        this(context, null);
    }

    public CollapsingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        scrollContentParent = new ObservableScrollView(context);
        scrollContent = new LinearLayout(context);
        scrollContent.setOrientation(LinearLayout.VERTICAL);
        headerLayout = new LinearLayout(context);
        headerLayout.setOrientation(LinearLayout.VERTICAL);
        collapsingLayout = new LinearLayout(context);
        collapsingLayout.setOrientation(LinearLayout.VERTICAL);


        fillingStatusView = new ImageView(context);
        fillingStatusView.setImageResource(R.color.sip_gray_dark);
        fillingStatusView.setScaleType(ImageView.ScaleType.FIT_XY);
        fillingStatusView.setAlpha(0.5f);


        addView(scrollContentParent, new FrameLayout.LayoutParams(-1, -1));
        scrollContentParent.addView(scrollContent, new ViewGroup.LayoutParams(-1, -1));
        scrollContent.addView(collapsingLayout, new LinearLayout.LayoutParams(-1, -2));
        headerLayout.addView(fillingStatusView, new LinearLayout.LayoutParams(-1, getStatusBarHeight()));
        addView(headerLayout, new LinearLayout.LayoutParams(-1, -2));

        scrollContentParent.setScrollViewListener(this);
    }

    public void setCollapsingCallback(OnCollapsingCallback collapsingCallback) {
        this.collapsingCallback = collapsingCallback;
    }

    /**
     * 初始化header视图
     *
     * @param layoutResId header视图布局id
     */
    public void setUpHeaderView(@LayoutRes int layoutResId) {
        LayoutInflater.from(getContext()).inflate(layoutResId, headerLayout, true);
    }

    /**
     * 初始化要折叠的视图
     *
     * @param layoutResId 折叠视图布局id
     */
    public void setUpCollapsingView(@LayoutRes int layoutResId) {
        LayoutInflater.from(getContext()).inflate(layoutResId, collapsingLayout, true);
    }

    /**
     * 初始化内容视图
     *
     * @param layoutResId 内容视图布局id
     */
    public void setUpContentView(@LayoutRes int layoutResId) {
        LayoutInflater.from(getContext()).inflate(layoutResId, scrollContent, true);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color 颜色值
     */
    public void setStatusBarColor(@ColorRes int color) {
        fillingStatusView.setImageResource(color);
    }

    /**
     * 设置状态栏资源
     *
     * @param resId 资源id
     */
    public void setStatusBarDrawable(@DrawableRes int resId) {
        fillingStatusView.setImageResource(resId);
    }

    /**
     * 根布局直接子布局，内容容器
     *
     * @return 内容容器
     */
    public LinearLayout getScrollContent() {
        return scrollContent;
    }

    /**
     * header布局
     *
     * @return header布局
     */
    public LinearLayout getHeaderView() {
        return headerLayout;
    }

    /**
     * 折叠布局
     *
     * @return 折叠布局
     */
    public LinearLayout getCollapsingLayout() {
        return collapsingLayout;
    }

    /**
     * statusbar的替代view
     *
     * @return statusbar的替代view
     */
    public ImageView getFillingStatusView() {
        return fillingStatusView;
    }

    public void setHeaderLayoutBgColor(@ColorRes int color) {
        headerLayout.setBackgroundResource(color);
    }

    public void setHeaderLayoutBgDrawable(@DrawableRes int drawable) {
        headerLayout.setBackgroundResource(drawable);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Resources res = Resources.getSystem();
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return res.getDimensionPixelSize(resId);
        }
        return 0;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        float alpha = Math.min(y * 1.0f / headerLayout.getMeasuredHeight() / 2, 1);
        if (null != collapsingCallback) {
            collapsingCallback.onCollapsing(alpha);
        }
    }
}
