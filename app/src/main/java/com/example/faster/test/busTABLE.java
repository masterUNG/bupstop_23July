package com.example.faster.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ByDiamon on 10/6/2560.
 */

public class busTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite;
    private SQLiteDatabase readSQLite;


    public static final String TABLE_BUS = "busTABLE";
    public static final String COLUMN_ID_BUS = "_id";
    public static final String COLUMN_bus = "bus";
    public static final String COLUMN_bus_details = "bus_details";

    public busTABLE(Context context){

        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();

    }

    public long addValueToBus(String strbus, String strbus_details){


        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_bus,strbus);
        objContentValues.put(COLUMN_bus_details,strbus_details);
        return writeSQLite.insert(TABLE_BUS,null,objContentValues);
    }

}
