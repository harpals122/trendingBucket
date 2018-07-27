package com.trending.trending_bucket.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trending.trending.R;
import com.trending.trending_bucket.activity.Cartz;
import com.trending.trending_bucket.util.CustomButtonListener;
import com.trending.trending_bucket.util.DBHelper;
import com.trending.trending_bucket.util.TotalPriceCallback;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterCart extends BaseAdapter {

    public ArrayList<Integer> quantity = new ArrayList<Integer>();

    CustomButtonListener customButtonListener;

    DecimalFormat formatData = new DecimalFormat("#.##");

    private ArrayList<Integer> prodID;

    private ArrayList<String> orderName;

    private ArrayList<Double> price;

    private ArrayList<String> image;

    private double Total_Price;

    private TotalPriceCallback TotalCallback;

    private Context context;

    public AdapterCart(Context context, TotalPriceCallback mCallback, ArrayList<Integer> prodID,ArrayList<String> orderName, ArrayList<Double> sub_price, ArrayList<String> image, double Total)
    {
        this.context = context;
        this.prodID = prodID;
        this.orderName = orderName;
        this.price = sub_price;
        this.image = image;
        this.Total_Price = Total;
        TotalCallback = mCallback;
        for(int i =0; i< orderName.size();i++ )
        {
            quantity.add(1);

        }
    }

    @Override
    public int getCount() {
        return orderName.size();
    }

    @Override
    public String getItem(int position) {
        return orderName.get(position);
    }


    @Override
    public long getItemId(int position) {
        return prodID.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row;
        final ListViewHolder listViewHolder;

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            listViewHolder = new ListViewHolder();
            listViewHolder.subTotal = (TextView) convertView.findViewById(R.id.subTotal);
            listViewHolder.orderName = (TextView) convertView.findViewById(R.id.from_name);
            listViewHolder.Price = (TextView) convertView.findViewById(R.id.plist_price_text);
            listViewHolder.ImgThumb = (ImageView) convertView.findViewById(R.id.list_image);
            listViewHolder.minus = (ImageView) convertView.findViewById(R.id.cart_minus_img);
            listViewHolder.delete = (ImageView) convertView.findViewById(R.id.cart_delete);
            listViewHolder.add = (ImageView) convertView.findViewById(R.id.cart_plus_img);
            listViewHolder.displayQty = (TextView) convertView.findViewById(R.id.cart_product_quantity_tv);
            convertView.setTag(listViewHolder);
        }
        else
        {
            row=convertView;
            listViewHolder= (ListViewHolder) row.getTag();
        }
        listViewHolder.orderName.setText(orderName.get(position));
        Picasso.with(context)
                .load(image.get(position))
                .placeholder(R.drawable.img_banner_placeholder)
                .fit()
                .into(listViewHolder.ImgThumb);

        Double thePrice = price.get(position);

        listViewHolder.Price.setText("$" + thePrice);
        Log.d("Check", quantity.get(position).toString());
        try
        {
            listViewHolder.displayQty.setText(quantity.get(position).toString());
            double SubPrice = (quantity.get(position)) * thePrice;
            TotalCallback.onTotal(Total_Price);
            SubPrice = Double.parseDouble(formatData.format(SubPrice));
            listViewHolder.subTotal.setText(Double.toString(SubPrice));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        listViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity.set(position, (quantity.get(position) + 1));
                listViewHolder.displayQty.setText(quantity.get(position).toString());
                Double thePrice = price.get(position);
                Double SubPrices = (quantity.get(position)) * thePrice;
                Total_Price = SubPrices;
                TotalCallback.onTotal(Total_Price);
                SubPrices = Double.parseDouble(formatData.format(SubPrices));
                listViewHolder.subTotal.setText(Double.toString(SubPrices));
            }
        });

        listViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(context);
                db.deleteData(prodID.get(position));
                Toast.makeText(context,"Item deleted",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context,Cartz.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


        listViewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(quantity.get(position)>0){
                        quantity.set(position,(quantity.get(position) - 1));
                        listViewHolder.displayQty.setText(quantity.get(position).toString());

                        Double thePrice = price.get(position);
                        double SubPricez = (quantity.get(position)) * thePrice;
                        Total_Price = SubPricez;
                        TotalCallback.onTotal(Total_Price);


                        SubPricez = Double.parseDouble(formatData.format(SubPricez));
                        listViewHolder.subTotal.setText(Double.toString(SubPricez));





                    }}

        });


        return convertView;
    }

    private class ListViewHolder {
        public TextView subTotal;
        public TextView orderName;
        public TextView Price;
        public ImageView ImgThumb;
        public ImageView minus;
        public ImageView add;
        public ImageView delete;
        public TextView displayQty;

    }


}

