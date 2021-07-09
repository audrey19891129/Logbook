package com.packet.ocx_android.ui.expense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.packet.ocx_android.R;
import com.packet.ocx_android.ui.travel.ListTravel;
import com.packet.ocx_android.ui.travel.NewTravel;
import com.packet.ocx_android.ui.travel.New_Travel_Retro;

public class Expense_Section extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Expense_Section() {}

    public static Expense_Section newInstance(String param1, String param2) {
        Expense_Section fragment = new Expense_Section();
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

        View view = inflater.inflate(R.layout.fragment_expense__section, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tablayout = view.findViewById(R.id.tabLayout_expenses);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_expenses_section, ListExpense.class, null)
                            .commit();
                        break;
                    case 1 : getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_expenses_section, NewExpense.class, null)
                            .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}