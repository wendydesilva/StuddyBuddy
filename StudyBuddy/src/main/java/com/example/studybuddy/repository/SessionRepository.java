package com.example.studybuddy.repository;

import com.example.studybuddy.model.Session;
import com.example.studybuddy.model.Student_SE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByStudentId(Long studentId);
}

