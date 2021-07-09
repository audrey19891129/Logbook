package com.packet.ocx_android;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.controllers.user.UserController;
import com.packet.ocx_android.ui.database.Schema;
import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.User.UserDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.HttpResponse;

public class LogInActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public Button btnLogin;
    private final String PREFS_SETUP = "PREFS_SETUP";
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String now = sdf.format(cal.getTime());
    public Schema schema;
    public static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    public static ArrayList<Address> addresses = new ArrayList<Address>();
    public Handler mHandler;

    @Override
    protected void onStart() {
        super.onStart();
        schema = new Schema(getApplicationContext());
        schema.openForWrite();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mHandler=new Handler();

        sharedPreferences = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        if(CheckPermissions()){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        else{
            RequestPermissions();
        }

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener(v->{
            if(!(username.getText().toString().trim().isEmpty() && password.getText().toString().trim().isEmpty())){

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            String email = username.getText().toString().trim();
                            String pass = password.getText().toString().trim();

                            if(UserController.logIn(email, pass, LogInActivity.this)){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(PREFS_SETUP, now).apply();
                                schema.close();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else{
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(LogInActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle(R.string.alert_error)
                                                .setMessage(R.string.login_invalid)
                                                .setNeutralButton("OK", (dialog, which) -> {
                                                })
                                                .show();
                                    }
                                });

                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(LogInActivity.this, new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                } , 0);
    }

    public boolean CheckPermissions() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(LogInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(LogInActivity.this, Manifest.permission.CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(LogInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(LogInActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 0 :{
                for(int granted : grantResults){
                    if(granted == 0){
                        if (
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        ) return;

                    }else {restart();}
                }
            }
        }
    }

    private void restart() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 2;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}