package com.garethlewis.eagles.util;

public class Team {

    private String place;
    private String nickname;

    public Team(String place, String nickname) {
        this.place = place;
        this.nickname = nickname;
    }

    public String getPlace() {
        return place;
    }

    public String getNickname() {
        return nickname;
    }
}
