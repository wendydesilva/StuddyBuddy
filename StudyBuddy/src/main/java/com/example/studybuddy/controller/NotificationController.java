package com.example.studybuddy.controller;

import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.NotificationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationRepository notificationRepository;

    // inject notification repository
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/student/notifications")
    public String studentNotifications(HttpSession session, Model model) {

        // get logged-in student
        User student = (User) session.getAttribute("loggedUser");

        // allow only student
        if (student == null || student.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        // student notifications
        List<Notification> notifications = notificationRepository.findByUser(student);

        // send to view
        model.addAttribute("notifications", notifications);

        return "student/notifications";
    }

    @GetMapping("/coach/notifications")
    public String coachNotifications(HttpSession session, Model model) {

        // get logged-in coach
        User coach = (User) session.getAttribute("loggedUser");

        // allow only coach
        if (coach == null || coach.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        // coach notifications
        List<Notification> notifications = notificationRepository.findByUser(coach);

        // send to view
        model.addAttribute("notifications", notifications);

        return "coach/notifications";
    }

    @GetMapping("/admin/notifications")
    public String adminNotifications(HttpSession session, Model model) {

        // get logged-in admin
        User admin = (User) session.getAttribute("loggedUser");

        // allow only admin
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        // admin notifications
        List<Notification> notifications = notificationRepository.findByUser(admin);

        // send to view
        model.addAttribute("notifications", notifications);

        return "admin/notifications";
    }
}