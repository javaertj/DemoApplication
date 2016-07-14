package com.drivingassisstantHouse.library.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drivingassisstantHouse.library.data.OnNetWorkEvent;
import com.drivingassisstantHouse.library.tools.SLog;
import com.mcxiaoke.bus.annotation.BusReceiver;

import butterknife.ButterKnife;

/**
 * Fragment基类
 *
 * @author sunji
 * @version 1.0
 */
@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment implements IBaseFragment {

    /**
     * 当前Fragment渲染的视图View
     **/
    private View mContextView = null;
    /**
     * 共通操作
     **/
    protected Operation mOperation = null;
    /**
     * 依附的Activity
     **/
    protected Activity mContext = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //缓存当前依附的activity
        mContext = activity;
        SLog.d("BaseFragment-->onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.d("BaseFragment-->onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SLog.d("BaseFragment-->onCreateView()");
        // 渲染视图View
        if (null == mContextView) {
            //初始化参数
            initParms(getArguments());

            View mView = bindView();
            if (null == mView) {
                mContextView = inflater.inflate(bindLayout(), container, false);
            } else {
                mContextView = mView;
            }
            //使用butterKnife注解-fragment需要解绑
            ButterKnife.bind(this, mContextView);
            // 控件初始化
            initView(mContextView);
            // 实例化共通操作
            mOperation = new Operation(getActivity());
            // 业务处理
            doBusiness(mContext);
        } else {
            //使用butterKnife注解-fragment需要解绑
            ButterKnife.bind(this, mContextView);
        }

        return mContextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        SLog.d("BaseFragment-->onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SLog.d("BaseFragment-->onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        SLog.d("BaseFragment-->onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        SLog.d("BaseFragment-->onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        SLog.d("BaseFragment-->onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        SLog.d("BaseFragment-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        SLog.d("BaseFragment-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        SLog.d("BaseFragment-->onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);//接除ButterKnife注解
        if (mContextView != null && mContextView.getParent() != null) {
            ((ViewGroup) mContextView.getParent()).removeView(mContextView);
        }
    }

    /**
     * 获取当前Fragment依附在的Activity
     *
     * @return
     */
    protected Activity getAttachActivity() {
        return getActivity();
    }

    /**
     * 获取共通操作机能
     */
    public Operation getOperation() {
        return this.mOperation;
    }

    @BusReceiver
    public void onMainNetWorkEvent(OnNetWorkEvent event) {
        onNetWorkChanged(event.connected);
    }

    @Override
    public void onNetWorkChanged(boolean connected) {

    }
}
