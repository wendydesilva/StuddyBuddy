package com.example.studybuddy.repository;

import com.example.studybuddy.session.CoachSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachSessionRepository extends JpaRepository<CoachSession, Long> {
    List<CoachSession>findByCoachId(long coachId);

    // total number of classes
    long countByCoachId(long coachId);

    //total number of students
    long countByCoachIdAndStudentIsNotNull(long coachId);
}
