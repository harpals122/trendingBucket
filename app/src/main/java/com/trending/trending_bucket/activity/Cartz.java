package com.trending.trending_bucket.activity;

import java.text.DecimalFormat;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;;
import android.widget.TextView;
import android.widget.Toast;

import com.trending.trending.R;
import com.trending.trending_bucket.fragment.CartListFragment;
import com.trending.trending_bucket.util.TotalPriceCallback;

public class Cartz extends AppCompatActivity implements TotalPriceCallback {

    //Declare Price Format
    DecimalFormat formatData = new DecimalFormat("#.##");
    // used to store app title
    private CharSequence mTitle = "Cart";
    private FragmentManager fragmentManager;
    private TextView Total_Price;
    private Double GrandPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartz);
        Total_Price = (TextView) findViewById(R.id.TotalPrice);
        fragmentManager = getSupportFragmentManager();
        //replace the activity with fragment
        fragmentManager.beginTransaction()
                        .replace(R.id.container, CartListFragment.newInstance())
                        .commit();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    /**
     * Slide menu item click listener
     * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    //Interface method

    public void onTotal(Double TotalPrice){
        TotalPrice = Double.parseDouble(formatData.format(TotalPrice));
        this.GrandPrice = TotalPrice;
        Total_Price.setText("Grand Total : $ " + Double.toString(TotalPrice));
    }


    public void buyNow(View view){
        // open database first
         try
         {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("GrandPrice", Double.toString(GrandPrice));
            startActivity(intent);
            finish();
         }
        catch (NullPointerException ex)
        {
             Toast.makeText(this,"Cart is Empty, please Place product",Toast.LENGTH_LONG).show();
        }
    }
}