package com.ykbjson.demo.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ykbjson.demo.R;


/**
 * 包名：com.drivingassisstantHouse.library.tools
 * 描述：view工具类
 * 创建者：yankebin
 * 日期：2015/11/17
 */
public class ViewUtils {

    private static String TAG = "ViewUtil";

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void SetTouchView(View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setOnTouchListener(OnTouchListener_view_transparency.Instance());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }

        View child = viewHolder.get(id);
        if (child == null) {
            child = view.findViewById(id);
            viewHolder.put(id, child);
        }
        return (T) child;
    }


    public static class OnTouchListener_view_transparency implements View.OnTouchListener {

        private static OnTouchListener_view_transparency _Instance;

        public static OnTouchListener_view_transparency Instance() {
            if (_Instance == null) {
                _Instance = new OnTouchListener_view_transparency();
            }

            return _Instance;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(TAG, "onTouch OnTouchListener_view_transparency event.getAction():" + event.getAction());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.25f);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setAlpha(1);
            }
            return false;
        }
    }


    public static void setViewHigh(View view, float scale) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new SetViewHigh(view, scale));
        }
    }

    public static class SetViewHigh implements ViewTreeObserver.OnGlobalLayoutListener {
        private View view;
        private float scale;


        public SetViewHigh(View view, float scale) {
            this.view = view;
            this.scale = scale;

        }

        public SetViewHigh(View view, float scale, Boolean isSetMinHeight) {
            this.view = view;
            this.scale = scale;

        }

        @Override
        public void onGlobalLayout() {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            Log.d(TAG, "SetViewHigh.onGlobalLayout. w:"
                    + view.getMeasuredWidth() + ";h:"
                    + view.getMeasuredHeight());
            ViewGroup.LayoutParams lp_view = view.getLayoutParams();
            Log.d(TAG, "lp_view.height:" + lp_view.height);
            lp_view.height = (int) ((float) (view.getMeasuredWidth()) * scale);
            Log.d(TAG, "lp_view.height:" + lp_view.height);
            view.setLayoutParams(lp_view);


        }
    }

    //MainNavigateTabBar
    private static final int[] APPCOMPAT_CHECK_ATTRS = {R.attr.colorPrimary};
    public static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        boolean failed=false;
        if (null != a) {
              failed = !a.hasValue(0);
            a.recycle();
        }
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }
}
