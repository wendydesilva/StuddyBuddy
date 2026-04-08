package com.example.studybuddy.repository;

import com.example.studybuddy.entity.Student;
import com.example.studybuddy.model.Student_SE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {}