package com.example.Lucky1.dto;

public class PlayerStatsRow {

    private String teamName;        // Название команды игрока
    private String competitionName; // Название турнира
    private int matches;
    private int goals;
    private int assists;
    private int minutes;
    private int yellowCards;
    private int redCards;

    public PlayerStatsRow() {
    }

    public PlayerStatsRow(String teamName, String competitionName, int matches, int goals, int assists, int minutes, int yellowCards, int redCards) {
        this.teamName = teamName;
        this.competitionName = competitionName;
        this.matches = matches;
        this.goals = goals;
        this.assists = assists;
        this.minutes = minutes;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }
}
