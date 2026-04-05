package com.example.studybuddy.repository;

import com.example.studybuddy.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
}