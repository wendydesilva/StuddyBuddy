package com.example.studybuddy.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student_SE student;

    @ManyToOne
    private SessionPlan plan;

    private double amount;
    private LocalDateTime paymentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student_SE getStudent() {
        return student;
    }

    public void setStudent(Student_SE student) {
        this.student = student;
    }

    public SessionPlan getPlan() {
        return plan;
    }

    public void setPlan(SessionPlan plan) {
        this.plan = plan;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}