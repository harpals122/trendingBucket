package com.trending.trending_bucket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trending.trending.R;

import static com.trending.trending_bucket.util.utilMethods.isUserSignedIn;

/**
 * @author prabh
 * @class NavigationAdapter
 * @brief Adapter for populating navigation drawer list view
 */

public class NavigationAdapter extends BaseExpandableListAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private String[] options;
    private int[] optionImages;
    private int[] optionImagesSelected;


    private String[] profileOptions = {"Update profile", "Change Password"};
    private String[] socialOptions = {"email us","call us", "facebook", "twitter", "google+", "linkedin"};

    public NavigationAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        /*
        * ! different options will be available based on user singed in/ signed out condition
        * to change the options modify options, optionImages, optionImagesSelected array
        * optionImages and optionImagesSelected are the array of images to show on idle and
        * on selection mode.
        */
        if (isUserSignedIn(context)) {
            options = new String[]{"Home", "Profile", "Contact Us", "Log out"};

            optionImages = new int[]{R.drawable.ic_home,
                    R.drawable.ic_profile,
                    R.drawable.ic_social,
                    R.drawable.ic_log_out};

            optionImagesSelected = new int[]{R.drawable.ic_home_tap,
                    R.drawable.ic_profile_tap,
                    R.drawable.ic_social_tap,
                    R.drawable.ic_log_out_tap};
        } else {

            options = new String[]{"Home", "Contact Us"};

            optionImages = new int[]{R.drawable.ic_home,
                    R.drawable.ic_social};

            optionImagesSelected = new int[]{R.drawable.ic_home_tap,
                    R.drawable.ic_social_tap};
        }
    }

    @Override
    public int getGroupCount() {
        return options.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (isUserSignedIn(context)) {
            if (groupPosition == 1)
                return profileOptions.length;
            else if (groupPosition == 2)
                return socialOptions.length;
            else
                return 0;
        } else {
            if (groupPosition == 1)
                return socialOptions.length;
            else
                return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return options[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (isUserSignedIn(context)) {
            if (groupPosition == 1)
                return profileOptions[childPosition];
            else if (groupPosition == 2)
                return socialOptions[childPosition];
            else
                return null;
        } else {
            if (groupPosition == 1)
                return socialOptions[childPosition];
            else
                return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;

    }

    @Override
    public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.layout_navigation_list_item, null);

        ImageView icon = (ImageView) convertView.findViewById(R.id.navItemImgView);
        ImageView arrowIcon = (ImageView) convertView.findViewById(R.id.navItemArrowImgView);
        TextView text = (TextView) convertView.findViewById(R.id.navItemTxtView);
        text.setText(options[groupPos]);
        icon.setImageResource(optionImages[groupPos]);

        if (((TextView) convertView.findViewById(R.id.navItemTxtView)).getText().equals("Home"))
            ((ImageView) convertView.findViewById(R.id.navItemArrowImgView)).setVisibility(View.INVISIBLE);

        if (((TextView) convertView.findViewById(R.id.navItemTxtView)).getText().equals("Log out"))
            ((ImageView) convertView.findViewById(R.id.navItemArrowImgView)).setVisibility(View.INVISIBLE);

        if (isExpanded) {

            if (!((TextView) convertView.findViewById(R.id.navItemTxtView)).getText().equals("Home") &&
                    !((TextView) convertView.findViewById(R.id.navItemTxtView)).getText().equals("Log out")) {
                icon.setImageResource(optionImagesSelected[groupPos]);
                text.setTextColor(context.getResources().getColor(R.color.purple_color_2));
            }

        } else {

            if (!((TextView) convertView.findViewById(R.id.navItemTxtView)).getText().equals("Home") &&
                    !((TextView) convertView.findViewById(R.id.navItemTxtView)).getText().equals("Log out")) {
                icon.setImageResource(optionImages[groupPos]);
                text.setTextColor(context.getResources().getColor(R.color.nav_list_text_color_1));
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_navigation_inner_list_item, null);
        }

        if (isUserSignedIn(context)) {

            if (groupPosition == 1) {
                TextView tv = (TextView) convertView.findViewById(R.id.navInnerItemTxtView);
                tv.setText(profileOptions[childPosition]);
            }

            if (groupPosition == 2) {
                TextView tv = (TextView) convertView.findViewById(R.id.navInnerItemTxtView);
                tv.setText(socialOptions[childPosition]);
            }
        } else {

            if (groupPosition == 1) {
                TextView tv = (TextView) convertView.findViewById(R.id.navInnerItemTxtView);
                tv.setText(socialOptions[childPosition]);
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
