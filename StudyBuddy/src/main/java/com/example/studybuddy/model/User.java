package com.example.studybuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // primary key

    @Column(nullable = false)
    private String name; // user name

    @Column(nullable = false, unique = true)
    private String email; // unique email

    @Column(nullable = false)
    private String password; // user password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // user role (student, coach, admin)

    @Column(nullable = false)
    private Integer totalPoints = 0; // accumulated points

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
}