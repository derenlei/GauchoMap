package edu.ucsb.cs.cs190i.derenlei.GauchoMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Deren Lei on 5/29/2017.
 */

public class DetailActivity extends AppCompatActivity {
    private WebView placeWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");

        placeWebView = (WebView) findViewById(R.id.webview);
        // Configure related browser settings
        placeWebView.getSettings().setLoadsImagesAutomatically(true);
        placeWebView.getSettings().setJavaScriptEnabled(true);
        placeWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        placeWebView.setWebViewClient(new PlaceDetailBowser());
        // placeWebView the initial URL
        placeWebView.loadUrl(url);

        Toast.makeText(this, "Loading details...", Toast.LENGTH_LONG).show();

    }


    private class PlaceDetailBowser extends WebViewClient {
        //reload when do something wrong
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
    //backpressed.
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
