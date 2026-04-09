package com.example.studybuddy.controller;

import com.example.studybuddy.entity.Coach;
import com.example.studybuddy.entity.Event;
import com.example.studybuddy.model.Session;
import com.example.studybuddy.service.CoachService;
//import com.example.studybuddy.service.StudentService;
//import com.example.studybuddy.session.CoachSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CoachController {
//    @Autowired
//    private StudentService studentService;
    @Autowired
    private CoachService coachService;

    @GetMapping("/coach/home/{id}")
    public String coachHomePage(@PathVariable long id, Model model){
        Coach coach = coachService.getCoach(id);
        List<Session> sessions = coachService.getSessionsByCoach(id);
        List<Event> events = coachService.getUpcomingEvent(id);
        long totalClasses = coachService.getTotalClasses(id);
        long totalStudents = coachService.getTotalStudents(id);
        double generatedPay = coachService.getGeneratedPay(id);
        long totalHours = coachService.getTotalHours(id);

        model.addAttribute("coach", coach);
        model.addAttribute("session", sessions);
        model.addAttribute("events", events);
        model.addAttribute("totalClasses", totalClasses);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("generatedPay", generatedPay);
        model.addAttribute("totalHours", totalHours);
        return "coach/home";
    }
}
