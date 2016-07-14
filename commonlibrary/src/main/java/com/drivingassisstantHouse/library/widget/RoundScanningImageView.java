package com.drivingassisstantHouse.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.drivingassisstantHouse.library.R;

import java.lang.ref.WeakReference;

/**
 * 包名：com.drivingassisstantHouse.library.widget
 * 描述：可裁剪为圆形和自带扫描线的<var>ImageView</var>
 * 创建者：yankebin
 * 日期：2015/12/15
 */
public class RoundScanningImageView extends ImageView {
    private Paint mPaint;
    private int mHeight = 0;
    private Path mPath;
    private float centerX;
    private float centerY;

    private float moveSpeed;
    private float outStrokeWidth;
    private int outStrokeColor;
    private int outStrokeAlpha;
    private boolean enableClipPathRound;
    private boolean enableScan;
    private float strokeWidth;
    private float scanLineHeight;
    private int invalidateTime;
    private PorterDuffXfermode porterDuffXfermode;
    private Drawable mLastDrawable;
    private WeakReference<Bitmap> mTempBitmap;
    private WeakReference<Bitmap> mLastBitmap;


    public RoundScanningImageView(Context context) {
        this(context, null);
    }

    public RoundScanningImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundScanningImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.RoundScanningImageView);
        moveSpeed = typedArray.getFloat(R.styleable.RoundScanningImageView_scan_speed, 10);
        outStrokeWidth = typedArray.getFloat(R.styleable.RoundScanningImageView_out_stroke_width, 5);
        outStrokeColor = typedArray.getColor(R.styleable.RoundScanningImageView_out_stroke_color, Color.LTGRAY);
        outStrokeAlpha = typedArray.getInt(R.styleable.RoundScanningImageView_out_stroke_alpha, 100);
        enableClipPathRound = typedArray.getBoolean(R.styleable.RoundScanningImageView_enable_clipPath_round, true);
        enableScan = typedArray.getBoolean(R.styleable.RoundScanningImageView_enable_scan, true);
        strokeWidth = typedArray.getFloat(R.styleable.RoundScanningImageView_stroke_width, 5);
        scanLineHeight = typedArray.getFloat(R.styleable.RoundScanningImageView_scan_line_height, 50);
        invalidateTime = typedArray.getInt(R.styleable.RoundScanningImageView_scan_invalidate_time, 50);
        typedArray.recycle();

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mPath = new Path();
        mPaint = new Paint();
    }

    /**
     * 创建渲染器
     *
     * @return
     */
    private LinearGradient buildLinearGradient() {
        LinearGradient linearGradient = new LinearGradient(0, mHeight - scanLineHeight, 0, mHeight, new int[]{Color.TRANSPARENT, Color.WHITE}, null, Shader.TileMode.CLAMP);
        return linearGradient;
    }

    /**
     * 画笔重置
     *
     * @param color
     * @param alpha
     */
    private void resetPaint(int color, int alpha) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(color);
        mPaint.setAlpha(alpha);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    /**
     * 获取裁剪后的圆形图片
     *
     * @param bmp
     * @param radius
     * @return
     */
    private Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth, squareHeight;
        int x, y;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        return output;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!enableClipPathRound && !enableScan) {
            super.onDraw(canvas);
        } else {
            if (enableClipPathRound) {
                int radius = getWidth() > getHeight() ? getHeight() / 2 : getWidth() / 2;
                radius -= outStrokeWidth / 2;
                //绘制圆边
                resetPaint(outStrokeColor, outStrokeAlpha);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(outStrokeWidth);
                canvas.drawCircle(centerX, centerY, radius, mPaint);
                //处理图片
                Drawable drawable = getDrawable();
                if (null != drawable) {

                    boolean isSameImg = true;
                    if (null != mLastDrawable) {
                        if (drawable.getConstantState() != mLastDrawable.getConstantState()) {
                            isSameImg = false;
                            mLastDrawable = drawable;
                        }
                    } else {
                        isSameImg = false;
                        mLastDrawable = drawable;
                    }

                    Bitmap roundBitmap = null;
                    if (isSameImg) {
                        roundBitmap = null == mLastBitmap || null == mLastBitmap.get() ? null : mLastBitmap.get();
                    }

                    if (null == roundBitmap) {
                        Bitmap mBitmap = null;
                        //防止引用上一次的缓存原图
                        if(isSameImg){
                            mBitmap = null == mTempBitmap || null == mTempBitmap.get() ? null : mTempBitmap.get();
                        }

                        if (null == mBitmap) {
                            mBitmap = ((BitmapDrawable) mLastDrawable).getBitmap();
                            if (null != mBitmap) {
                                mTempBitmap = new WeakReference<>(mBitmap);
                            }
                        }

                        if (null != mBitmap) {
                            radius -= strokeWidth;
                            //裁剪图片为圆形
                            roundBitmap = getCroppedRoundBitmap(mBitmap, radius);
                            if (null != roundBitmap) {
                                mLastBitmap = new WeakReference<>(roundBitmap);
                            }
                        }
                    } else {
                        radius -= strokeWidth;
                    }
                    if (null != roundBitmap) {
                        canvas.drawBitmap(roundBitmap, centerX - radius, centerY - radius, null);
                    }
                }
                //裁剪画布
                mPath.reset();
                canvas.clipPath(mPath); // makes the clip empty
                mPath.addCircle(centerX, centerY, centerX - strokeWidth - outStrokeWidth / 2, Path.Direction.CCW);
                canvas.clipPath(mPath, Region.Op.REPLACE);
            } else {
                super.onDraw(canvas);
            }
            if (enableScan) {
                //移动扫描白线的位置
                mHeight += moveSpeed;
                //绘制扫描线
                resetPaint(Color.TRANSPARENT, 255);
                mPaint.setXfermode(porterDuffXfermode);
                mPaint.setShader(buildLinearGradient());
                canvas.drawRect(0, mHeight - scanLineHeight, getWidth(), mHeight, mPaint);

                if (mHeight >= getHeight()) {
                    mHeight = 0;
                }
                postInvalidateDelayed(invalidateTime);
            }
        }
    }
}
