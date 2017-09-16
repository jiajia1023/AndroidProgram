package com.mylibrary.widgets;

import android.content.Context;
import android.net.http.SslError;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mylibrary.R;
import com.mylibrary.utils.FormatUtils;

/**
 * 带进度条的WebView
 */
public class ProgressWebView extends WebView {
    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, FormatUtils
                .dip2px(context, 2),
                0, 0));
        progressbar.setProgressDrawable(
                ContextCompat.getDrawable(context, R.drawable.layer_list_progress_bar));
        addView(progressbar);
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
                progressbar.postInvalidate();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //handler.cancel(); 默认的处理方式，WebView变成空白页
            //handler.process();接受证书
            //handleMessage(Message msg); 其他处理
            progressbar.setVisibility(View.GONE);
            //允许https://的访问
            handler.proceed();
        }
    }
}
