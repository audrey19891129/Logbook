package com.packet.ocx_android.controllers.connection;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.packet.ocx_android.controllers.user.UserController;
import com.packet.ocx_android.models.Depences;
import com.packet.ocx_android.models.User;
import com.packet.ocx_android.ui.database.User.UserDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import kotlin.text.Charsets;

public class Request {
    private static int timeout = 15; // <--- 15 seconds tu peut le modifier pour tester
    public static  String TAG = "REQUEST";
    private static CloseableHttpClient client;
    private static String BASE_URL =  "https://dev.buildup4u.com/transport/public/api/";
    //private static String BASE_URL = "http://192.168.0.106:60214/api/";

    public static JSONObject GET_obj(String service){
        SyncHttpClient clients = new SyncHttpClient();
        JSONObject jsonObject[] = new JSONObject[1];
        clients.setConnectTimeout(timeout * 1000);
        clients.setResponseTimeout(timeout * 1000);

        clients.get(BASE_URL + service, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                jsonObject[0] = response;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                jsonObject[0] = errorResponse;
            }
        });
        return jsonObject[0];
    }

    public static JSONArray GET_array(String service){
        SyncHttpClient clients = new SyncHttpClient();
        final JSONArray[] jsonElements = {new JSONArray()};
        clients.get(BASE_URL + service, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                jsonElements[0] = response;
            }
        });
        return jsonElements[0];
    }



    public static HttpResponse POST(String service, String obj, boolean token) throws IOException, JSONException {

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost httpPost = new HttpPost(BASE_URL + service);
        StringEntity entity = new StringEntity(obj, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        if(token){
            httpPost.addHeader("Authorization", "Bearer " + UserController.ACCESS_TOKEN);
        }
        HttpResponse response = client.execute(httpPost);

        Log.e(TAG, response.toString());
        Log.e(TAG, obj);

        return response;
    }


    public static HttpResponse PUT(String service, String obj, boolean token) throws IOException, JSONException {

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPut httpPost = new HttpPut(BASE_URL + service);
        StringEntity entity = new StringEntity(obj, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        if(token){
            httpPost.addHeader("Authorization", "Bearer " + UserController.ACCESS_TOKEN);
        }
        HttpResponse response = client.execute(httpPost);

        Log.e(TAG, response.toString());
        Log.e(TAG, obj);

        return response;
    }

    public static HttpResponse GET(String service, boolean token) throws IOException, JSONException {

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpGet = new HttpGet(BASE_URL + service);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        if(token){
            httpGet.addHeader("Authorization", "Bearer " + UserController.ACCESS_TOKEN);
        }
        HttpResponse response = client.execute(httpGet);

        return response;
    }


    public static String Md5(String md5){
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
