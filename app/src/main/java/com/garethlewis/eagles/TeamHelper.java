package com.garethlewis.eagles;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

public class TeamHelper {

    private static List<Integer> afcTeams = null;
    private static List<Integer> nfcTeams = null;

    private static List<Integer> afcEastTeams = null;
    private static List<Integer> afcNorthTeams = null;
    private static List<Integer> afcSouthTeams = null;
    private static List<Integer> afcWestTeams = null;
    private static List<Integer> nfcEastTeams = null;
    private static List<Integer> nfcNorthTeams = null;
    private static List<Integer> nfcSouthTeams = null;
    private static List<Integer> nfcWestTeams = null;

    /**
     * Takes a team name and returns an index representing where the team sits in an alpabetical
     * list of all NFL teams.
     * @param name
     *      The name of the team to get the index for.
     * @return
     *      The index of where the team is in alphabetical order.
     */
    public static int getTeamIndex(String name) {
        switch (name) {
            case "arizona": return 0;
            case "atlanta": return 1;
            case "baltimore": return 2;
            case "buffalo": return 3;
            case "carolina": return 4;
            case "chicago": return 5;
            case "cincinnati": return 6;
            case "cleveland": return 7;
            case "dallas": return 8;
            case "denver": return 9;
            case "detroit": return 10;
            case "green_bay": return 11;
            case "houston": return 12;
            case "indianapolis": return 13;
            case "jacksonville": return 14;
            case "kansas_city": return 15;
            case "miami": return 16;
            case "minnesota": return 17;
            case "new_england": return 18;
            case "new_orleans": return 19;
            case "ny_giants": return 20;
            case "ny_jets": return 21;
            case "oakland": return 22;
            case "philadelphia": return 23;
            case "pittsburgh": return 24;
            case "st_louis": return 25;
            case "san_diego": return 26;
            case "san_francisco": return 27;
            case "seattle": return 28;
            case "tampa_bay": return 29;
            case "tennessee": return 30;
            case "washington": return 31;
            default: return -1;
        }
    }

    /**
     * Takes an index and returns the team name associated with it.
     * @param index
     *      The index of the team to find the name of.
     * @return
     *      The team name.
     */
    public static String getTeamName(int index) {
        switch (index) {
            case 0: return "arizona";
            case 1: return "atlanta";
            case 2: return "baltimore";
            case 3: return "buffalo";
            case 4: return "carolina";
            case 5: return "chicago";
            case 6: return "cincinnati";
            case 7: return "cleveland";
            case 8: return  "dallas";
            case 9: return "denver";
            case 10: return "detroit";
            case 11: return "green_bay";
            case 12: return "houston";
            case 13: return "indianapolis";
            case 14: return "jacksonville";
            case 15: return "kansas_city";
            case 16: return "miami";
            case 17: return "minnesota";
            case 18: return "new_england";
            case 19: return "new_orleans";
            case 20: return "ny_giants";
            case 21: return "ny_jets";
            case 22: return "oakland";
            case 23: return "philadelphia";
            case 24: return "pittsburgh";
            case 25: return "st_louis";
            case 26: return "san_diego";
            case 27: return "san_francisco";
            case 28: return "seattle";
            case 29: return "tampa_bay";
            case 30: return "tennessee";
            case 31: return "washington";
            default: return "error";
        }
    }

    /**
     * Checks if two teams are in the same division.
     * @param context
     *      The context of the application.
     * @param team1
     *      One of the teams to check.
     * @param team2
     *      One of the teams to check.
     * @return
     *      true if the teams are in the same division, false otherwise.
     */
    public static boolean areSameDivision(Context context, int team1, int team2) {
        if (afcEastTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.afcEastTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            afcEastTeams = Arrays.asList(newArray);
        }
        if (afcEastTeams.contains(team1)) {
            return afcEastTeams.contains(team2);
        }

        if (afcNorthTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.afcNorthTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            afcNorthTeams = Arrays.asList(newArray);
        }
        if (afcNorthTeams.contains(team1)) {
            return afcNorthTeams.contains(team2);
        }

        if (afcSouthTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.afcSouthTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            afcSouthTeams = Arrays.asList(newArray);
        }
        if (afcSouthTeams.contains(team1)) {
            return afcSouthTeams.contains(team2);
        }

        if (afcWestTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.afcWestTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            afcWestTeams = Arrays.asList(newArray);
        }
        if (afcWestTeams.contains(team1)) {
            return afcWestTeams.contains(team2);
        }

        if (nfcEastTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.nfcEastTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            nfcEastTeams = Arrays.asList(newArray);
        }
        if (nfcEastTeams.contains(team1)) {
            return nfcEastTeams.contains(team2);
        }

        if (nfcNorthTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.nfcNorthTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            nfcNorthTeams = Arrays.asList(newArray);
        }
        if (nfcNorthTeams.contains(team1)) {
             return nfcNorthTeams.contains(team2);
        }

        if (nfcSouthTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.nfcSouthTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            nfcSouthTeams = Arrays.asList(newArray);
        }
        if (nfcSouthTeams.contains(team1)) {
            return nfcSouthTeams.contains(team2);
        }

        if (nfcWestTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.nfcWestTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            nfcWestTeams = Arrays.asList(newArray);
        }

        // team1 must be in the NFC West, so only check team2
         return nfcWestTeams.contains(team2);
    }

    /**
     * Checks if two teams are in the same conference.
     * @param context
     *      The context of the application.
     * @param team1
     *      One of the teams to check.
     * @param team2
     *      One of the teams to check.
     * @return
     *      true if the teams are in the same conference, false otherwise.
     */
    public static boolean areSameConference(Context context, int team1, int team2) {
        if (afcTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.afcTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            afcTeams = Arrays.asList(newArray);
        }
        if (afcTeams.contains(team1)) {
            return afcTeams.contains(team2);
        }

        if (nfcTeams == null) {
            int[] tmp = context.getResources().getIntArray(R.array.nfcTeams);
            Integer[] newArray = new Integer[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                newArray[i] = tmp[i];
            }
            nfcTeams = Arrays.asList(newArray);
        }
        // Don't need to check team1
        return nfcTeams.contains(team2);
    }

}
