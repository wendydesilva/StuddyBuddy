package com.example.studybuddy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //written close to the unique key which is the id
    private long id;
    private String fullName;
    private String email;
    private String subjectExpertise;
    private double hourlyRate;
    private String dob;

    //getters
    public long getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }
    public String getSubjectExpertise() {
        return subjectExpertise;
    }
    public double getHourlyRate() {
        return hourlyRate;
    }
    public String getDob() {
        return dob;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSubjectExpertise(String subjectExpertise) {
        this.subjectExpertise = subjectExpertise;
    }
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
}
