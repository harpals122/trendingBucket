package com.trending.trending_bucket.adapters;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trending.trending.R;
import com.trending.trending_bucket.fragment.SubCategoryFragment;
import com.trending.trending_bucket.model.Category;

import java.util.ArrayList;

/**
 * @author prabh
 * @class SubCategoryAdapter
 * @brief Adapter class for populating sub-category in list view
 */

public class SubCategoryAdapter extends ArrayAdapter<Category> implements View.OnClickListener {

    private final LayoutInflater inflater;
    private final ArrayList<Category> subCategoryList;
    private SubCategoryFragment.SubCategorySelectionCallbacks mCallbacks;
    private int i = 0;

    public SubCategoryAdapter(Activity activity, SubCategoryFragment.SubCategorySelectionCallbacks mCallbacks, ArrayList<Category> subCategoryList) {
        super(activity, R.layout.layout_sub_category_list);
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.subCategoryList = subCategoryList;
        this.mCallbacks = mCallbacks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder row;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_sub_category_list, null);
            row = new ViewHolder();
            row.subCategoryName = (TextView) convertView.findViewById(R.id.subCatTxtView);
            convertView.setTag(row);
        } else {
            row = (ViewHolder) convertView.getTag();
        }

        Category category = subCategoryList.get(position);
        row.subCategoryName.setText(category.getTitle());
        row.subCategoryName.setOnClickListener(this);
        row.subCategoryName.setTag(position);
        return convertView;
    }

    @Override
    public int getCount() {
        return subCategoryList.size();
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        Log.d("Key",subCategoryList.get(position).getId());
        mCallbacks.onSubCategorySelected(subCategoryList.get(position).getId(), subCategoryList.get(position).getTitle());
    }

    private static class ViewHolder {
        public TextView subCategoryName;
    }
}
