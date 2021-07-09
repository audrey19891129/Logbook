package com.packet.ocx_android.controllers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.packet.ocx_android.R;
import com.packet.ocx_android.models.Depences;
import com.packet.ocx_android.models.Depences_Type;
import com.packet.ocx_android.ui.database.Expense_Type.ExpenseTypeDB;
import com.packet.ocx_android.ui.expense.DetailExpense;
import com.packet.ocx_android.ui.travel.DetailTravel;

import java.util.ArrayList;

public class AdapterListExpense extends ArrayAdapter<Depences> {

    private Context context;
    private int layout;
    ArrayList<Depences> expenses;
    TextView date;
    ConstraintLayout btn;
    public static Depences selected;


    public AdapterListExpense(@NonNull Context context, int layout, ArrayList<Depences> expenses) {
        super(context, layout, expenses);
        this.context = context;
        this.layout = layout;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, parent, false);

        ExpenseTypeDB edb = new ExpenseTypeDB(getContext());
        edb.openForRead();
        ArrayList<Depences_Type> expense_types = edb.getExpenseTypes();
        edb.close();

        Depences i = expenses.get(position);
        if(i != null){
            date = convertView.findViewById(R.id.txt_expense_date);
            btn = convertView.findViewById(R.id.btn_expense);
            btn.setClickable(true);
            date.setText(i.dateDepence);

            btn.setOnClickListener(v->{
                selected = i;
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                DetailExpense myFragment = new DetailExpense();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_expenses_section, myFragment).addToBackStack(null).commit();
            });
        }
        return convertView;
    }
}
