package com.trending.trending_bucket.activity;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.trending.trending.R;
import com.trending.trending_bucket.util.utilMethods;


public class SplashActivity extends AppCompatActivity {
    private static int S = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView myView = (ImageView) findViewById(R.id.splashview);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myView.startAnimation(myFadeInAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {


                    if (utilMethods.isUserSignedIn(SplashActivity.this) == true) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(i);
                        SplashActivity.this.finish();

                    } else {
                        Intent i = new Intent(SplashActivity.this, LandingActivity.class);
                        SplashActivity.this.startActivity(i);
                        SplashActivity.this.finish();
                    }

                } else {
                    Intent a = new Intent(SplashActivity.this, NoInternetConnection.class);
                    SplashActivity.this.startActivity(a);
                    SplashActivity.this.finish();
                }
            }
        }, S);



        }
}