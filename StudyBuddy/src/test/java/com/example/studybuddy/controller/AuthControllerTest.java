package com.example.studybuddy.controller;

import com.example.studybuddy.model.Role;
import com.example.studybuddy.model.User;
import com.example.studybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock UserRepository repo;
    @Mock HttpSession session;
    @Mock Model model;
    @InjectMocks AuthController controller;

    @Test
    void login_success() {
        User u = new User();
        u.setPassword("123");
        u.setRole(Role.STUDENT);

        when(repo.findByEmail("a")).thenReturn(Optional.of(u));

        String view = controller.loginUser("a","123",Role.STUDENT,session,model);

        assertEquals("redirect:/student/home", view);
        verify(session).setAttribute("loggedUser", u);
    }

    @Test
    void login_fail() {
        when(repo.findByEmail("a")).thenReturn(Optional.empty());

        String view = controller.loginUser("a","123",Role.STUDENT,session,model);

        assertEquals("login", view);
        verify(model).addAttribute(eq("errorMessage"), any());
    }
}