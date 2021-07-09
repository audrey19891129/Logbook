package com.packet.ocx_android.controllers;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.Vehicles_Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class DropDown {
    
    public static JSONArray getList(String service, boolean token){

        JSONArray list = null;
        try {
            HttpResponse response = Request.GET(service, token);

            if(response.getStatusLine().getStatusCode()<300){
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
                String jsonContent = br.readLine();
                list = new JSONArray(jsonContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
