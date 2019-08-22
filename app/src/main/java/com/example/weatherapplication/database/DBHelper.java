package com.example.weatherapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "citiesDB";
    private static final int DATABASE_VERSION = 1;
    public static final String CITIES_TABLE = "cities";

    private static final String ID = "_id";
    public static final String CITY_NAME = "city";
    public static final String DATE = "date";
    public static final String TEMP = "temperature";
    public static final String WIND_SPEED = "wind_speed";
    public static final String PRESSURE = "pressure";
    public static final String HUMIDITY = "humidity";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CITIES_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CITY_NAME + " TEXT, " +
                DATE + " TEXT, " +
                TEMP + " TEXT, " +
                WIND_SPEED + " TEXT, " +
                PRESSURE + " TEXT, " +
                HUMIDITY + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevDbVersion, int newDbVersion) {
        db.execSQL("DROP TABLE if EXISTS " + CITIES_TABLE);
        onCreate(db);
    }
}
