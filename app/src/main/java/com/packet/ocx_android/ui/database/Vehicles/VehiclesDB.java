package com.packet.ocx_android.ui.database.Vehicles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.packet.ocx_android.models.Vehicle;
import com.packet.ocx_android.ui.database.Schema;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class VehiclesDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "user_id", "vehicules_type_id", "fuel_types_id", "brand", "model", "description", "nickname"};
    private SQLiteDatabase db;
    private VehiclesSQLite vehicle;
    private static final int VERSION = 1;
    private String TABLE = "Vehicles";


    public VehiclesDB(Context context){
        vehicle = new VehiclesSQLite(context, Schema.DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = vehicle.getWritableDatabase();
    }

    public void openForRead(){
        db = vehicle.getReadableDatabase();
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
        @Column("user_id")
        public int user_id;
        @Column("vehicules_type_id")
        public int vehicules_type_id;
        @Column("fuel_types_id")
        public int fuel_types_id;
        @Column("brand")
        public String brand;
        @Column("model")
        public String model;
        @Column("description")
        public String description;
        @Column("nickname")
        public String nickname;

        public DB_Vehicle(){}

        public DB_Vehicle(int id, int user_id, int vehicules_type_id, int fuel_types_id, String brand, String model, String description, String nickname) {
            this.id = id;
            this.user_id = user_id;
            this.vehicules_type_id = vehicules_type_id;
            this.fuel_types_id = fuel_types_id;
            this.brand = brand;
            this.model = model;
            this.description = description;
            this.nickname = nickname;
        }
        public int getVehicules_type_id() {
            return vehicules_type_id;
        }
        public void setVehicules_type_id(int vehicules_type_id) { this.vehicules_type_id = vehicules_type_id; }
        public int getFuel_types_id() {
            return fuel_types_id;
        }
        public void setFuel_types_id(int fuel_types_id) {
            this.fuel_types_id = fuel_types_id;
        }
        public String getModel() {
            return model;
        }
        public void setModel(String model) {
            this.model = model;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getNickname() {
            return nickname;
        }
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getBrand() {
            return brand;
        }
        public void setBrand(String brand) {
            this.brand = brand;
        }
        public int getUser_id() {
            return user_id;
        }
        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

    }

    public long insertVehicle(Vehicle vehicle){
        ContentValues content = new ContentValues();
        content.put("id", vehicle.id);
        content.put("user_id", vehicle.user_id);
        content.put("fuel_types_id", vehicle.fuel_types_id);
        content.put("vehicules_type_id", vehicle.vehicules_type_id);
        content.put("brand", vehicle.brand);
        content.put("model", vehicle.model);
        content.put("description", vehicle.description);
        content.put("nickname", vehicle.nickname);
        return db.insert(TABLE, null, content);
    }

    public ArrayList<Vehicle> getVehicles(){
        ArrayList<Vehicle> listVehicles = new ArrayList<>();
        Cursor cursor = db.query(TABLE, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listVehicles;
        }
        while(cursor.moveToNext()){
            DB_Vehicle db = microOrm.fromCursor(cursor, DB_Vehicle.class);
            Vehicle v = new Vehicle(db.id, db.user_id, db.vehicules_type_id, db.fuel_types_id, db.brand, db.model, db.description, db.nickname);
            listVehicles.add(v);
        }
        cursor.close();
        return listVehicles;
    }

    public void update(Vehicle v){
        ContentValues cv = new ContentValues();
        cv.put("description", v.description);
        cv.put("nickname", v.nickname);
        db.update(TABLE, cv, "id=?", new String[]{String.valueOf(v.id)});
    }

    public void delete(Vehicle v){
        db.execSQL("DELETE FROM Vehicles where id=" + v.id);
    }
}
