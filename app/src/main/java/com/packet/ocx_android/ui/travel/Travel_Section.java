package com.packet.ocx_android.ui.travel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.packet.ocx_android.R;
import com.packet.ocx_android.ui.vehicle.ListVehicle;
import com.packet.ocx_android.ui.vehicle.NewVehicle;

public class Travel_Section extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Travel_Section() {}

    public static Travel_Section newInstance(String param1, String param2) {
        Travel_Section fragment = new Travel_Section();
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
        View view = inflater.inflate(R.layout.fragment_travel__section, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tablayout = view.findViewById(R.id.tabLayout_travels);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_travels_section, ListTravel.class, null)
                .commit();

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_travels_section, ListTravel.class, null)
                            .commit();
                        break;
                    case 1 : getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_travels_section, NewTravel_V2.class, null)
                            .commit();
                        break;
                    case 2 : getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_travels_section, New_Travel_Retro.class, null)
                            .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }
}