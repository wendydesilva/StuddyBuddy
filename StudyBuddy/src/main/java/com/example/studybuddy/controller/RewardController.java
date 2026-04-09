package com.example.studybuddy.controller;

import com.example.studybuddy.model.Reward;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.RewardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RewardController {

    private final RewardRepository rewardRepository;

    // inject reward repository
    public RewardController(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/student/rewards")
    public String studentRewards(HttpSession session, Model model) {

        // get logged-in student
        User student = (User) session.getAttribute("loggedUser");

        // allow only student
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        // fetch rewards
        List<Reward> rewards = rewardRepository.findByStudent(student);

        // send data to view
        model.addAttribute("rewards", rewards);
        model.addAttribute("totalPoints", student.getTotalPoints());

        return "student/rewards";
    }
}