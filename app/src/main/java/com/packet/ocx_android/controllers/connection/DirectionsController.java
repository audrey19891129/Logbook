package com.packet.ocx_android.controllers.connection;

import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class DirectionsController {
    //"https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=YOUR_API_KEY";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String API_KEY = "&key=AIzaSyBk1SJpGo2KxQC--3p9ESdHTCfpxS9g8aY";
    private static final String ORIGIN = "origin=";
    private static final String DESTINATION = "&destination=";
    public static final String UNITS = "&units=metric";
    private static final int timeout = 15;
    public JSONObject response;

    public static JSONObject getDirections(String origin, String destination){
        SyncHttpClient connection = new SyncHttpClient();
        JSONObject jsonObject[] = new JSONObject[1];
        connection.setConnectTimeout(timeout * 1000);
        connection.setResponseTimeout(timeout * 1000);

        Log.e("TAG", BASE_URL + ORIGIN + origin + DESTINATION + destination + UNITS + API_KEY);

        connection.get(BASE_URL + ORIGIN + origin + DESTINATION + destination + UNITS + API_KEY, new JsonHttpResponseHandler() {
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

    public static String stringFormatter(String value){
        String step1 = value.replace(" ", "+");
        String step2 = step1.replace("é", "e");
        String step3 = step2.replace("è", "e");
        String step4 = step3.replace("î", "i");
        String step5 = step4.replace("'", "+");
        return step5;
    }

    public static String addressFormatter(String value){
        String step2 = value.replace("é", "e");
        String step3 = step2.replace("è", "e");
        String step4 = step3.replace("î", "i");
        return step4;
    }

    public static String requestFormatter(String origin, String destination){
        return BASE_URL + "saddr" + origin + "&daddr=" + destination + "=" + API_KEY;
    }
}
