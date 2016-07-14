package com.ykbjson.demo.customview.slide;

import android.view.View;

/**
 * 包名：com.drivingassisstantHouse.library.widget
 * 描述：侧滑菜单接口
 * 创建者：yankebin
 * 日期：2015/12/14
 */
public interface SlideBase
{
    /**
     * 主布局id
     */
    int MAIN_CONTAINER_ID=Integer.MAX_VALUE;
    /**
     * menu布局id
     */
    int MENU_CONTAINER_ID=MAIN_CONTAINER_ID-1;
    /**
     *content布局id
     */
    int CONTENT_CONTAINER_ID=MAIN_CONTAINER_ID-2;
    /**
     * 背景布局id
     */
    int BACKGROUND_CONTAINER_ID=MAIN_CONTAINER_ID-3;
    /**
     * 滑动状态的一种，表示无滑动。
     */
    int DO_NOTHING=1;
    /**
     * 滑动状态的一种，表示正在滑出左侧菜单。
     */
    int SHOW_MENU = DO_NOTHING+1;

    /**
     * 滑动状态的一种，表示正在隐藏左侧菜单。
     */
    int HIDE_MENU = DO_NOTHING+2;
    /**
     * 菜单效果的一种，菜单和content一起正常滑动。
     */
    int  MODE_NORMAL=1;
    /**
     * 菜单效果的一种，表示只有content滑动（content覆盖menu）。
     */
    int MODE_CONTENT_SCROLL_ONLY=MODE_NORMAL+1;
    /**
     * 菜单效果的一种，表示菜单和content一起缩放和滑动。
     */
    int MODE_SCROLL_ALL_WITH_SCALE =MODE_NORMAL+2;
    /**
     * 初始化子视图容器
     */
    void initContainer();

    /**
     * 创建一个子视图容器
     * @param id 容器id
     * @return
     */
    View createContainer(int id);

    /**
     * 设置菜单
     * @param layoutId
     */
    void setMenu(int layoutId);
    /**
     * 设置菜单
     * @param menuView
     */
    void setMenu(View menuView);

    /**
     * 设置content
     * @param layoutId
     */
    void setContent(int layoutId);

    /**
     * 设置content
     * @param contentView
     */
    void setContent(View contentView);

    /**
     * 添加视图
     * @param view
     * @param id
     */
    void addChildView(View view, int id);

    /**
     * 初始化数据
     */
    void onLayoutInit();

}
