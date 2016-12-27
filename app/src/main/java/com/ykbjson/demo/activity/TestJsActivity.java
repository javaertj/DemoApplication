package com.ykbjson.demo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.drivingassisstantHouse.library.base.BaseActivity;
import com.drivingassisstantHouse.library.data.IntentBean;
import com.drivingassisstantHouse.library.tools.ToolToast;
import com.drivingassisstantHouse.library.widget.ProgressWebView;
import com.ykbjson.demo.R;

import butterknife.Bind;


/**
 * 包名：com.ykbjson.demo.activity
 * 描述：
 * 创建者：yankebin
 * 日期：2016/8/4
 */

public class TestJsActivity extends BaseActivity implements ProgressWebView.OnTitleReceiveCallback {
    @Bind(R.id.advert_detail_webview)
    WebView webView;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {

        return R.layout.layout_test_collapsing;
    }

    @Override
    public void initView(View view) {
//        webView.setCallback(this);
//        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/test.html");
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onTitleReceived(String title) {
        ToolToast.showLong(title);
    }

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    public IntentBean create() {
        return null;
    }
}
