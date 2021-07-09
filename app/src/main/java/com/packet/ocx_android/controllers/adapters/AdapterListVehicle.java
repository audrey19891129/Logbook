package com.packet.ocx_android.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.packet.ocx_android.R;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.vehicle.DetailVehicle;

import java.util.ArrayList;

public class AdapterListVehicle extends ArrayAdapter<Vehicle> {

    private Context context;
    private int layoutVehicleList;
    ArrayList<Vehicle> vehicleList;
    public static Vehicle selected;

    public AdapterListVehicle(@NonNull Context context, int layoutVehicleList, ArrayList<Vehicle> vehicleList) {
        super(context, layoutVehicleList, vehicleList);
        this.context = context;
        this.layoutVehicleList = layoutVehicleList;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layoutVehicleList, parent, false);

        Vehicle i = vehicleList.get(position);

        if(i != null){
            TextView txt_brand = convertView.findViewById(R.id.txt_brand);
            if(i.nickname == null || i.nickname.contentEquals("") || i.nickname.contentEquals("aucun"))
                txt_brand.setText(i.brand + " " + i.model);
            else
                txt_brand.setText(i.nickname);
            ImageView img = convertView.findViewById(R.id.img_type);

            switch(i.vehicules_type_id){
                case 1:
                    img.setImageResource(R.drawable.ic_car);
                    break;
                case 2:
                    img.setImageResource(R.drawable.ic_truck);
                    break;
                case 3:
                    img.setImageResource(R.drawable.ic_moto);
                    break;
                case 4:
                    img.setImageResource(R.drawable.ic_scooter);
                    break;
                case 5:
                    img.setImageResource(R.drawable.ic_bike);
                    break;
            }

            ConstraintLayout cons = convertView.findViewById(R.id.constraint_vehicle);
            cons.setClickable(true);
            View finalConvertView = convertView;
            cons.setOnClickListener(v->{
                selected = i;
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailVehicle myFragment = new DetailVehicle();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_vehicle_section, myFragment).addToBackStack(null).commit();
            });
        }


        return convertView;
    }
}
