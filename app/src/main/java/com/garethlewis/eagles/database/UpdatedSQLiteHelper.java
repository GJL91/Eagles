package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.garethlewis.eagles.util.ScheduleParams;

import java.util.Date;

public class UpdatedSQLiteHelper extends MasterDatabase {

    private static UpdatedSQLiteHelper sInstance;
    private static final String TABLE_UPDATED = "LastUpdated";

    private static final String DATE_QUERY =
            "SELECT Date FROM " + TABLE_UPDATED + " WHERE Name = ?;";


    public static UpdatedSQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UpdatedSQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private UpdatedSQLiteHelper(Context context) {
        super(context);
    }

    public boolean setUpdated(String table) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", table);
        long date = Long.parseLong(String.valueOf(new Date().getTime()));
        contentValues.put("Date", date);

        return (db.replace(TABLE_UPDATED, null, contentValues) != -1);

//        return success;
    }

    public Date getLastUpdated(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(DATE_QUERY, new String[] {table});
        cursor.moveToFirst();

        String dateString = cursor.getString(cursor.getColumnIndex("Date"));

        cursor.close();

        return new Date(Long.parseLong(dateString));
    }

    public boolean needsUpdate(String table) {
        Date date = getLastUpdated(table);
        return (new Date().getTime() - date.getTime() > getTimeLimit(table));
    }

    private int getTimeLimit(String table) {
        switch (table) {
            case "Schedule": scheduleUpdateDelay();
            case "Media": return 300000;
            default: return 3600000;
        }
    }

    private int scheduleUpdateDelay() {
        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getContext());
        if (ScheduleParams.getNextGameTime() < new Date().getTime() || db.gameInProgress()) {
            return 60000;
        }

        return 259200000; // Default amount of 3 days.
    }

}
