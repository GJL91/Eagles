package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.garethlewis.eagles.util.ScheduleParams;

public class ScheduleParamsSQLiteHelper extends MasterDatabase {

    private static ScheduleParamsSQLiteHelper sInstance;

    private static String TABLE_NAME = "ScheduleParams";

    public static ScheduleParamsSQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ScheduleParamsSQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ScheduleParamsSQLiteHelper(Context context) {
        super(context);
    }

    public void readParams() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            int lastResult = cursor.getInt(0);
            int firstFixture = cursor.getInt(1);
            long nextGame = cursor.getLong(2);

            ScheduleParams.setLastResult(lastResult);
            ScheduleParams.setFirstFixture(firstFixture);
            ScheduleParams.setNextGameTime(nextGame);
        }

        cursor.close();
    }

    public void setParams() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("LastResultWeek", ScheduleParams.getLastResult());
        contentValues.put("FirstFixtureWeek", ScheduleParams.getFirstFixture());
        contentValues.put("NextGameTime", ScheduleParams.getNextGameTime());

        db.replace(TABLE_NAME, null, contentValues);
    }

//    public void setParameters(int lastResult, int firstFixture, long nextGame) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put("LastResultWeek", lastResult);
//        contentValues.put("FirstFixtureWeek", firstFixture);
//        contentValues.put("NextGameTime", nextGame);
//
//        db.replace(TABLE_NAME, null, contentValues);
//    }
    
}
