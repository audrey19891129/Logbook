package com.packet.ocx_android.ui.travel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.DropDown;
import com.packet.ocx_android.controllers.adapters.AdapterListTravel;
import com.packet.ocx_android.controllers.adapters.AdapterListVehicle;
import com.packet.ocx_android.models.Deplacement_Type;
import com.packet.ocx_android.models.Deplacements;
import com.packet.ocx_android.models.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListTravel extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RadioButton all, business, personnal;

    public ListTravel() { }

    public static ListTravel newInstance(String param1, String param2) {
        ListTravel fragment = new ListTravel();
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
        View view = inflater.inflate(R.layout.fragment_list_travel, container, false);
        ListView listView = view.findViewById(R.id.list_travels);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_travels_section);

        all = view.findViewById(R.id.radioButton_all);
        business = view.findViewById(R.id.radioButton_business);
        personnal = view.findViewById(R.id.radioButton_personnal);

        ArrayList<Deplacements> travels = new ArrayList<Deplacements>();
        JSONArray list = DropDown.getList("deplacements", true);
        if(list!=null && list.length() > 0){
            for(int i = 0; i < list.length(); i++){
                JSONObject json_obj = null;
                try {
                    json_obj = list.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Deplacements d = objectMapper.readValue(json_obj.toString(), Deplacements.class);
                    travels.add(d);
                    Log.e("DROPDOWN", d.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        all.setChecked(false);

        all.setOnClickListener(v->{
            AdapterListTravel adapter = new AdapterListTravel(getContext(), R.layout.fragment_list_travel__item, travels);
            listView.setAdapter(adapter);
        });

        personnal.setOnClickListener(v->{
            ArrayList<Deplacements> personnal_list = new ArrayList<Deplacements>();
            for(Deplacements d : travels){
                if(d.deplacement_type_id == 1){
                    personnal_list.add(d);

                    Log.e("DROPDOWN", d.toString());

                    AdapterListTravel adapter = new AdapterListTravel(getContext(), R.layout.fragment_list_travel__item, personnal_list);
                    listView.setAdapter(adapter);
                }
            }
        });

        business.setOnClickListener(v->{
            ArrayList<Deplacements> business_list = new ArrayList<Deplacements>();
            for(Deplacements d : travels){
                if(d.deplacement_type_id == 2){
                    business_list.add(d);
                    Log.e("DROPDOWN", d.toString());
                    AdapterListTravel adapter = new AdapterListTravel(getContext(), R.layout.fragment_list_travel__item, business_list);
                    listView.setAdapter(adapter);
                }
            }
        });

        return view;
    }
}