package com.drivingassisstantHouse.library.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.drivingassisstantHouse.library.config.SysEnv;
import com.drivingassisstantHouse.library.jni.ImageBlur;


/**
 * 图片工具类-截图，高斯模糊图片；二维码图片生成，验证码图片生成，图片缩放等
 * @author sunji
 * @version 1.0
 */
public class ToolPicture {
	
	/**
	 * 高斯模糊效果JNI实现
	 * @param sentBitmap
	 * @param radius
	 * @param canReuseInBitmap
	 * @return
	 */
	public static Bitmap doBlurJniArray(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        //Jni Pixels
        ImageBlur.blurIntArray(pix, w, h, radius);

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

	/**
	 * 高斯模糊效果JNI实现
	 * @param sentBitmap
	 * @param radius
	 * @param canReuseInBitmap
	 * @return
	 */
    public static Bitmap doBlurJniBitMap(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }
        //Jni BitMap
        ImageBlur.blurBitMap(bitmap, radius);

        return (bitmap);
    }

    /**
     * Java代码实现高斯模糊效果，效率差
     * @param sentBitmap
     * @param radius
     * @param canReuseInBitmap
     * @return
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
	
	public static Bitmap takeScreenShotQQ(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题栏
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		if(b1!=null&&!b1.isRecycled()){
			b1.recycle();
		}
		view.destroyDrawingCache();
		Bitmap bm = scaleBitmap2DefinedSize(b);
		if(b!=null&&!b.isRecycled()){
			b.recycle();
		}
		return bm;
	}
	
	private static Bitmap scaleBitmap2DefinedSize(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		int minLen = getMinlen(bitmap.getWidth(), bitmap.getHeight());
		float fScale = (float) SysEnv.SCREEN_WIDTH / (float) minLen;
		matrix.postScale(fScale, fScale);
		final Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return scaledBitmap;
	}
	
	private static int getMinlen(int width, int height) {
		if (width < height) {
			return width;
		} else {
			return height;
		}
	}
	
	/**
	 * 截取应用程序界面（去除状态栏）
	 * @param activity 界面Activity
	 * @return Bitmap对象
	 */
	public static Bitmap takeScreenShot(Activity activity){  
        View view =activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap bitmap = view.getDrawingCache();  
        Rect rect = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);  
        int statusBarHeight = rect.top;  
          
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap,0,statusBarHeight, SysEnv.SCREEN_WIDTH, SysEnv.SCREEN_HEIGHT - statusBarHeight);  
        view.destroyDrawingCache();  
        return bitmap2;  
    }
	
	/**
	 * 截取应用程序界面
	 * @param activity 界面Activity
	 * @return Bitmap对象
	 */
	public static Bitmap takeFullScreenShot(Activity activity){  
		
		activity.getWindow().getDecorView().setDrawingCacheEnabled(true);

		Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
		
		View view = activity.getWindow().getDecorView();
		Bitmap bmp2 = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
		//view.draw(new Canvas(bmp2));
		//bmp就是截取的图片了，可通过bmp.compress(CompressFormat.PNG, 100, new FileOutputStream(file));把图片保存为文件。
		
		//1、得到状态栏高度
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;
		System.out.println("状态栏高度：" + statusBarHeight);
		
		//2、得到标题栏高度
		int wintop = activity.getWindow().findViewById(android.R.id.content).getTop();
		int titleBarHeight = wintop - statusBarHeight;
		System.out.println("标题栏高度:" + titleBarHeight);
		
//		//把两个bitmap合到一起
//		Bitmap bmpall = Biatmap.createBitmap(width,height,Config.ARGB_8888);
//		Canvas canvas=new Canvas(bmpall);
//		canvas.drawBitmap(bmp1,x,y,paint);
//		canvas.drawBitmap(bmp2,x,y,paint);
		
        return bmp;  
    }
	
	/**
	 * 截取View内容，返回bitmap
	 * @param mView 需要截屏的目标View
	 * @return
	 */
	public static Bitmap takeViewScreenShot(View mView){
		Bitmap result = null;
        try {
        	mView.setDrawingCacheEnabled(true);
        	result = mView.getDrawingCache();
            mView.setDrawingCacheEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
	}
	
	/**
	 * 截取指定View内容，保存到指定的文件目录
	 * @param mView 截屏目标View
	 * @param filePath jpg文件路径+文件名
	 * @return
	 */
	public static File takeViewScreenShot(View mView,String filePath){
        return takeViewScreenShot(mView,filePath,80);
	}
	
	/**
	 * 截取指定View内容，保存到指定的文件目录
	 * @param mView 截屏目标View
	 * @param filePath jpg文件路径+文件名
	 * @param quality 0-100压缩率
	 * @return
	 */
	public static File takeViewScreenShot(View mView,String filePath,int quality){
		File myCaptureFile = new File(filePath);
		mView.setDrawingCacheEnabled(true);
        try {
            
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            mView.getDrawingCache()
                    .compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mView.setDrawingCacheEnabled(false);
        return myCaptureFile;
	}
	
	/**
	 * 获取View内容，转成bitmap
	 * @param v 目标View
	 * @return
	 */
    public static Bitmap loadBitmapFromView(View v) {  
        if (v == null) {  
            return null;  
        }  
        Bitmap screenshot;  
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);  
        c.translate(-v.getScrollX(), -v.getScrollY());  
        v.draw(c);  
        return screenshot;  
    }  
	
	/**
	 * 将View转成Bitmap
	 * @param view 目标View
	 * @return
	 */
	public static Bitmap gainViewBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}
	
	/**
	 * 根据指定内容生成自定义宽高的二维码图片 
	 * @param content 需要生成二维码的内容
	 * @param width 二维码宽度
	 * @param height 二维码高度
	 * @throws WriterException 生成二维码异常
	 */
	public static Bitmap makeQRImage(String content, int width, int height)
			throws WriterException {
		// 判断生成二维码数据合法性
		if (!ToolString.isNoBlankAndNoNull(content))
			return null;

		//二维码参数设置
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		//容错率L=7% M=15% Q=25% H=30% 容错率越高，越容易被快速扫描
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 1);
		
		// 图像数据转换，使用了矩阵转换
		BitMatrix bitMatrix = new QRCodeWriter().encode(content,
				BarcodeFormat.QR_CODE, width, height, hints);
		int[] pixels = new int[width * height];
		// 按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (bitMatrix.get(x, y))
					pixels[y * width + x] = 0xff000000;
				else
					pixels[y * width + x] = 0xffffffff;
			}
		}
		// 生成二维码图片的格式，使用ARGB_8888
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		
		return bitmap;
	}
	
	/**
	 * 生成带Logo的二维码
	 * @param strContent 二维码内容
	 * @param qrWH 二维码的宽高
	 * @param mLogoBitmap Logo图片
	 * @param logoWH LOGO的宽高(注意：LOGO的宽高最多只能是二维码图片宽高的0.2，否则生成的二维码无法扫描识别)
	 * @return
	 * @throws WriterException 生成二维码失败异常
	 */
	public static Bitmap makeQRImageWithLogo(String strContent,int qrWH,Bitmap mLogoBitmap,int logoWH) throws WriterException {

		// 判断生成二维码数据合法性
		if (!ToolString.isNoBlankAndNoNull(strContent))
			return null;
		
		//缩放Logo图片
		Matrix logoMatrix = new Matrix();
		float sx = (float) 2 * logoWH / mLogoBitmap.getWidth();
		float sy = (float) 2 * logoWH / mLogoBitmap.getHeight();
		logoMatrix.setScale(sx, sy);
		mLogoBitmap = Bitmap.createBitmap(mLogoBitmap, 0, 0,mLogoBitmap.getWidth(), mLogoBitmap.getHeight(), logoMatrix,false);
		
		//二维码参数设置
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		//容错率L=7% M=15% Q=25% H=30% 容错率越高，越容易被快速扫描
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 1);

		QRCodeWriter write = new QRCodeWriter();
		try {
			Field menuKeyField = QRCodeWriter.class.getDeclaredField("QUIET_ZONE_SIZE");
			if(menuKeyField != null) {
			    menuKeyField.setAccessible(true);
			    menuKeyField.setInt(write,1);//控制的边框的宽度
			}
		} catch (Exception e) {
			Log.e("cretaeBitmap", "反射设置QRCodeWriter.QUIET_ZONE_SIZE失败，原因："+e.getMessage());
		}
		BitMatrix matrix = write.encode(strContent,BarcodeFormat.QR_CODE,qrWH, qrWH,hints);
