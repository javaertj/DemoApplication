package com.ykbjson.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.drivingassisstantHouse.library.base.BaseActivity;
import com.drivingassisstantHouse.library.base.SimpleAdapterHolder;
import com.drivingassisstantHouse.library.data.IntentBean;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ykbjson.demo.R;
import com.ykbjson.demo.bean.AssistantManager;
import com.ykbjson.demo.customview.listview.WheelAdapter;
import com.ykbjson.demo.customview.listview.WheelView;
import com.ykbjson.demo.customview.otherview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 包名：com.ykbjson.demo.activity
 * 描述：
 * 创建者：yankebin
 * 日期：2016/5/25
 */
public class TestTravelListActivity extends BaseActivity {
    @Bind(R.id.wheel)
    WheelView wheel;
    @Bind(R.id.tv_web)
    TextView textView;
    private ArrayList<AssistantManager> assistantManagers = new ArrayList<>();
    private TravelListAdapter adapter;


    private void dataChange() {
        adapter = new TravelListAdapter(this, assistantManagers, R.layout.item_assistant_avatar);
        wheel.setAdapter(adapter);

    }


    @Override
    public int bindLayout() {
        return R.layout.activity_scroll_list;
    }

    @Override
    public void initParms(Bundle parms) {

    }


    @Override
    public void initView(View view) {
        for (int i = 0; i < 2; i++) {
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
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra("data")) {
                textView.setText(intent.getStringExtra("data"));
            }
        }
    }


    @Override
    public void doBusiness(Context mContext) {
        baseHandler.sendEmptyMessageDelayed(1, 200);
    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void handleMessage(Message msg) {
        dataChange();
    }

    @Override
    public IntentBean create() {
        return null;
    }

    private class TravelListAdapter extends WheelAdapter<AssistantManager> {
        private int mSelectPosition;

        /**
         * @param context 上下文
         * @param data    数据源
         * @param id      item的布局资源文件
         */
        public TravelListAdapter(Context context, List data, int id) {
            super(context, data, id);
        }

        @Override
        public void onHandleScroll(int selectPosition) {
            mSelectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public void covertView(SimpleAdapterHolder holder, int position, List<AssistantManager> dataSource, AssistantManager manager) {
            float scale = 1f;
            if (mSelectPosition == position) {
                scale = 1.2f;
            }
            TextView tvName = holder.getView(R.id.tv_name);
            tvName.setText(manager.getName());
            CircleImageView imageView = holder.getView(R.id.iv_avatar);
            ImageLoader.getInstance().displayImage(manager.getAvatar(), imageView);

            ViewHelper.setScaleX(holder.getmConvertView().findViewById(R.id.layout_content), scale);
            ViewHelper.setScaleY(holder.getmConvertView().findViewById(R.id.layout_content), scale);
        }
    }
}
