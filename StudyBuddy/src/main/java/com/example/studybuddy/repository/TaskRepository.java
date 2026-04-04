package com.example.studybuddy.repository;

import com.example.studybuddy.model.Goal;
import com.example.studybuddy.model.Task;
import com.example.studybuddy.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByGoal(Goal goal);

    List<Task> findByStudent_UserId(Long studentId);

    long countByGoal(Goal goal);

    long countByGoalAndStatus(Goal goal, TaskStatus status);

    long countByCoach_UserIdAndStatus(Long coachId, TaskStatus status);
}