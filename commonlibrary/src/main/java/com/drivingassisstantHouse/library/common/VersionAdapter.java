package com.drivingassisstantHouse.library.common;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.drivingassisstantHouse.library.R;

/**
 * 包名: com.drivingassisstantHouse.library.common
 * 描述:  版本屏幕适配
 * 创建者: zhangji-pc
 * 日期: 2016/4/21
 */
public class VersionAdapter {
    public static void fitScreen(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
