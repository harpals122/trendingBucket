package com.trending.trending_bucket.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.trending.trending.R;

/**
 * @author prabh kaur
 * @class LandingActivity
 * @brief Activity for showing Sign up, Sign In and See first option
 */
public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        ActivityCompat.requestPermissions(LandingActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
        ActivityCompat.requestPermissions(LandingActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

                ActivityCompat.requestPermissions(LandingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

        ActivityCompat.requestPermissions(LandingActivity.this,
                new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                1);

        ActivityCompat.requestPermissions(LandingActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                1);

        ActivityCompat.requestPermissions(LandingActivity.this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                1);

        ActivityCompat.requestPermissions(LandingActivity.this,
                new String[]{Manifest.permission.SEND_SMS},
                1);


            findViewById(R.id.btnSignUps).setOnClickListener(this);
            findViewById(R.id.btnSeeFirst).setOnClickListener(this);
            findViewById(R.id.btnSignIns).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSeeFirst:
                startActivity(new Intent(LandingActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.btnSignUps:
                startActivity(new Intent(LandingActivity.this, RegistrationActivity.class));
                break;
            case R.id.btnSignIns:
                startActivity(new Intent(LandingActivity.this, LoginActivity.class));
                break;
        }
    }
}
