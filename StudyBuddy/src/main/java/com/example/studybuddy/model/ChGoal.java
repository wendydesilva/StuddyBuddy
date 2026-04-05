package com.example.studybuddy.model;

public class ChGoal {

    private Long id;
    private String name;

    public ChGoal(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}