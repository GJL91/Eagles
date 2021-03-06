package com.garethlewis.eagles.entities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fixture {
    private String homeTeam;
    private String awayTeam;
    private Integer homeScore;
    private Integer awayScore;
    private String date;
    private String time;
    private String week;
    private Integer status;

    public Fixture() {}

    public Fixture(String homeTeam, String awayTeam, Integer homeScore, Integer awayScore,
                   String date, String time, String week, Integer status) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.date = date;
        this.time = time;
        this.week = week;
        this.status = status;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Converts the string representation of a date to epoch time for database
     * @param date string to be converted, example: THU, NOV 6 2014 1:00 PM
     * @return epoch time
     */
    public static long dateStringToEpoch(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d yyyy h:mm a");
        try {
            Date parsedDate = formatter.parse(date);
            return parsedDate.getTime();
        } catch (ParseException e) {
            Log.e("FixtureDateException", date);
            return 0;
        }
    }

    /**
     * Converts the epoch time long to a String representation
     * @param epochTime epoch time to be converted
     * @return Date string, example:
     */
    public static String epochToDateString(long epochTime) {
        Date epochDate = new Date(epochTime);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d yyyy h:mm a");
        return formatter.format(epochDate);
    }

    @Override
    public String toString() {
        return "Week " + week + ", " + date + ": " + awayTeam + " " + awayScore + " @ " + homeTeam + " " + homeScore;
    }
}
