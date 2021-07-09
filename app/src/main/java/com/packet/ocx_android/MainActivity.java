package com.packet.ocx_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.packet.ocx_android.controllers.user.UserController;
import com.packet.ocx_android.models.User;
import com.packet.ocx_android.services.init.Service_Init;
import com.packet.ocx_android.ui.database.Schema;
import com.packet.ocx_android.ui.database.User.UserDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private SharedPreferences sharedPreferences;
    private final String PREFS_SETUP = "PREFS_SETUP";
    public static double LAT;
    public static double LONG;
    public static Schema schema;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this needs a fix
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //

        sharedPreferences = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(PREFS_SETUP)){
            toLogIn();
        }
        else {
            UserDB udb = new UserDB(getApplicationContext());
            udb.openForRead();
            user = udb.getUser();
            udb.close();


            try {
                if (!UserController.logIn(user.email, user.password, MainActivity.this)) {
                    toLogIn();
                }
            } catch (IOException | JSONException e) {
                toLogIn();
                e.printStackTrace();
            }

            schema = new Schema(getApplicationContext());
            schema.openForWrite();
            //schema.close();
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.detailVehicle)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

            Service_Init.scheduleJob(MainActivity.this);
        }
    }

    public static String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(cal.getTime());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LAT = location.getLatitude();
        LONG = location.getLongitude();
    }

    public void toLogIn(){
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}