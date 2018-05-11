package com.untitledhorton.archive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        WebView web = (WebView) findViewById(R.id.wv);
        web.setBackgroundColor(Color.TRANSPARENT);

        String gif_url = "archive_splash.gif";
        String html = "<html><body><img style=\'width: 100% \' src=\'" + gif_url + "\'></body></html>";
        web.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
        new Handler().postDelayed(new Runnable() {

            @Override public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}


