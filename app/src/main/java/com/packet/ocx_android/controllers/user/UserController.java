package com.packet.ocx_android.controllers.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.User;
import com.packet.ocx_android.ui.database.User.UserDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import kotlin.text.Charsets;

public class UserController {

    public static SharedPreferences sharedPreferences;
    public static String ACCESS_TOKEN;

    public static boolean logIn(String username, String password, Context context) throws JSONException, IOException {
        String crypted = Request.Md5(password);
        String json = "{\"email\":\"" + username+ "\", \"password\":\"" + crypted + "\"}";

        HttpResponse response = Request.POST("userLogin", json, false);

        if(response.getStatusLine().getStatusCode()<300){
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
            String jsonContent = br.readLine();
            JSONObject obj = new JSONObject(jsonContent);
            if(!obj.has("user")){
                return false;
            }
            else{
                JSONObject json_user = obj.getJSONObject("user");
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(json_user.toString(), User.class);
                UserDB udb = new UserDB(context);
                udb.openForWrite();
                udb.getDb().execSQL("DELETE FROM User");
                user.setPassword(password);
                udb.insert(user);
                udb.close();

                String token = obj.get("access_token").toString();
                sharedPreferences = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ACCESS_TOKEN", token).apply();
                ACCESS_TOKEN = token;

                Log.e("USERCONTROLLER", token);

                return true;
            }
        }
        else{
            return false;
        }
    }
}

