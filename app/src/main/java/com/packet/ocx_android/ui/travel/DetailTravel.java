package com.packet.ocx_android.ui.travel;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.AdapterListTravel;
import com.packet.ocx_android.controllers.connection.DirectionsController;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.controllers.maps.Maps;
import com.packet.ocx_android.models.Deplacements;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Deplacements.DeplacementsDB;
import com.packet.ocx_android.ui.vehicle.ListVehicle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;

public class DetailTravel extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public MapView mapView;
    public EditText comment;
    public Deplacements travel = AdapterListTravel.selected;
    public Button modify, save, cancel, back, toggle;
    public Spinner type, vehicle;
    TextView date, add_start, add_end, time_s, time_e, travel_time, distance;
    public Handler handler;

    public DetailTravel() {}

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
        View view = inflater.inflate(R.layout.fragment_detail_travel, container, false);

        type = view.findViewById(R.id.spin_travel_type);
        vehicle = view.findViewById(R.id.spin_travel_vehicle);
        modify = view.findViewById(R.id.btn_modify);
        save = view.findViewById(R.id.btn_save_travel);
        cancel = view.findViewById(R.id.btn_cancel_travel);
        back = view.findViewById(R.id.btn_back_travels);
        date = view.findViewById(R.id.txt_date);
        add_start = view.findViewById(R.id.txt_add_S);
        add_end = view.findViewById(R.id.txt_add_E);
        time_s = view.findViewById(R.id.txt_time_S);
        time_e = view.findViewById(R.id.txt_time_E);
        distance = view.findViewById(R.id.txt_dist);
        comment = view.findViewById(R.id.txt_comment);

        //get list
        String types[] = {"Personnel", "Entreprise"};

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        type.setAdapter(spinnerArrayAdapter);

        //get vehicles list
        int selected = 0;
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        vehicles.add(new Vehicle(1, 1, 1, 1, "Honda", "Civic","rouge", "The swift"));
        vehicles.add(new Vehicle(2, 1, 2, 1,"Toyota", "Rav-4",  "description", null));
        ArrayList<String> vehicle_names = new ArrayList<>();
        for(int i = 0; i <vehicles.size(); i++){
            Vehicle v = vehicles.get(i);
            if(travel.vehicle_id == v.id)
                selected = i;
            if(v.nickname == null)
                vehicle_names.add(v.brand + " " + v.model);
            else
                vehicle_names.add(v.nickname);
        }
        spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, vehicle_names);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        vehicle.setAdapter(spinnerArrayAdapter);

        type.setSelection(travel.deplacement_type_id -1);
        vehicle.setSelection(selected);
        date.setText(travel.dateDeplacement);
        add_start.setText(travel.addressDepart);
        add_end.setText(travel.addressArrive);
        time_s.setText(travel.time_start);
        time_e.setText(travel.time_end);
        distance.setText(String.valueOf(travel.kilomitrage) + " km");
        comment.setText(travel.comment);

        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        type.setEnabled(false);
        vehicle.setEnabled(false);
        comment.setEnabled(false);

        //MAP
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        modify.setOnClickListener(v->{
            type.setEnabled(true);
            comment.setEnabled(true);
            vehicle.setEnabled(true);
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            modify.setVisibility(View.INVISIBLE);

        });

        back.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_travels_section, ListTravel.class, null)
                    .commit();
        });

        save.setOnClickListener(v->{


            travel.setDeplacement_type_id(type.getSelectedItemPosition() +1);
            travel.setComment(comment.getText().toString().trim());
            travel.setVehicle_id(vehicle.getSelectedItemPosition() +1);

            new Thread(()->{

                // SAVE TO SERVER
                try {
                    HttpResponse response = Request.POST("addDeplacement", travel.toJSON(), true);
                    if (response.getStatusLine().getStatusCode() < 300) {

                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle(R.string.alert_confirm)
                                .setMessage(R.string.alert_success_save)
                                .setPositiveButton("OK", (dia, gs) -> {})
                                .show();

                        //REDIRECT TO LIST
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_travels_section, ListTravel.class, null)
                                .commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }).start();
        });

        modify.setOnClickListener(v->{

            handler = new Handler();

            if(modify.getText().toString().contentEquals(getResources().getString(R.string.btn_save))){
                travel.setDeplacement_type_id(type.getSelectedItemPosition() +1);
                travel.setComment(comment.getText().toString().trim());
                travel.setVehicle_id(vehicle.getSelectedItemPosition() +1);
                travel.setUser_id(MainActivity.user.id);

                new Thread(()->{

                    // SAVE TO SERVER
                    try {
                        HttpResponse response = Request.PUT("updateDeplacement/" + travel.id, travel.toJSON_update(), true);
                        if (response.getStatusLine().getStatusCode() < 300) {

                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setTitle(R.string.alert_confirm)
                                    .setMessage(R.string.alert_success_save)
                                    .setPositiveButton("OK", (dia, gs) -> {})
                                    .show();

                            //REDIRECT TO LIST
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_travels_section, ListTravel.class, null)
                                    .commit();
                        }
                        else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(getContext())
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle(R.string.alert_error)
                                            .setMessage("Verifiez votre connection internet.")
                                            .setPositiveButton("OK", (dia, gs) -> {})
                                            .show();
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }).start();
            }
            else{
                vehicle.setEnabled(true);
                type.setEnabled(true);
                comment.setEnabled(true);
                modify.setText(getResources().getString(R.string.btn_save));
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Get addresses
        String start = travel.addressDepart;
        String end = travel.addressArrive;

        String str_start = DirectionsController.stringFormatter(start);
        String str_end = DirectionsController.stringFormatter(end);

        LatLng address_start = null;
        LatLng address_end = null;
        try {
            address_start = Maps.getLocationFromAddress(getContext(), start);
            address_end = Maps.getLocationFromAddress(getContext(), end);

            //get center
            LatLngBounds bounds;
            //check end point is not greater than start point
            if(Math.abs(address_end.latitude) < Math.abs(address_start.latitude))
                bounds = new LatLngBounds(address_end, address_start);
            else
                bounds = new LatLngBounds(address_start, address_end);

            int height = 100;
            int width = 100;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.addMarker(new MarkerOptions().position(address_start).title("START").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            googleMap.addMarker(new MarkerOptions().position(address_end).title("END").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

            //center the map between the 2 points
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10f));

        } catch (IOException e) {
            e.printStackTrace();
        }
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