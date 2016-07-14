package com.drivingassisstantHouse.library.widget.bannerview;

/**
 * 包名：com.drivingassisstantHouse.library.widget.bannerview
 * 描述：广告banner的位置指示器接口
 * 创建者：yankebin
 * 日期：2016/2/26
 */
public interface FlowIndicator extends ViewFlow.ViewSwitchListener {

    /**
     * Set the current ViewFlow. This method is called by the ViewFlow when the
     * FlowIndicator is attached to it.
     *
     * @param view
     */
    public void setViewFlow(ViewFlow view);

    /**
     * The scroll position has been changed. A FlowIndicator may implement this
     * method to reflect the current position
     *
     * @param h
     * @param v
     * @param oldh
     * @param oldv
     */
    public void onScrolled(int h, int v, int oldh, int oldv);
}