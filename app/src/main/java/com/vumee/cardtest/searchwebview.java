package com.vumee.cardtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class searchwebview extends AppCompatActivity {
    WebView webView;
    final String searchurl= SearchAdapter.getsearchurl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchwebview);
        webView = findViewById(R.id.searchwebview);
        final Button homebutton = this.findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(searchwebview.this.getApplicationContext(), MainActivity.class);
                searchwebview.this.startActivity(intent);
            }
        });

        final Button backbutton = this.findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                final Intent intent = new Intent(searchwebview.this.getApplicationContext(), MainActivity.class);
                searchwebview.this.startActivity(intent);


            }
        });
        ImageView homebackround = findViewById(R.id.homebacround);
        homebackround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(searchwebview.this.getApplicationContext(), MainActivity.class);
                searchwebview.this.startActivity(intent);
            }
        });

        ImageView backbackground = findViewById(R.id.backbackground);
        backbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WebView browser = searchwebview.this.findViewById(R.id.searchwebview);
                if (browser.canGoBack()) {
                    browser.goBack();
                } else {
                    final Intent intent = new Intent(searchwebview.this.getApplicationContext(), MainActivity.class);
                    searchwebview.this.startActivity(intent);
                }
            }
        });

        final WebSettings browserSetting = this.webView.getSettings();
        browserSetting.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.google.com");
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();

        webView.loadUrl(searchurl);
        this.webView.setWebViewClient(new searchwebview.MyWebViewClient());

        Button sharebtn = findViewById(R.id.sharebtn);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareurl = "http://open/smsactivity/"+webView.getUrl();

                GlobalClass globalClass = (GlobalClass) searchwebview.this.getApplicationContext();
                globalClass.setUrlcolleccted(shareurl);



            }
        });

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if (url.startsWith("http://open/smsactivity/")) {

            }
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public  boolean shouldOverrideUrlLoading(final WebView view, final WebResourceRequest request){

            final String clicked_url = String.valueOf(request.getUrl());

            if (clicked_url.startsWith("http://open/smsactivity/")) {


                GlobalClass globalClass = (GlobalClass) searchwebview.this.getApplicationContext();
                globalClass.setUrlcolleccted(clicked_url);

                view.loadUrl("https://sftv.app/sftvallproducts/yhoffers");

            }
            return false;
        }
        @Override public void onReceivedError(final WebView view, final int errorCode, final String description, final String failingUrl) {
            final String url = view.getUrl();
            if(url.startsWith("https://sftv.app")){
                view.loadUrl("file:///android_asset/error.html");
            }
        }
    }
}

