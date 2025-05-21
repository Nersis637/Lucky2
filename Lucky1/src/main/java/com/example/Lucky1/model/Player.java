package com.example.Lucky1.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "player_id")
    private Integer playerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "position")
    private String position;

    @Column(name = "matches")
    private Integer matches;

    @Column(name = "goals")
    private Integer goals;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id")
    private Team team;

    // === Новые поля ===
    @Column(name = "club_number")
    private Integer clubNumber;

    @Column(name = "national_team")
    private String nationalTeam;

    @Column(name = "national_team_number")
    private Integer nationalTeamNumber;

    @Column(name = "birth_city")
    private String birthCity;

    @Column(name = "foot")
    private String foot;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Integer weightKg;

    @Column(name = "bio")
    private String bio;

    @Column(name = "photo_filename")
    private String photoFilename;

    @Column(name = "assists")
    private Integer assists;

    @Column(name = "minutes_played")
    private Integer minutesPlayed;

    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @Column(name = "red_cards")
    private Integer redCards;

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    // === Геттеры и сеттеры ===

    public Integer getPlayerId() { return playerId; }

    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.age = calculateAge(dateOfBirth);
    }

    public Integer getAge() { return age; }

    public void setAge(Integer age) { this.age = age; }

    public String getNationality() { return nationality; }

    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public Integer getMatches() { return matches; }

    public void setMatches(Integer matches) { this.matches = matches; }

    public Integer getGoals() { return goals; }

    public void setGoals(Integer goals) { this.goals = goals; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public Team getTeam() { return team; }

    public void setTeam(Team team) { this.team = team; }

    public Integer getClubNumber() { return clubNumber; }

    public void setClubNumber(Integer clubNumber) { this.clubNumber = clubNumber; }

    public String getNationalTeam() { return nationalTeam; }

    public void setNationalTeam(String nationalTeam) { this.nationalTeam = nationalTeam; }

    public Integer getNationalTeamNumber() { return nationalTeamNumber; }

    public void setNationalTeamNumber(Integer nationalTeamNumber) { this.nationalTeamNumber = nationalTeamNumber; }

    public String getBirthCity() { return birthCity; }

    public void setBirthCity(String birthCity) { this.birthCity = birthCity; }

    public String getFoot() { return foot; }

    public void setFoot(String foot) { this.foot = foot; }

    public Integer getHeightCm() { return heightCm; }

    public void setHeightCm(Integer heightCm) { this.heightCm = heightCm; }

    public Integer getWeightKg() { return weightKg; }

    public void setWeightKg(Integer weightKg) { this.weightKg = weightKg; }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(Integer minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Transient
    public String getTranslatedPosition() {
        if (position == null) return "";
        return switch (position) {
            case "Centre-Forward" -> "нападающий";
            case "Right Winger" -> "правый вингер";
            case "Left Winger" -> "левый вингер";
            case "Attacking Midfield" -> "атакующий полузащитник";
            case "Central Midfield" -> "центральный полузащитник";
            case "Right-Back" -> "правый защитник";
            case "Left-Back" -> "левый защитник";
            case "Centre-Back" -> "центральный защитник";
            case "Defensive Midfield" -> "опорный полузащитник";
            case "Goalkeeper" -> "вратарь";
            default -> position;
        };
    }
    @Transient
    public String getFullName() {
        String name = (firstName != null ? firstName : "");
        String surname = (lastName != null ? lastName : "");
        return (name + " " + surname).trim();
    }
}

