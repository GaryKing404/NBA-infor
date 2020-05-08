package com.example.Bean;

public class RaceOverview {
    private int match_id;
    private String time;
    private String team1;
    private String team2;
    private int score1;
    private int score2;
    private boolean isCompleted = false;

    public RaceOverview(int match_id, String time, String team1, String team2) {
        this.match_id = match_id;
        this.time = time;
        this.team1 = team1;
        this.team2 = team2;
        this.isCompleted = false;
    }

    public RaceOverview(int match_id, String time, String team1, String team2, int score1, int score2) {
        this.match_id = match_id;
        this.time = time;
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.isCompleted = true;
    }

    public String getScore1() {
        return String.valueOf(score1);
    }

    public String getScore2() {
        return String.valueOf(score2);
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getTime() {
        return time;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getMatch_id() {
        return match_id;
    }

}

