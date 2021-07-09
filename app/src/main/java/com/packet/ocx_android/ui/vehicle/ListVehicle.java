package com.packet.ocx_android.ui.vehicle;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.AdapterListVehicle;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class ListVehicle extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ListVehicle() {}

    public static ListVehicle newInstance(String param1, String param2) {
        ListVehicle fragment = new ListVehicle();
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
        View view = inflater.inflate(R.layout.fragment_list_vehicle, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_vehicles_section);
        ListView listView = view.findViewById(R.id.list_vehicles);

        VehiclesDB vdb = new VehiclesDB(getContext());
        vdb.openForRead();
        ArrayList<Vehicle> vehicles = vdb.getVehicles();
        vdb.close();

        AdapterListVehicle adapter = new AdapterListVehicle(getContext(), R.layout.fragment_list_vehicle__item, vehicles);
        listView.setAdapter(adapter);

        return view;
    }
}