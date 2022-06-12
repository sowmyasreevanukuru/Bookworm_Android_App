package com.example.bookworm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="bookworm";
    public static final int DBVERSION=3;

    public static final String TABLE_NAME="tblUser";

    public static final String U_id="u_id";
    public static final String FIRSTNAME="first_name";
    public static final String LASTNAME="last_name";
    public static final String Email_id="Email_id";
    public static final String Password="Password";



    public DBHelper( Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        String query="CREATE TABLE "+TABLE_NAME+ "( "+
                U_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FIRSTNAME + " TEXT,"+
                LASTNAME + " TEXT,"+
                Email_id + " TEXT,"+
                Password + " TEXT"+
                ")";

        MyDB.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        MyDB.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(MyDB);

    }

    public Boolean insertData(String fname,String lname,String email,String pwd) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", fname);
        contentValues.put("last_name", lname);
        contentValues.put("Email_id", email);
        contentValues.put("Password", pwd);

        long result = MyDB.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }
    public Boolean checkemail(String email)
    {
        SQLiteDatabase MyDB1=this.getWritableDatabase();
        Cursor cursor=MyDB1.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+Email_id+" =?",new String[]{email});
        if(cursor.getCount() > 0)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean checkuserpassword(String email,String password)
    {
        SQLiteDatabase MyDB1=this.getWritableDatabase();
        Cursor cursor=MyDB1.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+Email_id+" =? and " + Password+" =?",new String[]{email,password});
        if(cursor.getCount() > 0)
        {
            return true;
        }
        else {
            return false;
        }
    }
}