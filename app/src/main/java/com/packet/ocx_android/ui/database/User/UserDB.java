package com.packet.ocx_android.ui.database.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.packet.ocx_android.models.User;
import com.packet.ocx_android.ui.database.Schema;
import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;
import java.util.ArrayList;

public class UserDB {

    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "name", "email", "email_verified_at", "password", "provider_id", "provider", "remember_token"};
    private SQLiteDatabase db;
    private UserSQLite user;
    private static final int VERSION = 1;
    private String TABLE = "User";

    public UserDB(Context context){
        user = new UserSQLite(context, Schema.DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = user.getWritableDatabase();
    }

    public void openForRead(){
        db = user.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }
    private class DB_User{
        @Column("id")
        public int id;
        @Column("name")
        public String name;
        @Column("email")
        public String email;
        @Column("email_verified_at")
        public String email_verified_at;
        @Column("password")
        public String password;
        @Column("provider_id")
        public String provider_id;
        @Column("provider")
        public String provider;
        @Column("remember_token")
        public String remember_token;

        public DB_User(){}

        public DB_User(int id, String name, String email, String email_verified_at, String password, String provider_id, String provider, String remember_token) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.email_verified_at = email_verified_at;
            this.password = password;
            this.provider_id = provider_id;
            this.provider = provider;
            this.remember_token = remember_token;
        }

        public int getId() { return id;
        }
        public void setId(int id) { this.id = id;
        }
        public String getName() { return name;
        }
        public void setName(String name) { this.name = name;
        }
        public String getEmail() { return email;
        }
        public void setEmail(String email) { this.email = email;
        }
        public String getEmail_verified_at() { return email_verified_at;
        }
        public void setEmail_verified_at(String email_verified_at) { this.email_verified_at = email_verified_at;
        }
        public String getPassword() { return password;
        }
        public void setPassword(String password) { this.password = password;
        }
        public String getProvider_id() { return provider_id;
        }
        public void setProvider_id(String provider_id) { this.provider_id = provider_id;
        }
        public String getProvider() { return provider;
        }
        public void setProvider(String provider) { this.provider = provider;
        }
        public String getRemember_token() { return remember_token;
        }
        public void setRemember_token(String remember_token) { this.remember_token = remember_token;
        }
    }

    public long insert(User user){
        ContentValues content = new ContentValues();
        content.put("id", user.id);
        content.put("name", user.name);
        content.put("email", user.email);
        content.put("email_verified_at", user.email_verified_at);
        content.put("password", user.password);
        content.put("provider_id", user.provider_id);
        content.put("provider", user.provider);
        content.put("remember_token", user.remember_token);
        return db.insert(TABLE, null, content);
    }

    public User getUser(){
        ArrayList<User> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return null;
        }
        while(cursor.moveToNext()){
            UserDB.DB_User db = microOrm.fromCursor(cursor, UserDB.DB_User.class);
            User u = new User(db.id, db.name, db.email, db.email_verified_at, db.password, db.provider_id, db.provider, db.remember_token, "", "");
            list.add(u);
        }
        cursor.close();
        return list.get(0);
    }

    public void update(User user){
        ContentValues cv = new ContentValues();
        cv.put("password", user.password);
        db.update(TABLE, cv, "id=?", new String[]{String.valueOf(user.id)});
    }
}
