package com.garethlewis.eagles.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.util.TeamHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleSQLiteHelper extends MasterDatabase {
    private static ScheduleSQLiteHelper sInstance;

    private static String TABLE_SCHEDULE = "Schedule";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_HOME_TEAM = "HomeTeam";
    private static final String COLUMN_AWAY_TEAM = "AwayTeam";
    private static final String COLUMN_HOME_TEAM_SCORE = "HomeTeamScore";
    private static final String COLUMN_AWAY_TEAM_SCORE = "AwayTeamScore";
    private static final String COLUMN_WEEK = "Week";
    private static final String COLUMN_STATUS = "Status";
    private static final String COLUMN_ADDED = "Added";

    private static final String MATCH_TEAM = COLUMN_HOME_TEAM + " = ? OR " + COLUMN_AWAY_TEAM + " = ?";

    public static ScheduleSQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ScheduleSQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    
    private ScheduleSQLiteHelper(Context context) {
        super(context);
    }

//    public boolean deleteAllFixtures() {
//        SQLiteDatabase dbw = this.getWritableDatabase();
//        return (dbw.delete("Schedule", "1", null) > 0);
//    }

    public boolean getAdded(String home, String away, String week) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Added FROM Schedule WHERE (HomeTeam = ? AND AwayTeam = ? AND Week = ?);", new String[] {home, away, week});

        if (cursor.moveToFirst()) {
            String added = cursor.getString(0);
            return "1".equals(added);
        }

        return false;
    }

    public boolean setAdded(List<Fixture> fixtures) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "(HomeTeam = ? AND AwayTeam = ? AND Week = ?)";

        for (Fixture f : fixtures) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ADDED, 1);

            String[] terms = new String[] { f.getHomeTeam(), f.getAwayTeam(), f.getWeek() };
            boolean success = (db.update(TABLE_SCHEDULE, contentValues, whereClause, terms) > 0);
            if (!success) {
                return false;
            }
        }
        return true;
    }

    public boolean insertManyFixtures(List<Fixture> fixtures) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Fixture f : fixtures) {
            boolean isAdded = getAdded(f.getHomeTeam(), f.getAwayTeam(), f.getWeek());
            if (isAdded) {
                continue;
            }
            ContentValues contentValues = new ContentValues();

            String time = f.getTime();
            if ("TBD".equals(time)) time = "5:32 AM";

            contentValues.put(COLUMN_DATE, Fixture.dateStringToEpoch(f.getDate() + " " + time));
            contentValues.put(COLUMN_HOME_TEAM, f.getHomeTeam());
            contentValues.put(COLUMN_AWAY_TEAM, f.getAwayTeam());
            contentValues.put(COLUMN_WEEK, f.getWeek());
            if (f.getHomeScore() != null) {
                contentValues.put(COLUMN_HOME_TEAM_SCORE, f.getHomeScore());
            }
            if (f.getAwayScore() != null) {
                contentValues.put(COLUMN_AWAY_TEAM_SCORE, f.getAwayScore());
            }
            contentValues.put(COLUMN_STATUS, f.getStatus());
            contentValues.put(COLUMN_ADDED, 0);
            int id = getFixture(db, f);
            boolean success;
            if (id != -1) {
                contentValues.put(COLUMN_ID, id);
                success = (db.replace(TABLE_SCHEDULE, null, contentValues) != -1);
            } else {
                success = (db.insert(TABLE_SCHEDULE, null, contentValues) != -1);
            }

            if (!success) return false;
        }

        UpdatedSQLiteHelper updatedSQLiteHelper = UpdatedSQLiteHelper.getInstance(getContext());
        updatedSQLiteHelper.setUpdated(TABLE_SCHEDULE);

        return true;
    }

