package com.example.studybuddy.repository;

import com.example.studybuddy.model.Goal;
import com.example.studybuddy.model.Reward;
import com.example.studybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByStudent(User student);

    List<Reward> findByGoal(Goal goal);

    int countByAssignedBy(User coach);
}