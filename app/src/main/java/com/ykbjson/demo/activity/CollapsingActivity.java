package com.ykbjson.demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.drivingassisstantHouse.library.base.BaseActivity;
import com.drivingassisstantHouse.library.data.IntentBean;
import com.ykbjson.demo.R;

/**
 * 包名：com.ykbjson.demo.activity
 * 描述：
 * 创建者：yankebin
 * 日期：2016/8/18
 */

public abstract class CollapsingActivity extends BaseActivity {
    public LinearLayout contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initBaseView();
        super.onCreate(savedInstanceState);
    }

    @Override
    public IntentBean create() {
        return null;
    }

    private void initBaseView() {
        super.setContentView(R.layout.activity_collapsing_base);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null == view.getLayoutParams() ? new ViewGroup.LayoutParams(-1, -1) : view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        contentView.addView(view, params);
    }

}
