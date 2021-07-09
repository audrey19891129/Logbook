package com.packet.ocx_android.ui.database.Expense_Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.packet.ocx_android.models.Depences_Type;
import com.packet.ocx_android.models.Fuel;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeDB;
import com.packet.ocx_android.ui.database.Fuel_Type.FuelTypeSQLite;
import com.packet.ocx_android.ui.database.Schema;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class ExpenseTypeDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "name"};
    private SQLiteDatabase db;
    private ExpenseTypeSQLite expense_type;
    private static final int VERSION = 1;
    private String TABLE = "Expense_Type";


    public ExpenseTypeDB(Context context){
        expense_type = new ExpenseTypeSQLite(context, Schema.DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = expense_type.getWritableDatabase();
    }

    public void openForRead(){
        db = expense_type.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_EXP {
        @Column("id")
        public int id;
        @Column("name")
        public String name;

        public DB_EXP(int id, String name) {
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

    public long insertExpenseType(Depences_Type d_type){
        ContentValues content = new ContentValues();
        content.put("id", d_type.id);
        content.put("name", d_type.name);
        return db.insert(TABLE, null, content);
    }

    public ArrayList<Depences_Type> getExpenseTypes(){
        ArrayList<Depences_Type> list = new ArrayList<Depences_Type>();
        Cursor cursor = db.query(TABLE, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return list;
        }
        while(cursor.moveToNext()){
            ExpenseTypeDB.DB_EXP db = microOrm.fromCursor(cursor, ExpenseTypeDB.DB_EXP.class);
            Depences_Type d = new Depences_Type(db.id, db.name);
            list.add(d);
        }
        cursor.close();
        return list;
    }
}

