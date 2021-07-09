package com.packet.ocx_android.ui.database.Fuel_Type;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FuelTypeSQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS Fuel_Type (" +

            "id INTEGER NOT NULL, " +
            "name VARCHAR NOT NULL ); ";

    public FuelTypeSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Fuel_Type'");
        onCreate(db);
    }
}
