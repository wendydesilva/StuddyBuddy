package com.example.studybuddy.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sessionTime;

    private String status;

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    private String instructor;

    @ManyToOne
    private Student_SE student;

    @ManyToOne
    private SessionPlan plan;


    public Session() {}


    public Long getId() { return id; }
    public LocalDateTime getSessionTime() { return sessionTime; }
    public void setSessionTime(LocalDateTime sessionTime) { this.sessionTime = sessionTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Student_SE getStudent() { return student; }
    public void setStudent(Student_SE student) { this.student = student; }
    public SessionPlan getPlan() { return plan; }
    public void setPlan(SessionPlan plan) { this.plan = plan; }
}