package com.example.studybuddy.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ProgressReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //written close to the unique key which is the id
    private long id;
    private LocalDate generatedDate;
    private String summaryData;

    @ManyToOne //many attendance record to one subject
    @JoinColumn(name = "subject_id") //JPA name for the foreign key column in the db
    private Subject subject;

    //Getters
    public long getId() {
        return id;
    }
    public LocalDate getGeneratedDate() {
        return generatedDate;
    }
    public String getSummaryData() {
        return summaryData;
    }
    public Subject getSubject() {
        return subject;
    }

    //Setters
    public void setId(long id) {
        this.id = id;
    }
    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }
    public void setSummaryData(String summaryData) {
        this.summaryData = summaryData;
    }
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
