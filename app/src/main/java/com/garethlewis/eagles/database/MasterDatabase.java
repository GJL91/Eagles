package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.garethlewis.eagles.database.entities.Standing;
import com.garethlewis.eagles.util.FileHandler;
import com.garethlewis.eagles.util.ScheduleParams;
import com.garethlewis.eagles.util.TeamHelper;

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
                "Week text not null, " +
                "Status integer not null, " +
                "Added integer not null);";

    private static final String UPDATED_CREATE =
            "CREATE Table LastUpdated (" +
                "Name TEXT NOT NULL PRIMARY KEY, " +
                "Date INTEGER NOT NULL);";

    private static final String STANDINGS_CREATE =
            "CREATE Table Standings (" +
                "Name TEXT NOT NULL PRIMARY KEY, " +
                "Wins INTEGER NOT NULL, " +
                "Losses INTEGER NOT NULL, " +
                "Ties INTEGER NOT NULL, " +
                "HomeRecord TEXT NOT NULL, " +
                "RoadRecord TEXT NOT NULL, " +
                "DivisionRecord TEXT NOT NULL, " +
                "ConferenceRecord TEXT NOT NULL, " +
                "PointsFor INTEGER NOT NULL, " +
                "PointsAgainst INTEGER NOT NULL, " +
                "Streak INTEGER NOT NULL);";

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
        db.execSQL(STANDINGS_CREATE);

        insertInitialUpdates(db);
        insertInitialStandings(db);

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
            db.execSQL("DROP TABLE IF EXISTS Standings");

            DATABASE_VERSION = newVersion;
            onCreate(db);
        }
    }

    private void insertInitialUpdates(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", "Media");
        contentValues.put("Date", 0);
        db.insert("LastUpdated", null, contentValues);

        contentValues = new ContentValues();
        contentValues.put("Name", "Schedule");
        contentValues.put("Date", 0);
        db.insert("LastUpdated", null, contentValues);

        contentValues = new ContentValues();
        contentValues.put("Name", "Standings");
        contentValues.put("Date", 0);
        db.insert("LastUpdated", null, contentValues);

        contentValues = new ContentValues();
        contentValues.put("Name", "Twitter");
        contentValues.put("Date", 0);
        db.insert("LastUpdated", null, contentValues);
    }

    private void insertInitialStandings(SQLiteDatabase db) {
        for (int i = 0; i < 32; i++) {
            Standing standing = new Standing();

            ContentValues contentValues = new ContentValues();
            String team = TeamHelper.getTeamNickname(i);

            Log.e("EAGLES", "Inserting standings record for team: " + team);

            contentValues.put("Name", team);
            contentValues.put("Wins", standing.getWins());
            contentValues.put("Losses", standing.getLosses());
            contentValues.put("Ties", standing.getTies());
            contentValues.put("HomeRecord", standing.getHomeRecord());
            contentValues.put("RoadRecord", standing.getRoadRecord());
            contentValues.put("DivisionRecord", standing.getDivisionRecord());
            contentValues.put("ConferenceRecord", standing.getConferenceRecord());
            contentValues.put("PointsFor", standing.getPointsFor());
            contentValues.put("PointsAgainst", standing.getPointsAgainst());
            contentValues.put("Streak", standing.getStreak());

            db.insert("Standings", null, contentValues);
        }

    }

    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        ScheduleParams.setFirstFixture(0);
        ScheduleParams.setLastResult(0);
        ScheduleParams.setNextGameTime(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new FileHandler.writeScheduleParams().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        } else {
            new FileHandler.writeScheduleParams().execute();
        }

        db.execSQL("DROP TABLE IF EXISTS Media");
        db.execSQL("DROP TABLE IF EXISTS Schedule");
        db.execSQL("DROP TABLE IF EXISTS LastUpdated");
        db.execSQL("DROP TABLE IF EXISTS Standings");

        onCreate(db);



//        db.close();
    }
}
