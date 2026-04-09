package com.example.studybuddy.controller;

import com.example.studybuddy.entity.ProgressReport;
import com.example.studybuddy.entity.Student;
import com.example.studybuddy.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //manage accounts
    @GetMapping("/manage")
    public String managePage(@RequestParam(required = false) String search, Model model){
        List<Student> students;
        if(search != null && !search.isEmpty()){
            students = adminService.searchStudents(search);
        }
        else
            students = adminService.getAllStudent();

        model.addAttribute("students", students);
        model.addAttribute("search", search);
        model.addAttribute("totalStudents", adminService.getTotalStudents());
        return "admin_manage";
    }

    //view single student profile
    @GetMapping("/manage/student/{id}")
    public String viewStudent(@PathVariable long id, Model model){
        Student student = adminService.getStudentById(id);
        model.addAttribute("student", student);
        return "admin_student_profile";
    }

    //add new student form form submission
    @PostMapping("/manage/add")
    public String addStudent(@ModelAttribute Student student){
        adminService.addStudent(student);
        return "redirect:/admin/manage";
    }

    //progress report page
    @GetMapping("/reports")
    public String reportPage(Model model){
        List<ProgressReport> reports = adminService.getAllReports();
        model.addAttribute("reports", reports);
        model.addAttribute("totalStudents", adminService.getTotalStudents());
        model.addAttribute("totalReports", adminService.getTotalReports());
        return "admin_reports";
    }
}
