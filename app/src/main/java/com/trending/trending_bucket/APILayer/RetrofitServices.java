package com.trending.trending_bucket.APILayer;

import android.content.Context;

public class RetrofitServices {

    private static RetrofitServices nyServiceInstance = null;
    Context ctx;

    public RetrofitServices(Context context) {
        this.ctx = context;
    }

    public static RetrofitServices getNYServiceInstance(Context ctx) {
        if (nyServiceInstance == null) {
            nyServiceInstance = new RetrofitServices(ctx);
        }

        return nyServiceInstance;
    }

//
//    public Call<ProductData> updateProducts(JsonObject requestBody) {
//        return NYAPIClient.NYAPIClient(ctx).create(ProductsInterface.class).updateProducts(requestBody);
//    }
}

