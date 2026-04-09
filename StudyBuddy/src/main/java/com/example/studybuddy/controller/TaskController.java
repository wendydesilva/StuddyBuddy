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

    // inject repositories and controllers
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

        // get logged-in student
        User student = (User) session.getAttribute("loggedUser");

        // allow only student
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        // fetch student tasks
        List<Task> tasks = taskRepository.findByStudent_UserId(student.getUserId());

        // send to view
        model.addAttribute("tasks", tasks);

        return "student/tasks";
    }

    @PostMapping("/student/tasks/submit")
    @Transactional
    public String submitTask(@RequestParam Long taskId,
                             @RequestParam String submissionNote,
                             HttpSession session,
                             RedirectAttributes ra) {

        // get logged-in student
        User student = (User) session.getAttribute("loggedUser");

        // allow only student
        if (student == null || student.getRole() != Role.STUDENT) return "redirect:/login";

        // find task
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task != null && task.getStudent().getUserId().equals(student.getUserId())) {

            // update task status
            task.setStatus(TaskStatus.SUBMITTED);
            task.setSubmissionNote(submissionNote);
            taskRepository.save(task);

            // notify coach
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

        // get logged-in coach
        User coach = (User) session.getAttribute("loggedUser");

        // allow only coach
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        // find goal
        Goal goal = goalRepository.findById(goalId).orElse(null);
        if (goal == null) return "redirect:/coach/goals";

        // ensure goal belongs to coach
        if (goal.getCoach() == null || !goal.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        // fetch tasks
        List<Task> tasks = taskRepository.findByGoal(goal);

        // send to view
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

        // get logged-in coach
        User coach = (User) session.getAttribute("loggedUser");

        // allow only coach
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        // find goal
        Goal goal = goalRepository.findById(goalId).orElse(null);
        if (goal == null) return "redirect:/coach/goals";

        // ensure goal belongs to coach
        if (goal.getCoach() == null || !goal.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        // create new task
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(LocalDate.parse(dueDate));
        task.setGoal(goal);
        task.setStudent(goal.getStudent());
        task.setCoach(coach);
        task.setStatus(TaskStatus.ASSIGNED);

        taskRepository.save(task);

        // notify student
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

        // get logged-in coach
        User coach = (User) session.getAttribute("loggedUser");

        // allow only coach
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        // find task
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return "redirect:/coach/goals";

        // ensure task belongs to coach
        if (!task.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        // mark task completed
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(task);

        // update goal progress
        Goal goal = task.getGoal();
        goalController.updateGoalProgress(goal);

        // assign reward points
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

        // notify student
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

        // get logged-in coach
        User coach = (User) session.getAttribute("loggedUser");

        // allow only coach
        if (coach == null || coach.getRole() != Role.COACH) return "redirect:/login";

        // find task
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return "redirect:/coach/goals";

        // ensure task belongs to coach
        if (!task.getCoach().getUserId().equals(coach.getUserId())) {
            return "redirect:/coach/goals";
        }

        // reject task
        task.setStatus(TaskStatus.REJECTED);
        taskRepository.save(task);

        // notify student
        Notification studentNotification = new Notification();
        studentNotification.setUser(task.getStudent());
        studentNotification.setMessage("Your submitted task was rejected: " + task.getTitle());
        notificationRepository.save(studentNotification);

        ra.addFlashAttribute("errorMessage", "Task submission rejected.");
        return "redirect:/coach/goals/" + task.getGoal().getGoalId() + "/tasks";
    }
}