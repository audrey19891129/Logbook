package com.packet.ocx_android.ui.vehicle;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.DropDown;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.Fuel;
import com.packet.ocx_android.models.Mileage;
import com.packet.ocx_android.models.User;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.models.Vehicles_Type;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeDB;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeDB;
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

public class NewVehicle extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    Handler handler;
    public Button btn_save;
    public ImageView btn_back;
    public TextView txt_brand, txt_odometer, txt_model, txt_description, txt_nickname;
    public static Vehicle vehicle;

    public NewVehicle() {}

    public static NewVehicle newInstance(String param1, String param2) {
        NewVehicle fragment = new NewVehicle();
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
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_new_vehicle, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_vehicles_section);
        vehicle = new Vehicle();

        //SPINNER
        VehicleTypeDB vtdb = new VehicleTypeDB(getContext());
        vtdb.openForRead();
        ArrayList<Vehicles_Type> v_types = vtdb.getVehicleTypes();
        vtdb.close();
        Spinner type = view.findViewById(R.id.spin_new_type);
        ArrayAdapter<Vehicles_Type> spinnerArrayAdapterType_vt = new ArrayAdapter<Vehicles_Type>(getActivity(), android.R.layout.simple_spinner_dropdown_item, v_types);
        type.setAdapter(spinnerArrayAdapterType_vt);

        //SPINNER
        FuelTypeDB fdb = new FuelTypeDB(getContext());
        fdb.openForRead();
        ArrayList<Fuel> fuels = fdb.getFuelTypes();
        fdb.close();
        Spinner fuel = view.findViewById(R.id.spin_new_fuel);
        ArrayAdapter<Fuel> spinnerArrayAdapterType_f = new ArrayAdapter<Fuel>(getActivity(), android.R.layout.simple_spinner_dropdown_item, fuels);
        fuel.setAdapter(spinnerArrayAdapterType_f);

        btn_save = view.findViewById(R.id.btn_new_vehicle_save);
        txt_brand = view.findViewById(R.id.txt_brand);
        txt_odometer = view.findViewById(R.id.txt_odometer);
        txt_model = view.findViewById(R.id.txt_model);
        txt_description = view.findViewById(R.id.txt_description);
        txt_nickname = view.findViewById(R.id.txt_nickname);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(type.getSelectedItem().toString().contentEquals("Velo")){
                    txt_description.setEnabled(true);
                    txt_nickname.setEnabled(true);
                    txt_brand.setEnabled(false);
                    txt_odometer.setEnabled(false);
                    txt_model.setEnabled(false);
                    fuel.setEnabled(false);
                    fuel.setSelection(4);
                }
                else{
                    txt_description.setEnabled(true);
                    txt_nickname.setEnabled(true);
                    txt_brand.setEnabled(true);
                    txt_odometer.setEnabled(true);
                    txt_model.setEnabled(true);
                    fuel.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btn_save.setOnClickListener(v->{
            if(!type.getSelectedItem().toString().contentEquals("Velo") && (txt_brand.getText().toString().trim().isEmpty() || txt_model.getText().toString().isEmpty() || txt_odometer.getText().toString().isEmpty())){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.alert_error)
                        .setMessage(R.string.alert_empty_field)
                        .setNeutralButton("OK", (dialog, which) -> {
                        })
                        .show();
            }
            else{
                // ADD CALL TO SEND INFO

                    vehicle.setId(0);
                    vehicle.setUser_id(MainActivity.user.id);
                    vehicle.setVehicules_type_id(type.getSelectedItemPosition() +1);
                    if(txt_description.getText().toString().trim().isEmpty())
                        vehicle.setDescription("aucune");
                    else
                        vehicle.setDescription(txt_description.getText().toString());
                    if(txt_nickname.getText().toString().trim().isEmpty())
                        vehicle.setNickname("aucun");
                    else
                        vehicle.setNickname(txt_nickname.getText().toString());
                    vehicle.setFuel_types_id(fuel.getSelectedItemPosition() +1);
                    if(type.getSelectedItem().toString().contentEquals("Velo")){
                        vehicle.setBrand("Vélo");
                        vehicle.setModel("non applicable");
                    }
                    else{
                        vehicle.setBrand(txt_brand.getText().toString());
                        vehicle.setModel(txt_model.getText().toString());

                        Mileage mileage = new Mileage(0, 1, MainActivity.getTime(), Integer.parseInt(txt_odometer.getText().toString()));
                    }

                    //write in sqlite
                    VehiclesDB vdb = new VehiclesDB(getContext());
                    vdb.openForWrite();
                    vdb.insertVehicle(vehicle);
                    vdb.close();

                    //write new mileage to sqlite


                    //send new vehicle to online db in new thread

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpResponse response = Request.POST("createvehicule", vehicle.toJSON(), true);
                            if(response.getStatusLine().getStatusCode() < 300){

                                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
                                String jsonContent = br.readLine();
                                JSONObject veh_json = new JSONObject(jsonContent);
                                ObjectMapper objectMapper = new ObjectMapper();
                                Vehicle newVehicle = objectMapper.readValue(veh_json.toString(), Vehicle.class);

                                vdb.openForWrite();
                                vdb.delete(vehicle);
                                vdb.insertVehicle(newVehicle);
                                vdb.close();

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(getContext())
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setTitle(R.string.alert_success)
                                                .setMessage(R.string.alert_success_save)
                                                .setNeutralButton("OK", (dialog, which) -> {
                                                })
                                                .show();
                                    }
                                });

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container_vehicle_section, ListVehicle.class, null)
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
                                                .setNeutralButton("OK", (dialog, which) -> {
                                                })
                                                .show();
                                    }
                                });
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                    //when call is successful, create new call in new thread for mileage
                    // when call is successfull delete new vehicle and new milage from sqlite
            }

        });


        return view;
    }
}