package com.example.Lucky1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @Column(name = "tournament_id")
    private Integer tournamentId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "format", length = 50)
    private String format;

    @Column(name = "stage", length = 50)
    private String stage;

    public Tournament() {
    }

    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
