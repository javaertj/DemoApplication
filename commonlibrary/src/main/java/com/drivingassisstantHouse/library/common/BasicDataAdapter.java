package com.drivingassisstantHouse.library.common;

import android.view.View;
import android.view.ViewGroup;

import com.drivingassisstantHouse.library.base.BaseMAdapter;
import com.drivingassisstantHouse.library.base.BaseViewHolder;


/**
 * 基本的列表数据展示Adapter，只需要传入BaseViewHolder的实现类即可
 * 具体示例写法见ImageListviewActivity.java.
 *
 *  http://git.oschina.net/zftlive/AjavaAndroidSample/blob/master/src/com/zftlive/android/sample/image/ImageListviewActivity.java
 * @author sunji
 * @version 1.0
 *
 */
public class BasicDataAdapter<T> extends BaseMAdapter<T> {

	/**
	 * Item控件缓存ViewHolder
	 */
	private BaseViewHolder<T> mViewHolder;
	
	public BasicDataAdapter(BaseViewHolder<T> mViewHolder){
		this.mViewHolder = mViewHolder;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(null == convertView){
			mViewHolder = mViewHolder.createViewHolder();
			convertView = mViewHolder.inflateLayout();
			mViewHolder.initView(convertView);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (BaseViewHolder<T>)convertView.getTag();
		}
		//设置数据
		T rowData = (T)getItem(position);
		mViewHolder.fillData(rowData, position);
		return convertView;
	}
}