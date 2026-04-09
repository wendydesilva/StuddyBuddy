package com.example.studybuddy.repository;

import com.example.studybuddy.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    //find all groups a student belongs to
    List<StudyGroup> findByMembersId(long studentId);
}
