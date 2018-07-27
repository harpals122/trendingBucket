package com.trending.trending_bucket.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trending.trending.R;
import com.trending.trending_bucket.fragment.shopcategoryfragment;
import com.trending.trending_bucket.fragment.shoplistfragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by prabh kaur on 19-03-2018.
 */
public class Shoplist_adapter extends ArrayAdapter

{
    View row;
    Context mcontext;
    TextView Shopname;
    ImageView Shopimage;
String Shopid;
    com.trending.trending_bucket.fragment.shoplistfragment shoplistfragment;
    FragmentManager fragmentManager;
    ArrayList<String>shopimage=new ArrayList<String>();
    ArrayList<String> shopid=new ArrayList<String>();
    ArrayList<String>shopname=new ArrayList<String>();

    public Shoplist_adapter(Context context, int resource, ArrayList<String> shopimage,
                            ArrayList<String> shopname, ArrayList<String> shopid, shoplistfragment shoplistfragment)
    {
        super(context, resource);
        this.mcontext=context;
        this.shopimage=shopimage;
        this.shopid=shopid;
        this.shopname=shopname;
this.shoplistfragment=shoplistfragment;
    }


    @Override
    public int getCount() {
       return shopid.size();
    }

    @Override
    public Object getItem(int position) {
        return shopid.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=layoutInflater.inflate(R.layout.shoplistrowlayout,parent ,false);
        Shopname=(TextView) row.findViewById(R.id.shop_name);
        Shopimage=(ImageView) row.findViewById(R.id.shop_image);
        Picasso.with(mcontext).load(shopimage.get(position)).fit().placeholder(R.drawable.img_placeholder)
                .into(Shopimage);
        Shopname.setText(shopname.get(position));

        row.setOnClickListener(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {

                                       Shopid=shopid.get(position);
             fragmentManager=shoplistfragment.getFragmentManager();
                                       shopcategoryfragment s =new shopcategoryfragment();
                                       fragmentManager.beginTransaction().replace(R.id.con,s.newInstance(Shopid)).addToBackStack(null).commit();
                                                             }
                               }
        );

        return row;
    }
}
