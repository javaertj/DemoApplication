package com.drivingassisstantHouse.library.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * 自定义字体工具类
 * @author sunji
 * @version 1.0
 *
 */
public class ToolFont {
	
	private final static String TAG = ToolFont.class.getSimpleName();
	
	/**
	 * 从assets目录创建自定义字体样式
	 * @param mContext 上下文
	 * @param fontFileName assets目录下的字体文件名称
	 * @return
	 */
	public static Typeface createTypeface(Context mContext,String fontFileName){
		Typeface type = null;
		try {
			type = Typeface.createFromAsset(mContext.getAssets(), fontFileName);
		} catch (Exception e) {
			ToolLog.e(TAG, "创建字体失败，原因："+e.getMessage());
		}
		return type;
	}
	
	/**
	 * 给传递的目标控件设置自定义字体样式
	 * @param mContext 上下文
	 * @param views 需要设置字体的控件
	 */
	public static void applyFontStyle(Context mContext,Typeface style,TextView... views){
		if(null == views) return;
		
		if(null != views && views.length > 0 ){
			for (TextView view : views) {
				view.setTypeface(style);
			}
		}
	}
}
