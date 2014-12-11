package com.garethlewis.eagles.util;

public class ScheduleParams {
    private static String filename = "schedule_parameters.dat";

    private static String saveString = "";

    private static int lastResultWeek = 0;
    private static int firstFixtureWeek = 0;

    public static int getLastResult() {
        return lastResultWeek;
    }

    public static void setLastResult(int week) {
        lastResultWeek = week;
    }

    public static int getFirstFixture() {
        return firstFixtureWeek;
    }

    public static void setFirstFixture(int week) {
        firstFixtureWeek = week;
    }
}
