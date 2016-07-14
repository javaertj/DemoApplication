package com.drivingassisstantHouse.library.tools;

/**
 * 包名：com.drivingassisstantHouse.library.tools
 * 描述：泛型viewholder
 * 创建者：yankebin
 * 日期：2016/3/18
 */

import android.util.SparseArray;
import android.view.View;


public class ViewHolder {
    private final SparseArray<View> views;
    private View convertView;
    private Object tag;

    private ViewHolder(View convertView) {
        this.views = new SparseArray<>();
        this.convertView = convertView;
        convertView.setTag(this);
    }

    public View getConvertView() {
        return convertView;
    }

    public static ViewHolder get(View convertView) {
        if (null == convertView || null == convertView.getTag()) {
            return new ViewHolder(convertView);
        }
        ViewHolder existedHolder = (ViewHolder) convertView.getTag();
        return existedHolder;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}