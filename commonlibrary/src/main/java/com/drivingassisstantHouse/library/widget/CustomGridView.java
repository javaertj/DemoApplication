package com.drivingassisstantHouse.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 包名：com.drivingassisstantHouse.library.widget
 * 描述：可以在scrollview里正常显示的gridview
 * 创建者：yankebin
 * 日期：2016/3/21
 */
public class CustomGridView extends GridView {
    private Boolean isSetHigh = true;

    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Boolean getIsSetHigh() {
        return isSetHigh;
    }

    public void setIsSetHigh(Boolean isSetHigh) {
        this.isSetHigh = isSetHigh;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSetHigh) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
