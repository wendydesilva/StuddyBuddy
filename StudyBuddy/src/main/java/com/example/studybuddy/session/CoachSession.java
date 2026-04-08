package com.example.studybuddy.session;

import com.example.studybuddy.entity.Coach;
import com.example.studybuddy.entity.Student;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class CoachSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDateTime sessionDateTime;
    private String status;
    private double price;
    private String subjectName;


    //getters
    public long getId() {
        return id;
    }
    public Coach getCoach() {
        return coach;
    }
    public Student getStudent() {
        return student;
    }
    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }
    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }
    public String getSubjectName() {
        return subjectName;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }
    public void setCoach(Coach coach) {
        this.coach = coach;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
