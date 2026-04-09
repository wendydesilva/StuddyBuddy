package com.example.studybuddy.controller;

import com.example.studybuddy.entity.Attendance;
import com.example.studybuddy.entity.Student;
import com.example.studybuddy.entity.TimetableEntry;
import com.example.studybuddy.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class StudentDashboardController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/student/home/{id}") //listens for GET requests to the URL and tells spring which URL this method responds to
    //Method that listens for web requests and returns the page (student_home.html)
    public String studentHomePage(@PathVariable long id, Model model) { //extracts the number from the URL, a container you use to pass data to the HTML template
        //call/fetch and store results
        Student student = studentService.getStudent(id);
        List<Attendance> attendanceByStudent = studentService.getAttendanceByStudent(id);
        List<TimetableEntry> timetableByStudent = studentService.getTimetableByStudent(id);
        //Adding the models
        model.addAttribute("student", student);
        model.addAttribute("attendance", attendanceByStudent);
        model.addAttribute("timetable", timetableByStudent);
        return "student/home";
    }
}
