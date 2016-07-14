package com.drivingassisstantHouse.library.tools;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.drivingassisstantHouse.library.config.SysEnv;

import java.text.DecimalFormat;


/**
 * 单位换算工具类
 * @author sunji<br>
 * 	px  ：像素 <br>
	in  ：英寸<br>
	mm  ：毫米<br>
	pt  ：磅，1/72 英寸<br>
	dp  ：一个基于density的抽象单位，如果一个160dpi的屏幕，1dp=1px<br>
	dip ：等同于dp<br>
	sp  ：同dp相似，但还会根据用户的字体大小偏好来缩放。<br>
	建议使用sp作为文本的单位，其它用dip<br>
	布局时尽量使用单位dip，少使用px <br>
 */
public class ToolUnit {

	private static DecimalFormat decimalFormat=new DecimalFormat();

	/**
	 * 截取小数
	 * @param source
	 * @param format
	 * @return
	 */
	public static String decimalFormat(float source,String format){
		decimalFormat.applyPattern(format);
		return decimalFormat.format(source);
	}
	
	/**
	 * 获取当前分辨率下指定单位对应的像素大小（根据设备信息）
	 * px,dip,sp -> px
	 *
	 * Paint.setTextSize()单位为px
	 *
	 * 代码摘自：TextView.setTextSize()
	 *
	 * @param unit  TypedValue.COMPLEX_UNIT_*
	 * @param size
	 * @return
	 */
	public static float getRawSize(Context mContext, int unit, float size) {
		Resources r;
		if (mContext == null)
			r = Resources.getSystem();
		else
			r = mContext.getResources();
		
		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}
	
	/**设备显示材质**/
	private static DisplayMetrics mDisplayMetrics = SysEnv.getDisplayMetrics();
	
	/**
	 * sp转换px
	 * @param spValue sp数值
	 * @return px数值
	 */
	public static int spTopx(float spValue) {
        return (int) (spValue * mDisplayMetrics.scaledDensity + 0.5f);
    }

	/**
	 * px转换sp
	 * @param pxValue px数值
	 * @return sp数值
	 */
    public static int pxTosp(float pxValue) {
        return (int) (pxValue / mDisplayMetrics.scaledDensity + 0.5f);
    }

	/**
	 * dip转换px
	 * @param dipValue dip数值
	 * @return px数值
	 */
    public static int dipTopx(float dipValue) {
        return (int) (dipValue * mDisplayMetrics.density + 0.5f);
    }

	/**
	 * dip转换px
	 * @param dipValue dip数值
	 * @return px数值
	 */
	public static float dipTopx2(float dipValue) {
		return   (dipValue * mDisplayMetrics.density + 0.5f);
	}

	/**
	 * px转换dip
	 * @param pxValue px数值
	 * @return dip数值
	 */
    public static int pxTodip(float pxValue) {
        return (int) (pxValue / mDisplayMetrics.density + 0.5f);
    }
}
