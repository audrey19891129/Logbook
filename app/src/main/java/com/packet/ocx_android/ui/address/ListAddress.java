package com.packet.ocx_android.ui.address;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.DropDown;
import com.packet.ocx_android.controllers.adapters.AdapterListAddress;
import com.packet.ocx_android.models.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListAddress extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ListAddress() {}

    public static ListAddress newInstance(String param1, String param2) {
        ListAddress fragment = new ListAddress();
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
        View view = inflater.inflate(R.layout.fragment_list_address, container, false);

        ListView listView = view.findViewById(R.id.list_address);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_list_repertory);

        ArrayList<Address> addresses = new ArrayList<Address>();

        JSONArray list = DropDown.getList("getAddressUser", true);
        if(list!=null && list.length() > 0){
            for(int i = 0; i < list.length(); i++){
                JSONObject json_obj = null;
                try {
                    json_obj = list.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Address a = objectMapper.readValue(json_obj.toString(), Address.class);
                    addresses.add(a);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        TextView none = view.findViewById(R.id.txt_NoAdresses);
        if(addresses.isEmpty())
            none.setVisibility(View.VISIBLE);

            AdapterListAddress adapter = new AdapterListAddress(getContext(), R.layout.fragment_list_address__item, addresses);
            listView.setAdapter(adapter);

        return view;
    }
}