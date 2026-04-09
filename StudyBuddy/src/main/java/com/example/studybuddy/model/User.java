package com.example.studybuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Integer totalPoints = 0;

    @Column
    private String course;

    @Column
    private String subjectExpertise;

    @Column
    private String experienceYears;

    @Column
    private String dob;
    public User() {}


    public String getFullName() {
        return this.name;
    }

    public Long getId() {
        return this.userId;
    }

// --- GETTERS AND SETTERS ---

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getSubjectExpertise() { return subjectExpertise; }
    public void setSubjectExpertise(String subjectExpertise) { this.subjectExpertise = subjectExpertise; }

    public String getExperienceYears() { return experienceYears; }
    public void setExperienceYears(String experienceYears) { this.experienceYears = experienceYears; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}