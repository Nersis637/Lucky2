package com.example.Lucky1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    private Long id;

    @Column(name = "team_home", nullable = false)
    private String teamHome;

    @Column(name = "team_away", nullable = false)
    private String teamAway;

    @Column(name = "home_logo_url")
    private String homeLogoUrl;

    @Column(name = "away_logo_url")
    private String awayLogoUrl;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @Column(name = "home_score", nullable = false)
    private int homeScore = 0;

    @Column(name = "away_score", nullable = false)
    private int awayScore = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(String teamHome) {
        this.teamHome = teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(String teamAway) {
        this.teamAway = teamAway;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getAwayLogoUrl() {
        return awayLogoUrl;
    }

    public void setAwayLogoUrl(String awayLogoUrl) {
        this.awayLogoUrl = awayLogoUrl;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}