//    /**
//     * Takes a single <code>Fixture</code> and adds it to the database.
//     * If the fixture already exists then it replaces the found record.
//     * @param item The Fixture to insert.
//     * @return If the insert was successful.
//     */
//    public boolean insertFixture(Fixture item) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(COLUMN_DATE, Fixture.dateStringToEpoch(item.getDate()));
//        contentValues.put(COLUMN_HOME_TEAM, item.getHomeTeam());
//        contentValues.put(COLUMN_AWAY_TEAM, item.getAwayTeam());
//        contentValues.put(COLUMN_WEEK, item.getWeek());
//        if (item.getHomeScore() != null) {
//            contentValues.put(COLUMN_HOME_TEAM_SCORE, item.getHomeScore());
//        }
//        if (item.getAwayScore() != null) {
//            contentValues.put(COLUMN_AWAY_TEAM_SCORE, item.getAwayScore());
//        }
//        int id = getFixture(db, item);
//        boolean success;
//        if(id != -1){
//            contentValues.put(COLUMN_ID,id);
//            success = (db.replace(TABLE_SCHEDULE, null, contentValues) != -1);
//        }else{
//            success = (db.insert(TABLE_SCHEDULE, null, contentValues) != -1);
//        }
//        return success;
//    }

    /**
     * Gets the next game for the specified team.
     * @param teamName team to retrieve the next game for.
     * @return The object containing the fixture.
     */
    public Fixture getNextGame(String teamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = COLUMN_DATE + " > ? AND ( " + MATCH_TEAM + ")";
        String[] selectionArgs = new String[]{String.valueOf(new Date().getTime()), teamName, teamName};
        String orderBy = COLUMN_DATE + " ASC";
        Cursor cursor = db.query(TABLE_SCHEDULE, null, where, selectionArgs, null, null, orderBy, "1");
        Fixture fixture = null;
        while (cursor.moveToNext()) {
            fixture = cursorToFixture(cursor);
        }
        cursor.close();
        return fixture;
    }

    /**
     * Gets the previous two games for the specified team.
     * @param teamName team to retrieve the previous two games for.
     * @return A list of the previous two results.
     */
    public Fixture getLastGame(String teamName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Fixture lastGame = null;

        String where = COLUMN_DATE + " < ? AND ( " + MATCH_TEAM + ")";
        String[] selectionArgs = new String[]{String.valueOf(new Date().getTime()), teamName, teamName};
        String orderBy = COLUMN_DATE + " DESC";
        Cursor cursor = db.query(TABLE_SCHEDULE, null, where, selectionArgs, null, null, orderBy, "1");
        while (cursor.moveToNext()) {
            lastGame = cursorToFixture(cursor);
        }

        cursor.close();
        return lastGame;
    }

    public boolean gameInProgress() {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = COLUMN_STATUS + " = 1";
        Cursor cursor = db.query(TABLE_SCHEDULE, null, where, null, null, null, null);
        return cursor.moveToFirst();
    }

