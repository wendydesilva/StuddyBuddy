package com.example.studybuddy.model;

import jakarta.persistence.*;

@Entity
public class SessionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int durationMinutes;


    public SessionPlan() {}
    public SessionPlan(String name, double price, int durationMinutes) {
        this.name = name;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }


    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
}