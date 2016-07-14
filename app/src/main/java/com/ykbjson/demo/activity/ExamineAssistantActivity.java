package com.ykbjson.demo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ykbjson.demo.R;
import com.ykbjson.demo.bean.AssistantManager;
import com.ykbjson.demo.customview.otherview.CircleImageView;
import com.ykbjson.demo.customview.viewpager.HackyViewPager;
import com.ykbjson.demo.tools.SLog;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 包名：com.simpletour.client.ui.travel
 * 描述：行车助理页面
 * 创建者：yankebin
 * 日期：2016/3/31
 */
public class ExamineAssistantActivity extends AppCompatActivity {
    @Bind(R.id.viewpager_top)
    HackyViewPager viewpagerTop;
    @Bind(R.id.viewpager_middle)
    HackyViewPager viewpagerMiddle;
    @Bind(R.id.viewpager_bottom)
    HackyViewPager viewpagerBottom;
    @Bind(R.id.viewpager_top_container)
    FrameLayout viewpagerTopContainer;

    private String mobile;
    private int touchFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine_assistant);
        ButterKnife.bind(this);
        ArrayList<AssistantManager> assistantManagers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AssistantManager manager = new AssistantManager();
            manager.setMobile("15208279347");
            manager.setName("客服" + i);
            manager.setNickName("简途客户" + i);
            manager.setAssistantId(i);
            manager.setAvatar("drawable://" + R.drawable.customer_service_head);
            manager.setPhoto("drawable://" + R.drawable.ad_page);
            manager.setDescription("呵呵呵呵 的期望的开启电脑去");
            manager.setType(i % 2 == 0 ? 0 : 1);
            manager.setTourismSections("成都-都江堰-青城山");
            assistantManagers.add(manager);
        }
        dataChange(assistantManagers);

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }


    private void dataChange(ArrayList<AssistantManager> assistants) {
        initPagers(assistants);
    }

    private void initPagers(ArrayList<AssistantManager> assistants) {
        int size = assistants.size();
        int offset = getResources().getDimensionPixelSize(R.dimen.dimen_10);
        int width = getResources().getDisplayMetrics().widthPixels;

        ViewGroup.LayoutParams bottomParams = viewpagerBottom.getLayoutParams();
        bottomParams.width = width - offset * 8;
        viewpagerBottom.setLayoutParams(bottomParams);

        ViewGroup.LayoutParams topParams = viewpagerTop.getLayoutParams();
        topParams.width = (width - 4 * offset) / 4;
        viewpagerTop.setLayoutParams(topParams);

        AssistantAdapterTop topAdapter = new AssistantAdapterTop(assistants, this);
        AssistantImageAdapter middleAdapter = new AssistantImageAdapter(assistants, this);
        AssistantInfoAdapter bottomAdapter = new AssistantInfoAdapter(assistants, this);
        viewpagerTop.setAdapter(topAdapter);
        viewpagerMiddle.setAdapter(middleAdapter);
        viewpagerBottom.setAdapter(bottomAdapter);

        viewpagerTop.setOffscreenPageLimit(size);
        viewpagerTop.setPageMargin(offset);

        viewpagerMiddle.setOffscreenPageLimit(size);

        viewpagerBottom.setOffscreenPageLimit(size);
        viewpagerBottom.setPageMargin(offset);

        viewpagerTop.addOnPageChangeListener(new MultiplePageChangeListener(0));
        viewpagerMiddle.addOnPageChangeListener(new MultiplePageChangeListener(1));
        viewpagerBottom.addOnPageChangeListener(new MultiplePageChangeListener(2));

        viewpagerTopContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewpagerTop.dispatchTouchEvent(event);
            }
        });
    }


    private class MultiplePageChangeListener implements ViewPager.OnPageChangeListener {
        private float fPoint1;
        private float fPoint2;
        private int from;

        public MultiplePageChangeListener(int from) {
            this.from = from;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                return;
            }
            SLog.d("from : " + from);
            handleScroll(from, position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int index;
            if (state == 0) {
                SLog.d("state : " + state);
                switch (from) {
                    case 0:
                        index = viewpagerTop.getCurrentItem();
                        viewpagerBottom.setCurrentItem(index);
                        viewpagerMiddle.setCurrentItem(index);
                        break;
                    case 1:
                        index = viewpagerMiddle.getCurrentItem();
                        viewpagerBottom.setCurrentItem(index);
                        viewpagerTop.setCurrentItem(index);
                        break;
                    case 2:
                        index = viewpagerBottom.getCurrentItem();
                        viewpagerTop.setCurrentItem(index);
                        viewpagerMiddle.setCurrentItem(index);
                        break;
                }
                handleSelected();
            }
        }

        private void handleSelected() {
            for (int i = 0; i < viewpagerTop.getAdapter().getCount(); i++) {
                invalidateViewPager(viewpagerTop, i);
            }
        }

        private void handleScroll(int from, int position, float positionOffset, int positionOffsetPixels) {
            ViewPager sourcePager = null, pager1 = null, pager2 = null;
            switch (from) {
                //top
                case 0:
                    sourcePager = viewpagerTop;
                    pager1 = viewpagerBottom;
                    pager2 = viewpagerMiddle;
                    break;
                //middle
                case 1:
                    sourcePager = viewpagerMiddle;
                    pager1 = viewpagerTop;
                    pager2 = viewpagerBottom;
                    break;
                //bottom
                case 2:
                    sourcePager = viewpagerBottom;
                    pager1 = viewpagerTop;
                    pager2 = viewpagerMiddle;
                    break;
                default:
                    break;
            }
            if (null != pager1 && null != pager2 && null != sourcePager) {
                handleScroll(sourcePager, pager1, pager2, position, positionOffsetPixels);
            }
        }

        private void handleScroll(ViewPager sourcePager, ViewPager pager1, ViewPager pager2, int position, int positionOffsetPixels) {
            float scrollX1, scrollX2;
            float baseW;
            float mCurrentScroll;

            baseW = (sourcePager.getWidth() + sourcePager.getPageMargin()) * 1f;
            mCurrentScroll = baseW * position + positionOffsetPixels;

            scrollX1 = mCurrentScroll * ((pager1.getWidth() + pager1.getPageMargin()) / baseW) + fPoint1;
            scrollX2 = mCurrentScroll * ((pager2.getWidth() + pager2.getPageMargin()) / baseW) + fPoint2;
            //滑动内部Viewpager
            pager1.scrollTo((int) scrollX1, 0);
            pager2.scrollTo((int) scrollX2, 0);

            fPoint1 = getPoint(scrollX1);
            fPoint2 = getPoint(scrollX2);
        }

        private float getPoint(float source) {
            final BigDecimal b1 = new BigDecimal(Float.toString(source));
            final BigDecimal b2 = new BigDecimal(Integer.toString((int) source));
            //浮点数的小数部分
            return b1.subtract(b2).floatValue();
        }
    }

    private void invalidateViewPager(ViewPager viewPager, int position) {
        TextView tvName = (TextView) viewPager.findViewWithTag("tvName" + position);
        CircleImageView imageView = (CircleImageView) viewPager.findViewWithTag("imageView" + position);
        if (null == tvName || null == imageView) {
            return;
        }
        SLog.d("currentItem : " + viewpagerTop.getCurrentItem() + " position : " + position);
        invalidateViewPager(viewPager.getCurrentItem(), imageView, tvName, position);
    }

    private void invalidateViewPager(int currentItem, ImageView imageView, TextView textView, int position) {
        float alpha;
        if (currentItem == position) {
            textView.setVisibility(View.VISIBLE);
            alpha = 1f;
        } else {
            textView.setVisibility(View.INVISIBLE);
            alpha = 0.5f;
        }
        imageView.setAlpha(alpha);
    }

    private class AssistantAdapterTop extends PagerAdapter {
        private ArrayList<AssistantManager> mData = new ArrayList<>();
        private Context mContext;

        public AssistantAdapterTop(ArrayList<AssistantManager> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_assistant_avatar, container, false);
            AssistantManager manager = mData.get(position);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText(manager.getName());
            tvName.setTag("tvName" + position);

            final CircleImageView imageView = (CircleImageView) view.findViewById(R.id.iv_avatar);
            imageView.setTag("imageView" + position);

            final int failedResId = manager.getType() == AssistantManager.MANAGER_TYPE_ASSISTANT ? R.drawable.guide_head : R.drawable.customer_service_head;

            if (TextUtils.isEmpty(manager.getAvatar())) {
                ImageLoader.getInstance().displayImage("drawable://" + failedResId, imageView);
            } else {
                ImageLoader.getInstance().displayImage(manager.getAvatar(), imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        imageView.setImageResource(failedResId);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        imageView.setImageBitmap(loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }

            invalidateViewPager(0, imageView, tvName, position);

            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpagerTop.setCurrentItem(position, true);
                }
            });
            return view;
        }

        @Override
        public int getCount() {
            return null == mData ? 0 : mData.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }

    private class AssistantImageAdapter extends PagerAdapter {
        private ArrayList<AssistantManager> mData = new ArrayList<>();
        private Context mContext;

        public AssistantImageAdapter(ArrayList<AssistantManager> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_assistant, container, false);
            AssistantManager manager = mData.get(position);

            ImageView imageView = (ImageView) view.findViewById(R.id.iv_assistant);
            ImageView imageViewNo = (ImageView) view.findViewById(R.id.iv_assistant_no);
            if (TextUtils.isEmpty(manager.getPhoto())) {
                imageViewNo.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                imageViewNo.setVisibility(View.INVISIBLE);
            }
            int failedResId = manager.getType() == AssistantManager.MANAGER_TYPE_ASSISTANT ? R.drawable.guide : R.drawable.customer_service;
            imageViewNo.setImageResource(failedResId);

            if (!TextUtils.isEmpty(manager.getPhoto())) {
                ImageLoader.getInstance().displayImage(manager.getPhoto(), imageView);
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return null == mData ? 0 : mData.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }

    private class AssistantInfoAdapter extends PagerAdapter {
        private ArrayList<AssistantManager> mData = new ArrayList<>();
        private Context mContext;

        public AssistantInfoAdapter(ArrayList<AssistantManager> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final AssistantManager manager = mData.get(position);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_assistant_info, container, false);
            TextView tvName = (TextView) view.findViewById(R.id.tv_travel_name);
            TextView tvSelections = (TextView) view.findViewById(R.id.tv_travel_sections);
            TextView tvCall = (TextView) view.findViewById(R.id.tv_contact_assistant);

            String format = manager.getType() == AssistantManager.MANAGER_TYPE_SERVICE ? "我是简途客服%s,您有疑问都可随时咨询我" : "我是为您这段行程提供服务的的行车助理,我的名字叫%s,您有疑问都可随时咨询我";
            tvName.setText(String.format(format, manager.getNickName()));
            tvSelections.setText(manager.getTourismSections());
            tvCall.setText(String.format("打电话给 %s", manager.getNickName()));

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return null == mData ? 0 : mData.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
    }

}