//    /**
//     * Convenience method for getting the three games required for the home header
//     * @param teamName team to retrieve fixtures fo.
//     * @return An array of the previous game and the next game.
//     * @throws FixtureNotFoundException if any data is wrong.
//     */
//    public Fixture[] getHomeHeaderGames(String teamName) throws FixtureNotFoundException {
//        Fixture[] fixtures = new Fixture[2];
//        Fixture lastGame = getLastGame(teamName);
//        Fixture nextGame = getNextGame(teamName);
//        if (lastGame == null || nextGame == null) {
//            throw new FixtureNotFoundException();
//        }
//
//        fixtures[0] = lastGame;
//        fixtures[1] = nextGame;
//        return fixtures;
//    }

    /**
     * Gets the fixtures or results for a specified team.
     * @param teamIndex Position of the team to retrieve fixtures and results for.
     * @return A list of fixtures or results for the specified team
     */
    public List<Fixture> getFixturesForTeam(int teamIndex){
        String teamName = TeamHelper.getTeamNickname(teamIndex);
        return getFixturesForTeam(teamName);
    }

    /**
     * Gets the fixtures or results for a specified team.
     * @param teamName Name of the team to retrieve fixtures and results for.
     * @return A list of fixtures or results for the specified team
     */
    public List<Fixture> getFixturesForTeam(String teamName){
        List<Fixture> fixtureList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = new String[]{teamName, teamName};
        String orderBy = COLUMN_ID + " ASC";
        Cursor cursor = db.query(TABLE_SCHEDULE, null, MATCH_TEAM, selectionArgs, null, null, orderBy);
        while (cursor.moveToNext()) {
            Fixture fixture = cursorToFixture(cursor);
            fixtureList.add(fixture);
        }
        cursor.close();
        return fixtureList;
    }

    /**
     * Fetches all fixtures for a specified game week.
     * @param week the game week to get fixtures for.
     * @return a list of the fixtures.
     */
    public List<Fixture> getFixturesForWeek(int week) {
        List<Fixture> fixtureList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = new String[]{"" + week};
        Cursor cursor = db.rawQuery("SELECT * FROM SCHEDULE WHERE Week = ?;", selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Fixture fixture = cursorToFixture(cursor);
            fixtureList.add(fixture);
            cursor.moveToNext();
        }
        cursor.close();
        return fixtureList;
    }

    public List<Fixture> getPostseasonFixtures() {
        List<Fixture> fixtureList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = new String[] {"17"};
        Cursor cursor = db.rawQuery("SELECT * FROM SCHEDULE WHERE CAST(Week as INTEGER) > ?;", selectionArgs);
        while (cursor.moveToNext()) {
            Fixture fixture = cursorToFixture(cursor);
            fixtureList.add(fixture);
        }
        cursor.close();
        return fixtureList;
    }

    /**
     * Converts the cursor returned from a query into a Fixture object.
     * @param cursor Cursor to convert
     * @return Converted Fixture
     */
    private Fixture cursorToFixture(Cursor cursor) {
        Fixture fixture = new Fixture();

        String date = Fixture.epochToDateString(cursor.getLong(1));
        String[] dateParts = date.split(" ");

        String time = dateParts[dateParts.length - 2] + " " + dateParts[dateParts.length - 1];

        fixture.setDate(date.replace(time, "").trim());
        if ("5:32 AM".equals(time) && cursor.getInt(3) == -1) time = "TBD";
        fixture.setTime(time);

        fixture.setHomeTeam(cursor.getString(2));
        fixture.setHomeScore(cursor.getInt(3));
        fixture.setAwayTeam(cursor.getString(4));
        fixture.setAwayScore(cursor.getInt(5));

        fixture.setWeek(cursor.getString(6));
        fixture.setStatus(cursor.getInt(7));
        return fixture;
    }

    /**
     * Gets the fixture with the specified values if exists.
     * @param fixture Fixture to check for.
     * @return The id of the fixtures, if fixture doesn't exist returns -1.
     */
    private int getFixture(SQLiteDatabase db, Fixture fixture) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selection = COLUMN_HOME_TEAM + " = ? AND " + COLUMN_AWAY_TEAM + " = ? AND " + COLUMN_WEEK + " = ?";
//        String epoch = String.valueOf(Fixture.dateStringToEpoch(fixture.getDate() + " " + fixture.getTime()));
        if ("TBC".equals(fixture.getHomeTeam())) {
            return -1;
        }

        String[] selectionArgs = new String[]{fixture.getHomeTeam(), fixture.getAwayTeam(), fixture.getWeek()};
        Cursor cursor = db.rawQuery("SELECT _id FROM SCHEDULE WHERE (HomeTeam = ? AND AwayTeam = ? AND Week = ?)", selectionArgs);
