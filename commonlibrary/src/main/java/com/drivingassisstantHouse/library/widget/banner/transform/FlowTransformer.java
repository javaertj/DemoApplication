package com.drivingassisstantHouse.library.widget.banner.transform;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class FlowTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        ViewHelper.setRotationY(page, position * -30f);
    }
}
