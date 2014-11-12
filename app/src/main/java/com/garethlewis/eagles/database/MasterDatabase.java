package com.garethlewis.eagles.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MasterDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eagles.db";
    private static int DATABASE_VERSION = 2;

    private static final String MEDIA_CREATE =
            "CREATE Table Media (" +
                "Post_ID TEXT NOT NULL PRIMARY KEY," +
                "Title TEXT NOT NULL," +
                "Link TEXT NOT NULL," +
                "Time TEXT NULL," +
                "Type INTEGER NOT NULL," +
                "Image TEXT NULL);";

    // Database creation sql statement
    private static final String SCHEDULE_CREATE =
            "create table Schedule (" +
                "_id integer primary key autoincrement, " +
                "Date integer not null, " +
                "HomeTeam text not null, " +
                "HomeTeamScore integer null, " +
                "AwayTeam text not null, " +
                "AwayTeamScore integer null, " +
                "Week text not null);";

    private static final String UPDATED_CREATE =
            "CREATE Table LastUpdated (" +
                "Name TEXT NOT NULL PRIMARY KEY, " +
                "Date INTEGER NOT NULL);";

    private Context context;

    public MasterDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MEDIA_CREATE);
        db.execSQL(SCHEDULE_CREATE);
        db.execSQL(UPDATED_CREATE);

//        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//        while (cursor.moveToNext()) {
//            Log.e("EAGLES", "Table: " + cursor.getString(0));
//        }
//        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (DATABASE_VERSION != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Media");
            db.execSQL("DROP TABLE IF EXISTS Schedule");
            db.execSQL("DROP TABLE IF EXISTS LastUpdated");

            DATABASE_VERSION = newVersion;
            onCreate(db);
        }
    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS Media");
        db.execSQL("DROP TABLE IF EXISTS Schedule");
        db.execSQL("DROP TABLE IF EXISTS LastUpdated");

        onCreate(db);

//        db.close();
    }
}
