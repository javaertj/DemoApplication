package com.drivingassisstantHouse.library.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * 观察view scrollview
 *	用于顶部和底部悬浮布局 渐显效果
 */
public class ObservableScrollView extends ScrollView {

	private boolean interceptTouchAnyWay = true;

	private ArrayList<Callbacks> mCallbacks = new ArrayList<>();

	public ObservableScrollView(Context context) {
		this(context, null);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(OVER_SCROLL_NEVER);
	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOverScrollMode(OVER_SCROLL_NEVER);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		handleOnScrollCallback(l, t, oldl, oldt);
	}

	/**
	 * 重写此方法让scrollview一直拦截touch事件，解决当scrollview内容没超出屏幕时也能拦截touch事件
	 * 因为源码 onInterceptTouchEvent 里有如下说明
	 * Don't try to intercept touch if we can't scroll anyway.
	 * if (getScrollY() == 0 && !canScrollVertically(1)) {
	 * return false;
	 * }
	 */
	@Override
	public boolean canScrollVertically(int direction) {
		return interceptTouchAnyWay || super.canScrollVertically(direction);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!mCallbacks.isEmpty()) {
			switch (ev.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					handleOnDownMotionEvent();
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					handleOnUpOrCancelMotionEvent();
					break;
			}
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public int computeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}

	public void setInterceptTouchAnyWay(boolean interceptTouchAnyWay) {
		this.interceptTouchAnyWay = interceptTouchAnyWay;
	}

	private void handleOnDownMotionEvent() {
		if (checktock()) {
			return;
		}
		for (Callbacks callbacks : mCallbacks) {
			callbacks.onDownMotionEvent();
		}
	}

	private void handleOnUpOrCancelMotionEvent() {
		if (checktock()) {
			return;
		}
		for (Callbacks callbacks : mCallbacks) {
			callbacks.onUpOrCancelMotionEvent();
		}
	}

	private void handleOnScrollCallback(int l, int t, int oldl, int oldt) {
		if (checktock()) {
			return;
		}
		for (Callbacks callbacks : mCallbacks) {
			callbacks.onScrollCallback(l, t, oldl, oldt);
		}
	}

	private boolean checktock() {
		return mCallbacks.isEmpty();
	}

	public boolean addScrollCallbacks(Callbacks listener) {
		return !mCallbacks.contains(listener) && mCallbacks.add(listener);
	}

	public boolean removeScrollCallbacks(Callbacks listener) {
		return mCallbacks.contains(listener) && mCallbacks.remove(listener);
	}

	public interface Callbacks {
		public void onScrollCallback(int l, int t, int oldl, int oldt);

		public void onDownMotionEvent();

		public void onUpOrCancelMotionEvent();
	}

}