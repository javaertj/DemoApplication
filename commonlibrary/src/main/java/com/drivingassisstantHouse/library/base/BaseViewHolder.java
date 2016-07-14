package com.drivingassisstantHouse.library.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Adapter的ViewHolder基类，配合BasicDataAdapter用于一种类型Item的listview数据展示，提供开发效率、省去一大堆繁琐代码
 * 
 * @author 曾繁添
 * @version 1.0
 * @param <T>
 */
public abstract class BaseViewHolder<T> {

	/**
	 * Context上下文
	 */
	protected Context mContext;
	/**
	 * inflate的布局View
	 */
	protected View mItemView;
	
	public BaseViewHolder(Context mContext) {
		this.mContext = mContext;
	}
	
	/**
	 * 实例化ViewHolder
	 * @return
	 */
	public abstract BaseViewHolder<T> createViewHolder();
	
	/**
	 * 绑定Item布局layout，只调用一次
	 * 
	 * @return
	 */
	public abstract int bindLayout();
	
	/**
	 * 初始化View，只调用一次
	 */
	public View inflateLayout(){
		mItemView = LayoutInflater.from(mContext).inflate(bindLayout(), null);
		return mItemView;
	}
	
	/**
	 * 初始化Item的各个控件，只调用一次
	 * @param view Item渲染的视图View
	 */
	public abstract void initView(View view);
	
	/**
	 * 填充Item各个控件数据
	 * @param rowData 当前行数据
	 * @param position 集合数据的当前索引
	 */
	public abstract void fillData(T rowData,int position);
	
	/**
	 * 查找Item布局中的控件
	 * @param id 控件ID
	 * @return
	 */
	public final View findViewById(int id){
		return mItemView.findViewById(id);
	}
	
	/**
	 * 获取当前Item渲染的视图View
	 * @return
	 */
	public final View getRootView(){
		return mItemView;
	}
	
	/**
	 * 获得上下文
	 * @return
	 */
	public Context getContext(){
		return this.mContext;
	}
	
	/**
	 * 设置上下文
	 * @param mContext
	 */
	public void setContext(Context mContext){
		this.mContext = mContext;
	}
}
