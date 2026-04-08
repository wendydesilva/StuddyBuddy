package com.example.studybuddy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //written close to the unique key which is the id
    private long id;
    private String subjectName;
    private String colorHex;

    //Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setSubjectName(String fullName) {
        this.subjectName = fullName;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    //Getters
    public long getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getColorHex() {
        return colorHex;
    }
}
