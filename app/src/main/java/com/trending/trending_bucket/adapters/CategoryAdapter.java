package com.trending.trending_bucket.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trending.trending.R;
import com.trending.trending_bucket.fragment.CategoryFragment;
import com.trending.trending_bucket.model.Category;

import java.util.ArrayList;

/**
 * @author prabh
 * @class CategoryAdapter
 * @brief Adapter class for populating category in list view
 */

public class CategoryAdapter extends ArrayAdapter<Category> implements View.OnClickListener {

    private final LayoutInflater inflater;
    private final ArrayList<Category> categoryList;
    AbsListView.LayoutParams params;
    private Activity activity;
    private CategoryFragment.CategorySelectionCallbacks mCallbacks;
    private String dummyUrl = "http://www.org";

    public CategoryAdapter(Activity activity, CategoryFragment.CategorySelectionCallbacks mCallbacks, ArrayList<Category> categoryList) {
        super(activity, R.layout.layout_category_list);
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.categoryList = categoryList;
        this.mCallbacks = mCallbacks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder row;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_category_list, null);
            row = new ViewHolder();
            row.bannerImage = (ImageView) convertView.findViewById(R.id.catBannerImageView);
            row.categoryName = (TextView) convertView.findViewById(R.id.catNameTV);
//            params = (AbsListView.LayoutParams) convertView.getLayoutParams();
//            if(params!=null) {
//                params.height = utilMethods.getWindowSize(activity).y / 3;
//            }
            convertView.setTag(row);
        } else {
            row = (ViewHolder) convertView.getTag();
//            params = (AbsListView.LayoutParams) convertView.getLayoutParams();
//            if(params!=null) {
//                params.height = utilMethods.getWindowSize(activity).y / 3;
//            }
        }

        Category category = categoryList.get(position);

        row.categoryName.setText(category.getTitle());
        Picasso.with(activity)
                .load(category.getImageUrl())
                .placeholder(R.drawable.img_banner_placeholder)
                .fit()
                .into(row.bannerImage);

        row.bannerImage.setOnClickListener(this);
        row.categoryName.setTag(position);
        row.bannerImage.setTag(position);
        return convertView;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        mCallbacks.onCategorySelected(categoryList.get(position).getId(),
                categoryList.get(position).getTitle());
    }

    private static class ViewHolder {
        public ImageView bannerImage;
        public TextView categoryName;
    }
}
