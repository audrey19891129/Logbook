package com.packet.ocx_android.ui.database.Vehicle_Type;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class VehicleTypeSQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS Vehicle_Type (" +
            "id INTEGER NOT NULL, " +
            "name VARCHAR NOT NULL ); ";

    public VehicleTypeSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Vehicle_Type'");
        onCreate(db);
    }
}