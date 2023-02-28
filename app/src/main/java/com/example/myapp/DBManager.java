package com.example.myapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DBManager extends SQLiteOpenHelper {
    static String DB_name = "MyApp";
    static String Table_Saved_name= "tbl_saved";
    static int DBVersion = 2;

    public DBManager(Context context)
    {
        super(context,DB_name, null, DBVersion);
    }


    private static Calendar getDate(Cursor c) {
        try {
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            String s = c.getString(3);
            date = (Date) formatter.parse(s);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
            return Calendar.getInstance();
        }
    }

    public static void addToDB (SQLiteDatabase db, SavedProject sp) {
        db.execSQL("INSERT INTO " + Table_Saved_name + "(name,number_of_people,category) VALUES('" + sp.getProject_name() + "','" + sp.getNumber_of_people() + "','" + sp.getCategory() + "');");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists  tbl_saved(name text,number_of_people text,category text, date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2) {
            db.execSQL("DROP TABLE IF EXISTS tbl_saved");
            onCreate(db);
        }
    }
}
