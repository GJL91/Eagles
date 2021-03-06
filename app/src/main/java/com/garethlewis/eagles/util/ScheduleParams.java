package com.garethlewis.eagles.util;

public class ScheduleParams {
    private static int lastResultWeek = 0;
    private static int firstFixtureWeek = 0;
    private static long nextGameTime = 0;

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

    public static long getNextGameTime() {
        return nextGameTime;
    }

    public static void setNextGameTime(long time) { nextGameTime = time; }
}
