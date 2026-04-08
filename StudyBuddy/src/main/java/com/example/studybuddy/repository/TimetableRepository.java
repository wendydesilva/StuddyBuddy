package com.example.studybuddy.repository;

import com.example.studybuddy.entity.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<TimetableEntry, Long> {
    //method written to get the timetable list for a student (for StudentService)
    List<TimetableEntry> findByStudentId(long studentId);
}
