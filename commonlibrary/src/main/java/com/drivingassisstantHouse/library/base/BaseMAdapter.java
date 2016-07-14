package com.drivingassisstantHouse.library.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adapter基类
 * @author sunji
 * @version 1.0
 * @param <T> Adapter绑定的数据源行类型Bean/Map/Object
 */
public abstract class BaseMAdapter<T> extends android.widget.BaseAdapter {

	/** 数据存储集合 **/
	private List<T> mDataList = new ArrayList<T>();
	/** Context上下文 **/
	private Activity mContext;
	/** 每一页显示条数 **/
	private int mPerPageSize = 10;

	public BaseMAdapter() {
		this(null);
	}
	
	public BaseMAdapter(Activity mContext) {
		this(mContext,10);
	}
	
	public BaseMAdapter(Activity mContext,int mPerPageSize){
		this.mContext = mContext;
		this.mPerPageSize = mPerPageSize;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public T getItem(int position) {
		if (position < mDataList.size())
			return mDataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 获取当前页
	 * @return 当前页
	 */
	public int getPageNo(){
		return (getCount() / mPerPageSize) + 1;
	}
	
	/**
	 * 添加数据
	 * @param object 数据项
	 */
	public boolean addItem(T object){
		return mDataList.add(object);
	}
	
	/**
	 * 在指定索引位置添加数据
	 * @param location 索引
	 * @param object 数据
	 */
	public void addItem(int location,T object){
	     mDataList.add(location, object);
	}
	
	/**
	 * 集合方式添加数据
	 * @param collection 集合
	 */
	public boolean addItem(Collection collection){
		return mDataList.addAll(collection);
	}
	
	/**
	 * 在指定索引位置添加数据集合
	 * @param location 索引
	 * @param collection 数据集合
	 */
	public boolean addItem(int location,Collection collection){
		return mDataList.addAll(location,collection);
	}
	
	/**
	 * 移除指定对象数据
	 * @param object 移除对象
	 * @return 是否移除成功
	 */
	public boolean removeItem(Object object){
		return mDataList.remove(object);
	}
	
	/**
	 * 移除指定索引位置对象
	 * @param location 删除对象索引位置
	 * @return 被删除的对象
	 */
	public Object removeItem(int location){
	    return mDataList.remove(location);
	}
	
	/**
	 * 移除指定集合对象
	 * @param collection 待移除的集合
	 * @return 是否移除成功
	 */
	public boolean removeAll(Collection collection){
		return mDataList.removeAll(collection);
	}
	
	/**
	 * 清空数据
	 */
	public void clear() {
		mDataList.clear();
	}
	
	/**
	 * 获取Activity方法
	 * @return Activity的子类
	 */
	public Activity getActivity(){
		return mContext;
	}
}
