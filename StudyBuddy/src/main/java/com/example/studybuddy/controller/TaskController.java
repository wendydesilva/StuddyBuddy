package com.example.studybuddy.controller;

import com.example.studybuddy.model.Goal;
import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.Reward;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.Task;
import com.example.studybuddy.model.TaskStatus;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.GoalRepository;
import com.example.studybuddy.repository.NotificationRepository;
import com.example.studybuddy.repository.RewardRepository;
import com.example.studybuddy.repository.TaskRepository;
import com.example.studybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    private final GoalController goalController;
    private final NotificationRepository notificationRepository;

    public TaskController(TaskRepository taskRepository,
                          GoalRepository goalRepository,
                          UserRepository userRepository,
                          RewardRepository rewardRepository,
                          GoalController goalController,
                          NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.rewardRepository = rewardRepository;
        this.goalController = goalController;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/student/tasks")
    public String studentTasks(HttpSession session, Model model) {
        User student = (User) session.getAttribute("loggedUser");
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        List<Task> tasks = taskRepository.findByStudent_UserId(student.getUserId());
        model.addAttribute("tasks", tasks);
        return "student/tasks";
    }

    @PostMapping("/student/tasks/submit")
    @Transactional
    public String submitTask(@RequestParam Long taskId,
                             @RequestParam String submissionNote,
                             HttpSession session,
                             RedirectAttributes ra) {
        User student = (User) session.getAttribute("loggedUser");
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null && task.getStudent().getUserId().equals(student.getUserId())) {
            task.setStatus(TaskStatus.SUBMITTED);
            task.setSubmissionNote(submissionNote);
            taskRepository.save(task);

            if (task.getCoach() != null) {
                Notification coachNotification = new Notification();
                coachNotification.setUser(task.getCoach());
                coachNotification.setMessage("Student submitted task: " + task.getTitle());
                notificationRepository.save(coachNotification);
            }

            ra.addFlashAttribute("successMessage", "Task submitted for coach review.");
        }

        return "redirect:/student/tasks";
    }

    @GetMapping("/coach/goals/{goalId}/tasks")
    public String coachGoalTasks(@PathVariable Long goalId,
                                 HttpSession session,
                                 Model model) {
        User coach = (User) session.getAttribute("loggedUser");
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        Goal goal = goalRepository.findById(goalId).orElse(null);
        if (goal == null) return "redirect:/coach/goals";

        if (goal.getCoach() == null || !goal.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        List<Task> tasks = taskRepository.findByGoal(goal);
        model.addAttribute("goal", goal);
        model.addAttribute("tasks", tasks);
        return "coach/tasks";
    }

    @PostMapping("/coach/goals/{goalId}/tasks/assign")
    @Transactional
    public String assignTask(@PathVariable Long goalId,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String dueDate,
                             HttpSession session,
                             RedirectAttributes ra) {
        User coach = (User) session.getAttribute("loggedUser");
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        Goal goal = goalRepository.findById(goalId).orElse(null);
        if (goal == null) return "redirect:/coach/goals";

        if (goal.getCoach() == null || !goal.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(LocalDate.parse(dueDate));
        task.setGoal(goal);
        task.setStudent(goal.getStudent());
        task.setCoach(coach);
        task.setStatus(TaskStatus.ASSIGNED);

        taskRepository.save(task);

        Notification studentNotification = new Notification();
        studentNotification.setUser(goal.getStudent());
        studentNotification.setMessage("A new task has been assigned to you: " + task.getTitle());
        notificationRepository.save(studentNotification);

        ra.addFlashAttribute("successMessage", "Task assigned successfully.");
        return "redirect:/coach/goals/" + goalId + "/tasks";
    }

    @PostMapping("/coach/tasks/complete")
    @Transactional
    public String completeTask(@RequestParam Long taskId,
                               @RequestParam Integer points,
                               HttpSession session,
                               RedirectAttributes ra) {
        User coach = (User) session.getAttribute("loggedUser");
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return "redirect:/coach/goals";

        if (!task.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);

        Goal goal = task.getGoal();
        goalController.updateGoalProgress(goal);

        if (points != null && points > 0) {
            Reward reward = new Reward();
            reward.setStudent(task.getStudent());
            reward.setGoal(goal);
            reward.setTask(task);
            reward.setAssignedBy(coach);
            reward.setPointsAwarded(points);
            rewardRepository.save(reward);

            User student = task.getStudent();
            student.setTotalPoints(student.getTotalPoints() + points);
            userRepository.save(student);
        }

        Notification studentNotification = new Notification();
        studentNotification.setUser(task.getStudent());
        studentNotification.setMessage("Your task was marked completed: " + task.getTitle());
        notificationRepository.save(studentNotification);

        ra.addFlashAttribute("successMessage", "Task marked completed and points awarded.");
        return "redirect:/coach/goals/" + goal.getGoalId() + "/tasks";
    }

    @PostMapping("/coach/tasks/reject")
    @Transactional
    public String rejectTask(@RequestParam Long taskId,
                             HttpSession session,
                             RedirectAttributes ra) {
        User coach = (User) session.getAttribute("loggedUser");
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return "redirect:/coach/goals";

        if (!task.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        task.setStatus(TaskStatus.REJECTED);
        taskRepository.save(task);

        Notification studentNotification = new Notification();
        studentNotification.setUser(task.getStudent());
        studentNotification.setMessage("Your submitted task was rejected: " + task.getTitle());
        notificationRepository.save(studentNotification);

        ra.addFlashAttribute("errorMessage", "Task submission rejected.");
        return "redirect:/coach/goals/" + task.getGoal().getGoalId() + "/tasks";
    }
}