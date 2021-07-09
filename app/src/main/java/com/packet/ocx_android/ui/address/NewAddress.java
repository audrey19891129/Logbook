package com.packet.ocx_android.ui.address;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.Countries;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.controllers.maps.Maps;
import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Address.AddressDB;
import com.packet.ocx_android.ui.database.Deplacements.DeplacementsDB;
import com.packet.ocx_android.ui.vehicle.ListVehicle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class NewAddress extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button save;
    EditText name, addressLine, description;
    Address address;
    Handler handler;


    public NewAddress() {}

    public static NewAddress newInstance(String param1, String param2) {
        NewAddress fragment = new NewAddress();
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
        View view = inflater.inflate(R.layout.fragment_new_address, container, false);

        save = view.findViewById(R.id.btn_save_new_address);
        description = view.findViewById(R.id.txt_description_add);
        name = view.findViewById(R.id.txt_address_name);
        addressLine = view.findViewById(R.id.txt_addressLine);
        address = new Address();
        handler = new Handler();


        save.setOnClickListener(v->{

            address.setDescription(description.getText().toString().trim());
            address.setUser_id(MainActivity.user.id);

            if(!name.getText().toString().isEmpty() && !addressLine.getText().toString().isEmpty()) {

                address.setDescription(description.getText().toString().trim());
                if(description.getText().toString().trim().isEmpty()){
                    address.setDescription("aucune");
                }
                address.setNom(name.getText().toString());
                address.setAddresse(addressLine.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            LatLng addressline = Maps.getLocationFromAddress(getContext(), addressLine.getText().toString());
                            if(addressline != null){

                                HttpResponse response = Request.POST("createaddress", address.toJSON(), true);
                                if (response.getStatusLine().getStatusCode() < 300) {

                                    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
                                    String jsonContent = br.readLine();
                                    JSONObject add_json = new JSONObject(jsonContent);
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    Address newAddress = objectMapper.readValue(add_json.toString(), Address.class);

                                    AddressDB adb = new AddressDB(getContext());
                                    adb.openForWrite();
                                    adb.delete(address);
                                    adb.insertAddress(newAddress);
                                    adb.close();

                                    handler.post(()->{
                                        alert(getContext(), android.R.drawable.ic_dialog_info, getResources().getString(R.string.alert_success),getResources().getString(R.string.alert_success_save));
                                    });

                                    getParentFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container_address_section, ListAddress.class, null)
                                            .commit();
                                }
                                else{
                                    handler.post(()->{
                                        alert(getContext(), android.R.drawable.ic_delete, getResources().getString(R.string.alert_error),getResources().getString(R.string.alert_could_not_save));
                                    });
                                }
                            }
                            else{
                                handler.post(()->{
                                    alert(getContext(), android.R.drawable.ic_delete, getResources().getString(R.string.alert_error),getResources().getString(R.string.alert_invalid_address));
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else{
                alert(getContext(), android.R.drawable.ic_delete, getResources().getString(R.string.alert_error),getResources().getString(R.string.alert_empty_field));
            }
        });
        return view;
    }

    public void alert(Context context, int icon, String title, String message){
        new AlertDialog.Builder(getContext())
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", (dialog, which) -> {
                })
                .show();
    }
}