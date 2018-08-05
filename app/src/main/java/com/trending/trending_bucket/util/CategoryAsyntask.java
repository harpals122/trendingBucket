package com.trending.trending_bucket.util;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.trending.trending_bucket.interfaces.categoryinterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by prabh kaur on 27-03-2018.
 */
public class CategoryAsyntask extends AsyncTask<Void, Void, String> {
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    private WeakReference<ProgressBar> textViewRef;
    String login_url,JSON_STRING,json_string;
    private final com.trending.trending_bucket.interfaces.categoryinterface categoryinterface;

    public CategoryAsyntask(categoryinterface categoryinterface) {
   this.categoryinterface=categoryinterface;
    }

    @Override
    protected void onPreExecute() {
        categoryinterface.categoryprogress();
        login_url = "http://quiz8tnow.com/trending/allcategories.php";
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
          url = new URL(login_url);
             httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            String s="1";
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("shop_id", "UTF-8") + "=" + URLEncoder.encode(s);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();


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
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        categoryinterface.categorypostresult(result);
categoryinterface.categoryrogressfinished();
    }
}