//        Cursor cursor = db.query(TABLE_SCHEDULE,null,selection,selectionArgs,null,null,null);
        int id=-1;
        if(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public float checkHeadToHead(String team1, String team2) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = new String[] {team1, team2};
        Cursor cursor = db.rawQuery("SELECT HomeTeamScore, AwayTeamScore FROM SCHEDULE WHERE (HomeTeam = ? AND AwayTeam = ? AND CAST(Week AS INTEGER) <= 17)", selectionArgs);

        int wins = 0, losses = 0, ties = 0;
        while (cursor.moveToNext()) {
            int home = cursor.getInt(0), away = cursor.getInt(1);
            if (home > away) {
                wins++;
            } else {
                if (away > home) {
                    losses++;
                } else {
                    ties++;
                }
            }
        }

        cursor.close();
        cursor = db.rawQuery("SELECT HomeTeamScore, AwayTeamScore FROM SCHEDULE WHERE (AwayTeam = ? AND HomeTeam = ? AND CAST(Week AS INTEGER) <= 17)", selectionArgs);
        while (cursor.moveToNext()) {
            int home = cursor.getInt(0), away = cursor.getInt(1);
            if (home > away) {
                losses++;
            } else {
                if (away > home) {
                    wins++;
                } else {
                    ties++;
                }
            }
        }

        cursor.close();
        return (float) (wins + ties) / (float) (wins + losses + ties);
//        Cursor cursor = db.rawQuery("SELECT AwayTeam FROM SCHEDULE WHERE (HomeTeam = ? AND HomeTeamScore > AwayTeamScore And HomeTeamScore != -1 AND Status = 2);", selectionArgs);
    }

    public float[] checkCommonGames(String team1, String team2) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = new String[] {team1};
        Cursor cursor = db.rawQuery("SELECT AwayTeam, HomeTeamScore, AwayTeamScore FROM SCHEDULE WHERE HomeTeam = ? AND Status = 2 AND AwayTeam <> ''  AND CAST(Week AS INTEGER) <= 17", selectionArgs);

        List<Integer> results = new ArrayList<>();

        List<String> opponents = new ArrayList<>();
        while (cursor.moveToNext()) {
            opponents.add(cursor.getString(0));
            int home = cursor.getInt(1), away = cursor.getInt(2);
            if (home > away) {
                results.add(2);
            } else {
                if (away > home) {
                    results.add(0);
                } else {
                    results.add(1);
                }
            }
        }

        cursor.close();
        cursor = db.rawQuery("SELECT HomeTeam, HomeTeamScore, AwayTeamScore FROM SCHEDULE WHERE AwayTeam = ? AND Status = 2 AND CAST(Week AS INTEGER) <= 17", selectionArgs);
        while (cursor.moveToNext()) {
            opponents.add(cursor.getString(0));
            int home = cursor.getInt(1), away = cursor.getInt(2);
            if (home > away) {
                results.add(0);
            } else {
                if (away > home) {
                    results.add(2);
                } else {
                    results.add(1);
                }
            }
        }

        Set<String> set = new HashSet<>(opponents);
        opponents.clear();
        opponents.addAll(set);

        // Create a
        String homeWhereClause = "";
        String awayWhereClause = "";
        int size = opponents.size();

        selectionArgs = new String[size];

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                homeWhereClause += " OR AwayTeam = ?";
                awayWhereClause += " OR HomeTeam = ?";
            } else {
                homeWhereClause += "WHERE HomeTeam = '" + team2 + "' AND (AwayTeam = ?";
                awayWhereClause += "WHERE AwayTeam = '" + team2 + "' AND (HomeTeam = ?";
            }
            selectionArgs[i] = opponents.get(i);
        }

        if (!"".equals(homeWhereClause)) {
            homeWhereClause += ");";
        }
        if (!"".equals(awayWhereClause)) {
            awayWhereClause += ");";
        }

        int team1Wins = 0, team1Losses = 0, team1Ties = 0;
        int team2Wins = 0, team2Losses = 0, team2Ties = 0;

        cursor.close();
        cursor = db.rawQuery("SELECT AwayTeam, HomeTeamScore, AwayTeamScore FROM SCHEDULE " + homeWhereClause, selectionArgs);
        while (cursor.moveToNext()) {
            int home = cursor.getInt(1), away = cursor.getInt(2);
            int index = opponents.indexOf(cursor.getString(0));
            int result = results.get(index);
            if (result == 0) {
                team1Losses++;
            } else  {
                if (result == 1) {
                    team1Ties++;
                } else {
                    team1Wins++;
                }
            }

            if (home > away) {
                team2Wins++;
            } else {
                if (away > home) {
                    team2Losses++;
                } else {
                    team2Ties++;
                }
            }
        }

        cursor.close();
        cursor = db.rawQuery("SELECT HomeTeam, HomeTeamScore, AwayTeamScore FROM SCHEDULE " + awayWhereClause, selectionArgs);
        while (cursor.moveToNext()) {
            int home = cursor.getInt(1), away = cursor.getInt(2);
            int index = opponents.indexOf(cursor.getString(0));
            int result = results.get(index);
            if (result == 0) {
                team1Losses++;
            } else  {
                if (result == 1) {
                    team1Ties++;
                } else {
                    team1Wins++;
                }
            }

            if (home > away) {
                team2Losses++;
            } else {
                if (away > home) {
                    team2Wins++;
                } else {
                    team2Ties++;
                }
            }
        }

        float team1Pct = (float) (team1Wins + (team1Ties / 2)) / (float) (team1Wins + team1Losses + team1Ties);
        float team2Pct = (float) (team2Wins + (team2Ties / 2)) / (float) (team2Wins + team2Losses + team2Ties);

