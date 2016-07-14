package com.drivingassisstantHouse.library.base;

import android.content.ContentProvider;

/**
 * android 系统中的四大组件之一ContentProvider基类
 * @author sunji
 * @version 1.0
 *
 */
public abstract class BaseContentProvider extends ContentProvider {

	/**日志输出标志**/
	protected final String TAG = this.getClass().getSimpleName();
}
