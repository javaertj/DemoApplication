package com.ykbjson.demo.customview.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ykbjson.demo.R;
import com.ykbjson.demo.customview.viewpager.CustomViewPager;
import com.ykbjson.demo.customview.viewpager.MultiTransformer;
import com.ykbjson.demo.tools.SLog;
import com.ykbjson.demo.tools.ToolUnit;

import java.util.ArrayList;

/**
 * 包名：com.drivingassisstantHouse.library.widget.bannerview
 * 描述：广告banner
 * 创建者：yankebin
 * 日期：2016/2/27
 */
public class BannerView extends RelativeLayout {
    public static final int BANNER_TYPE_GUIDE = 1;
    public static final int BANNER_TYPE_ADVERT = BANNER_TYPE_GUIDE + 1;

    protected int type = BANNER_TYPE_GUIDE;
    private BaseBannerViewpager viewPager;
    private LinearLayout indicator;
    private boolean isAutoLoop = false;
    private long loopDelayTime = 4000;
    private int scrollDuration = 900;
    private ArrayList<ImageView> dots = new ArrayList<>();
    private boolean pause;

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
        type = typedArray.getInt(R.styleable.BannerView_bannerType, BANNER_TYPE_GUIDE);
        isAutoLoop = typedArray.getBoolean(R.styleable.BannerView_autoPlay, false);
        loopDelayTime = typedArray.getInt(R.styleable.BannerView_loopTime, 4000);
        scrollDuration = typedArray.getInt(R.styleable.BannerView_scrollDuration, 900);
        if (null != typedArray) {
            typedArray.recycle();
        }
        init();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != VISIBLE) {
            removeCallbacks(autoRunner);
        } else {
            if (isAutoLoop) {
                startAutoPlay();
            }
        }
    }

    private Runnable autoRunner = new Runnable() {
        @Override
        public void run() {
            if (!isAutoLoop || pause) {
                removeCallbacks(this);
                return;
            }
            int position = viewPager.getCurrentItem();
            viewPager.setCurrentItem(position + 1, true);
            postDelayed(this, loopDelayTime);
        }
    };

    private Runnable indicatorRunner = new Runnable() {
        @Override
        public void run() {
            int currentPosition = viewPager.getCurrentItem();
            for (int i = 0; i < dots.size(); i++) {
                ImageView imageView = dots.get(i);
                if (currentPosition % dots.size() == i) {
                    imageView.setImageResource(R.drawable.banner_dot_shape_selected);
                } else {
                    imageView.setImageResource(R.drawable.banner_dot_shape_normal);
                }
            }
        }
    };

    public void setIsAutoLoop(boolean isLoop) {
        this.isAutoLoop = isLoop;
    }

    public boolean isAutoLoop() {
        return isAutoLoop;
    }

    public void setPagerTransformer(CustomViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(false, transformer);
    }

    public void setBannerAdapter(BaseBannerPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    private void init() {
        viewPager = new BaseBannerViewpager(getContext());
        viewPager.setLayoutParams(new LayoutParams(-1, -1));

        indicator = new LinearLayout(getContext());
        indicator.setGravity(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        params.bottomMargin = 5;
        indicator.setLayoutParams(params);

        addView(viewPager);
        addView(indicator);

        initPagerListener();

        setPagerTransformer(new MultiTransformer());
    }

    private void initPagerListener() {
        viewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeDotsColors();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initDots(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size <=0 was disallow");
        }
        removeCallbacks(autoRunner);
        dots.clear();
        indicator.removeAllViews();
        //圆点容器
        LinearLayout dotsLayout = new LinearLayout(getContext());
        dotsLayout.setOrientation(LinearLayout.HORIZONTAL);
        dotsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));


        LinearLayout.LayoutParams pointViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pointViewParams.setMargins(ToolUnit.dipTopx(8), 0,
                ToolUnit.dipTopx(8), ToolUnit.dipTopx(8));

        for (int i = 0; i < size; i++) {
            ImageView pointView = new ImageView(getContext());
            pointView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            pointView.setLayoutParams(pointViewParams);
            if (0 == i) {
                pointView.setImageResource(R.drawable.banner_dot_shape_selected);
            } else {
                pointView.setImageResource(R.drawable.banner_dot_shape_normal);
            }
            dots.add(pointView);
            indicator.addView(pointView);
        }
        if (isAutoLoop) {
            startAutoPlay();
        }
    }

    private void startAutoPlay() {
        removeCallbacks(autoRunner);
        postDelayed(autoRunner, loopDelayTime);
    }

    public void changeDotsColors() {
        postDelayed(indicatorRunner, isAutoLoop ? scrollDuration + 100 : 100);
    }

    private class BaseBannerViewpager extends CustomViewPager {
        private int parentId;

        public BaseBannerViewpager(Context context) {
            this(context, null);
        }

        public BaseBannerViewpager(Context context, AttributeSet attrs) {
            super(context, attrs);
            setScrollerDuration(scrollDuration);
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            if (parentId > 0) {
                ViewPager pager = (ViewPager) findViewById(parentId);

                if (pager != null) {
                    pager.requestDisallowInterceptTouchEvent(true);
                }

            }
            return super.onInterceptTouchEvent(event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            int action = ev.getAction();
            SLog.d("action : " + action + " pause : " + pause);
            if (action == MotionEvent.ACTION_DOWN || MotionEvent.ACTION_MOVE == action) {
                if (!pause) {
                    pause = true;
                    boolean isRemove = BannerView.this.removeCallbacks(autoRunner);
                    SLog.d("isRemove : " + isRemove);
                }
            }

            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                if (pause) {
                    pause = false;
                    BannerView.this.postDelayed(autoRunner, loopDelayTime);
                }
            }

            return super.onTouchEvent(ev);
        }

        public void setAllowChildMovement(int id) {
            this.parentId = id;
        }

        @Override
        public void setAdapter(BaseBannerPagerAdapter adapter) {
            initDots(adapter.getRealCount());
            super.setAdapter(adapter);
        }
    }
}
