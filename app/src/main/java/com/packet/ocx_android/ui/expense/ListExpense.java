package com.packet.ocx_android.ui.expense;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.DropDown;
import com.packet.ocx_android.controllers.adapters.AdapterListExpense;
import com.packet.ocx_android.controllers.adapters.AdapterListVehicle;
import com.packet.ocx_android.models.Depences;
import com.packet.ocx_android.models.Deplacements;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListExpense extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static ListView listView;

    public ListExpense() {}

    public static ListExpense newInstance(String param1, String param2) {
        ListExpense fragment = new ListExpense();
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
        View view = inflater.inflate(R.layout.fragment_list_expense, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_expenses_section);
        listView = view.findViewById(R.id.list_expenses);


        ArrayList<Depences> expenses = new ArrayList<Depences>();
        JSONArray list = DropDown.getList("depences/get", true);
        if(list!=null && list.length() > 0){
            for(int i = 0; i < list.length(); i++){
                JSONObject json_obj = null;
                try {
                    json_obj = list.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Depences d = objectMapper.readValue(json_obj.toString(), Depences.class);
                    expenses.add(d);

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

        Log.e("LISTEXP", expenses.toString());

        AdapterListExpense adapter = new AdapterListExpense(getContext(), R.layout.fragment_list_expenses__item, expenses);
        listView.setAdapter(adapter);

        return view;
    }
}