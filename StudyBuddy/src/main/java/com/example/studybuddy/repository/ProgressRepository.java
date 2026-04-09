package com.example.studybuddy.repository;

import com.example.studybuddy.entity.ProgressReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<ProgressReport, Long> {
    List<ProgressReport> findByStudentId(long studentId);
}
