package com.example.studybuddy.controller;

import com.example.studybuddy.model.*;
import com.example.studybuddy.repository.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock TaskRepository taskRepo;
    @Mock GoalRepository goalRepo;
    @Mock UserRepository userRepo;
    @Mock RewardRepository rewardRepo;
    @Mock GoalController goalController;
    @Mock NotificationRepository notifRepo;
    @Mock HttpSession session;
    @Mock Model model;
    @Mock RedirectAttributes ra;

    @InjectMocks TaskController controller;

    @Test
    void submitTask_ok() {
        User s = new User();
        s.setUserId(1L);
        s.setRole(Role.STUDENT); // ✅ REQUIRED

        User c = new User();
        c.setUserId(2L);

        Task t = new Task();
        t.setStudent(s);
        t.setCoach(c);

        when(session.getAttribute("loggedUser")).thenReturn(s);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(t));

        String view = controller.submitTask(1L, "done", session, ra);

        assertEquals("redirect:/student/tasks", view);
        assertEquals(TaskStatus.SUBMITTED, t.getStatus());
    }

    @Test
    void completeTask_ok() {
        User c = new User();
        c.setUserId(2L);
        c.setRole(Role.COACH); // ✅ REQUIRED

        User s = new User();
        s.setUserId(1L);

        Goal g = new Goal();
        g.setGoalId(7L);

        Task t = new Task();
        t.setCoach(c);
        t.setStudent(s);
        t.setGoal(g);
        t.setStatus(TaskStatus.SUBMITTED);

        when(session.getAttribute("loggedUser")).thenReturn(c);
        when(taskRepo.findById(1L)).thenReturn(Optional.of(t));

        controller.completeTask(1L, 20, session, ra);

        assertEquals(TaskStatus.COMPLETED, t.getStatus());
    }
}