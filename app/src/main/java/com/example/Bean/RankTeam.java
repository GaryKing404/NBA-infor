package com.example.Bean;

public class RankTeam {

    private int rank_num;
    private String team;
    private int win_num;
    private int lose_num;
    private int win_rate;

    public RankTeam(int rank_num, String team, int win_num, int lose_num, int win_rate) {
        this.rank_num = rank_num;
        this.team = team;
        this.win_num = win_num;
        this.lose_num = lose_num;
        this.win_rate = win_rate;
    }

    public void setLose_num(int lose_num) {
        this.lose_num = lose_num;
    }

    public void setRank_num(int rank_num) {
        this.rank_num = rank_num;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setWin_num(int win_num) {
        this.win_num = win_num;
    }

    public void setWin_rate(int win_rate) {
        this.win_rate = win_rate;
    }

    public String getLose_num() {
        return String.valueOf(lose_num);
    }

    public String getRank_num() {
        return String.valueOf(rank_num);
    }

    public String getWin_num() {
        return String.valueOf(win_num);
    }

    public String getWin_rate() {
        return String.valueOf(win_rate);
    }

    public String getTeam() {
        return team;
    }
}
