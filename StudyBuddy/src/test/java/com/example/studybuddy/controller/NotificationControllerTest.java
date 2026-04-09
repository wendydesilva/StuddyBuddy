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

    @Mock
    NotificationRepository repo;
    @Mock
    HttpSession session;
    @Mock
    Model model;
    @InjectMocks
    NotificationController controller;

    @Test
    void studentNotifications_success() {

        // create student user
        User u = new User();
        u.setRole(Role.STUDENT);

        // mock logged-in user
        when(session.getAttribute("loggedUser")).thenReturn(u);

        // mock notifications data
        when(repo.findByUser(u)).thenReturn(List.of(new Notification()));

        // call method
        String view = controller.studentNotifications(session, model);

        // check correct view
        assertEquals("student/notifications", view);

        // verify data sent to view
        verify(model).addAttribute(eq("notifications"), any());
    }

    @Test
    void studentNotifications_redirect() {

        // mock no user logged in
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // check redirect to login
        assertEquals("redirect:/login", controller.studentNotifications(session, model));
    }
}