package com.example.faster.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ByDiamon on 15/6/2560.
 */

public class busrouteTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite;
    private SQLiteDatabase readSQLite;


    public static final String TABLE_BUSROUTE = "busrouteTABLE";
    public static final String COLUMN_ID_BUSROUTE = "id_busroute";
    public static final String COLUMN_bus = "bus";
    public static final String COLUMN_Namebusstop = "Namebusstop";
    public static final String COLUMN_X = "X";
    public static final String COLUMN_Y = "Y";


    public  busrouteTABLE(Context context){

        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();


    }
    public long addValueToBusroute(String strbus, String strNamebusstop, String strxX, String stryY) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_bus,strbus);
        objContentValues.put(COLUMN_Namebusstop,strNamebusstop);
        objContentValues.put(COLUMN_X,strxX);
        objContentValues.put(COLUMN_Y,stryY);

        return writeSQLite.insert(TABLE_BUSROUTE,null,objContentValues);
    }

}
