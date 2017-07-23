package com.example.faster.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ByDiamon on 6/6/2560.
 */

public class BusstopTABLE {


    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite;
    private SQLiteDatabase readSQLite;

    public static final String TABLE_BUSSTOP = "busstopTABLE"; //ชื่อเทเบิล

    //ชื่อคอลัมน์
    public static final String COLUMN_ID_BUSSTOP = "_id";
    public static final String COLUMN_X = "X";
    public static final String COLUMN_Y = "Y";
    public static final String COLUMN_Namebusstop = "Namebusstop";
    public static final String COLUMN_buspassing = "buspassing";

    public BusstopTABLE(Context context){

        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();
    }

    public long addValueToBusstop(String strX, String strY, String strNamebusstop, String strbuspassing) {
        //สร้างออบเจ็ค ContentValues เพื่อเพิ่มข้อมูล
        ContentValues objContentValues = new ContentValues();
        //ใส่ข้อมูล
        objContentValues.put(COLUMN_X,strX);
        objContentValues.put(COLUMN_Y,strY);
        objContentValues.put(COLUMN_Namebusstop,strNamebusstop);
        objContentValues.put(COLUMN_buspassing,strbuspassing);

        //เพิ่มข้อมูลลงเทเบิล
        return writeSQLite.insert(TABLE_BUSSTOP,null,objContentValues);
    }
}
