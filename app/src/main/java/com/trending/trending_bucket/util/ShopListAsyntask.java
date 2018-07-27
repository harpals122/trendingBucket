package com.trending.trending_bucket.util;

import android.os.AsyncTask;

import com.trending.trending_bucket.interfaces.shopinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by prabh kaur on 27-03-2018.
 */
public class ShopListAsyntask extends AsyncTask<Void, Void, String> {
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    com.trending.trending_bucket.interfaces.shopinterface shopinterface;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String login_url,JSON_STRING,json_string;

    public ShopListAsyntask() {

    }

    public ShopListAsyntask(shopinterface shopinterface) {
this.shopinterface=shopinterface;
    }

    @Override
    protected void onPreExecute() {
        login_url = "http://theownthoughts.com/trending/shoplist.php";
        shopinterface.shopprogress();
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            url = new URL(login_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);



            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {

                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            json_string = stringBuilder.toString().trim();

            return json_string;
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
    shopinterface.shoppostresult(result);
    shopinterface.shopprogressfinished();
    }
}





