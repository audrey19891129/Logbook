package com.packet.ocx_android.services.init;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.controllers.DropDown;
import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.models.Depences_Type;
import com.packet.ocx_android.models.Fuel;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.models.Vehicles_Type;
import com.packet.ocx_android.ui.database.Address.AddressDB;
import com.packet.ocx_android.ui.database.Expense_Type.ExpenseTypeDB;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeDB;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeDB;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Init extends JobService {

    private static String TAG = "INIT SERVICE";
    private boolean jobCancelled = false;
    JSONArray list;



    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "job started");
        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    public void doBackgroundWork(JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run() {

                if(jobCancelled){
                    return;
                }
                Log.e(TAG, "Job Started");

                VehiclesDB vdb = new VehiclesDB(getApplicationContext());
                AddressDB adb = new AddressDB(getApplicationContext());
                VehicleTypeDB vtdb = new VehicleTypeDB(getApplicationContext());
                FuelTypeDB fdb = new FuelTypeDB((getApplicationContext()));
                ExpenseTypeDB edb = new ExpenseTypeDB(getApplicationContext());

                try {

                    // VEHICLES TABLE
                    vdb.openForWrite();
                    vdb.getDb().execSQL("DELETE FROM Vehicles");
                    list = DropDown.getList("getVehicleUser", true);
                    if(list!=null && list.length() > 0){
                        for(int i = 0; i < list.length(); i++){
                            JSONObject json_obj = list.getJSONObject(i);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Vehicle v = objectMapper.readValue(json_obj.toString(), Vehicle.class);
                            vdb.insertVehicle(v);
                        }
                        vdb.close();
                    }

                    // ADDRESS TABLE
                    list = DropDown.getList("getAddressUser", true);
                    if(list!=null && list.length() > 0){
                        adb.openForWrite();
                        adb.getDb().execSQL("DELETE FROM Address");
                        for(int i = 0; i < list.length(); i++){
                            JSONObject json_obj = list.getJSONObject(i);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Address a = objectMapper.readValue(json_obj.toString(), Address.class);
                            adb.insertAddress(a);
                        }
                        adb.close();
                    }

                    // VEHICLE_TYPE TABLE
                    list = DropDown.getList("getvehiculeType", false);
                    if(list!=null && list.length() > 0){
                        vtdb.openForWrite();
                        vtdb.getDb().execSQL("DELETE FROM Vehicle_Type");
                        for(int i = 0; i < list.length(); i++){
                            JSONObject json_obj = list.getJSONObject(i);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Vehicles_Type vt = objectMapper.readValue(json_obj.toString(), Vehicles_Type.class);
                            vtdb.insertVehicleType(vt);
                        }
                        vtdb.close();
                    }

                    // FUEL_TYPE TABLE
                    list = DropDown.getList("getFuelType", false);
                    if(list!=null && list.length() > 0){
                        fdb.openForWrite();
                        fdb.getDb().execSQL("DELETE FROM Fuel_Type");
                        for(int i = 0; i < list.length(); i++){
                            JSONObject json_obj = list.getJSONObject(i);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Fuel f = objectMapper.readValue(json_obj.toString(), Fuel.class);
                            fdb.insertFuelType(f);
                        }
                        fdb.close();
                    }

                    // TRAVEL_TYPE TABLE


                    // EXPENSE_TYPE

                    list = DropDown.getList("typeDepence", false);

                    Log.e(TAG, list.toString());

                    if(list!=null && list.length() > 0){
                        edb.openForWrite();
                        edb.getDb().execSQL("DELETE FROM Expense_Type");
                        for(int i = 0; i < list.length(); i++){
                            JSONObject json_obj = list.getJSONObject(i);
                            ObjectMapper objectMapper = new ObjectMapper();
                            Depences_Type d = objectMapper.readValue(json_obj.toString(), Depences_Type.class);
                            edb.insertExpenseType(d);
                        }
                        edb.close();
                    }


                    MainActivity.schema.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "Job Finished");
            }
        }).start();
    }
}
