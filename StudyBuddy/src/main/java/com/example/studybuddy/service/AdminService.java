package com.example.studybuddy.service;

import com.example.studybuddy.entity.ProgressReport;
import com.example.studybuddy.entity.Student;
import com.example.studybuddy.entity.StudyGroup;
import com.example.studybuddy.repository.ProgressRepository;
import com.example.studybuddy.repository.StudentRepository;
import com.example.studybuddy.repository.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ProgressRepository progressRepository;

    //getting all a students
    public List<Student> getAllStudent(){
        return studentRepository.findAll();
    }

    //searching by student name
    public List<Student> searchStudents(String keyword){
        return studentRepository.findAll().stream()
                .filter(s->s.getFullName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
    }

    //adding a new student
    public Student addStudent(Student student){
        return studentRepository.save(student);
    }

    //getting student profile
    public Student getStudentById(long id){
        return studentRepository.findById(id).orElseThrow(()-> new RuntimeException("Student not found"));
    }

    //dashboard stats
    public long getTotalStudents(){
        return studentRepository.count();
    }
    public long getTotalReports(){
        return progressRepository.count();
    }

    //getting all progress reports
    public List<ProgressReport> getAllReports(){
        return progressRepository.findAll();
    }
}
