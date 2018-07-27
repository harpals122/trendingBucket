package com.trending.trending_bucket.interfaces;

/**
 * Created by prabh on 4/21/2018.
 */
//! callback interface listen by CategoryActivity to detect user click on category
public interface CatGridSelectionCallback{
    void onCatGridSelected(String catID, String title);
}