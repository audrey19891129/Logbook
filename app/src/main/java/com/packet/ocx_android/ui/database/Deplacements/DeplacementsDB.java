package com.packet.ocx_android.ui.database.Deplacements;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.packet.ocx_android.models.Deplacements;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class DeplacementsDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "dateDeplacement", "addressDepart", "addressArrive", "kilomitrage", "vehicle_id", "user_id", "comment", "timeStart", "timeStop", "deplacement_type_id", "temps"};
    private SQLiteDatabase db;
    private DeplacementsSQLite deplacement;
    private static final int VERSION = 1;
    private static final String DB_NAME = "schema.db";
    private static final String TABLE = "Deplacements";
    private static final String COL_ID = "id";
    private static final String COL_DATE = "dateDeplacement";
    private static final String COL_ADD_D = "addressDepart";
    private static final String COL_ADD_A = "addressArrive";
    private static final String COL_KILO = "kilomitrage";
    private static final String COL_VEH_ID = "vehicle_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_COMMENT = "comment";
    private static final String COL_T_START = "timeStart";
    private static final String COL_T_END = "timeStop";
    private static final String COL_TYPE = "deplacement_type_id";
    private static final String COL_TEMPS = "temps";



    public DeplacementsDB(Context context){
        deplacement = new DeplacementsSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = deplacement.getWritableDatabase();
    }

    public void openForRead(){
        db = deplacement.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_Deplacements {
        @Column(COL_ID)
        public int id;
        @Column(COL_DATE)
        public String dateDeplacement;
        @Column(COL_ADD_D)
        public String addressDepart;
        @Column(COL_ADD_A)
        public String addressArrive;
        @Column(COL_KILO)
        public double kilomitrage;
        @Column(COL_VEH_ID)
        public int vehicle_id;
        @Column(COL_USER_ID)
        public int user_id;
        @Column(COL_COMMENT)
        public String comment;
        @Column(COL_T_START)
        public String timeStart;
        @Column(COL_T_END)
        public String timeStop;
        @Column(COL_TYPE)
        public int deplacement_type_id;
        @Column(COL_TEMPS)
        public String temps;


        public DB_Deplacements(){}

        public DB_Deplacements(int id, String dateDeplacement, String addressDepart,
                               String addressArrive, double kilomitrage, int vehicle_id, int user_id,
                               String comment, String timeStart, String timeStop,
                               int deplacement_type_id, String temps) {
            this.id = id;
            this.dateDeplacement = dateDeplacement;
            this.addressDepart = addressDepart;
            this.addressArrive = addressArrive;
            this.kilomitrage = kilomitrage;
            this.vehicle_id = vehicle_id;
            this.user_id = user_id;
            this.comment = comment;
            this.timeStart = timeStart;
            this.timeStop = timeStop;
            this.deplacement_type_id = deplacement_type_id;
            this.temps = temps;
        }

        public int getDeplacement_type_id() { return deplacement_type_id; }
        public void setDeplacement_type_id(int deplacement_type_id) {this.deplacement_type_id = deplacement_type_id; }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getDateDeplacement() {
            return dateDeplacement;
        }
        public void setDateDeplacement(String dateDeplacement) {this.dateDeplacement = dateDeplacement;}
        public String getAddressDepart() {
            return addressDepart;
        }
        public void setAddressDepart(String addressDepart) {this.addressDepart = addressDepart; }
        public String getAddressArrive() {return addressArrive; }
        public void setAddressArrive(String addressArrive) {this.addressArrive = addressArrive; }
        public double getKilomitrage() {return kilomitrage; }
        public void setKilomitrage(double kilomitrage) {this.kilomitrage = kilomitrage; }
        public int getVehicle_id() { return vehicle_id; }
        public void setVehicle_id(int vehicle_id) {this.vehicle_id = vehicle_id; }
        public int getUser_id() {return user_id; }
        public void setUser_id(int user_id) {this.user_id = user_id; }
        public String getComment() {return comment; }
        public void setComment(String comment) {this.comment = comment; }
        public String getTimeStart() {return timeStart; }
        public void setTimeStart(String timeStart) {this.timeStart = timeStart; }
        public String getTimeStop() {return timeStop; }
        public void setTimeStop(String timeStop) {this.timeStop = timeStop; }
        public String getTemps() { return temps;}
        public void setTemps(String temps) { this.temps = temps; }
    }

    public long insertDeplacements(Deplacements d){

        ContentValues content = new ContentValues();
        content.put(COL_ID, d.id);
        content.put(COL_DATE, d.dateDeplacement);
        content.put(COL_ADD_D, d.addressDepart);
        content.put(COL_ADD_A, d.addressArrive);
        content.put(COL_KILO, d.kilomitrage);
        content.put(COL_VEH_ID, d.vehicle_id);
        content.put(COL_USER_ID, d.user_id);
        content.put(COL_COMMENT, d.comment);
        content.put(COL_T_START, d.time_start);
        content.put(COL_T_END, d.time_end);
        content.put(COL_TYPE, d.deplacement_type_id);
        content.put(COL_TEMPS, d.temps);
        return db.insertOrThrow(TABLE, null, content);
    }

    public ArrayList<Deplacements> getDeplacements(){
        ArrayList<Deplacements> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return list;
        }
        while(cursor.moveToNext()){
            DB_Deplacements db = microOrm.fromCursor(cursor, DB_Deplacements.class);
            Deplacements d = new Deplacements(db.id, db.user_id, db.vehicle_id,db.deplacement_type_id, db.dateDeplacement, db.addressDepart, db.addressArrive, db.timeStart, db.timeStop, db.kilomitrage, db.comment, db.temps);
            list.add(d);
        }
        cursor.close();
        return list;
    }

    public void update(Deplacements d){
        ContentValues cv = new ContentValues();
        cv.put("dateDeplacement", d.dateDeplacement);
        cv.put("addressDepart", d.addressDepart);
        cv.put("addressArrive", d.addressArrive);
        cv.put("kilomitrage", d.kilomitrage);
        cv.put("vehicle_id", d.vehicle_id);
        cv.put("user_id", d.user_id);
        cv.put("comment", d.comment);
        cv.put("timeStart", d.time_start);
        cv.put("timeStop", d.time_end);
        cv.put("deplacement_type_id", d.deplacement_type_id);
        cv.put("temps", d.temps);
        db.update(TABLE, cv, "id=?", new String[]{String.valueOf(d.id)});
    }

    public void delete(Deplacements d){
        db.execSQL("DELETE FROM " + TABLE + " where id=" + d.id);
    }

    public Deplacements fetchOngoing(){
        Deplacements ongoing = null;
        Cursor cursor = db.query(TABLE, columns, COL_COMMENT + "= 'ongoing'", null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return null;
        }
        while(cursor.moveToNext()){
            DB_Deplacements db = microOrm.fromCursor(cursor, DB_Deplacements.class);
            Deplacements d = new Deplacements(db.id, db.user_id, db.vehicle_id,db.deplacement_type_id, db.dateDeplacement, db.addressDepart, db.addressArrive, db.timeStart, db.timeStop, db.kilomitrage, db.comment, db.temps);
            ongoing = d;
        }
        cursor.close();
        return ongoing;
    }
}
