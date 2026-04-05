package com.example.studybuddy.repository;

import com.example.studybuddy.model.SessionPlan;
import com.example.studybuddy.model.Session;
import com.example.studybuddy.model.Student_SE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionPlanRepository extends JpaRepository<SessionPlan, Long> {


    Optional<SessionPlan> findByName(String name);
}

