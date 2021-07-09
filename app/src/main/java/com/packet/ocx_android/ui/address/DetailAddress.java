package com.packet.ocx_android.ui.address;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.AdapterListAddress;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.ui.database.Address.AddressDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class DetailAddress extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Address address;
    TextView addressLine;
    EditText description, name;
    Button modify, delete;

    public DetailAddress() {}

    public static DetailAddress newInstance(String param1, String param2) {
        DetailAddress fragment = new DetailAddress();
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
        View view = inflater.inflate(R.layout.fragment_detail_address, container, false);

        address = AdapterListAddress.selected;

        description = view.findViewById(R.id.txt_description_address_detail);
        name = view.findViewById(R.id.txt_address_name_detail);
        addressLine = view.findViewById(R.id.txt_address_line_detail);
        modify = view.findViewById(R.id.btn_modify_address);
        delete = view.findViewById(R.id.btn_delete_address);


        addressLine.setText(address.addresse);
        name.setText(address.nom);
        description.setText(address.description);
        name.setEnabled(false);
        description.setEnabled(false);

        modify.setOnClickListener(v->{
            if(modify.getText().toString().contentEquals(getResources().getString(R.string.btn_save))){

                if(description.getText().toString().trim().isEmpty()){
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle(getResources().getString(R.string.alert_error))
                            .setMessage(getResources().getString(R.string.alert_error_missing_descr_addr))
                            .setPositiveButton("OK", (dia, gs) -> {
                            })
                            .show();
                }
                else{
                    address.setDescription(description.getText().toString().trim());

                    try {
                        HttpResponse response = Request.POST("updateaddress", address.toJSON_update(), true);
                        if(response.getStatusLine().getStatusCode() < 300){

                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setTitle(R.string.alert_success)
                                    .setMessage(R.string.alert_success_save)
                                    .setNeutralButton("OK", (dialog, which) -> {
                                    })
                                    .show();

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

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container_address_section, ListAddress.class, null)
                                    .commit();

                        }
                        else{
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(R.string.alert_error)
                                    .setMessage("verifiez votre connection internet.")
                                    .setNeutralButton("OK", (dialog, which) -> {
                                    })
                                    .show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                description.setEnabled(true);
                modify.setText(getResources().getString(R.string.btn_save));
            }
        });

        delete.setOnClickListener(v->{
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.alert_confirm)
                    .setMessage("Etes-vous sur(e) de vouloir supprimer cette adresse?")
                    .setNegativeButton(getResources().getString(R.string.btn_cancel), (dialog, which) -> {
                    })
                    .setPositiveButton(getResources().getString(R.string.btn_yes), (dialog, d)->{
                        try {
                            HttpResponse response = Request.POST("DeleteAddress", address.toJSON_update(), true);
                            if(response.getStatusLine().getStatusCode() < 300){
                                AddressDB adb = new AddressDB(getContext());
                                adb.openForWrite();
                                adb.delete(address);
                                adb.close();

                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container_address_section, ListAddress.class, null)
                                        .commit();

                            }
                            else{
                                new AlertDialog.Builder(getContext())
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle(R.string.alert_error)
                                        .setMessage("verifiez votre connection internet.")
                                        .setNeutralButton("OK", (dia, which) -> {
                                        })
                                        .show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
        });

        return view;
    }
}