package com.packet.ocx_android.ui.database.Deplacements;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DeplacementsSQLite  extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS Deplacements (" +
            "id INTEGER DEFAULT NULL, " +
            "dateDeplacement DATE DEFAULT NULL, " +
            "addressDepart VARCHAR DEFAULT NULL, " +
            "addressArrive VARCHAR DEFAULT NULL, " +
            "kilomitrage  DOUBLE DEFAULT NULL, " +
            "vehicle_id INT DEFAULT NULL, " +
            "user_id INT DEFAULT NULL, " +
            "comment VARCHAR DEFAULT NULL, " +
            "timeStart  TIME DEFAULT NULL, " +
            "timeStop  TIME DEFAULT NULL, " +
            "deplacement_type_id INTEGER DEFAULT NULL, " +
            "temps VARCHAR DEFAULT NULL )";


    public DeplacementsSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Deplacements'");
        onCreate(db);
    }
}
