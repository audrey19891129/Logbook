package com.packet.ocx_android.controllers.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.packet.ocx_android.R;
import com.packet.ocx_android.models.Vehicle;

import java.util.ArrayList;


public class CustomAdapter_Vehicle extends BaseAdapter {
    Context context;
    ArrayList<Vehicle> vehicles;
    LayoutInflater inflter;

    public CustomAdapter_Vehicle(Context applicationContext, ArrayList<Vehicle> vehicles) {
        this.context = applicationContext;
        this.vehicles = vehicles;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return vehicles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.vehicles_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.img_vehicle_icon);
        TextView names = (TextView) view.findViewById(R.id.txt_spin_vehicle);

        Vehicle v = vehicles.get(i);
        switch(v.vehicules_type_id){
            case 2:
                icon.setImageResource(R.drawable.ic_truck);
                break;
            case 3:
                icon.setImageResource(R.drawable.ic_moto);
                break;
            case 4:
                icon.setImageResource(R.drawable.ic_scooter);
                break;
            case 5:
                icon.setImageResource(R.drawable.ic_bike);
                break;
            default : icon.setImageResource(R.drawable.ic_car);
            break;
        }

        if(vehicles.get(i).nickname == null || vehicles.get(i).nickname.contentEquals("") || vehicles.get(i).nickname.contentEquals("aucun"))
            names.setText(vehicles.get(i).brand + " " + vehicles.get(i).model);
        else
            names.setText(vehicles.get(i).nickname);
        return view;
    }

}
