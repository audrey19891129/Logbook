package com.packet.ocx_android.ui.database.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserSQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS User (" +
            "id INTEGER NOT NULL, " +
            "name VARCHAR NOT NULL, " +
            "email VARCHAR NOT NULL, " +
            "email_verified_at VARCHAR , " +
            "password VARCHAR , " +
            "provider_id VARCHAR , " +
            "provider VARCHAR , " +
            "remember_token  VARCHAR ) ";

    public UserSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'User'");
        onCreate(db);
    }
}
