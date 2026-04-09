package com.example.studybuddy.controller;

import com.example.studybuddy.model.Goal;
import com.example.studybuddy.model.GoalStatus;
import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.TaskStatus;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.GoalRepository;
import com.example.studybuddy.repository.NotificationRepository;
import com.example.studybuddy.repository.TaskRepository;
import com.example.studybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoalController {

    // Injecting repositories (used to interact with database)
    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationRepository notificationRepository;



    @GetMapping("/student/goals")
    public String studentGoals(HttpSession session, Model model) {

        User student = (User) session.getAttribute("loggedUser");

        if (student == null || student.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        // goals list for the student
        List<Goal> goals = goalRepository.findByStudentOrderByGoalIdDesc(student);


        for (Goal goal : goals) {
            long totalTasks = taskRepository.countByGoal(goal);
            long completedTasks = taskRepository.countByGoalAndStatus(goal, TaskStatus.COMPLETED);

            // Save task counts inside goal (used for UI display)
            goal.setTotalTasks((int) totalTasks);
            goal.setCompletedTasks((int) completedTasks);
        }

        // Send data to HTML page
        model.addAttribute("goals", goals);
        model.addAttribute("goal", new Goal()); // empty goal for form
        model.addAttribute("coaches", userRepository.findByRole(Role.COACH));

        return "student/goals";
    }


    // student create goal
    @PostMapping("/student/goals/create")
    public String createGoal(@ModelAttribute Goal goal,
                             @RequestParam(required = false) Long coachId,
                             HttpSession session) {

        User student = (User) session.getAttribute("loggedUser");

        // Check if valid student
        if (student == null || student.getRole() != Role.STUDENT) {
            return "redirect:/login";
        }

        // Set default goal values
        goal.setStudent(student);
        goal.setStatus(GoalStatus.PENDING); // waiting for coach approval
        goal.setProgress(0);

        // If coach selected → assign coach
        if (coachId != null) {
            User coach = userRepository.findById(coachId).orElse(null);
            if (coach != null && coach.getRole() == Role.COACH) {
                goal.setCoach(coach);
            }
        }

        // Save goal to database
        goalRepository.save(goal);

        // Notify coach about new goal request
        if (goal.getCoach() != null) {
            Notification coachNotification = new Notification();
            coachNotification.setUser(goal.getCoach());
            coachNotification.setMessage("New goal request from "
                    + student.getName() + ": " + goal.getTitle());

            notificationRepository.save(coachNotification);
        }

        return "redirect:/student/goals";
    }



    @GetMapping("/coach/goals/requests")
    public String coachGoalRequests(HttpSession session, Model model) {

        User coach = (User) session.getAttribute("loggedUser");

        // Only coach allowed
        if (coach == null || coach.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        // Get all pending goals
        List<Goal> pendingGoals = goalRepository.findByStatus(GoalStatus.PENDING);

        model.addAttribute("goals", pendingGoals);
        model.addAttribute("pageTitle", "Goal Requests");
        model.addAttribute("showApprovalButtons", true);

        return "coach/goals";
    }


    // coach view assigned goal
    @GetMapping("/coach/goals/assigned")
    public String coachAssignedGoals(HttpSession session, Model model) {

        User coach = (User) session.getAttribute("loggedUser");

        if (coach == null || coach.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        // Get goals assigned to this coach
        List<Goal> assignedGoals =
                goalRepository.findByCoach_UserIdOrderByGoalIdDesc(coach.getUserId());

        model.addAttribute("goals", assignedGoals);
        model.addAttribute("pageTitle", "Assigned Goals");
        model.addAttribute("showApprovalButtons", false);

        return "coach/goals";
    }


    //approve goal
    @PostMapping("/coach/goals/approve")
    public String approveGoal(@RequestParam Long goalId, HttpSession session) {

        User coach = (User) session.getAttribute("loggedUser");

        if (coach == null || coach.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        // Find goal
        Goal goal = goalRepository.findById(goalId).orElse(null);

        if (goal != null) {
            goal.setStatus(GoalStatus.ACTIVE); // goal becomes active
            goalRepository.save(goal);

            // Notify student
            Notification studentNotification = new Notification();
            studentNotification.setUser(goal.getStudent());
            studentNotification.setMessage("Your goal was approved: " + goal.getTitle());

            notificationRepository.save(studentNotification);
        }

        return "redirect:/coach/goals/assigned";
    }


    // reject goal
    @PostMapping("/coach/goals/reject")
    public String rejectGoal(@RequestParam Long goalId, HttpSession session) {

        User coach = (User) session.getAttribute("loggedUser");

        if (coach == null || coach.getRole() != Role.COACH) {
            return "redirect:/login";
        }

        Goal goal = goalRepository.findById(goalId).orElse(null);

        if (goal != null) {
            goal.setStatus(GoalStatus.REJECTED);
            goalRepository.save(goal);

            // Notify student
            Notification studentNotification = new Notification();
            studentNotification.setUser(goal.getStudent());
            studentNotification.setMessage("Your goal was rejected: " + goal.getTitle());

            notificationRepository.save(studentNotification);
        }

        return "redirect:/coach/goals/requests";
    }


    // progress update
    public void updateGoalProgress(Goal goal) {

        // Count tasks
        long totalTasks = taskRepository.countByGoal(goal);
        long completedTasks = taskRepository.countByGoalAndStatus(goal, TaskStatus.COMPLETED);

        // If no tasks, progress is 0
        if (totalTasks == 0) {
            goal.setProgress(0);
        } else {
            int progress = (int) ((completedTasks * 100) / totalTasks);
            goal.setProgress(progress);

            // Update goal status based on progress
            if (progress == 100) {
                goal.setStatus(GoalStatus.COMPLETED);
            } else if (goal.getStatus() != GoalStatus.REJECTED) {
                goal.setStatus(GoalStatus.ACTIVE);
            }
        }

        // Save updated goal
        goalRepository.save(goal);
    }
}