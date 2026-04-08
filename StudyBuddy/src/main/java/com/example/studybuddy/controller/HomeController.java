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
        // redirect user to login page
        return "redirect:/login";
    }

    @GetMapping("/student/home")
    public String studentHome(HttpSession session, Model model) {

        // get logged-in user
        User user = (User) session.getAttribute("loggedUser");

        // allow only student
        if (user == null || user.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        // send user data to view
        model.addAttribute("user", user);

        return "student/home";
    }

    @GetMapping("/coach/home")
    public String coachHome(HttpSession session, Model model) {

        // get logged-in user
        User user = (User) session.getAttribute("loggedUser");

        // allow only coach
        if (user == null || user.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        // send user data to view
        model.addAttribute("user", user);

        return "coach/home";
    }

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {

        // get logged-in user
        User user = (User) session.getAttribute("loggedUser");

        // allow only admin
        if (user == null || user.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        // send user data to view
        model.addAttribute("user", user);

        return "admin/home";
    }
}