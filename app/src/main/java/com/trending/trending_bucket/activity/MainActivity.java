package com.trending.trending_bucket.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.trending.trending.R;
import com.trending.trending_bucket.adapters.Viewpager_Adapter;
import com.trending.trending_bucket.adapters.category_grid_adapter;
import com.trending.trending_bucket.adapters.product_grid_adapter;
import com.trending.trending_bucket.fragment.CategoryFragment;
import com.trending.trending_bucket.fragment.NavigationDrawerFragment;
import com.trending.trending_bucket.fragment.ProductListFragment;
import com.trending.trending_bucket.fragment.SubCategoryFragment;
import com.trending.trending_bucket.fragment.shoplistfragment;
import com.trending.trending_bucket.interfaces.CatGridSelectionCallback;
import com.trending.trending_bucket.interfaces.categoryinterface;
import com.trending.trending_bucket.interfaces.productinterface;
import com.trending.trending_bucket.interfaces.shopinterface;
import com.trending.trending_bucket.util.CategoryAsyntask;
import com.trending.trending_bucket.util.ProductsAsyntask;
import com.trending.trending_bucket.util.ShopListAsyntask;
import com.trending.trending_bucket.util.utilMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.trending.trending_bucket.util.constants.isResultListFragmentOpened;
import static com.trending.trending_bucket.util.utilMethods.hideSoftKeyboard;
import static com.trending.trending_bucket.util.utilMethods.showNoInternetDialog;
import static com.trending.trending_bucket.util.utilMethods.showSoftKeyboard;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        categoryinterface, shopinterface, productinterface, NavigationDrawerFragment.NavigationDrawerCallbacks,
        CategoryFragment.CategorySelectionCallbacks, SubCategoryFragment.SubCategorySelectionCallbacks, CatGridSelectionCallback,
        utilMethods.InternetConnectionListener {

    // object of all back method

    CatGridSelectionCallback mCallback;
    // defines the actions
    private final int HOME_ACTION = 1;
    // finds if mobile is connected with internet
    utilMethods.InternetConnectionListener internetConnectionListener;
    // finds out navigation deapth
    private int navigationDepth = 0;
    // reference of search view
    private SearchView mySearchView;
    // reference of fragment manager
    private FragmentManager fragmentManager;
    // reference of navigation drawer fragment
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    // store sub title of category fetched from server
    private String subCategoryTitle = null;
    // shows result title
    private String resultListTitle = null;
    // shows title for detail view
    private String detailViewTitle = null;
    // sets the tile for search query
    private String searchQueryTitle = null;
    // geocoder libary object holds all the abstract methods belong to geocoder libarary
    Geocoder geocoder;
    // reference of address text box
    TextView Address;
    List<android.location.Address> addresses;
    // Gps tracker class object
    // stores data in the array list of shops , products , categories
    ArrayList<String> shopimage, shopname, shopid, productname, productimage, productid, categoryname, categoryid, categoryicon, productdiscount;
    //detect double click bool value
    boolean doubleclick = false;
    // stores json data in the json objects
    JSONObject categoryjsonobj, categoryjobj, shopjsonobj, shopjobj, productjsonobj, productjobj;
    // provides network info
    NetworkInfo info;
    // detect connectivity manager
    ConnectivityManager cm;

    // json array to store object data in array

    JSONArray categoryjsonarray, shopjsonarray, productjsonarray;

    // counts the number of shops , products , categories

    int categorycount = 0, productcount = 0, shopcount = 0;

    // object of shoplist fragment

    com.trending.trending_bucket.fragment.shoplistfragment shoplistfragment;

    // string store data from asyn task

    String category_string = null, product_string = null, shop_string = null;

    //  view pager adapter class object

    Viewpager_Adapter adapter;

    // reference of view pager
    ViewPager viewPager;
    //  category grid adapter class object
    category_grid_adapter categoryadapter;
    //  product_grid_adapter class object
    product_grid_adapter productadapter;
    // gridview reference
    GridView categorygridview, productgridview;
    // progress bar reference
    ProgressBar shopprogress, categoryprogress, productprogress;
    // suggestion list view to store suggestions
    private ListView suggestionListView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets layout of MainActivity
        setContentView(R.layout.activity_main);
        // sets elevation level for action bar
        getSupportActionBar().setElevation(0);
        // creates object of search manager class setting the context
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // creates object of searchableinfo class sets the info to be search
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        // get the title for the page
        mTitle = getTitle();
        // reference to navigation drawer sets layout
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //setup the drawer fragment
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        // reference to search view
        mySearchView = (SearchView) findViewById(R.id.searchView);
        // passing parameters to search view
        mySearchView.setSearchableInfo(searchableInfo);
        // passing listener
        mySearchView.setOnQueryTextListener(this);
        //setting input type
        mySearchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        // setting hint for search view
        mySearchView.setQueryHint(Html.fromHtml("<small><small>" + getString(R.string.search_hint) + "</small></small>"));
        int searchPlateId = mySearchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = mySearchView.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        int submitAreaId = mySearchView.getContext().getResources().getIdentifier("android:id/submit_area", null, null);
        View submitArea = mySearchView.findViewById(submitAreaId);
        submitArea.setBackgroundColor(R.color.gray_color);
        int searchImgIcon = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView searchImgView = (ImageView) mySearchView.findViewById(searchImgIcon);
        searchImgView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        searchImgView.setVisibility(View.GONE);
        int closeButtonId = mySearchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);

        ImageView closeButton = (ImageView) mySearchView.findViewById(closeButtonId);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySearchView.setQuery(null, false);
                utilMethods.hideSoftKeyboard(MainActivity.this);
            }
        });


        //prabh code below

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(15);
        final Context c = getBaseContext();
        categorygridview = (GridView) findViewById(R.id.categorygridview);
        productgridview = (GridView) findViewById(R.id.productgridview);
        shopprogress = (ProgressBar) findViewById(R.id.shopviewpager);

        productprogress = (ProgressBar) findViewById(R.id.pgrid);

        categoryprogress = (ProgressBar) findViewById(R.id.grid);

        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        info = cm.getActiveNetworkInfo();

        // this.registerReceiver(mBroadcastReciver, newIntentFilter("start.fragment.action"));

        mCallback = (CatGridSelectionCallback) this;

        launchHome();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Trending Bucket");
    }

    private void launchHome() {
        if (utilMethods.isConnectedToInternet(this)) {
            initHome();
        } else {
            internetConnectionListener = this;
            showNoInternetDialog(this, internetConnectionListener,
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), HOME_ACTION);
        }
    }


    private void initHome() {
        new CategoryAsyntask(MainActivity.this).execute();
        new ShopListAsyntask(MainActivity.this).execute();
        new ProductsAsyntask(MainActivity.this).execute();
    }


    public void viewallshops(View v) {
        if (info != null && info.isConnected()) {
            shoplistfragment = new shoplistfragment();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.con, shoplistfragment.newInstance()).addToBackStack(null).commit();


        } else {
            Toast.makeText(MainActivity.this, "Please make sure you have internet connection!!", Toast.LENGTH_LONG).show();
        }
    }

    public void viewallcategories(View v) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, CategoryFragment.newInstance(0))
                .addToBackStack(null).commit();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mySearchView.setQuery(query, false);
            mySearchView.clearFocus();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (mySearchView.getVisibility() == View.VISIBLE) {
            hideSearchView();
        }
        startActivity(new Intent(this, MainActivity.class));
        navigationDepth = 0;
        isResultListFragmentOpened = false;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        if (navigationDepth == 1)
            actionBar.setTitle(subCategoryTitle);
        else if (navigationDepth == 2)
            actionBar.setTitle(resultListTitle);
        else if (navigationDepth == 3)
            actionBar.setTitle(detailViewTitle);
        else if (navigationDepth == 4)
            actionBar.setTitle(searchQueryTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Options for options action bar item select
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mySearchView.isFocused()) {
            mySearchView.setFocusable(false);
        }
        hideSoftKeyboard(MainActivity.this);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                if (!mNavigationDrawerFragment.isDrawerOpen())
                    showHideSearchView();
                break;
            case R.id.action_cart:
                if (!mNavigationDrawerFragment.isDrawerOpen())
                    hideSearchView();
                Intent intent = new Intent(this, Cartz.class);
                startActivity(intent);
                break;

            case R.id.home:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHideSearchView() {
        if (mySearchView.getVisibility() == View.VISIBLE) {
            mySearchView.setFocusable(false);
            mySearchView.setVisibility(View.GONE);
            hideSoftKeyboard(this);
        } else {
            mySearchView.setVisibility(View.VISIBLE);
            mySearchView.setQuery("", false);
            mySearchView.setFocusable(true);
            mySearchView.requestFocus();
            showSoftKeyboard(this);
        }
    }

    private void hideSearchView() {
        if (mySearchView.getVisibility() == View.VISIBLE) {
            mySearchView.setVisibility(View.GONE);
        }
        hideSuggestionList();
    }

    private void hideSuggestionList() {
        if (suggestionListView != null && suggestionListView.getVisibility() == View.VISIBLE) {
            suggestionListView.setAdapter(null);
            suggestionListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCategorySelected(String catID, String title) {
        hideSearchView();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, SubCategoryFragment.newInstance(catID)).addToBackStack(null)
                .commit();
        navigationDepth++;
        getSupportActionBar().setTitle(title);
        subCategoryTitle = title;
    }

    @Override
    public void onSubCategorySelected(String subCatID, String title) {
        hideSearchView();
        Log.d("ProductList", subCatID);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, ProductListFragment.newInstance(subCatID)).addToBackStack(null)
                .commit();
        navigationDepth++;
        getSupportActionBar().setTitle(title);
        resultListTitle = title;
    }

    //Check this file
    @Override
    public boolean onQueryTextSubmit(String query) {
        mySearchView.clearFocus();
        hideSearchView();
        utilMethods.hideSoftKeyboard(this);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, ProductListFragment.newInstance("", query)).addToBackStack(null)
                .commit();
        navigationDepth = 4;
        getSupportActionBar().setTitle(query);
        searchQueryTitle = query;
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    @Override
    public void shoppostresult(String result) {
        shop_string = result;

        try {
            shopjsonobj = new JSONObject(shop_string);
            shopjsonarray = shopjsonobj.getJSONArray("shops");
            shopimage = new ArrayList<String>();
            shopid = new ArrayList<String>();
            shopname = new ArrayList<String>();
            while (shopcount < 4) {
                shopjobj = shopjsonarray.getJSONObject(shopcount);
                shopimage.add(shopjobj.getString("shop_image"));
                shopname.add(shopjobj.getString("shop_name"));
                shopid.add(shopjobj.getString("shopid"));
                shopcount++;
            }
            fragmentManager = getSupportFragmentManager();
            adapter = new Viewpager_Adapter(MainActivity.this, shopimage, shopname, shopid, fragmentManager);
            viewPager.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shopprogress() {
        shopprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void shopprogressfinished() {
        shopprogress.setVisibility(View.GONE);
    }

    @Override
    public void categorypostresult(String result) {
        category_string = result;
        try {
            categoryjsonobj = new JSONObject(category_string);
            categoryjsonarray = categoryjsonobj.getJSONArray("category");
            categoryid = new ArrayList<String>();
            categoryname = new ArrayList<String>();
            categoryicon = new ArrayList<String>();


            while (categorycount < 9) {
                categoryjobj = categoryjsonarray.getJSONObject(categorycount);
                categoryname.add(categoryjobj.getString("category_name"));
                categoryid.add(categoryjobj.getString("category_id"));
                categoryicon.add(categoryjobj.getString("category_icon"));
                categorycount++;
            }
            categoryadapter = new category_grid_adapter(MainActivity.this, mCallback, categoryname, categoryid, categoryicon);
            categorygridview.setAdapter(categoryadapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void categoryprogress() {
        categoryprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void categoryrogressfinished() {
        categoryprogress.setVisibility(View.GONE);
    }

    @Override
    public void productpostresult(String result) {

        product_string = result;
        try {
            productjsonobj = new JSONObject(product_string);
            productjsonarray = productjsonobj.getJSONArray("products");
            productimage = new ArrayList<String>();
            productdiscount = new ArrayList<String>();
            productid = new ArrayList<String>();
            productname = new ArrayList<String>();
            while (productcount < 12) {
                productjobj = productjsonarray.getJSONObject(productcount);
                productname.add(productjobj.getString("product_name"));
                productimage.add(productjobj.getString("product_image"));
                productid.add(productjobj.getString("product_id"));
                productdiscount.add(productjobj.getString("product_discount"));
                productcount++;
            }
            productadapter = new product_grid_adapter(MainActivity.this, productname, productimage, productid,productdiscount);
            productgridview.setAdapter(productadapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void productprogress() {
        productgridview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT ));
        productprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void progressprogressfinished() {

        productprogress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        getFragmentManager().popBackStack();

        return;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCatGridSelected(String subID, String title) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.con, SubCategoryFragment.newInstance(subID)).addToBackStack(null)
                .commit();
        navigationDepth++;
        getSupportActionBar().setTitle(title);
        subCategoryTitle = title;

    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == HOME_ACTION) {
            initHome();
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == HOME_ACTION) {
            this.finish();
        }
    }
}

