package com.packet.ocx_android.ui.database.Fuel_Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.packet.ocx_android.models.Fuel;
import com.packet.ocx_android.models.Vehicles_Type;
import com.packet.ocx_android.ui.database.Schema;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeDB;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeSQLite;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class FuelTypeDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "name"};
    private SQLiteDatabase db;
    private FuelTypeSQLite fuel_type;
    private static final int VERSION = 1;
    private String TABLE = "Fuel_Type";


    public FuelTypeDB(Context context){
        fuel_type = new FuelTypeSQLite(context, Schema.DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = fuel_type.getWritableDatabase();
    }

    public void openForRead(){
        db = fuel_type.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_FUEL {
        @Column("id")
        public int id;
        @Column("name")
        public String name;

        public DB_FUEL(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public long insertFuelType(Fuel f_type){
        ContentValues content = new ContentValues();
        content.put("id", f_type.id);
        content.put("name", f_type.name);
        return db.insert(TABLE, null, content);
    }

    public ArrayList<Fuel> getFuelTypes(){
        ArrayList<Fuel> list = new ArrayList<Fuel>();
        Cursor cursor = db.query(TABLE, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return list;
        }
        while(cursor.moveToNext()){
            FuelTypeDB.DB_FUEL db = microOrm.fromCursor(cursor, FuelTypeDB.DB_FUEL.class);
            Fuel f = new Fuel(db.id, db.name);
            list.add(f);
        }
        cursor.close();
        return list;
    }
}
