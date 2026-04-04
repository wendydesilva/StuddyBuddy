package com.example.studybuddy.repository;

import com.example.studybuddy.model.Goal;
import com.example.studybuddy.model.GoalStatus;
import com.example.studybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByStudent(User student);

    List<Goal> findByCoach(User coach);

    List<Goal> findByStatus(GoalStatus status);

    List<Goal> findByStudentAndStatus(User student, GoalStatus status);

    List<Goal> findByCoachAndStatus(User coach, GoalStatus status);

    List<Goal> findByStudentOrderByGoalIdDesc(User student);

    List<Goal> findByCoach_UserIdAndStatusOrderByGoalIdDesc(Long coachId, GoalStatus status);

    List<Goal> findByCoach_UserIdOrderByGoalIdDesc(Long coachId);
}