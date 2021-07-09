package com.packet.ocx_android.ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.packet.ocx_android.ui.database.Address.AddressDB;
import com.packet.ocx_android.ui.database.Address.AddressSQLite;
import com.packet.ocx_android.ui.database.Deplacements.DeplacementsSQLite;
import com.packet.ocx_android.ui.database.Expense_Type.ExpenseTypeSQLite;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeSQLite;
import com.packet.ocx_android.ui.database.User.UserSQLite;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeSQLite;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;
import com.packet.ocx_android.models.Address;
import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesSQLite;

import java.util.ArrayList;

public class Schema {

    private static final int VERSION = 1;
    public static final String DB_NAME = "schema.db";

    private SQLiteDatabase db;
    private SchemaSQLite schema;

    public Schema(Context context){
        schema = new SchemaSQLite(context, DB_NAME, null, VERSION);
    }
    public void openForWrite(){
        db = schema.getWritableDatabase();
    }

    public void openForRead(){
        db = schema.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

}
