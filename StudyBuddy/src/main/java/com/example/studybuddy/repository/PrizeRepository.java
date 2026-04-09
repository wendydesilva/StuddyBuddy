package com.example.studybuddy.repository;

import com.example.studybuddy.model.Prize;
import com.example.studybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Long> {

    List<Prize> findByStudent(User student);

    List<Prize> findByStatus(Prize.PrizeStatus status);
    //Calculate how many points student has already spent on approved prizes. If none, return 0.
    @Query("select coalesce(sum(p.pointsRequired), 0) from Prize p where p.student = :student and p.status = 'APPROVED'")
    Integer getTotalApprovedPointsUsed(User student);
}