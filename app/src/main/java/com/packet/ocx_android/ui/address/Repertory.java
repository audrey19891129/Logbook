package com.packet.ocx_android.ui.address;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.connection.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import kotlin.text.Charsets;

public class Repertory extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Handler mHandler;

    public Repertory() {}

    public static Repertory newInstance(String param1, String param2) {
        Repertory fragment = new Repertory();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_list_repertory);
        mHandler=new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repertory, container, false);

        ConstraintLayout addresses = view.findViewById(R.id.repertory_adresses);
        addresses.setClickable(true);
        addresses.setOnClickListener(v->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpResponse response = Request.GET("getAddressUser", true);
                        Log.e("REPERTORY", response.toString() );

                        if(response.getStatusLine().getStatusCode()<300){
                            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), Charsets.UTF_8));
                            String jsonContent = br.readLine();
                            JSONArray list = new JSONArray(jsonContent);

                            if(list.length()==0){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(getActivity())
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle(R.string.alert_error)
                                                .setMessage("no addresses")
                                                .setNeutralButton("OK", (dialog, which) -> {
                                                })
                                                .show();
                                    };
                                });
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        });

        return view;
    }
}
