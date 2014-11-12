package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class UpdatedSQLiteHelper extends MasterDatabase {

    private static final String TABLE_UPDATED = "LastUpdated";

    private static final String DATE_QUERY =
            "SELECT Date FROM " + TABLE_UPDATED + " WHERE Name = ?;";

    private SQLiteDatabase db;

    public UpdatedSQLiteHelper(Context context) {
        super(context);
    }

    public boolean setUpdated(String table) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", table);
        long date = Long.parseLong(String.valueOf(new Date().getTime()));
        contentValues.put("Date", date);

        boolean success = (db.replace(TABLE_UPDATED, null, contentValues) != -1);
        db.close();

        return success;
    }

    public Date getLastUpdated(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(DATE_QUERY, new String[] {table});
        cursor.moveToFirst();

        String dateString = cursor.getString(cursor.getColumnIndex("Date"));

        cursor.close();
        db.close();

        return new Date(Long.parseLong(dateString));
    }

}
