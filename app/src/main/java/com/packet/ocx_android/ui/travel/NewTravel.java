package com.packet.ocx_android.ui.travel;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.connection.DirectionsController;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.controllers.maps.Maps;
import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.models.Deplacements;
import com.packet.ocx_android.models.Travel_Type;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Address.AddressDB;
import com.packet.ocx_android.ui.database.Deplacements.DeplacementsDB;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;
import com.packet.ocx_android.ui.vehicle.ListVehicle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class NewTravel extends Fragment{//} implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public MapView mapView;
    Spinner type, vehicle, addresses;
    public EditText date, add_start, add_end, time_s, time_e, travel_time, distance, comment;
    RadioButton rb_spin, rb_manual;
    SharedPreferences sharedPreferences;
    public static Deplacements travel = new Deplacements();
    LatLng address_start = null, address_end = null;
    Button save;
    Switch toggle;
    //Marker m_start, m_end;
    GoogleMap googlemap;
    JSONObject response = null;

    public NewTravel() {}

    public static DetailTravel newInstance(String param1, String param2) {
        DetailTravel fragment = new DetailTravel();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_travel, container, false);
        sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        type = view.findViewById(R.id.spin_travel_type);
        vehicle = view.findViewById(R.id.spin_travel_new_vehicle);
        addresses = view.findViewById(R.id.spin_add_E);
        rb_spin = view.findViewById(R.id.rb_spinner);
        rb_manual = view.findViewById(R.id.rb_manual);
        date = view.findViewById(R.id.txt_date);
        add_start = view.findViewById(R.id.txt_add_S);
        add_end = view.findViewById(R.id.txt_add_E);
        time_s = view.findViewById(R.id.txt_time_S);
        travel_time = view.findViewById(R.id.txt_duration);
        time_e = view.findViewById(R.id.txt_time_E);
        distance = view.findViewById(R.id.txt_dist);
        comment = view.findViewById(R.id.txt_comment);
        save = view.findViewById(R.id.btn_save);
        toggle = view.findViewById(R.id.toggleButton);

        getLatLongStart();

        //SPINNER
        ArrayList<Travel_Type> types = new ArrayList<Travel_Type>();
        types.add(new Travel_Type(1,"Personnel"));
        types.add(new Travel_Type(2,"Entreprise"));


        ArrayAdapter<Travel_Type> spinnerArrayAdapterType = new ArrayAdapter<Travel_Type>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        type.setAdapter(spinnerArrayAdapterType);

        int selected = 0;
        VehiclesDB vdb = new VehiclesDB(getContext());
        vdb.openForRead();
        ArrayList<Vehicle> vehicles = vdb.getVehicles();
        vdb.close();
        ArrayList<String> vehicle_names = new ArrayList<>();
        String str_vehicle = sharedPreferences.getString("PREF_VEHICLE", null);

        Gson gson = new Gson();
        Vehicle vehicle_obj = gson.fromJson(str_vehicle, Vehicle.class);

        for(int i = 0; i <vehicles.size(); i++){
            Vehicle v = vehicles.get(i);
            if(vehicle_obj.id == v.id)
                selected = i;
            if(v.nickname == null || v.nickname.contentEquals("") || v.nickname.contentEquals("aucun"))
                vehicle_names.add(v.brand + " " + v.model);
            else
                vehicle_names.add(v.nickname);
        }
        SpinnerAdapter spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, vehicle_names);
        ((ArrayAdapter) spinnerArrayAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        vehicle.setAdapter(spinnerArrayAdapter);


        AddressDB adb = new AddressDB(getContext());
        adb.openForRead();
        ArrayList<com.packet.ocx_android.models.Address> addresses_list = adb.getAddresses();
        adb.close();

        ArrayList<String> names = new ArrayList<>();
        for(Address a : addresses_list){
            names.add(a.description);
        }

        SpinnerAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
        ((ArrayAdapter) adapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addresses.setAdapter(adapter);

        vehicle.setSelection(selected);
        date.setText(MainActivity.getDate());
        time_s.setText(MainActivity.getTime());

        distance.setEnabled(false);
        time_s.setEnabled(false);
        time_e.setEnabled(false);
        travel_time.setEnabled(false);
        travel.setAllez(false);


        //MAP
        /*
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);*/

        add_end.setVisibility(View.INVISIBLE);
        addresses.setVisibility(View.VISIBLE);
        rb_spin.setChecked(true);
        addresses.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        rb_spin.setOnClickListener(v->{
            addresses.setVisibility(View.VISIBLE);
            add_end.setVisibility(View.INVISIBLE);
        });

        rb_manual.setOnClickListener(v->{
            addresses.setVisibility(View.INVISIBLE);
            add_end.setVisibility(View.VISIBLE);
        });

        addresses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel.setAddressArrive(addresses_list.get(position).toString());
                try {
                    address_end = Maps.getLocationFromAddress(getContext(), addresses_list.get(position).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel.setVehicle_id(vehicles.get(position).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel.setDeplacement_type_id(types.get(position).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        add_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!add_end.getText().toString().trim().isEmpty())
                travel.setAddressArrive(add_end.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        add_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!add_start.getText().toString().trim().isEmpty())
                travel.setAddressDepart(add_start.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        toggle.setOnClickListener(v->{

            Log.e("NEW", "clicked");

            if(toggle.isChecked())
                travel.setAllez(true);
            else
                travel.setAllez(false);
        });


        save.setOnClickListener(v->{

            //VALIDATE ADDRESS FIELDS
            if(travel.addressArrive.contentEquals("")){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle(R.string.alert_error)
                        .setMessage(R.string.alert_new_travel_address_end)
                        .setPositiveButton("OK", (dia, gs) -> {})
                        .show();
            }
            else {
                boolean valid = true;
                try {
                    travel.setDateDeplacement(MainActivity.getDate());
                    travel.setTime_start(MainActivity.getTime());

                    address_end = Maps.getLocationFromAddress(getContext(), travel.addressArrive);


                    if (address_end != null) {

                    } else {
                        valid = false;
                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_delete)
                                .setTitle(R.string.alert_error)
                                .setMessage(R.string.alert_new_travel_address_end)
                                .setPositiveButton("OK", (dia, gs) -> {
                                    //ERASE NEW TRAVEL FROM DB
                                }).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (valid) {

                    //GET ITINERARY ESTIMATE
                        String str_start = DirectionsController.stringFormatter(travel.addressDepart);
                        String str_end = DirectionsController.stringFormatter(travel.addressArrive);
                        response = DirectionsController.getDirections(str_start, str_end);

                    Log.e("NEW TRAVEL", response.toString());

                        try {
                            String distance_km = response.getJSONArray("routes")
                                    .getJSONObject(0).getJSONArray("legs")
                                    .getJSONObject(0).getJSONObject("distance")
                                    .getString("value");

                            String duration = response.getJSONArray("routes")
                                    .getJSONObject(0).getJSONArray("legs")
                                    .getJSONObject(0).getJSONObject("duration")
                                    .getString("text");

                            String exp_arrival = response.getJSONArray("routes")
                                    .getJSONObject(0).getJSONArray("legs")
                                    .getJSONObject(0).getJSONObject("duration")
                                    .getString("value"); //in seconds


                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                            Date now = new Date();
                            long seconds = now.getTime();
                            seconds = seconds + (Integer.parseInt(exp_arrival) * 1000); //*1000 bc miliseconds
                            Date then = new Date(seconds);
                            double kilo = Double.parseDouble(distance_km);
                            travel.setKilomitrage(kilo/1000);
                            travel.setTemps(duration);
                            travel.setTime_end(sdf.format(then));
                            travel.setUser_id(MainActivity.user.id);
                            if(comment.getText().toString().trim().isEmpty())
                                travel.setComment("aucun");
                            else
                                travel.setComment(comment.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle(R.string.alert_confirm)
                            .setMessage(R.string.alert_confirmation_new_travel)
                            .setPositiveButton("Oui", (d1, v1) -> {

                                //setRemainingFields();

                                new Thread(()->{
                                    // SAVE TO SQLITE
                                    DeplacementsDB ddb = new DeplacementsDB(getContext());
                                    ddb.openForWrite();
                                    ddb.insertDeplacements(travel);
                                    ddb.close();

                                    // SAVE TO SERVER
                                    try {
                                        HttpResponse response = Request.POST("addDeplacement", travel.toJSON(), true);
                                        if (response.getStatusLine().getStatusCode() < 300) {
                                            ddb.openForWrite();
                                            ddb.delete(travel);
                                            ddb.close();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }).start();

                                //REDIRECT

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container_travels_section, ListTravel.class, null)
                                        .commit();

                                //ACTIVATE GOOGLE MAPS NAVIGATION

                                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + address_start.latitude + "," + address_start.longitude + "&daddr=" + address_end.latitude + "," + address_end.longitude);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                }
                            })
                            .setNegativeButton("Non", (d2, v2) -> {
                                new AlertDialog.Builder(getContext())
                                        .setIcon(android.R.drawable.ic_delete)
                                        .setTitle("ANNULATION")
                                        .setMessage("Les données saisies seront effacées.")
                                        .setPositiveButton("OUI", (dia, gs) -> {
                                            //ERASE NEW TRAVEL FROM DB
                                        })
                                        .setNegativeButton("ANNULER", (dia, gs) -> {
                                            setRemainingFields();
                                        })
                                        .show();
                            })
                            .show();
                }
            }
        });

        return view;
    }

    public void setRemainingFields(){
        time_e.setText(travel.time_end);
        distance.setText(String.valueOf(travel.kilomitrage) + " km");
        travel_time.setText(travel.temps);
    }

    public void getLatLongStart(){
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
                        address_start = new LatLng(location.getLatitude(), location.getLongitude());

                        try {
                            String address = Maps.getAddressFromLocation(getActivity(), location.getLatitude(), location.getLongitude());
                            add_start.setText(address);
                            travel.setAddressDepart(address);

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


/*
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googlemap = googleMap;
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
                        address_start = new LatLng(location.getLatitude(), location.getLongitude());

                        try {
                            String address = Maps.getAddressFromLocation(getActivity(), location.getLatitude(), location.getLongitude());
                            add_start.setText(address);
                            travel.setAddressDepart(address);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int height = 100;
                        int width = 100;
                        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        m_start = googleMap.addMarker(new MarkerOptions().position(address_start).title("START").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        LatLng dflt = new LatLng(0, 0);
                        m_end =  googleMap.addMarker(new MarkerOptions().position(dflt).title("END").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        m_end.setVisible(false);

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address_start, 10f));
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);


    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
     */

}