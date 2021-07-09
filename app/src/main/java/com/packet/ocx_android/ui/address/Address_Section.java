package com.packet.ocx_android.ui.address;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.ui.vehicle.ListVehicle;
import com.packet.ocx_android.ui.vehicle.NewVehicle;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class Address_Section extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Address_Section() {}

    public static Address_Section newInstance(String param1, String param2) {
        Address_Section fragment = new Address_Section();
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
        View view = inflater.inflate(R.layout.fragment_address__section, container, false);

        TabLayout tablayout = view.findViewById(R.id.tablayout_addresses);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_address_section, ListAddress.class, null)
                            .commit();
                        break;
                    case 1 : getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_address_section, NewAddress.class, null)
                            .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

}