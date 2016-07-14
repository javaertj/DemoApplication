package com.drivingassisstantHouse.library.widget.floatwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drivingassisstantHouse.library.R;
import com.drivingassisstantHouse.library.tools.ToolImage;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 包名：com.drivingassisstantHouse.library.widget.floatwindow
 * 描述：广告悬浮视图容器
 * 创建者：yankebin
 * 日期：2016/2/29
 */
public class FloatView extends RelativeLayout {
    private WindowManager windowManager;
    public FloatView(Context context) {
        this(context, null);
    }

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutParams(new LayoutParams(-1, -1));
        LayoutInflater.from(getContext()).inflate(R.layout.layout_float_view_container, this, true);
    }


    public void show(String url, WindowManager manager, WindowManager.LayoutParams params) {
        windowManager = manager;
        show(url);
        windowManager.addView(this, params);
    }

    public void show(String url) {
        ImageView advert = (ImageView) findViewById(R.id.iv_advert);
        ImageLoader.getInstance().displayImage(url, advert, ToolImage.getRoundedOptions(R.drawable.empty_photo,15));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            windowManager.removeView(FloatView.this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
