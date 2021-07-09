package com.packet.ocx_android.ui.vehicle;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.AdapterListVehicle;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.Fuel;
import com.packet.ocx_android.models.Vehicles_Type;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeDB;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeDB;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;
import com.packet.ocx_android.models.Vehicle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailVehicle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailVehicle extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Spinner spin_type, spin_fuel;
    public Button btn_save, btn_cancel, btn_modify, btn_back;
    public TextView txt_brand, txt_odometer, txt_model, txt_description, txt_nickname;

    public DetailVehicle() {}

public static DetailVehicle newInstance(String param1, String param2) {
        DetailVehicle fragment = new DetailVehicle();
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_vehicles_section);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_vehicle, container, false);

        //SPINNER

        VehicleTypeDB vtdb = new VehicleTypeDB(getContext());
        vtdb.openForRead();
        ArrayList<Vehicles_Type> v_types = vtdb.getVehicleTypes();
        vtdb.close();

        FuelTypeDB fdb = new FuelTypeDB(getContext());
        fdb.openForRead();
        ArrayList<Fuel> f_types = fdb.getFuelTypes();
        fdb.close();

        spin_type = view.findViewById(R.id.spin_detail_type);
        ArrayAdapter<Vehicles_Type> spinnerArrayAdapterType_vt = new ArrayAdapter<Vehicles_Type>(getActivity(), android.R.layout.simple_spinner_dropdown_item, v_types);
        spin_type.setAdapter(spinnerArrayAdapterType_vt);
        spin_type.setEnabled(false);

        //SPINNER
        spin_fuel = view.findViewById(R.id.spin_detail_fuel);
        ArrayAdapter<Fuel> spinnerArrayAdapterType_f = new ArrayAdapter<Fuel>(getActivity(), android.R.layout.simple_spinner_dropdown_item, f_types);
        spin_fuel.setAdapter(spinnerArrayAdapterType_f);
        spin_fuel.setEnabled(false);

        btn_modify = view.findViewById(R.id.btn_detail_vehicle_modify);
        btn_save = view.findViewById(R.id.btn_detail_vehicle_save);
        btn_cancel = view.findViewById(R.id.btn_detail_vehicle_cancel);
        btn_back = view.findViewById(R.id.btn_back_vehicles);
        txt_brand = view.findViewById(R.id.txt_brand);
        txt_odometer = view.findViewById(R.id.txt_odometer);
        txt_model = view.findViewById(R.id.txt_model);
        txt_description = view.findViewById(R.id.txt_description);
        txt_nickname = view.findViewById(R.id.txt_nickname);

        //SET FIELDS
        Vehicle vehicle = AdapterListVehicle.selected;

        Log.e("DETAIL", vehicle.toString());

        spin_fuel.setSelection(vehicle.fuel_types_id -1);
        spin_type.setSelection(vehicle.vehicules_type_id -1);
        txt_brand.setText(vehicle.brand);
        txt_nickname.setText(vehicle.nickname);
        txt_description.setText(vehicle.description);
        txt_model.setText(vehicle.model);

        btn_modify.setOnClickListener(v->{
            txt_odometer.setEnabled(true);
            txt_nickname.setEnabled(true);
            txt_description.setEnabled(true);

            btn_modify.setVisibility(View.INVISIBLE);
            btn_save.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
        });

        btn_cancel.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_vehicle_section, ListVehicle.class, null)
                    .commit();
        });

        btn_save.setOnClickListener(v->{
            if(txt_description.getText().toString().trim().isEmpty() || txt_nickname.getText().toString().trim().isEmpty()){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.alert_error)
                        .setMessage(R.string.alert_empty_field)
                        .setNeutralButton("OK", (dialog, which) -> {
                        })
                        .show();
            }
            else{
                vehicle.setNickname(txt_nickname.getText().toString().trim());
                vehicle.setDescription(txt_description.getText().toString().trim());
               /* if(vehicle.type_id != 5 && vehicle.odometer != Integer.parseInt(txt_odometer.getText().toString())){
                    if(vehicle.odometer > Integer.parseInt(txt_odometer.getText().toString())){
                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(R.string.alert_error)
                                .setMessage(R.string.alert_odometer)
                                .setNeutralButton("OK", (dialog, which) -> {
                                })
                                .show();
                    }
                    else{
                        vehicle.setOdometer(Integer.parseInt(txt_odometer.toString()));
                        //send to online db
*/
                        //write in sqlite
                        VehiclesDB vdb = new VehiclesDB(getContext());
                        vdb.openForWrite();
                        vdb.update(vehicle);
                        vdb.close();

                        AdapterListVehicle.selected = null;

                        new Thread(()->{

                            HttpResponse response = null;
                            try {
                                response = Request.POST("updatevehicule", vehicle.toString(), true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }).start();

                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle(R.string.alert_success)
                                .setMessage(R.string.alert_success_save)
                                .setNeutralButton("OK", (dialog, which) -> {
                                })
                                .show();


                        //REDIRECT TO VEHICLES LIST
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_vehicle_section, ListVehicle.class, null)
                                .commit();
                    }
             //   }
           // }
        });

        btn_back.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_vehicle_section, ListVehicle.class, null)
                    .commit();
        });


        return view;
    }
}