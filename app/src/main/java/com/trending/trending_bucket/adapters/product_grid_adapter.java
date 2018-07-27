package com.trending.trending_bucket.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trending.trending.R;
import com.trending.trending_bucket.activity.ProductDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by prabh kaur on 03-04-2018.
 */
public class product_grid_adapter extends BaseAdapter  {
    View row;
    private Context mcontext;
    ImageView Productimage;
    TextView Productname, ProductDiscount;
    String Productid;
    private String shopID;
    private String prodID;
    ArrayList<String>productimage=new ArrayList<String>();
    ArrayList<String> productid=new ArrayList<String>();
    ArrayList<String> shopid=new ArrayList<String>();
    ArrayList<String>productname=new ArrayList<String>();
    ArrayList<String>prdocuDiscount=new ArrayList<String>();

    public product_grid_adapter(Context context, ArrayList<String> productname,
                                ArrayList<String> productimage,
                                ArrayList<String> productid,
                                ArrayList<String> prdocuDiscount)
    {
        this.mcontext = context;
        this.productid=productid;
        this.productimage=productimage;
        this.productname=productname;
        this.prdocuDiscount=prdocuDiscount;
    }




    @Override
    public int getCount()
    {
        return productid.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater=(LayoutInflater) this.mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row=layoutInflater.inflate(R.layout.productgrid,null);

        Productimage=(ImageView) row.findViewById(R.id.products_image);
        Productname=(TextView) row.findViewById(R.id.products_text);
        ProductDiscount=(TextView) row.findViewById(R.id.discountlabel);

            Picasso.with(mcontext).load(productimage.get(position)).fit().placeholder(R.drawable.img_placeholder)
                    .into(Productimage);
        Productname.setText(productname.get(position));
        ProductDiscount.setText(prdocuDiscount.get(position)+ "%\nOFF");
        Productid=productid.get(position);
        row.setBackgroundResource(R.drawable.gridborder);

        row.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       Intent intent = new Intent(mcontext, ProductDetail.class);
                                       intent.putExtra("prodID", productid.get(position));
                                       Log.d("prodID", productid.get(position));
                                       intent.putExtra("shopID", "check");
                                       mcontext.startActivity(intent);
                                   }
                               }
        );

            return row;
     }
 }
