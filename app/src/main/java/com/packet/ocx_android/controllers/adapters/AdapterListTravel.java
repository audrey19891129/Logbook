package com.packet.ocx_android.controllers.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.packet.ocx_android.R;
import com.packet.ocx_android.models.Deplacements;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.travel.DetailTravel;
import com.packet.ocx_android.ui.vehicle.DetailVehicle;

import java.util.ArrayList;

public class AdapterListTravel extends ArrayAdapter<Deplacements> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Context context;
    private int layout;
    ArrayList<Deplacements> travels;
    public static Deplacements selected;

    public TextView date, time_s, time_e;

    public AdapterListTravel(@NonNull Context context, int layout, ArrayList<Deplacements> travels) {
        super(context, layout, travels);
        this.context = context;
        this.layout = layout;
        this.travels = travels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, parent, false);

        Deplacements i = travels.get(position);

        if(i != null){

            date = convertView.findViewById(R.id.txt_date);
            time_s = convertView.findViewById(R.id.txt_time_S);
            time_e = convertView.findViewById(R.id.txt_time_E);

            date.setText(i.dateDeplacement);
            time_s.setText(i.time_start);
            time_e.setText(i.time_end);

            ConstraintLayout cons = convertView.findViewById(R.id.constraint_vehicle);
            cons.setClickable(true);
            View finalConvertView = convertView;
            cons.setOnClickListener(v->{
                selected = i;
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailTravel myFragment = new DetailTravel();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_travels_section, myFragment).addToBackStack(null).commit();
            });
        }


        return convertView;
    }
}