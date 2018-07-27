package com.trending.trending_bucket.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.trending.trending.R;
import com.trending.trending_bucket.util.utilMethods;

public class NoInternetConnection extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nointernetconnection);}

    public void retry(View v) {
                ConnectivityManager   cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();

                if (info != null && info.isConnected()) {
                    if(utilMethods.isUserSignedIn(NoInternetConnection.this) ==true)
                    {
                        Intent i = new Intent(NoInternetConnection.this, MainActivity.class);
                        startActivity(i);
                       finish();
                    }
                       else
                    {
                        Intent i = new Intent(NoInternetConnection.this, LandingActivity.class);
                        startActivity(i);
                        finish();
                    }
                    }
             }

}

