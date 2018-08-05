package com.trending.trending_bucket.APILayer;

import android.content.Context;
import android.os.Build;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.trending.trending_bucket.util.constants.ROOT_URL;

public class RetrofitAPIClient {

    public static Retrofit retrofit = null;

    public static Retrofit NYAPIClient(Context ctx) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit;
    }
}