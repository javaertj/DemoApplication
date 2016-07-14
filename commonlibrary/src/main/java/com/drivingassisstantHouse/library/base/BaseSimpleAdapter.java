package com.drivingassisstantHouse.library.base;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.drivingassisstantHouse.library.tools.ViewUtils;


/**
 * @author sunJi
 *
 * 简单通用型Adapter(回调型)
 * @param <T>
 */
public   class BaseSimpleAdapter<T> extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<T> mDatas;
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * item布局索引
	 */
	private int layoutId;
	
	private AdapterInter mInterface;

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public AdapterInter getmInterface() {
		return mInterface;
	}

	public void setmInterface(AdapterInter mInterface) {
		this.mInterface = mInterface;
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
	public BaseSimpleAdapter(Context context, List<T> datas, int id) {
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
		mInterface.covertView(holder, position,mDatas, mDatas.get(position));
		return holder.getmConvertView();
	}
//可以把此类变成抽象类，让子类继承时在里面处理方法，不过使用接口更灵活，因为Activiy也可以实现interface
//	public abstract void covertView(SimpleAdapterHolder holder, int position,
//			Object obj);
	
	public interface AdapterInter<T> {
		public void covertView(SimpleAdapterHolder holder, int position,List<T> datas, Object obj);
	}


	public static void setAdapter(GridView gv, BaseSimpleAdapter adapter) {
		gv.setAdapter(adapter);
		if (adapter.getCount() % gv.getNumColumns() == 0) {
			ViewUtils.setViewHigh(gv, (1f /  gv.getNumColumns()) * (adapter.getCount() /  gv.getNumColumns()));
		} else {
			ViewUtils.setViewHigh(gv, (1f /  gv.getNumColumns()) * (adapter.getCount() /  gv.getNumColumns() + 1));
		}
	}
}