//        Log.e("EAGLES",team1 + " - " + team1Pct + ", " + team2 + " - " + team2Pct);

        return new float[] {team1Pct, team2Pct};
    }

    public float checkStrengthOfVictory(String team) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = new String[] { team };
        Cursor cursor = db.rawQuery("SELECT AwayTeam FROM SCHEDULE WHERE HomeTeam = ? AND HomeTeamScore > AwayTeamScore AND Status = 2 AND CAST(Week AS INTEGER) <= 17", selectionArgs);

        List<String> victories = new ArrayList<>();
        while (cursor.moveToNext()) {
            victories.add(cursor.getString(0));
        }

        cursor.close();

        cursor = db.rawQuery("SELECT HomeTeam FROM SCHEDULE WHERE AwayTeam = ? AND AwayTeamScore > HomeTeamScore AND Status = 2 AND CAST(Week AS INTEGER)<= 17", selectionArgs);

        while (cursor.moveToNext()) {
            victories.add(cursor.getString(0));
        }

        int[] record = new int[3]; // [0] = wins, [1] = losses, [2] = ties.
        int count = victories.size();

        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getContext());
        for (int i = 0; i < count; i++) {
            record = readRecord(standingsDB, record, victories.get(i));
        }

        cursor.close();
        return ((record[0] + ((float) record[2] / 2)) / (float) (record[0] + record[1] + record[2]));
    }

    public float checkStrengthOfSchedule(String team) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = new String[] { team };
        Cursor cursor = db.rawQuery("SELECT AwayTeam FROM SCHEDULE WHERE HomeTeam = ? AND Status = 2 AND AwayTeam <> '' AND CAST(Week AS INTEGER) <= 17", selectionArgs);

        List<String> victories = new ArrayList<>();
        while (cursor.moveToNext()) {
            victories.add(cursor.getString(0));
        }

        cursor.close();

        cursor = db.rawQuery("SELECT HomeTeam FROM SCHEDULE WHERE AwayTeam = ? AND Status = 2 AND CAST(Week AS INTEGER) <= 17", selectionArgs);

        while (cursor.moveToNext()) {
            victories.add(cursor.getString(0));
        }

        int[] record = new int[3]; // [0] = wins, [1] = losses, [2] = ties.
        int count = victories.size();

        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getContext());
        for (int i = 0; i < count; i++) {
            record = readRecord(standingsDB, record, victories.get(i));
        }

        cursor.close();
        return ((record[0] + ((float) record[2] / 2)) / (float) (record[0] + record[1] + record[2]));
    }

    private int[] readRecord(StandingsSQLiteHelper db, int[] total, String team) {
        String record = db.getRecord(team); // In format X-X(-X)
        String[] parts = record.split("-");

        try {
            int ties = 0;
            int wins = Integer.parseInt(parts[0].trim());
            int losses = Integer.parseInt(parts[1].trim());

            if (parts.length > 2) {
                ties = Integer.parseInt(parts[2].trim());
            }

            total[0] += wins;
            total[1] += losses;
            total[2] += ties;
        } catch (NumberFormatException e) {
            // Error getting record from database. Message already thrown, so do nothing.
            Log.e("EAGLES1","'" + team + "': " + e.getMessage());
        }

        return total;
    }
}