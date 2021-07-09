package com.packet.ocx_android.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.packet.ocx_android.R;

import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.ui.address.*;
import com.packet.ocx_android.ui.travel.DetailTravel;

import java.util.ArrayList;

public class AdapterListAddress  extends ArrayAdapter<Address> {

    private ArrayList<Address> addresses;
    private Context context;
    int resource;
    Button address;
    public static Address selected;

    public AdapterListAddress(@NonNull Context context, int resource, ArrayList<Address> addresses) {
        super(context, resource, addresses);
        this.context = context;
        this.resource = resource;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resource, parent, false);

        Address a = addresses.get(position);

        if(a != null) {

            address = convertView.findViewById(R.id.btn_address_item);
            address.setText(a.nom);

            address.setOnClickListener(v->{
                selected = a;

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailAddress myFragment = new DetailAddress();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_address_section, myFragment).addToBackStack(null).commit();
            });

        }
        return  convertView;
    }
}
