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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    @Mock GoalRepository repo;
    @Mock UserRepository userRepo;
    @Mock TaskRepository taskRepo;
    @Mock NotificationRepository notifRepo;
    @Mock HttpSession session;
    @Mock Model model;

    @InjectMocks GoalController controller;

    @Test
    void createGoal_success() {
        User student = new User();
        student.setRole(Role.STUDENT);

        Goal g = new Goal();

        when(session.getAttribute("loggedUser")).thenReturn(student);

        String view = controller.createGoal(g, null, session);

        assertEquals("redirect:/student/goals", view);
        verify(repo).save(g);
    }

    @Test
    void approveGoal_success() {
        User coach = new User();
        coach.setRole(Role.COACH);

        Goal g = new Goal();
        g.setStudent(new User());

        when(session.getAttribute("loggedUser")).thenReturn(coach);
        when(repo.findById(1L)).thenReturn(Optional.of(g));

        String view = controller.approveGoal(1L, session);

        assertEquals("redirect:/coach/goals/assigned", view);
        assertEquals(GoalStatus.ACTIVE, g.getStatus());
    }
}