package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.garethlewis.eagles.TeamHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.database.entities.Standing;

import java.util.List;

public class StandingsSQLiteHelper extends MasterDatabase {

    private static StandingsSQLiteHelper sInstance;

    private Context context;

    public static StandingsSQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new StandingsSQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private StandingsSQLiteHelper(Context context) {
        super(context);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean deleteAllStandings() {
        SQLiteDatabase dbw = this.getWritableDatabase();
        boolean success = (dbw.delete("Standings", "1", null) > 0);


        return success;
    }

    public boolean updateStandings(List<Fixture> fixtures) {
        SQLiteDatabase db = this.getWritableDatabase();
        Standing[] standings = getExistingStandings(db);

        for (Fixture f : fixtures) {
            String team = f.getHomeTeam().toLowerCase().replace(" ", "_").replace(".", "");
            int home = TeamHelper.getTeamIndex(team);
            team = f.getAwayTeam().toLowerCase().replace(" ", "_").replace(".", "");
            int away = TeamHelper.getTeamIndex(team);

            int homeScore = f.getHomeScore();
            int awayScore = f.getAwayScore();

            if (away == -1 || homeScore == -1) { // Bye week || future fixture, don't update standings
                continue;
            }

            boolean sameDivision = TeamHelper.areSameDivision(context, home, away);
            boolean sameConference = TeamHelper.areSameConference(context, home, away);
            if (homeScore < awayScore) {
                standings[home].addHomeLoss(sameDivision, sameConference);
                standings[away].addRoadWin(sameDivision, sameConference);
            } else {
                if (awayScore < homeScore) {
                    standings[home].addHomeWin(sameDivision, sameConference);
                    standings[away].addRoadLoss(sameDivision, sameConference);
                } else {
                    standings[home].addHomeTie(sameDivision, sameConference);
                    standings[away].addRoadTie(sameDivision, sameConference);
                }
            }

            standings[home].addPointsFor(homeScore);
            standings[home].addPointsAgainst(awayScore);
            standings[away].addPointsFor(awayScore);
            standings[away].addPointsAgainst(homeScore);
        }

        for (int i = 0; i < standings.length; i++) {
            ContentValues contentValues = new ContentValues();
            String team = TeamHelper.getTeamName(i);

            contentValues.put("Name", team);
            contentValues.put("Wins", standings[i].getWins());
            contentValues.put("Losses", standings[i].getLosses());
            contentValues.put("Ties", standings[i].getTies());
            contentValues.put("HomeRecord", standings[i].getHomeRecord());
            contentValues.put("RoadRecord", standings[i].getRoadRecord());
            contentValues.put("DivisionRecord", standings[i].getDivisionRecord());
            contentValues.put("ConferenceRecord", standings[i].getConferenceRecord());
            contentValues.put("PointsFor", standings[i].getPointsFor());
            contentValues.put("PointsAgainst", standings[i].getPointsAgainst());
            contentValues.put("Streak", standings[i].getStreak());

            boolean success = (db.replace("Standings", null, contentValues) != -1);
            if (!success) {
                return false;
            }
        }

        return true;
    }

    private Standing[] getExistingStandings(SQLiteDatabase db) {
        Standing[] standings = new Standing[32];
        for (int i = 0; i < 32; i++) standings[i] = new Standing();

        Cursor cursor = db.rawQuery("SELECT * FROM Standings", null);
        while (cursor.moveToNext()) {
            int team = TeamHelper.getTeamIndex(cursor.getString(0));
            standings[team].setWins(cursor.getInt(1));
            standings[team].setLosses(cursor.getInt(2));
            standings[team].setTies(cursor.getInt(3));

            standings[team].setHomeRecord(cursor.getString(4));
            standings[team].setRoadRecord(cursor.getString(5));
            standings[team].setDivisionRecord(cursor.getString(6));
            standings[team].setConferenceRecord(cursor.getString(7));

            standings[team].setPointsFor(cursor.getInt(8));
            standings[team].setPointsAgainst(cursor.getInt(9));
            standings[team].setStreak(cursor.getInt(10));
        }

        cursor.close();
        return standings;
    }

    public Standing[] getAllTeams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return getExistingStandings(db);
    }

    public String getRecord(String team) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Wins, Losses, Ties FROM Standings Where Name = ?", new String[] {team});
        cursor.moveToFirst();
        String record = cursor.getString(0) + " - " + cursor.getString(1);
        String ties = cursor.getString(2);
        if (!"0".equals(ties)) record += (" - " + ties);
        return record;
    }

}
