package com.garethlewis.eagles.entities;

import android.support.annotation.Nullable;

public class Standing implements Comparable<Standing>{

    private String name;

    private int wins;
    private int losses;
    private int ties;
    
    private int homeWins;
    private int homeLosses;
    private int homeTies;

    private int roadWins;
    private int roadLosses;
    private int roadTies;

    private int divisionWins;
    private int divisionLosses;
    private int divisionTies;

    private int conferenceWins;
    private int conferenceLosses;
    private int conferenceTies;
    
    private int pointsFor;
    private int pointsAgainst;
    private int streak;

    public Standing() {
        this.name = "";

        this.wins = 0;
        this.losses = 0;
        this.ties = 0;

        this.homeWins = 0;
        this.homeLosses = 0;
        this.homeTies = 0;

        this.roadWins = 0;
        this.roadLosses = 0;
        this.roadTies = 0;

        this.divisionWins = 0;
        this.divisionLosses = 0;
        this.divisionTies = 0;

        this.conferenceWins = 0;
        this.conferenceLosses = 0;
        this.conferenceTies = 0;

        this.pointsFor = 0;
        this.pointsAgainst = 0;
        this.streak = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addHomeWin(boolean sameDivision, boolean sameConference) {
        this.wins++;
        this.homeWins++;
        if (sameDivision) this.divisionWins++;
        if (sameConference) this.conferenceWins++;

        if (streak > 0) streak++;
        else streak = 1;
    }
    
    public void addHomeLoss(boolean sameDivision, boolean sameConference) {
        this.losses++;
        this.homeLosses++;
        if (sameDivision) this.divisionLosses++;
        if (sameConference) this.conferenceLosses++;

        if (streak < 0) streak--;
        else streak = -1;
    }
    
    public void addHomeTie(boolean sameDivision, boolean sameConference) {
        this.ties++;
        this.homeTies++;
        if (sameDivision) this.divisionTies++;
        if (sameConference) this.conferenceTies++;

        streak = 0;
    }

    public void addRoadWin(boolean sameDivision, boolean sameConference) {
        this.wins++;
        this.roadWins++;
        if (sameDivision) this.divisionWins++;
        if (sameConference) this.conferenceWins++;

        if (streak > 0) streak++;
        else streak = 1;
    }

    public void addRoadLoss(boolean sameDivision, boolean sameConference) {
        this.losses++;
        this.roadLosses++;
        if (sameDivision) this.divisionLosses++;
        if (sameConference) this.conferenceLosses++;

        if (streak < 0) streak--;
        else streak = -1;
    }

    public void addRoadTie(boolean sameDivision, boolean sameConference) {
        this.ties++;
        this.roadTies++;
        if (sameDivision) this.divisionTies++;
        if (sameConference) this.conferenceTies++;

        streak = 0;
    }

    public void addPointsFor(int points) {
        this.pointsFor += points;
    }

    public void addPointsAgainst(int points) {
        this.pointsAgainst += points;
    }
    
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public void setHomeRecord(String record) {
        String[] parts = record.split("-");
        this.homeWins = Integer.parseInt(parts[0]);
        this.homeLosses = Integer.parseInt(parts[1]);

        if (parts.length == 3) this.homeTies = Integer.parseInt(parts[2]);
        else this.homeTies = 0;
    }

    public void setRoadRecord(String record) {
        String[] parts = record.split("-");
        this.roadWins = Integer.parseInt(parts[0]);
        this.roadLosses = Integer.parseInt(parts[1]);

        if (parts.length == 3) this.roadTies = Integer.parseInt(parts[2]);
        else this.roadTies = 0;
    }

    public void setDivisionRecord(String record) {
        String[] parts = record.split("-");
        this.divisionWins = Integer.parseInt(parts[0]);
        this.divisionLosses = Integer.parseInt(parts[1]);

        if (parts.length == 3) this.divisionTies = Integer.parseInt(parts[2]);
        else this.divisionTies = 0;
    }

    public void setConferenceRecord(String record) {
        String[] parts = record.split("-");
        this.conferenceWins = Integer.parseInt(parts[0]);
        this.conferenceLosses = Integer.parseInt(parts[1]);

        if (parts.length == 3) this.conferenceTies = Integer.parseInt(parts[2]);
        else this.conferenceTies = 0;
    }

    public String getHomeRecord() {
        String record = homeWins + "-" + homeLosses;
        if (homeTies != 0) record += ("-" + homeTies);
        return record;
    }

    public String getRoadRecord() {
        String record = roadWins + "-" + roadLosses;
        if (roadTies != 0) record += ("-" + roadTies);
        return record;
    }

    public String getDivisionRecord() {
        String record = divisionWins + "-" + divisionLosses;
        if (divisionTies != 0) record += ("-" + divisionTies);
        return record;
    }

    public String getConferenceRecord() {
        String record = conferenceWins + "-" + conferenceLosses;
        if (conferenceTies != 0) record += ("-" + conferenceTies);
        return record;
    }

    public int getPointsFor() {
        return pointsFor;
    }

    public void setPointsFor(int pointsFor) {
        this.pointsFor = pointsFor;
    }

    public int getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(int pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    @Override
    public int compareTo(@Nullable Standing other) {
        if (other == null) return 1;

        float myPct = (float) (this.wins + (this.ties / 2)) / (float) (this.wins + this.losses + this.ties);
        float otherPct = (float) (other.wins + (other.ties / 2)) / (float) (other.wins + other.losses + other.ties);

        String tmp1 = String.format("%.3f", myPct);
        String tmp2 = String.format("%.3f", otherPct);

        if (!tmp1.equals(tmp2)) {
            return (Float.compare(myPct, otherPct) * -1);
        }

        // TODO: check results against each other.

        myPct = (float) (this.divisionWins + (this.divisionTies / 2)) / (float) (this.divisionWins + this.divisionLosses + this.divisionTies);
        otherPct = (float) (other.divisionWins + (other.divisionTies / 2)) / (float) (other.divisionWins + other.divisionLosses + other.divisionTies);

        tmp1 = String.format("%.3f", myPct);
        tmp2 = String.format("%.3f", otherPct);

        if (!tmp1.equals(tmp2)) {
            return (Float.compare(myPct, otherPct) * -1);
        }

        myPct = (float) (this.conferenceWins + (this.conferenceTies / 2)) / (float) (this.conferenceWins + this.conferenceLosses + this.conferenceTies);
        otherPct = (float) (other.conferenceWins + (other.conferenceTies / 2)) / (float) (other.conferenceWins + other.conferenceLosses + other.conferenceTies);

//        tmp1 = String.format("%.3f", myPct);
//        tmp2 = String.format("%.3f", otherPct);

//        if (!tmp1.equals(tmp2)) {
            return (Float.compare(myPct, otherPct) * -1);
//        }
        
//        return 0;
    }
}
