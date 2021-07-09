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

import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;

public class NewTravel_V2 extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public MapView mapView;
    Spinner addresses;
    public EditText add_end, comment;
    RadioButton rb_spin, rb_manual;
    SharedPreferences sharedPreferences;
    public static Deplacements travel;
    LatLng address_start = null, address_end = null;
    Button save;
    Switch toggle;
    Marker m_start = null, m_end = null;
    GoogleMap googlemap;
    JSONObject response = null;
    ImageButton personnal, business;
    public Handler handler;

    public NewTravel_V2() {
    }

    public static NewTravel_V2 newInstance(String param1, String param2) {
        NewTravel_V2 fragment = new NewTravel_V2();
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
        View view = inflater.inflate(R.layout.fragment_new_travel__v2, container, false);
        sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        addresses = view.findViewById(R.id.spin_add_E_v2);
        add_end = view.findViewById(R.id.txt_add_E_v2);
        rb_spin = view.findViewById(R.id.rb_spinner_v2);
        rb_manual = view.findViewById(R.id.rb_manual_v2);
        comment = view.findViewById(R.id.txt_comment_v2);
        save = view.findViewById(R.id.btn_save_v2);
        toggle = view.findViewById(R.id.toggleButton_v2);
        travel = new Deplacements();
        travel.setDeplacement_type_id(1);
        personnal = view.findViewById(R.id.btn_personnal_v2);
        business = view.findViewById(R.id.btn_business_v2);
        personnal.setBackgroundColor(getResources().getColor(R.color.dark_orange));
        handler = new Handler();


        VehiclesDB vdb = new VehiclesDB(getContext());
        vdb.openForRead();
        ArrayList<Vehicle> vehicles = vdb.getVehicles();
        vdb.close();
        String str_vehicle = sharedPreferences.getString("PREF_VEHICLE", null);

        Gson gson = new Gson();
        Vehicle vehicle_obj = gson.fromJson(str_vehicle, Vehicle.class);
        travel.setVehicle_id(vehicle_obj.id);


        AddressDB adb = new AddressDB(getContext());
        adb.openForRead();
        ArrayList<com.packet.ocx_android.models.Address> addresses_list = adb.getAddresses();
        adb.close();

        ArrayList<String> names = new ArrayList<>();
        for (Address a : addresses_list) {
            names.add(a.nom);
        }

        SpinnerAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
        ((ArrayAdapter) adapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        addresses.setAdapter(adapter);
        travel.setAllez(false);

        //MAP
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        add_end.setVisibility(View.INVISIBLE);
        addresses.setVisibility(View.VISIBLE);
        rb_spin.setChecked(true);
        addresses.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        rb_spin.setOnClickListener(v -> {
            addresses.setVisibility(View.VISIBLE);
            add_end.setVisibility(View.INVISIBLE);
        });

        rb_manual.setOnClickListener(v -> {
            addresses.setVisibility(View.INVISIBLE);
            add_end.setVisibility(View.VISIBLE);
        });

        personnal.setOnClickListener(v->{
            travel.setDeplacement_type_id(1);
            personnal.setBackgroundColor(getResources().getColor(R.color.dark_orange));
            business.setBackgroundColor(getResources().getColor(R.color.white));
        });
        business.setOnClickListener(v->{
            travel.setDeplacement_type_id(2);
            business.setBackgroundColor(getResources().getColor(R.color.dark_orange));
            personnal.setBackgroundColor(getResources().getColor(R.color.white));
        });

        addresses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(rb_spin.isChecked()) {
                    travel.setAddressArrive(addresses_list.get(position).toString());
                    try {
                        address_end = Maps.getLocationFromAddress(getContext(), addresses_list.get(position).toString());

                        if (address_end != null && address_start != null) {
                            mapView.invalidate();
                            setMap(googlemap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        add_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!add_end.getText().toString().trim().isEmpty()){
                    travel.setAddressArrive(add_end.getText().toString());
                    new Thread(()->{
                        try {
                            address_end = Maps.getLocationFromAddress(getContext(), travel.addressArrive);
                            if(address_end != null){
                                handler.post(()->{
                                    mapView.invalidate();
                                    setMap(googlemap);
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        toggle.setOnClickListener(v -> {

            Log.e("NEW", "clicked");

            if (toggle.isChecked())
                travel.setAllez(true);
            else
                travel.setAllez(false);
        });

        save.setOnClickListener(v -> {

            //VALIDATE ADDRESS FIELDS
            if (travel.addressArrive.contentEquals("")) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle(R.string.alert_error)
                        .setMessage(R.string.alert_new_travel_address_end)
                        .setPositiveButton("OK", (dia, gs) -> {
                        })
                        .show();
            } else {
                boolean valid = true;
                try {
                    travel.setDateDeplacement(MainActivity.getDate());
                    travel.setTime_start(MainActivity.getTime());

                    address_end = Maps.getLocationFromAddress(getContext(), travel.addressArrive);

                    if (address_end != null) {
                        m_end.setPosition(address_end);
                        m_end.setVisible(true);
                        LatLngBounds bounds;

                        if (Math.abs(address_end.latitude) < Math.abs(address_start.latitude))
                            bounds = new LatLngBounds(address_end, address_start);
                        else {
                            bounds = new LatLngBounds(address_start, address_end);
                        }
                        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10f));
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
                        travel.setKilomitrage(kilo / 1000);
                        travel.setTemps(duration);
                        travel.setTime_end(sdf.format(then));
                        travel.setUser_id(MainActivity.user.id);
                        if (comment.getText().toString().trim().isEmpty())
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

                                new Thread(() -> {
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
                                        })
                                        .show();
                            })
                            .show();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        setCurrentLocation(googleMap);
    }

    public void setCurrentLocation(GoogleMap googleMap){
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
                            travel.setAddressDepart(address);
                            setMap(googleMap);

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

    public void setMap(GoogleMap googleMap){

        Log.e("MAPS_V2", "SET MAP" );

        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        m_start = googleMap.addMarker(new MarkerOptions().position(address_start).title("START").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        if (address_end != null) {
            LatLngBounds bounds;
            //check end point is not greater than start point
            if(Math.abs(address_end.latitude) < Math.abs(address_start.latitude))
                bounds = new LatLngBounds(address_end, address_start);
            else
                bounds = new LatLngBounds(address_start, address_end);

            if(m_end != null)
                m_end.remove();
            m_end = googleMap.addMarker(new MarkerOptions().position(address_end).title("END").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10f));
        }
        else
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address_start, 10f));
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
}
