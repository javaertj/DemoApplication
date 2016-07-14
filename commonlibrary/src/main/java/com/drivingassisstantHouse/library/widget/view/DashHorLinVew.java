package com.drivingassisstantHouse.library.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 包名: com.drivingassisstantHouse.library.widget.view
 * 描述:
 * 创建者: zhangji-pc
 * 日期: 2016/4/7
 */
public class DashHorLinVew extends View {

    public DashHorLinVew(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);//颜色可以自己设置
        Path path = new Path();
        path.moveTo(0, 0);//起始坐标
        path.lineTo(1000,0);//终点坐标
        PathEffect effects = new DashPathEffect(new float[]{10,10,10,10},10);//设置虚线的间隔和点的长度
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
