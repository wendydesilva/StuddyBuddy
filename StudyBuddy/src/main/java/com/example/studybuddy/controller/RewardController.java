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

    public RewardController(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/student/rewards")
    public String studentRewards(HttpSession session, Model model) {
        User student = (User) session.getAttribute("loggedUser");
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        List<Reward> rewards = rewardRepository.findByStudent(student);
        model.addAttribute("rewards", rewards);
        model.addAttribute("totalPoints", student.getTotalPoints());
        return "student/rewards";
    }
}