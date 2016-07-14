package com.drivingassisstantHouse.library.widget.bannerview;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 包名：com.simpletour.client.activity
 * 描述：banner适配器
 * 创建者：yankebin
 * 日期：2016/2/27
 */
public abstract class BaseBannerPagerAdapter<T> extends android.support.v4.view.PagerAdapter {
    protected SparseArray<View> views;
    private boolean isLoop;
    protected ArrayList<T> datas;

    private BaseBannerPagerAdapter() {
        datas = new ArrayList<>();
        views = new SparseArray<>();
    }

    public BaseBannerPagerAdapter(ArrayList<T> datas) {
        this();
        this.datas = datas;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return null != object && object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        position=position%getRealCount();
        container.removeView(views.get(position));
    }

    @Override
    public final int getCount() {
        return isLoop ? Integer.MAX_VALUE>>1 : datas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % getRealCount();
        View view = views.get(position);
        if (view == null) {
            view = newView(position);
            views.put(position, view);
        }
        container.addView(view);
        return view;
    }

    public void notifyUpdateView(int position) {
        View view = updateView(views.get(position), position);
        views.put(position, view);
        notifyDataSetChanged();
    }

    public View updateView(View view, int position) {
        return view;
    }

    public abstract View newView(int position);

    public final int getRealCount() {
        return datas.size();
    }

    public void subscribe(BannerView bannerView) {
        isLoop =bannerView.isAutoLoop()|| bannerView.type == BannerView.BANNER_TYPE_ADVERT;
        bannerView.setBannerAdapter(this);
    }
}