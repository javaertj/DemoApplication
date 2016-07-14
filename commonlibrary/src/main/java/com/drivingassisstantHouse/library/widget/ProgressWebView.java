package com.drivingassisstantHouse.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drivingassisstantHouse.library.R;

/**
 * 包名：com.drivingassisstantHouse.library.widget
 * 描述：带进度条的WebView
 * 创建者：yankebin
 * 日期：2016/2/26
 */
public class ProgressWebView extends WebView {

    public interface OnTitleReceiveCallback {
        public void onTitleReceived(String title);
    }


    public void setCallback(OnTitleReceiveCallback callback) {
        this.callback = callback;
    }

    public void setEnableProgress(boolean enableProgress) {
        this.enableProgress = enableProgress;
    }

    public void setJavaScriptEnabled(boolean javaScriptEnabled) {
        this.javaScriptEnabled = javaScriptEnabled;
    }

    private OnTitleReceiveCallback callback;
    private ProgressBar progressbar;


    private boolean enableProgress;
    private boolean javaScriptEnabled;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.ProgressWebView);
        enableProgress = typedArray.getBoolean(R.styleable.ProgressWebView_enable_progress, true);
        javaScriptEnabled = typedArray.getBoolean(R.styleable.ProgressWebView_enable_java_script, false);
        typedArray.recycle();
        if (enableProgress) {
            initProgressView(context);
        }
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());

        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        WebSettings settings = getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);

        //混合https和http请求，不然不能显示https的图片
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);

        //是否支持缩放
        getSettings().setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //自适应屏幕
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    private void initProgressView(Context context) {
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                10, 0, 0));

        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
    }



    private String loadUrl = null;
    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            if(url.contains("http://m.amap.com")){
//                view.loadUrl("javascript:window.onload=function(){document.getElementsByClassName('common_top J_common_top')[0].remove();" +
//                                "document.getElementsByClassName('common_bottom')[0].remove();" +
//                                "}"
//                );
//            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains("http://m.amap.com")){
                if(null == loadUrl){
                    loadUrl = url;
                    view.loadUrl(loadUrl);
                }
            }else{
                view.loadUrl(url);
            }
            Log.d("WebViewClient", "shouldOverrideUrlLoading>>"+url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.contains("http://m.amap.com")){
                view.loadUrl("javascript:window.onload=function(){var style = document.createElement('style');style.type='text/css';style.appendChild(document.createTextNode('.common_top.J_common_top{display:none !important}.map_all{top:0 !important}'));document.head.appendChild(style)}"
                );
            }
            super.onPageFinished(view, url);
        }
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (!enableProgress) {
                return;
            }
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (null != callback) {
                callback.onTitleReceived(title);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!enableProgress) {
            return;
        }
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
    }

}
