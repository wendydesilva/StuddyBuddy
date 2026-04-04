package com.example.studybuddy.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prize")
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prizeId;

    @Column(nullable = false)
    private String prizeName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer pointsRequired;

    private LocalDate requestedDate;
    private LocalDate awardedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrizeStatus status = PrizeStatus.PENDING;

    private String adminNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    public enum PrizeStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getAwardedDate() {
        return awardedDate;
    }

    public void setAwardedDate(LocalDate awardedDate) {
        this.awardedDate = awardedDate;
    }

    public PrizeStatus getStatus() {
        return status;
    }

    public void setStatus(PrizeStatus status) {
        this.status = status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}