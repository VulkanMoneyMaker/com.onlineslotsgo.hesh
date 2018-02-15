package com.onlineslotsgo.hesh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class WebStart extends AppCompatActivity {
    private static final String TAG = WebStart.class.getSimpleName();

    private static final String URL = "OPENED_URL";

    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        bar = findViewById(R.id.progress);
        bar.setVisibility(View.VISIBLE);

        String url = getIntent().getStringExtra(URL);
        holdsValue(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void holdsValue(String url) {
        Log.d(TAG, "holdsValue");
        bar.setVisibility(View.GONE);

        WebView webView = findViewById(R.id.web_view);
        webView.setWebViewClient(setupClient());
        correctValues(webView.getSettings());
        webView.loadUrl(url);
    }

    @NonNull
    public static Intent getMainActivityIntent(Context context, String url) {
        Intent intent = new Intent(context, WebStart.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void correctValues(@NonNull WebSettings webSettings) {
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
    }

    @NonNull
    private WebViewClient setupClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @RequiresApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
    }
}
