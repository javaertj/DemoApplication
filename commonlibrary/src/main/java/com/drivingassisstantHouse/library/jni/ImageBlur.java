package com.drivingassisstantHouse.library.jni;

import android.graphics.Bitmap;

/**
 * 高斯模糊JNI实现
 * Created by Qiujuer
 * on 2014/4/19.
 * 
 * https://github.com/qiujuer/ImageBlurring
 */
public class ImageBlur {
    
	static {
        System.loadLibrary("ImageBlur");
    }
    
    public static native void blurIntArray(int[] pImg, int w, int h, int r);

    public static native void blurBitMap(Bitmap bitmap, int r);
}
