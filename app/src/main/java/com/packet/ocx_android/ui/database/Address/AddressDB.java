package com.packet.ocx_android.ui.database.Address;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.packet.ocx_android.models.Address;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class AddressDB {
    private static String[] columns = new String[]{"id", "user_id", "description", "name", "address"};
    public MicroOrm microOrm = new MicroOrm();
    private SQLiteDatabase db;
    private AddressSQLite address;
    private static final int VERSION = 1;

    public AddressDB(Context context){
        address = new AddressSQLite(context, AddressSQLite.DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = address.getWritableDatabase();
    }

    public void openForRead(){
        db = address.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertAddress(Address address){
        ContentValues content = new ContentValues();
        content.put(AddressSQLite.COL_ID, address.getId());
        content.put(AddressSQLite.COL_USER_ID, address.getUser_id());
        content.put(AddressSQLite.COL_DESCR, address.getDescription());
        content.put(AddressSQLite.COL_NAME, address.getNom());
        content.put(AddressSQLite.COL_ADDRESS, address.getAddresse());
        return db.insert(AddressSQLite.TABLE_ADDRESS, null, content);
    }

    private static class DB_Address{
        @Column(AddressSQLite.COL_ID)
        public int id;
        @Column(AddressSQLite.COL_USER_ID)
        public int user_id;
        @Column(AddressSQLite.COL_DESCR)
        public String description;
        @Column(AddressSQLite.COL_NAME)
        public String name;
        @Column(AddressSQLite.COL_ADDRESS)
        public String address;

        public DB_Address() {};

        public DB_Address(int id, int user_id, String description, String name, String address) {
            this.id = id;
            this.user_id = user_id;
            this.description = description;
            this.name = name;
            this.address = address;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getUser_id() {return user_id; }
        public void setUser_id(int user_id) { this.user_id = user_id; }
        public String getDescription() {return description; }
        public void setDescription(String description) {this.description = description; }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


    public ArrayList<Address> getAddresses(){
        ArrayList<Address> listAddress = new ArrayList<>();
        Cursor cursor = db.query(AddressSQLite.TABLE_ADDRESS, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listAddress;
        }
        while(cursor.moveToNext()){
            DB_Address dba = microOrm.fromCursor(cursor, DB_Address.class);
            Address address = new Address(dba.id, dba.user_id, dba.description, dba.name, dba.address);
            listAddress.add(address);
        }
        cursor.close();
        return listAddress;
    }
    public Address getById(int id){
        Cursor c = db.query(AddressSQLite.TABLE_ADDRESS, columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            DB_Address dba = microOrm.fromCursor(c, DB_Address.class);
            Address address = new Address(dba.id, dba.user_id, dba.description, dba.name, dba.address);
            return address;
        }
        c.close();
        return null;
    }

    public void update(Address a){
        ContentValues cv = new ContentValues();
        cv.put("description", a.description);
        cv.put("name", a.nom);
        cv.put("address", a.addresse);
        db.update(AddressSQLite.TABLE_ADDRESS, cv, "id=?", new String[]{String.valueOf(a.id)});
    }

    public void delete(Address a){
        db.execSQL("DELETE FROM " + AddressSQLite.TABLE_ADDRESS + " where id=" + a.id);
    }
}
