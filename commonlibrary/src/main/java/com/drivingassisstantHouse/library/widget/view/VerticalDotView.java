package com.drivingassisstantHouse.library.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.drivingassisstantHouse.library.R;

/**
 * 包名：com.drivingassisstantHouse.library.widget.view
 * 描述：垂直圆点分割线
 * 创建者：yankebin
 * 日期：2016/4/19
 */
public class VerticalDotView extends ImageView {
    private int circleRadius;
    private int totalHeight;
    private Paint mPaint;
    private Path mPath;

    public VerticalDotView(Context context) {
        this(context, null);
    }

    public VerticalDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circleRadius = getResources().getDimensionPixelSize(R.dimen.dimen_1);

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.sip_gray_dark));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int index = 0;
        do {
            mPath.reset();
            mPath.addCircle((width - circleRadius) / 2, index * 5 * circleRadius+circleRadius, circleRadius, Path.Direction.CCW);
            canvas.drawPath(mPath, mPaint);
            totalHeight =index * 5 * circleRadius+circleRadius;
            index++;
        } while (totalHeight <= height);
    }
}
