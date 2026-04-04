package com.example.studybuddy.controller;

import com.example.studybuddy.model.Notification;
import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.NotificationRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock NotificationRepository repo;
    @Mock HttpSession session;
    @Mock Model model;
    @InjectMocks NotificationController controller;

    @Test
    void studentNotifications_success() {
        User u = new User(); u.setRole(Role.STUDENT);

        when(session.getAttribute("loggedUser")).thenReturn(u);
        when(repo.findByUser(u)).thenReturn(List.of(new Notification()));

        String view = controller.studentNotifications(session, model);

        assertEquals("student/notifications", view);
        verify(model).addAttribute(eq("notifications"), any());
    }

    @Test
    void studentNotifications_redirect() {
        when(session.getAttribute("loggedUser")).thenReturn(null);

        assertEquals("redirect:/login", controller.studentNotifications(session, model));
    }
}