//		BitMatrix matrix = new MultiFormatWriter().encode(strContent,BarcodeFormat.QR_CODE, qrWH, qrWH, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - logoWH && x < halfW + logoWH && y > halfH - logoWH && y < halfH + logoWH) {
					pixels[y * width + x] = mLogoBitmap.getPixel(x - halfW + logoWH, y - halfH + logoWH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else { 
						//无信息设置像素点为白色
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path 图片绝对路径
	 * @return degree 旋转的角度
	 * @throws IOException
	 */
	public static int gainPictureDegree(String path) throws Exception {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (Exception e) {
			throw(e);
		}
		
		return degree;
	}
	
    /**
     * 旋转图片 
     * @param angle 角度
     * @param bitmap 源bitmap
     * @return Bitmap 旋转角度之后的bitmap
     */  
    public static Bitmap rotaingBitmap(int angle,Bitmap bitmap) {  
        //旋转图片 动作   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
        //重新构建Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix, true);  
        return resizedBitmap;  
    }
    
    /**
     * Drawable转成Bitmap 
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                    : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
    
    /**
     * 从资源文件中获取图片
     * @param context 上下文
     * @param drawableId 资源文件id
     * @return
     */
    public static Bitmap gainBitmap(Context context,int drawableId){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
        return bmp;
    }
    
    /**
     * 灰白图片（去色）
     * @param bitmap 需要灰度的图片
     * @return 去色之后的图片
     */
    public static Bitmap toBlack(Bitmap bitmap) {
        Bitmap resultBMP = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Config.RGB_565);
        Canvas c = new Canvas(resultBMP);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return resultBMP;
    }
    
    /**
     * 将bitmap转成 byte数组
     * 
     * @param bitmap
     * @return
     */
    public static byte[] toBtyeArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    
    /**
     * 将byte数组转成 bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    
    /**
     * 将Bitmap转换成指定大小
     * 
     * @param bitmap 需要改变大小的图片
     * @param width 宽
     * @param height 高
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    
    
    /**
     * 在图片右下角添加水印
     * @param srcBMP 原图
     * @param markBMP 水印图片
     * @return 合成水印后的图片
     */
    public static Bitmap composeWatermark(Bitmap srcBMP, Bitmap markBMP) {
        if (srcBMP == null) {
            return null;
        }
        
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(srcBMP.getWidth(), srcBMP.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // 在 0，0坐标开始画入原图
        cv.drawBitmap(srcBMP, 0, 0, null);
        // 在原图的右下角画入水印
        cv.drawBitmap(markBMP, srcBMP.getWidth() - markBMP.getWidth() + 5, srcBMP.getHeight() - markBMP.getHeight() + 5, null);
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();
        
        return newb;
    }
    
    /**
     * 将图片转成指定弧度（角度）的图片
     * 
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        //根据图片创建画布
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    
	/**
	 * 缩放图片
	 * 
	 * @param bmp 需要缩放的图片源
	 * @param newW 需要缩放成的图片宽度
	 * @param newH 需要缩放成的图片高度
	 * @return 缩放后的图片
	 */
	public static Bitmap zoom(Bitmap bmp, int newW, int newH) {
		
		// 获得图片的宽高
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		// 计算缩放比例
		float scaleWidth = ((float) newW) / width;
		float scaleHeight = ((float) newH) / height;
		
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
		
		return newbm;
	}
    
	/**
	 * 获得倒影的图片
	 * @param bitmap 原始图片
	 * @return 带倒影的图片
	 */
	public static Bitmap makeReflectionImage(Bitmap bitmap){  
        final int reflectionGap = 4;  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
          
        Matrix matrix = new Matrix();  
        matrix.preScale(1, -1);  
        
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height/2, width, height/2, matrix, false);  
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);  
          
        Paint deafalutPaint = new Paint();  
        Canvas canvas = new Canvas(bitmapWithReflection);  
        canvas.drawBitmap(bitmap, 0, 0, null);  
        canvas.drawRect(0, height,width,height + reflectionGap, deafalutPaint);  
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);  
          
        Paint paint = new Paint();  
        LinearGradient shader = new LinearGradient(0,bitmap.getHeight(),0,bitmapWithReflection.getHeight()+reflectionGap,0x70ffffff,0x00ffffff,TileMode.CLAMP);  
        paint.setShader(shader);  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
        canvas.drawRect(0,height,width,bitmapWithReflection.getHeight()+reflectionGap,paint);  
   
        return bitmapWithReflection;  
    }  
	
	/**
	 * 获取验证码图片
	 * @param width 验证码宽度
	 * @param height 验证码高度
	 * @return 验证码Bitmap对象
	 */
	public synchronized static Bitmap makeValidateCode(int width, int height){
		return ValidateCodeGenerator.createBitmap(width, height);
	}
	
	/**
	 * 获取验证码值
	 * @return 验证码字符串
	 */
	public synchronized static String gainValidateCodeValue(){
		return ValidateCodeGenerator.getCode();
	}
    
    /**
     * 随机生成验证码内部类
     *
     */
    final static class ValidateCodeGenerator{
    	private static final char[] CHARS = {
    		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
    		'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
    		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    	};
    	
    	//default settings
    	private static final int DEFAULT_CODE_LENGTH = 4;
    	private static final int DEFAULT_FONT_SIZE = 20;
    	private static final int DEFAULT_LINE_NUMBER = 3;
    	private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
    	private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = 30;
    	
    	//variables
    	private static String value;
    	private static int padding_left, padding_top;
    	private static Random random = new Random();
    	
    	public static Bitmap createBitmap(int width,int height) {
    		padding_left = 0;
    		//创建画布
    		Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
    		Canvas c = new Canvas(bp);

    		//随机生成验证码字符
    		StringBuilder buffer = new StringBuilder();
    		for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
    			buffer.append(CHARS[random.nextInt(CHARS.length)]);
    		}
    		value = buffer.toString();
    		
    		//设置颜色
    		c.drawColor(Color.WHITE);
    		
    		//设置画笔大小
    		Paint paint = new Paint();
    		paint.setTextSize(DEFAULT_FONT_SIZE);
    		for (int i = 0; i < value.length(); i++) {
    			//随机样式
    			randomTextStyle(paint);
        		padding_left += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
        		padding_top = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
    			c.drawText(value.charAt(i) + "", padding_left, padding_top, paint);
    		}
    		for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
    			drawLine(c, paint);
    		}
    		//保存  
    		c.save(Canvas.ALL_SAVE_FLAG);
    		c.restore();
    		
    		return bp;
    	}
    	
    	public static String getCode() {
    		return value;
    	}
    	
    	private static void randomTextStyle(Paint paint) {
    		int color = randomColor(1);
    		paint.setColor(color);
    		paint.setFakeBoldText(random.nextBoolean());//true为粗体，false为非粗体
    		float skewX = random.nextInt(11) / 10;
    		skewX = random.nextBoolean() ? skewX : -skewX;
    		paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
    		paint.setUnderlineText(true); //true为下划线，false为非下划线
    		paint.setStrikeThruText(true); //true为删除线，false为非删除线
    	}
    	
    	private static void drawLine(Canvas canvas, Paint paint) {
    		int color = randomColor(1);
    		int startX = random.nextInt(DEFAULT_WIDTH);
    		int startY = random.nextInt(DEFAULT_HEIGHT);
    		int stopX = random.nextInt(DEFAULT_WIDTH);
    		int stopY = random.nextInt(DEFAULT_HEIGHT);
    		paint.setStrokeWidth(1);
    		paint.setColor(color);
    		canvas.drawLine(startX, startY, stopX, stopY, paint);
    	}
    	
    	private static int randomColor(int rate) {
    		int red = random.nextInt(256) / rate;
    		int green = random.nextInt(256) / rate;
    		int blue = random.nextInt(256) / rate;
    		return Color.rgb(red, green, blue);
    	}
    }
}
