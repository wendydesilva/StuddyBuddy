package com.example.studybuddy.controller;

import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }

    @GetMapping("/student/home")
    public String studentHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole().name().equals("STUDENT") == false) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "student/home";
    }

    @GetMapping("/coach/home")
    public String coachHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "coach/home";
    }

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "admin/home";
    }
}