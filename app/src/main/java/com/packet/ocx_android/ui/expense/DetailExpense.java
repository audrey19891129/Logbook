package com.packet.ocx_android.ui.expense;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.AdapterListExpense;
import com.packet.ocx_android.models.Depences;
import com.packet.ocx_android.models.Depences_Type;
import com.packet.ocx_android.ui.travel.ListTravel;

import java.util.ArrayList;

public class DetailExpense extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    TextView amount, description, type;
    Button back;

    public DetailExpense() {}

    public static DetailExpense newInstance(String param1, String param2) {
        DetailExpense fragment = new DetailExpense();
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
        View view = inflater.inflate(R.layout.fragment_detail_expense, container, false);
        Depences i = AdapterListExpense.selected;

        ArrayList<Depences_Type> expense_types = new ArrayList<Depences_Type>();
        expense_types.add(new Depences_Type(1, "Essence"));
        expense_types.add(new Depences_Type(2, "Restaurant"));

        amount = view.findViewById(R.id.txt_expense_amount);
        description = view.findViewById(R.id.txt_expense_descr);
        type = view.findViewById(R.id.txt_expense_type);
        description.setText(i.description);
        type.setText(expense_types.get(i.depense_type_id -1).getName());
        amount.setText(String.valueOf(i.montant) + " $");
        back = view.findViewById(R.id.btn_back_list_expense);

        back.setOnClickListener(v->{
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_expenses_section, ListExpense.class, null)
                    .commit();
        });


        return view;
    }
}