package com.packet.ocx_android.ui.database.Vehicle_Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.models.Vehicles_Type;
import com.packet.ocx_android.ui.database.Schema;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesDB;
import com.packet.ocx_android.ui.database.Vehicles.VehiclesSQLite;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class VehicleTypeDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "name"};
    private SQLiteDatabase db;
    private VehicleTypeSQLite vehicle_type;
    private static final int VERSION = 1;
    private String TABLE = "Vehicle_Type";


    public VehicleTypeDB(Context context){
        vehicle_type = new VehicleTypeSQLite(context, Schema.DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = vehicle_type.getWritableDatabase();
    }

    public void openForRead(){
        db = vehicle_type.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_Vehicle {
        @Column("id")
        public int id;
        @Column("name")
        public String name;

        public DB_Vehicle(int id, String name) {
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

    public long insertVehicleType(Vehicles_Type v_type){
        ContentValues content = new ContentValues();
        content.put("id", v_type.id);
        content.put("name", v_type.name);
        return db.insert(TABLE, null, content);
    }

    public ArrayList<Vehicles_Type> getVehicleTypes(){
        ArrayList<Vehicles_Type> list = new ArrayList<Vehicles_Type>();
        Cursor cursor = db.query(TABLE, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return list;
        }
        while(cursor.moveToNext()){
            VehicleTypeDB.DB_Vehicle db = microOrm.fromCursor(cursor, VehicleTypeDB.DB_Vehicle.class);
            Vehicles_Type v = new Vehicles_Type(db.id, db.name);
            list.add(v);
        }
        cursor.close();
        return list;
    }
}
