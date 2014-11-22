package com.davepagurek.badjokes;

import android.os.AsyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import android.util.Log;

import android.transition.Slide;
import android.transition.Transition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dave_000 on 2014-11-21.
 */
public class GetJSONTask extends AsyncTask<String, Void, JSONObject>  {
    MainActivity main;

    public void setCallbackInstance(MainActivity instance) {
        main = instance;
    }

    protected JSONObject doInBackground(String... url) {
        HttpResponse httpResponse = null;
        HttpEntity httpEntity=null;
        String json=null;
        InputStream is = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url[0]);

            Log.wtf("test", url[0]);

            httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(JSONObject result) {
        try {
            String q = result.getString("q");
            String a = result.getString("a");
            int id = result.getInt("id");

            main.launchJoke(id, q, a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
