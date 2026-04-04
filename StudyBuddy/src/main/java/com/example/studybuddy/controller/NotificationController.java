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

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/student/notifications")
    public String studentNotifications(HttpSession session, Model model) {
        User student = (User) session.getAttribute("loggedUser");
        if (student == null || student.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        List<Notification> notifications = notificationRepository.findByUser(student);
        model.addAttribute("notifications", notifications);
        return "student/notifications";
    }

    @GetMapping("/coach/notifications")
    public String coachNotifications(HttpSession session, Model model) {
        User coach = (User) session.getAttribute("loggedUser");
        if (coach == null || coach.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        List<Notification> notifications = notificationRepository.findByUser(coach);
        model.addAttribute("notifications", notifications);
        return "coach/notifications";
    }

    @GetMapping("/admin/notifications")
    public String adminNotifications(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        List<Notification> notifications = notificationRepository.findByUser(admin);
        model.addAttribute("notifications", notifications);
        return "admin/notifications";
    }
}