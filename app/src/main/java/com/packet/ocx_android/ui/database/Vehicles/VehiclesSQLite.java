package com.packet.ocx_android.ui.database.Vehicles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class VehiclesSQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS Vehicles (" +

            "id INTEGER NOT NULL, " +
            "user_id INTEGER NOT NULL, " +
            "vehicules_type_id  INTEGER NOT NULL, " +
            "fuel_types_id  INTEGER DEFAULT NULL, " +
            "brand  VARCHAR NOT NULL, " +
            "model  VARCHAR NOT NULL, " +
            "description  VARCHAR , " +
            "nickname  VARCHAR ); ";

    public VehiclesSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Vehicles'");
        onCreate(db);
    }
}
