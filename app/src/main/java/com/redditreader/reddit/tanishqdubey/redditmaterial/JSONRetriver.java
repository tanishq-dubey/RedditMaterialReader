package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;

/**
 * Created by Tanishq Dubey on 2/16/2015.
 */
public class JSONRetriver {
    static InputStream inputStream = null;
    static JSONArray jsonArray = null;
    static  String json = "";
    
    public JSONRetriver(){
    }
    
    public String getJSONFromUrl(String url){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) !=null){
                    builder.append(line);
                }
            } else if (statusCode == 429) {
                Log.e("ERROR: >>", "Too Many Requests");
            }else{
                Log.e("ERROR: >>", "Failed to download file, HTTP ERROR: "+statusCode);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try{
            jsonArray = new JSONArray(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
