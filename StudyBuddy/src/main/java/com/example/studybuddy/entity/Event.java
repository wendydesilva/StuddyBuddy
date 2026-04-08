package com.example.studybuddy.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    //getters
    public long getId() {
        return id;
    }
    public Coach getCoach() {
        return coach;
    }
    public String getTitle() {
        return title;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }
    public void setCoach(Coach coach) {
        this.coach = coach;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
