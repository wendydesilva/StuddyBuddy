package com.example.studybuddy.controller;

import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired //access user data from the database
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            @RequestParam Role role,
                            HttpSession session,
                            Model model) {

        User user = userRepository.findByEmail(email).orElse(null);

        // check if user exists, password matches, and role is correct
        if (user != null && user.getPassword().equals(password) && user.getRole() == role) {
            session.setAttribute("loggedUser", user);  // store user in session to keep them logged in
            // redirect user based on their role
            if (role == Role.STUDENT) {
                return "redirect:/student/home";
            } else if (role == Role.COACH) {
                return "redirect:/coach/home";
            } else {
                return "redirect:/admin/home";
            }
        }
        // send error message back to login page if validation fails
        model.addAttribute("errorMessage", "Invalid email, password, or role.");
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String saveUser(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam Role role,
                           Model model) {
        // prevent users from creating admin accounts
        if (role == Role.ADMIN) {
            model.addAttribute("errorMessage", "Admin account cannot be created here.");
            return "signup";
        }

        // check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists.");
            return "signup";
        }

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            user.setTotalPoints(0); // initialize points to zero

            userRepository.save(user);

            model.addAttribute("successMessage", "Account created successfully. Please log in.");
            return "login";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "This account could not be created. Please try a different email.");
            return "signup";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Something went wrong. Please try again.");
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/admin-login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @PostMapping("/admin-login")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.getPassword().equals(password) && user.getRole() == Role.ADMIN) {
            session.setAttribute("loggedUser", user);
            return "redirect:/admin/home";
        }

        model.addAttribute("errorMessage", "Invalid admin login details.");
        return "admin/login";
    }
    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                Model model) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            model.addAttribute("errorMessage", "No account found with that email.");
            return "forgot-password";
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        model.addAttribute("successMessage", "Password reset successful. Please log in.");
        return "login";
    }
}