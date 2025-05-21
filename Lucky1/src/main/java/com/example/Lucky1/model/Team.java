package com.example.Lucky1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @Column(name = "team_id")
    private Integer teamId;

    private String name;

    private String country;

    private String city;

    private String coach;

    @Column(name = "logo_url", columnDefinition = "text")
    private String logoUrl;

    @Column(name = "short_name")
    private String shortName;

    private String tla;

    private Integer founded;

    private String venue;

    @Column(name = "champions_league_points")
    private Integer championsLeaguePoints;

    // Геттеры и сеттеры

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTla() {
        return tla;
    }

    public void setTla(String tla) {
        this.tla = tla;
    }

    public Integer getFounded() {
        return founded;
    }

    public void setFounded(Integer founded) {
        this.founded = founded;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getChampionsLeaguePoints() {
        return championsLeaguePoints;
    }

    public void setChampionsLeaguePoints(Integer championsLeaguePoints) {
        this.championsLeaguePoints = championsLeaguePoints;
    }
}
