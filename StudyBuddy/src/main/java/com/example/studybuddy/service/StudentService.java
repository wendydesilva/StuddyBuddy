package com.example.studybuddy.service;

import com.example.studybuddy.entity.Attendance;
import com.example.studybuddy.entity.Student;
import com.example.studybuddy.entity.TimetableEntry;
import com.example.studybuddy.repository.AttendanceRepository;
import com.example.studybuddy.repository.StudentRepository;
import com.example.studybuddy.repository.SubjectRepository;
import com.example.studybuddy.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Student getStudent(long id) {
        //expecting one result, and we use orElse to display message that it's not found
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    //fetching an attendance record for a student, and since we're expecting multiple results so we use List
    public List<Attendance> getAttendanceByStudent(long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    //fetching the timetable record for a student, and since we're expecting multiple results so we use List
    public List <TimetableEntry> getTimetableByStudent(long studentId){
        return timetableRepository.findByStudentId(studentId);
    }
}
