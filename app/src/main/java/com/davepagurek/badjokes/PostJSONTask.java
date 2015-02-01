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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dave_000 on 2014-11-21.
 */
public class PostJSONTask extends AsyncTask<String, Void, JSONObject>  {
    AddActivity main;
    String _q = "";
    String _a = "";

    public void setCallbackInstance(AddActivity instance) {
        main = instance;
    }

    protected JSONObject doInBackground(String... url) {
        Boolean change = false;
        HttpResponse httpResponse = null;
        HttpEntity httpEntity=null;
        String json=null;
        InputStream is = null;

        _q = url[1];
        _a = url[2];

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("question", _q));
            nameValuePairs.add(new BasicNameValuePair("answer", _a));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            Log.wtf("test", url[0]);

            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
            return null;
        }
        JSONObject object = new JSONObject();
        try {
            object = new JSONObject(json);

            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(JSONObject result) {
        if (result == null) {
            main.error("connection");
            return;
        }
        try {
            String status = result.getString("status");

            Log.e("test", status);

            if (status.equals("success")) {
                int id = result.getInt("id");
                //Boolean change = result.getBoolean("change");

                main.success(id, _q, _a);
            } else {
                main.error(status);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
