package com.packet.ocx_android.ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.packet.ocx_android.ui.database.Address.AddressSQLite;
import com.packet.ocx_android.ui.database.Deplacements.DeplacementsSQLite;
import com.packet.ocx_android.ui.database.Expense_Type.ExpenseTypeDB;
import com.packet.ocx_android.ui.database.Expense_Type.ExpenseTypeSQLite;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeSQLite;
import com.packet.ocx_android.ui.database.User.UserSQLite;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeSQLite;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesSQLite;

public class SchemaSQLite extends SQLiteOpenHelper {
    public SchemaSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VehiclesSQLite.CREATE_DB);
        db.execSQL(DeplacementsSQLite.CREATE_DB);
        db.execSQL(AddressSQLite.CREATE_DB);
        db.execSQL(UserSQLite.CREATE_DB);
        db.execSQL(VehicleTypeSQLite.CREATE_DB);
        db.execSQL(FuelTypeSQLite.CREATE_DB);
        db.execSQL(ExpenseTypeSQLite.CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
