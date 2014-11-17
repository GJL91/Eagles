package com.garethlewis.eagles;

public class ScheduleParams {
    private static String filename = "schedule_paramaters.dat";

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

//    private static void setString() {
//        saveString = "LastResultWeek = " + lastResultWeek + "\n";
//        saveString = saveString + "FirstFixtureWeek = " + firstFixtureWeek;
//    }

//    public static void writeToFile() {
//        setString();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            new writeParams().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
//        } else {
//            new writeParams().execute();
//        }
//    }
//
//    public static void readFromFile() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            new readParams().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
//        } else {
//            new readParams().execute();
//        }
//    }



}
