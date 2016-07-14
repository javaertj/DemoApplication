package com.drivingassisstantHouse.library.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * @author sunJi
 *
 * 简单通用型Adapter(继承型)
 * @param <T>
 */
public  abstract class BSimpleEAdapter<T> extends BaseAdapter {

	/**
	 * 数据源
	 */
	protected List<T> mDatas;
	/**
	 * 上下文
	 */
	protected Context mContext;
	/**
	 * item布局索引
	 */
	private int layoutId;


	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}


	public List<T> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<T> mDatas) {
		this.mDatas = mDatas;
	}

	public int getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(int layoutId) {
		this.layoutId = layoutId;
	}

	/**
	 *
	 * @param context
	 *            上下文
	 * @param datas
	 *            数据源
	 * @param id
	 *            item的布局资源文件
	 */
	public BSimpleEAdapter(Context context, List<T> datas, int id) {
		this.mContext = context;
		this.mDatas = datas;
		this.layoutId = id;
	}

	/**
	 * 数据源改变，刷新界面
	 * 
	 * @param datas
	 */
	public void refersh(List<T> datas) {
		this.mDatas = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mDatas != null)
			return mDatas.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDatas != null) {
			return mDatas.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		SimpleAdapterHolder holder = SimpleAdapterHolder.get(convertView, parent, layoutId,
				position);
		covertView(holder, position,mDatas, getItem(position));
		return holder.getmConvertView();
	}
//可以把此类变成抽象类，让子类继承时在里面处理方法，不过使用接口更灵活，因为Activiy也可以实现interface
 	public abstract void covertView(SimpleAdapterHolder holder, int position,List<T> datas,
 			Object obj);
	
//	public interface AdapterInter<T> {
//		public void covertView(SimpleAdapterHolder holder, int position, List<T> datas, Object obj);
//	}


}
