package com.example.studybuddy.repository;

import com.example.studybuddy.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    //method written to get the attendance list for a student (for StudentService)
    List<Attendance> findByStudentId(long studentId);
}
