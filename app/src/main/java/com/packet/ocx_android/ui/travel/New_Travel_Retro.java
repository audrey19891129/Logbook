package com.packet.ocx_android.ui.travel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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

import com.google.android.gms.maps.model.LatLng;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.HttpResponse;

public class New_Travel_Retro extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    EditText date, add_start, add_end, comment, time_s, time_e, travel_time, distance;
    Spinner vehicle, spin_add_S, spin_add_E, type;
    RadioButton rb_s_s, rb_s_e, rb_m_s, rb_m_e;
    Button save;
    SharedPreferences sharedPreferences;
    LatLng address_start = null, address_end = null;
    JSONObject response = null;

    public New_Travel_Retro() {}

    public static New_Travel_Retro newInstance(String param1, String param2) {
        New_Travel_Retro fragment = new New_Travel_Retro();
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
        View view =  inflater.inflate(R.layout.fragment_new__travel__retro, container, false);
        sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        Deplacements travel = new Deplacements();

        type = view.findViewById(R.id.spin_travel_type_retro);
        vehicle = view.findViewById(R.id.spin_travel_vehicle_retro);
        spin_add_S = view.findViewById(R.id.spin_add_S_retro);
        spin_add_E = view.findViewById(R.id.spin_add_E_retro);
        rb_s_s = view.findViewById(R.id.rb_spinner_start);
        rb_m_s = view.findViewById(R.id.rb_manual_start);
        rb_s_e = view.findViewById(R.id.rb_spinner_end);
        rb_m_e = view.findViewById(R.id.rb_manual_end);
        date = view.findViewById(R.id.txt_date_retro);
        add_start = view.findViewById(R.id.txt_add_S_retro);
        add_end = view.findViewById(R.id.txt_add_E_retro);
        time_s = view.findViewById(R.id.txt_time_S_retro);
        travel_time = view.findViewById(R.id.txt_duration_retro);
        time_e = view.findViewById(R.id.txt_time_E_retro);
        distance = view.findViewById(R.id.txt_dist_retro);
        comment = view.findViewById(R.id.txt_comment_retro);
        save = view.findViewById(R.id.btn_save_travel_retro);

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
        spin_add_S.setAdapter(adapter);
        spin_add_E.setAdapter(adapter);
        spin_add_S.setSelection(0);
        spin_add_E.setSelection(0);
        spin_add_E.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        spin_add_S.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        rb_s_s.setChecked(true);
        rb_s_e.setChecked(true);


        // SPINNER TYPE
        ArrayList<Travel_Type> types = new ArrayList<Travel_Type>();
        types.add(new Travel_Type(1,"Personnel"));
        types.add(new Travel_Type(2,"Entreprise"));
        ArrayAdapter<Travel_Type> spinnerArrayAdapterType = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        type.setAdapter(spinnerArrayAdapterType);

        // SPINNER VEHICLE
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
        vehicle.setSelection(selected);

        travel.setUser_id(MainActivity.user.id);
        travel.setComment("aucun");
        travel.setAllez(false);


        rb_s_e.setOnClickListener(v->{
            add_end.setVisibility(View.INVISIBLE);
            spin_add_E.setVisibility(View.VISIBLE);
        });

        rb_m_e.setOnClickListener(v->{
            add_end.setVisibility(View.VISIBLE);
            spin_add_E.setVisibility(View.INVISIBLE);
        });

        rb_s_s.setOnClickListener(v->{
            add_start.setVisibility(View.INVISIBLE);
            spin_add_S.setVisibility(View.VISIBLE);
        });

        rb_m_s.setOnClickListener(v->{
            add_start.setVisibility(View.VISIBLE);
            spin_add_S.setVisibility(View.INVISIBLE);
        });

        spin_add_E.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel.setAddressArrive(addresses_list.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spin_add_S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travel.setAddressDepart(addresses_list.get(position).toString());
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!add_start.getText().toString().trim().isEmpty())
                    travel.setAddressDepart(add_start.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        save.setOnClickListener(v->{

            Log.e("TEST", travel.toString());

            travel.setVehicle_id(vehicle.getSelectedItemPosition() + 1);
            travel.setDeplacement_type_id(type.getSelectedItemPosition() + 1);
            travel.setTemps(travel_time.getText().toString());
            String travel_date = date.getText().toString().trim();

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date value = sdf.parse(travel_date);
                travel.setDateDeplacement(travel_date);

                if(!travel.addressDepart.contentEquals(travel.addressArrive)){

                    try {
                        address_start = Maps.getLocationFromAddress(getContext(), travel.addressDepart);
                        address_end = Maps.getLocationFromAddress(getContext(), travel.addressArrive);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                        if(address_end != null && address_start != null) {

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

                               double kilo = Double.parseDouble(distance_km);
                               travel.setKilomitrage(kilo / 1000);
                               travel.setTemps(duration);

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }


                               travel_time.setText(travel.temps);
                               distance.setText(travel.kilomitrage + " km");

                               if (!comment.getText().toString().trim().isEmpty())
                                   travel.setComment(comment.getText().toString());

                               sdf = new SimpleDateFormat("hh:mm");
                               try {
                                   sdf.parse(time_s.getText().toString());
                                   travel.setTime_start(time_s.getText().toString() + ":00");

                                   try {
                                       sdf.parse(time_e.getText().toString());
                                       travel.setTime_end(time_e.getText().toString() + ":00");

                                       // WRITE TO DB
                                       DeplacementsDB ddb = new DeplacementsDB(getContext());
                                       ddb.openForWrite();
                                       ddb.insertDeplacements(travel);
                                       ddb.close();

                                       //SEND TO SERVER
                                       new Thread(()->{
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

                                   } catch (ParseException e) {
                                       alert(getContext(), android.R.drawable.ic_delete,"ANNULATION", "L'heure d arrivee saisie n est pas valide");
                                   }
                               } catch (ParseException e) {
                                   alert(getContext(), android.R.drawable.ic_delete,"ANNULATION", "L'heure de depart saisie n est pas valide");
                               }
                            }
                        else{
                            alert(getContext(), android.R.drawable.ic_delete,"ANNULATION", "Verifiez les adresses de depart et d arrivee.");
                        }
                    }
                else{
                    alert(getContext(), android.R.drawable.ic_delete,"ANNULATION", "la date saisie n'est pas valide");
                }
            } catch (ParseException e) {
                alert(getContext(), android.R.drawable.ic_delete,"ANNULATION", "la date saisie n'est pas valide");
            }
        });


        return view;
    }

    private void alert(Context context, int icon, String title, String message){
        new AlertDialog.Builder(context)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dia, gs) -> {
                })
                .show();
    }

}