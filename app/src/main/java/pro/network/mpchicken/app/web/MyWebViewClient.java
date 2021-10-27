package pro.network.mpchicken.app.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }
}