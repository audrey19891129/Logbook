package com.packet.ocx_android.ui.database.Address;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressSQLite extends SQLiteOpenHelper {
    public static final String DB_NAME = "schema.db";
    public static final String TABLE_ADDRESS = "Address";
    public static final String COL_ID = "id";
    public static final String COL_DESCR = "description";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_NAME = "name";
    public static final String COL_ADDRESS = "address";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_USER_ID + " INTEGER NOT NULL, " +
            COL_DESCR + " VARCHAR , " +
            COL_NAME + " VARCHAR NOT NULL, " +
            COL_ADDRESS + " VARCHAR NOT NULL);";

    public AddressSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        onCreate(db);
    }
}
