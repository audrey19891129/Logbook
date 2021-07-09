package com.packet.ocx_android.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.controllers.adapters.CustomAdapter_Vehicle;
import com.packet.ocx_android.controllers.connection.DirectionsController;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.controllers.maps.Maps;
import com.packet.ocx_android.models.Deplacements;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.address.Address_Section;
import com.packet.ocx_android.ui.address.ListAddress;
import com.packet.ocx_android.ui.address.Repertory;
import com.packet.ocx_android.ui.database.Deplacements.DeplacementsDB;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;
import com.packet.ocx_android.ui.expense.Expense_Section;
import com.packet.ocx_android.ui.travel.NewTravel_V2;
import com.packet.ocx_android.ui.travel.Travel_Section;
import com.packet.ocx_android.R;
import com.packet.ocx_android.ui.vehicle.Vehicle_Section;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;

public class DashboardFragment extends Fragment {

    TextView txt_fname;
    ImageButton repertory, travels, vehicle_section, auto_track, expenses_section, report_section;
    private SharedPreferences sharedPreferences;
    Handler mHandler;
    ArrayList<Vehicle> vehicles;
    ImageView track_img;
    public static Deplacements ongoing = null;
    VehiclesDB vdb;
    DeplacementsDB ddb;
    LatLng address_start = null;
    LatLng address_end = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_dashboard);

        vdb = new VehiclesDB(getContext());
        ddb = new DeplacementsDB(getContext());

        sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        txt_fname = view.findViewById(R.id.txt_firstName);
        repertory = view.findViewById(R.id.btn_repertory);
        travels = view.findViewById(R.id.btn_Travels);
        vehicle_section = view.findViewById(R.id.btn_Vehicles);
        auto_track = view.findViewById(R.id.btn_Track);
        track_img = view.findViewById(R.id.iv_track);
        expenses_section = view.findViewById(R.id.btn_Expenses);
        report_section = view.findViewById(R.id.btn_Report);


        String[] strings = MainActivity.user.name.split(" ");
        txt_fname.setText(strings[0].toUpperCase());

        //SPINNER
        vdb.openForRead();
        vehicles = vdb.getVehicles();
        vdb.close();
        Spinner options = view.findViewById(R.id.spin_vehicles_options);
        CustomAdapter_Vehicle customAdapter=new CustomAdapter_Vehicle(getContext(),vehicles);
        options.setAdapter(customAdapter);

        ddb.openForRead();
        ongoing = ddb.fetchOngoing();
        ddb.close();

        if(ongoing == null){
            track_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
        }
        else{
            track_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
            track_img.setImageResource(R.drawable.ic_stop);
        }


        options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson  = new Gson();
                String string_vehicle =gson.toJson(vehicles.get(position));
                editor.putString("PREF_VEHICLE", string_vehicle).apply();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        repertory.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, Address_Section.class, null)
                    .commit();
        });

        report_section.setOnClickListener(v->{

        });

        travels.setOnClickListener(v->{

            if(vehicles.size() == 0){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle(R.string.alert_error)
                        .setMessage("Vous devez d'abord créer un véhicule pour accéder à cette fonctionnalité.")
                        .setPositiveButton("OK", (dia, gs) -> {})
                        .show();
            }
            else {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, Travel_Section.class, null)
                        .commit();
            }
        });

        vehicle_section.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, Vehicle_Section.class, null)
                    .commit();
        });

        expenses_section.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, Expense_Section.class, null)
                    .commit();
        });

        auto_track.setOnClickListener(v->{

            if(vehicles.size() == 0){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle(R.string.alert_error)
                        .setMessage("Vous devez d'abord créer un véhicule pour accéder à cette fonctionnalité.")
                        .setPositiveButton("OK", (dia, gs) -> {})
                        .show();
            }
            else {

                if (ongoing == null) {

                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("CONFIRMATION")
                            .setMessage("Etes-vous sur(e) de vouloir activer un nouveau deplacement?")
                            .setPositiveButton(R.string.btn_yes, (dia, d) -> {

                                ongoing = new Deplacements();
                                ongoing.setId(0);
                                ongoing.setUser_id(MainActivity.user.id);
                                ongoing.setVehicle_id(vehicles.get(options.getSelectedItemPosition()).id);
                                ongoing.setDateDeplacement(MainActivity.getDate());
                                ongoing.setTime_start(MainActivity.getTime());

                                new AlertDialog.Builder(getContext())
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setTitle("NOUVEAU DEPLACEMENT")
                                        .setMessage("Selectionnez un type de deplacement")
                                        .setPositiveButton(R.string.travel_type_personnal, (dia2, d2) -> {
                                            ongoing.setDeplacement_type_id(1);
                                            setToDB();
                                        })
                                        .setNegativeButton(R.string.travel_type_business, (dia2, d2) -> {
                                            ongoing.setDeplacement_type_id(2);
                                            setToDB();
                                        })
                                        .setNeutralButton(R.string.btn_cancel, (dia2, d2) -> {
                                        })
                                        .show();

                            })
                            .setNegativeButton(R.string.btn_no, (dia, d) -> {
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("CONFIRMATION")
                            .setMessage("Etes-vous sur(e) de vouloir desactiver ce deplacement?")
                            .setPositiveButton(R.string.btn_yes, (dia, d) -> {

                                ongoing.setTime_end(MainActivity.getTime());
                                updateOngoing();
                            })
                            .setNegativeButton(R.string.btn_no, (dia, d) -> {
                            })
                            .show();
                }
            }
        });

        return view;
    }

    public void setToDB(){

        Log.e("DASH", "SETTODB");

        track_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
        track_img.setImageResource(R.drawable.ic_stop);
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                address_start = new LatLng(location.getLatitude(), location.getLongitude());

                try {
                    String address = Maps.getAddressFromLocation(getActivity(), location.getLatitude(), location.getLongitude());
                    ongoing.setAddressDepart(address);
                    ongoing.setComment("ongoing");
                    ddb.openForWrite();
                    ddb.insertDeplacements(ongoing);
                    ddb.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }



    public void updateOngoing(){
        track_img.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
        track_img.setImageResource(R.drawable.ic_play);

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        address_end = new LatLng(location.getLatitude(), location.getLongitude());

                        try {
                            String address = Maps.getAddressFromLocation(getActivity(), location.getLatitude(), location.getLongitude());
                            ongoing.setAddressArrive(address);
                            ongoing.setComment("aucun");
                            ddb.openForWrite();
                            ddb.update(ongoing);
                            ddb.close();
                            getEstimate();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    public void getEstimate(){
        new Thread(() -> {
            String str_start = DirectionsController.stringFormatter(ongoing.addressDepart);
            String str_end = DirectionsController.stringFormatter(ongoing.addressArrive);

            Log.e("DASH", str_start);
            Log.e("DASH", str_end);

            JSONObject response = DirectionsController.getDirections(str_start, str_end);

            try {
                String distance_km = response.getJSONArray("routes")
                        .getJSONObject(0).getJSONArray("legs")
                        .getJSONObject(0).getJSONObject("distance")
                        .getString("value");

                String duration = response.getJSONArray("routes")
                        .getJSONObject(0).getJSONArray("legs")
                        .getJSONObject(0).getJSONObject("duration")
                        .getString("text");

                double kilo = Double.parseDouble(distance_km);

                ongoing.setKilomitrage(kilo/1000);
                ongoing.setTemps(duration);
                ddb.openForWrite();
                ddb.update(ongoing);
                ddb.close();

                //SEND TO SERVER
                new Thread(()->{
                    try {
                        HttpResponse resp = Request.POST("addDeplacement", ongoing.toJSON(), true);
                        if (resp.getStatusLine().getStatusCode() < 300) {
                            ddb.openForWrite();
                            ddb.delete(ongoing);
                            ddb.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }).start();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
}