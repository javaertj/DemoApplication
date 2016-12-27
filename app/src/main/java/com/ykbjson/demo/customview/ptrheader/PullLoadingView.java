package com.ykbjson.demo.customview.ptrheader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 包名：com.ykbjson.demo.customview.ptrheader
 * 描述：下拉刷新加载视图
 * 创建者：yankebin
 * 日期：2016/11/21
 */

public class PullLoadingView extends ImageView {
    public static final int MODE_INIT = -1;
    public static final int MODE_PULLING = MODE_INIT + 1;
    public static final int MODE_LOADING = MODE_INIT + 2;
    public static final int MODE_LOADING_COMPLETE = MODE_INIT + 3;
    public static final int MODE_ALL_COMPLETE = MODE_INIT + 4;


    private static final int CIRCLE_DEGREE = 360;
    private static final String LOADING_TEXT = "loading...";

    private int delayToCloseHeader = 1000;
    private float maxPullY;
    private float width;
    private float height;
    private Paint arcPaint;
    private Paint textPaint;
    private RectF rectF;
    private int degree;
    private int mode = MODE_INIT;
    private float currentPosition;
    private long invalidDelayTime = 30L;
    private float loadingTextWidth;
    private float line1X;
    private float line1Y;
    private float line2X;
    private float line2Y;
    private float radius;
    private float center;
    private float checkStartX;
    private float step;
    private float lineThick;
    private boolean isNeedDrawSecondLine;
    private OnRefreshCompleteCallback completeCallback;
    private final Canvas mCanvas;//当布局本身或父布局被隐藏后，不会回调onDraw方法，此时手动传入此参数强制驱动onDraw方法执行，保证绘画不被阻断

    public interface OnRefreshCompleteCallback {
        void onRefreshComplete();
    }

    public PullLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLoadingView(Context context) {
        this(context, null);
    }

    public PullLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCanvas = new Canvas();
        final float density = getResources().getDisplayMetrics().density;
        step = density * 2;
        lineThick = density * 2;

        arcPaint = new Paint();
        arcPaint.setColor(Color.RED);
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(lineThick);
        arcPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(6 * density);

        loadingTextWidth = textPaint.measureText(LOADING_TEXT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        center = width / 2;
        radius = width / 2 - lineThick;
        checkStartX = center - width / 5;
        rectF = new RectF(lineThick, lineThick, width - lineThick, height - lineThick);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //打钩
        if (mode == MODE_ALL_COMPLETE) {
            //画圆
            canvas.drawArc(rectF, 0, CIRCLE_DEGREE, false, arcPaint);
            if (line1X < radius / 3) {
                line1X += step;
                line1Y += step;
            }
            //画第一根线
            canvas.drawLine(checkStartX, center, checkStartX + line1X, center + line1Y, arcPaint);

            if (line1X >= radius / 3) {

                if (!isNeedDrawSecondLine) {
                    line2X = line1X;
                    line2Y = line1Y;
                    isNeedDrawSecondLine = true;
                }
                line2X += step;
                line2Y -= step;
                //画第二根线
                canvas.drawLine(checkStartX + line1X - lineThick / 2,
                        center + line1Y, checkStartX + line2X, center + line2Y, arcPaint);
            }
            if (line2X > radius) {
                line1X = line1Y = line2X = line2Y = 0;
                isNeedDrawSecondLine = false;
                refreshComplete();
                return;
            }
            uiChange();
        }
        //画圆
        else if (mode == MODE_LOADING_COMPLETE) {
            //画圆弧
            canvas.drawArc(rectF, -CIRCLE_DEGREE / 4, degree, false, arcPaint);
            //画圆和勾的过程做完后停止刷新界面
            if (degree == CIRCLE_DEGREE) {
                setMode(MODE_ALL_COMPLETE);
                return;
            }
            //继续画圆
            checkDegree();
            uiChange();
        }
        //渐进闭合圆
        else if (mode == MODE_PULLING) {
            float sweepAngle = currentPosition / maxPullY * CIRCLE_DEGREE;
            canvas.drawArc(rectF, -CIRCLE_DEGREE / 4, sweepAngle, false, arcPaint);
            canvas.drawArc(rectF, sweepAngle - (CIRCLE_DEGREE / 4 - 5), 5, false, arcPaint);
        }
        //旋转圆弧
        else if (mode == MODE_LOADING) {
            canvas.drawText(LOADING_TEXT, (width - loadingTextWidth) / 2, height / 2, textPaint);
            canvas.drawArc(rectF, degree, CIRCLE_DEGREE / 8, false, arcPaint);
            canvas.drawArc(rectF, degree + CIRCLE_DEGREE / 8 + 2, 5, false, arcPaint);
            degree += 10;
            if (degree == CIRCLE_DEGREE) {
                degree = 0;
            }
            uiChange();
        }
        //初始化
        else {
            super.draw(canvas);
        }
    }

    public void setCompleteCallback(OnRefreshCompleteCallback completeCallback) {
        this.completeCallback = completeCallback;
    }

    public void setDelayToCloseHeader(int delayToCloseHeader) {
        this.delayToCloseHeader = delayToCloseHeader;
    }

    public void setMaxPullY(float maxPullY) {
        this.maxPullY = maxPullY;
    }

    /**
     * 设置动画的颜色，包括文字和圆圈以及打钩的线
     *
     * @param loadingColor
     */
    public void setLoadingColor(int loadingColor) {
        arcPaint.setColor(loadingColor);
        textPaint.setColor(loadingColor);
    }

    public void setStep(float step) {
        this.step = step;
    }

    /**
     * 设置当前模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        degree = 0;
        if (mode == MODE_PULLING) {
            this.mode = mode;
            dispatchInvalidate();
        } else {
            if (this.mode != mode) {
                this.mode = mode;
                if (mode == MODE_LOADING_COMPLETE) {
                    invalidDelayTime = 50L;
                } else if (mode == MODE_LOADING) {
                    invalidDelayTime = 15L;
                } else if (mode == MODE_ALL_COMPLETE) {
                    invalidDelayTime = 5L;
                } else {
                    invalidDelayTime = 30L;
                }
                uiChange();
            }
        }
    }

    /**
     * 检测角度
     */
    private void checkDegree() {
        if (degree < CIRCLE_DEGREE) {
            degree += 45;
            if (degree > CIRCLE_DEGREE) {
                degree = CIRCLE_DEGREE;
            }
        }
    }

    /**
     * ui变化
     */
    private synchronized void uiChange() {
        removeCallbacks(invalidateRunner);
        postDelayed(invalidateRunner, invalidDelayTime);
    }

    /**
     * 刷新完成
     */
    private synchronized void refreshComplete() {
        removeCallbacks(closeHeaderRunner);
        postDelayed(closeHeaderRunner, delayToCloseHeader);
    }


    /**
     * 下拉距离变化
     *
     * @param currentPos
     */
    public void onUIPositionChange(int currentPos) {
        currentPosition = currentPos;
        setMode(MODE_PULLING);
    }

    /**
     * 根据view的可见性决定调postInvalidate方法或传入空canvas去驱动onDraw方法继续执行，保证绘画不被阻断
     */
    private void dispatchInvalidate() {
        if (isShown()) {
            postInvalidate();
        } else {
            draw(mCanvas);
        }
    }

    private Runnable invalidateRunner = new Runnable() {
        @Override
        public void run() {
            dispatchInvalidate();
        }
    };

    private Runnable closeHeaderRunner = new Runnable() {
        @Override
        public void run() {
            if (null != completeCallback) {
                completeCallback.onRefreshComplete();
            }
        }
    };
}
